package cn.wp.hpc.module.k8s.service.workload;

import cn.wp.hpc.module.k8s.model.cluster.ConfigmapInfo;
import cn.wp.hpc.module.k8s.service.node.K8SClientFactory;
import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service("k8sConfigService")
public class ConfigServiceImpl implements ConfigService {
    final static private Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    K8SClientFactory k8SClientFactory;

    @Override
    public Map<String, Object> getConfigmaps(String cluster, int pageNo, int pageSize, String keyword) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<ConfigMap> configMaps = k8sClient.configMaps().list().getItems();
            List<ConfigmapInfo> configmapInfoList = configMaps.stream().map(ele -> {
                ConfigmapInfo info = new ConfigmapInfo(ele.getMetadata().getName(),
                        ele.getMetadata().getNamespace(), ele.getMetadata().getCreationTimestamp());
                // data中所有key逗号分割
                StringJoiner joiner = new StringJoiner(",");
                ele.getData().keySet().forEach(tmp -> joiner.add(tmp));
                info.setData(joiner.toString());
                return info;
            }).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(configmapInfoList, pageNo, pageSize, keyword);
        } catch (Exception e) {
            logger.error("Failed to get configmaps for {}, {}", cluster, e.getMessage());
            return null;
        }
    }


    @Override
    public Map<String, Object> getSecrets(String cluster, int pageNo, int pageSize, String keyword) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        List<Secret> configMaps = k8sClient.secrets().list().getItems();
        List<ConfigmapInfo> configmapInfoList = configMaps.stream().map(ele -> {
            ConfigmapInfo info = new ConfigmapInfo(ele.getMetadata().getName(),
                    ele.getMetadata().getNamespace(), ele.getMetadata().getCreationTimestamp());
            info.setType(ele.getType());
            info.setDataNum(ele.getData().size());
            return info;
        }).collect(Collectors.toList());
        return K8SNodeUtil.pageHelper(configmapInfoList, pageNo, pageSize, keyword);
    }

    @Override
    public Map<String, Object> getServiceAccounts(String cluster, int pageNo, int pageSize, String keyword) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        List<ServiceAccount> configMaps = k8sClient.serviceAccounts().list().getItems();
        List<ConfigmapInfo> configmapInfoList = configMaps.stream().map(ele -> {
            ConfigmapInfo info = new ConfigmapInfo(ele.getMetadata().getName(),
                    ele.getMetadata().getNamespace(), ele.getMetadata().getCreationTimestamp());
            if (ele.getSecrets().size() > 0) {
                info.setSecretName(ele.getSecrets().get(0).getName());
            }
            return info;
        }).collect(Collectors.toList());
        return K8SNodeUtil.pageHelper(configmapInfoList, pageNo, pageSize, keyword);
    }

    @Override
    public ConfigMap getConfigmapDetail(String cluster, String namespace, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.configMaps().inNamespace(namespace).withName(name).get();
    }

    @Override
    public Secret getSecretDetail(String cluster, String namespace, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.secrets().inNamespace(namespace).withName(name).get();
    }

    @Override
    public ServiceAccount getServiceAccountDetail(String cluster, String namespace, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.serviceAccounts().inNamespace(namespace).withName(name).get();
    }
}
