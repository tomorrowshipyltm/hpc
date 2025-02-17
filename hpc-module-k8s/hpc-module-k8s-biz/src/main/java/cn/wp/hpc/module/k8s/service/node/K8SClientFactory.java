package cn.wp.hpc.module.k8s.service.node;

import cn.wp.hpc.module.k8s.model.cluster.K8SCluster;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;

/**
 * 工厂模式：根据clusterName获取k8sClient
 */
public interface K8SClientFactory {
    /**
     * 获取k8sClient
     * @param clusterName
     * @return
     */
    KubernetesClient getClient(String clusterName);

    /**
     * 获取k8sClusterList, 根据rmType=k8s判断
     * 所有类型集群的clusterName全局唯一，等效clusterId，包括slurm、pbs和k8s
     * @return
     */
    List<K8SCluster> getK8SClusters();
}
