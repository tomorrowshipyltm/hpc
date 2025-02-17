package cn.wp.hpc.module.k8s.service.workload;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.ServiceAccount;

import java.util.Map;

/**
 * 配置服务接口，包括 configmap、secret 和 serviceAccount
 */
public interface ConfigService {
    /**
     * 获取configmap list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> getConfigmaps(String cluster, int pageNo, int pageSize, String keyword);

    /**
     * 获取secret list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> getSecrets(String cluster, int pageNo, int pageSize, String keyword);


    /**
     * 获取serviceAccount list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> getServiceAccounts(String cluster, int pageNo, int pageSize, String keyword);

    /**
     * 获取指定namespace下configmap详情
     * @param cluster
     * @param namespace
     * @param name
     * @return
     */
    ConfigMap getConfigmapDetail(String cluster, String namespace, String name);

    /**
     * 获取指定namespace下secret详情
     * @param cluster
     * @param namespace
     * @param name
     * @return
     */
    Secret getSecretDetail(String cluster, String namespace, String name);

    /**
     * 获取指定namespace下serviceAccount详情
     * @param cluster
     * @param namespace
     * @param name
     * @return
     */
    ServiceAccount getServiceAccountDetail(String cluster, String namespace, String name);

}
