package cn.wp.hpc.module.k8s.service.node;


import cn.wp.hpc.module.k8s.model.cluster.K8SCluster;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class K8SClientFactoryImpl implements K8SClientFactory {
    // 缓存client, 避免重复创建
    private Map<String, KubernetesClient> clientMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(K8SClientFactoryImpl.class);

//    @Autowired
//    CloudMetadataService metadataService;

    @Override
    public KubernetesClient getClient(String clusterName) {
        if (clientMap.containsKey(clusterName)) {
            return clientMap.get(clusterName);
        } else {
            KubernetesClient client = createClient(clusterName);
            if (client != null) {
                clientMap.put(clusterName, client);
                return client;
            } else {
                logger.error("Failed to get KubernetesClient for cluster: {}", clusterName);
                throw new RuntimeException("KubernetesClient is null for cluster: " + clusterName);
            }
        }
    }

    private KubernetesClient createClient(String clusterName) {
        // 获取kubeconfig path
        String kubeconfig = getKubeconfigPath(clusterName);
        try {
            File kubeconfigFile = new File(kubeconfig);
            if (!kubeconfigFile.exists()) {
                logger.error("Can't find kubeconfig {} for {}", kubeconfig, clusterName);
                return null;
            }
            // 读取kubeconfig文件内容
            String content = new String(Files.readAllBytes(kubeconfigFile.toPath()), StandardCharsets.UTF_8);
            Config config = Config.fromKubeconfig(content);
            if (config == null) {
                logger.error("Failed to load kubeconfig for {}", clusterName);
                return null;
            }
            // 设置信任所有证书
            config.setTrustCerts(true);
            config.setAutoConfigure(false);
            // 设置连接超时时间
            config.setConnectionTimeout(2000);
            // 使用加载的配置创建KubernetesClient
            return new DefaultKubernetesClient(config);
        } catch (Exception e) {
            logger.error("Fail to read kubeconfig {}", kubeconfig);
            return null;
        }
    }

    /**
     * 从metadata 获取kubeconfig 绝对路径
     * @param clusterName
     */
    private String getKubeconfigPath(String clusterName) {
        String defaultPath = "/root/.kube/config";
//        defaultPath = "/Users/yueli/.kube/config_199";
        // 从元数据里获取集群-options, TODO 从其它地方获取kubeconfig路径
//        ClusterDTO clusterDTO = metadataService.fetchClusterByName(clusterName);
//        if (clusterDTO == null) {
//            logger.error("Can't find cluster {} in metadata", clusterName);
//            return defaultPath;
//        }
//        List<ModelOption> options = clusterDTO.getOptions();
//        for (ModelOption option : options) {
//            if (option.getName().equals(KUBECONFIG_KEY)) {
//                String path = option.getValue();
//                if (StrUtil.isNotBlank(path)) {
//                    return path;
//                }
//            }
//        }
        return defaultPath;
    }

    /**
     * 获取k8s集群列表，根据rmType=kubernetes判断
     * @return
     */
    @Override
    public List<K8SCluster> getK8SClusters() {
        List<K8SCluster> result = new ArrayList<>();
        // 目前集群分为slurm、pbs、condor和k8s，从中筛选
//        List<ClusterDTO> clusterDTOS = metadataService.queryClusters("", 1, 100);
//        if (CollUtil.isEmpty(clusterDTOS)) {
//            return result;
//        }
//        for (ClusterDTO clusterDTO : clusterDTOS) {
//            RmDTO rmDTO = metadataService.fetchDefaultRmByClusterId(clusterDTO.getId());
//            if (rmDTO != null && rmDTO.getType().equalsIgnoreCase(CLUSTER_TYPE_K8S)) {
//                K8SCluster k8SCluster = new K8SCluster(rmDTO.getClusterId(), rmDTO.getClusterName());
//                result.add(k8SCluster);
//            }
//        }
        return result;
    }
}
