package cn.wp.hpc.module.k8s.service.node;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.module.k8s.model.cluster.K8SNode;
import cn.wp.hpc.module.k8s.model.cluster.NodeResource;
import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static cn.wp.hpc.module.k8s.enums.K8SConstant.*;
import static cn.wp.hpc.module.k8s.util.K8SNodeUtil.transferLocalFile;

@Service("k8sNodeService")
public class NodeServiceImpl implements NodeService {
    final static private Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    K8SClientFactory k8SClientFactory;

    // 自定义线程池，基于ExecutorService es = Executors.newFixedThreadPool();
//    @Autowired
//    TaskExecuteService taskExecuteService;

    @Override
    public Map<String, Object> getNodeList(String cluster, int pageNo, int pageSize) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<K8SNode> k8SNodeList = new ArrayList<>();
            List<Node> nodeList = k8sClient.nodes().list().getItems();
            for (Node node : nodeList) {
                K8SNode ele = K8SNodeUtil.parseK8SNode(node);
                if (ele != null) {
                    k8SNodeList.add(ele);
                }
            }
            return K8SNodeUtil.pageHelper(k8SNodeList, pageNo, pageSize, null);
        } catch (Exception e) {
            logger.error("Failed to get node list: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> queryNodeList(String cluster, int pageNo, int pageSize, String name,
                                             String osImage, String cpuBrand, String accelerator) {

        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<K8SNode> k8SNodeList = new ArrayList<>();
            List<Node> nodeList = k8sClient.nodes().list().getItems();
            List<Node> filterList = filterNodeList(nodeList, name, osImage, cpuBrand, accelerator);
            for (Node node : filterList) {
                K8SNode ele = K8SNodeUtil.parseK8SNode(node);
                if (ele != null) {
                    k8SNodeList.add(ele);
                }
            }
            return K8SNodeUtil.pageHelper(k8SNodeList, pageNo, pageSize, null);
        } catch (Exception e) {
            logger.error("Failed to get node list: {}", e.getMessage());
            return null;
        }
    }

    public List<Node> filterNodeList(List<Node> nodeList, String name, String osImage, String cpuBrand, String accelerator) {
        return nodeList.stream()
                .filter(node -> (StrUtil.isBlank(name) || node.getMetadata().getName().toLowerCase().contains(name.toLowerCase()))
                        && (StrUtil.isBlank(osImage) || node.getStatus().getNodeInfo().getOsImage().toLowerCase().contains(osImage.toLowerCase()))
                        && (StrUtil.isBlank(cpuBrand) || (getLabelValue(node, LABEL_CPU_BRAND) != null && getLabelValue(node, LABEL_CPU_BRAND).toLowerCase().contains(cpuBrand.toLowerCase())))
                        && (StrUtil.isBlank(accelerator) || (getLabelValue(node, LABEL_ACCELERATOR) != null && getLabelValue(node, LABEL_ACCELERATOR).toLowerCase().contains(accelerator.toLowerCase()))))
                .collect(Collectors.toList());
    }

    String getLabelValue(Node node, String key) {
        return node.getMetadata().getLabels().getOrDefault(key, null);
    }

    @Override
    public Node getNodeDetail(String cluster, String nodeName) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            return k8sClient.nodes().withName(nodeName).get();
        } catch (Exception e) {
            logger.error("Failed to get node detail: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteNode(String cluster, String name) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            Node node = k8sClient.nodes().withName(name).get();
            if (node.getStatus().getAddresses().size() > 0) {
                logger.info("Ready to delete node {}, {}", name, node.getStatus().getAddresses().get(0).getAddress());
            }
            // 删除节点前防护检查
            if (K8SNodeUtil.shouldDelete(node)) {
                k8sClient.nodes().withName(name).delete();
            } else {
                logger.info("Should not delete node {}", name);
            }
        } catch (Exception e) {
            logger.error("Failed to delete node {}: {}", name, e.getMessage());
        }
    }

    @Override
    public void patchNode(String cluster, String name, String payload) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            k8sClient.nodes().withName(name).patch(payload);
        } catch (Exception e) {
            logger.error("Fail to patch ndoe {} with {}, err: \n {}", name, payload, e.getMessage());
        }
    }

    @Override
    public K8SNode getNodeFeedback(String cluster, String nodeName) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            Node node = k8sClient.nodes().withName(nodeName).get();
            return K8SNodeUtil.parseK8SNode(node);
        } catch (Exception e) {
            logger.error("Fail to get feedback for {}, err: {}", nodeName, e.getMessage());
            return null;
        }
    }

    @Override
    public List<NodeResource> getNodeResource(String cluster, String nodeName) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            Node node = k8sClient.nodes().withName(nodeName).get();
            List<Pod> podList = k8sClient.pods().inAnyNamespace().withField(LABEL_SPEC_NODENAME, nodeName).list().getItems();
            return K8SNodeUtil.getNodeResource(node, podList);
        } catch (Exception e) {
            logger.error("Fail to get resource of {}, err: {}", nodeName, e.getMessage());
            return Collections.emptyList();
        }
    }

    /** 判断节点重名 **/
    @Override
    public boolean checkNodeName(String cluster, String name) {
        if (StrUtil.isBlank(name)) {
            return false;
        }
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<Node> nodeList = k8sClient.nodes().list().getItems();
            Set<String> existingNames = nodeList.stream().map(Node::getMetadata)
                    .map(ObjectMeta::getName).collect(Collectors.toSet());
            if (existingNames.contains(name)) {
                return false;
            }
        } catch (Exception e) {
            logger.error("fait to valid node name, {}", e.getMessage());
        }
        return true;
    }

    /**
     * 添加节点分2步
     * 1. 节点元数据加入到集群，节点列表能看到状态 "部署中"
     * 2. 异步执行ansible-playbook，输出持久化到文件
     * @param node
     */
    @Override
    public void addNodeAsync(K8SNode node) {
        // check valid
        if (StrUtil.isBlank(node.getName()) || StrUtil.isBlank(node.getCluster())) {
            logger.error("node param invalid");
            return;
        }
        KubernetesClient k8sClient = k8SClientFactory.getClient(node.getCluster());
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName(node.getName());
        Map<String, String> labels = node.getLabels();
        labels.put(LABEL_NODE_ROLE_WORKER, "");
        objectMeta.setLabels(labels);
        NodeSpec spec = new NodeSpec();
        // 设置为不可调度
        spec.setUnschedulable(true);
        NodeStatus status = new NodeStatus();
        List<NodeAddress> nodeAddresseList = Arrays.asList(new NodeAddress(node.getInternalIP(), "InternalIP"));
        status.setAddresses(nodeAddresseList);
        Node newNode = new Node("v1", "Node", objectMeta, spec, status);
        // 持久化到etcd
        k8sClient.nodes().create(newNode);

        // 异步执行ansible-playbook
        try {
            // 这里从公共线程池获取名为default的threadPool
//            ThreadPoolExecutor executor = taskExecuteService.getExecutor("default");
            ExecutorService executor = Executors.newFixedThreadPool(8);
            executor.submit(() -> deployNodeByAnsible(node));
        } catch (Exception e) {
            logger.error("Fail to run deployNodeByAnsible, err: {}", e.getMessage());
        }

    }

    /**
     * 使用ansible部署节点
     * 用户新增空白节点，需要在节点上部署docker、kubelet、配置，通过ansible yaml脚本编排实现
     * @param node
     */
    void deployNodeByAnsible(K8SNode node) {
        String ip = node.getInternalIP();
        if (StrUtil.isBlank(ip)) {
            logger.error("ip is null");
            return;
        }
        // 日志文件名
        String timestamp = Long.toString(System.currentTimeMillis());
        String filename = ip + ".log";
        String password = K8SNodeUtil.escapeSpecialCharacters(node.getPassword());
        String port = node.getPort();
        // 追加用户账密到 /etc/ansible/hosts
        String ansibleHostPath = "/etc/ansible/hosts";
        // String hostEntry = ip + " ansible_ssh_user=root ansible_ssh_port=41198 ansible_ssh_pass=\\#9k";
        String ipGroup = node.getName();
        String hostEntry = String.format("[%s] \n%s ansible_ssh_user=%s ansible_ssh_port=%s ansible_ssh_pass=%s", ipGroup,
                ip, node.getUser(), port, password);
        try {
            Files.write(Paths.get(ansibleHostPath), Arrays.asList(hostEntry), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Fail to append {}, {}", ansibleHostPath, e.getMessage());
            return;
        }

        // 执行 Ansible playbook 并将输出实时写入临时文件
        String playbookYml = "etc/script/playbook-k8s.yml";
        String playbookPath = transferLocalFile(playbookYml);
        if (!FileUtil.exist(playbookPath)) {
            logger.error("Playbook {} not exists", playbookPath);
            return;
        }
        K8SNodeUtil.replaceInFile(playbookPath, NODE_DEPLOY_PLACEHOLDER, ipGroup);
        // 输出日志到/tmp/${ip}.log
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(NODE_LOG_PREFIX + filename))) {
            ProcessBuilder pb = new ProcessBuilder("ansible-playbook", playbookPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Fail to execute ansible-playbook, {}", e.getMessage());
            return;
        }

        try {
            // 恢复playbook.yml
            K8SNodeUtil.replaceInFile(playbookPath, ipGroup, NODE_DEPLOY_PLACEHOLDER);
            // 清理 /etc/ansible/hosts 文件中追加的用户账密信息
            String content = new String(Files.readAllBytes(Paths.get(ansibleHostPath)));
            content = content.replace(hostEntry, "");
            Files.write(Paths.get(ansibleHostPath), content.getBytes());
        } catch (IOException e) {
            logger.error("Fail to revert ansibleHostPath, {}", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 查询节点安装日志，路径/tmp/${ip}.log
     * 目前只支持单节点添加，所以文件名能区分
     * 后续可能一个ansibel并发执行多个ip, 无法通过文件名区分
     * @param ip
     */
    @Override
    public String queryAnsibleLog(String ip) {
        String logFilePath = NODE_LOG_PREFIX + ip + ".log";
        StringBuilder logContent = new StringBuilder();
        if (Files.notExists(Paths.get(logFilePath))) {
            logger.error("File not exists, {}", logFilePath);
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                logContent.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("Failed to read ansibel log of {}, {}", ip, e.getMessage());
        }
        return logContent.toString();
    }
}
