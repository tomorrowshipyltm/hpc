package cn.wp.hpc.module.k8s.service.workload;

import io.fabric8.kubernetes.api.model.PersistentVolume;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.storage.StorageClass;

import java.util.Map;

/**
 * 存储卷管理接口
 */
public interface StorageService {

    /**
     * 获取persistentvolume list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    Map<String, Object> getPersistentVolumes(String cluster, int pageNo, int pageSize, String keyword);

    /**
     * 获取persistentvolume详情
     * @param cluster
     * @param name
     */
    PersistentVolume getPersistentvolumeDetail(String cluster, String name);

    /**
     * 获取persistentvolumeClaim list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    Map<String, Object> getPVCs(String cluster, int pageNo, int pageSize, String keyword);

    /**
     * 获取persistentvolumeClaim详情
     * @param cluster
     * @param name
     */
    PersistentVolumeClaim getPVCDetail(String cluster, String namespace, String name);

    /**
     * 获取storageClass list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    Map<String, Object> getStorageClasss(String cluster, int pageNo, int pageSize, String keyword);

    /**
     * 获取storageClass详情
     * @param cluster
     * @param name
     */
    StorageClass getStorageClassDetail(String cluster, String name);
}
