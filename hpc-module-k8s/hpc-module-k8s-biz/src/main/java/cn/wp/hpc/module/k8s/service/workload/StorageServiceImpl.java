package cn.wp.hpc.module.k8s.service.workload;

import cn.hutool.core.collection.CollUtil;
import cn.wp.hpc.module.k8s.model.workload.PVCInfo;
import cn.wp.hpc.module.k8s.model.workload.PersistentVolumeInfo;
import cn.wp.hpc.module.k8s.model.workload.StorageClassInfo;
import cn.wp.hpc.module.k8s.service.node.K8SClientFactory;
import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import io.fabric8.kubernetes.api.model.PersistentVolume;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.wp.hpc.module.k8s.enums.K8SConstant.STORAGE_CLASS_DEFAULT;


@Service("k8sStorageService")
public class StorageServiceImpl implements StorageService {
    final static private Logger logger = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    K8SClientFactory k8SClientFactory;

    @Override
    public Map<String, Object> getPersistentVolumes(String cluster, int pageNo, int pageSize, String keyword) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<PersistentVolume> pvList = k8sClient.persistentVolumes().list().getItems();
            List<PersistentVolumeInfo> persistentVolumeInfoList = pvList.stream().map(ele -> {
                PersistentVolumeInfo info = new PersistentVolumeInfo(ele.getMetadata().getName(),
                        ele.getStatus().getPhase(), ele.getMetadata().getCreationTimestamp());
                info.setCapacity(ele.getSpec().getCapacity().get("storage").toString());
                info.setReclaim(ele.getSpec().getPersistentVolumeReclaimPolicy());
                List<String> accessModes = ele.getSpec().getAccessModes();
                if (CollUtil.isNotEmpty(accessModes)) {
                    info.setAccessMode(ele.getSpec().getAccessModes().get(0));
                }
                return info;
            }).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(persistentVolumeInfoList, pageNo, pageSize, keyword);
        } catch (Exception e) {
            logger.error("Failed to get persistentVolumes for {}, {}", cluster, e.getMessage());
            return null;
        }
    }

    @Override
    public PersistentVolume getPersistentvolumeDetail(String cluster, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.persistentVolumes().withName(name).get();
    }

    @Override
    public Map<String, Object> getPVCs(String cluster, int pageNo, int pageSize, String keyword) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<PersistentVolumeClaim> pvcList = k8sClient.persistentVolumeClaims().list().getItems();
            List<PVCInfo> infoList = pvcList.stream().map(ele -> {
                PVCInfo info = new PVCInfo(ele.getMetadata().getName(), ele.getMetadata().getNamespace(),
                        ele.getStatus().getPhase(), ele.getMetadata().getCreationTimestamp());
                List<String> accessModes = ele.getSpec().getAccessModes();
                if (CollUtil.isNotEmpty(accessModes)) {
                    info.setAccessMode(ele.getSpec().getAccessModes().get(0));
                }
                info.setPv(ele.getSpec().getVolumeName());
                return info;
            }).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(infoList, pageNo, pageSize, keyword);
        } catch (Exception e) {
            logger.error("Failed to get persistentVolumes for {}, {}", cluster, e.getMessage());
            return null;
        }
    }

    @Override
    public PersistentVolumeClaim getPVCDetail(String cluster, String namespace, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.persistentVolumeClaims().inNamespace(namespace).withName(name).get();
    }

    @Override
    public Map<String, Object> getStorageClasss(String cluster, int pageNo, int pageSize, String keyword) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<StorageClass> storageClassList = k8sClient.storage().storageClasses().list().getItems();
            List<StorageClassInfo> infoList = storageClassList.stream().map(ele -> {
                StorageClassInfo info = new StorageClassInfo(ele.getMetadata().getName(), ele.getProvisioner(),
                        ele.getMetadata().getCreationTimestamp());
                info.setReclaimPolicy(ele.getReclaimPolicy());
                info.setVolumeBindingMode(ele.getVolumeBindingMode());
                if (ele.getMetadata().getAnnotations().containsKey(STORAGE_CLASS_DEFAULT)) {
                    info.setDefaultClass(true);
                }
                return info;
            }).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(infoList, pageNo, pageSize, keyword);
        } catch (Exception e) {
            logger.error("Failed to get storageClasss for {}, {}", cluster, e.getMessage());
            return null;
        }
    }

    @Override
    public StorageClass getStorageClassDetail(String cluster, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.storage().storageClasses().withName(name).get();
    }

}
