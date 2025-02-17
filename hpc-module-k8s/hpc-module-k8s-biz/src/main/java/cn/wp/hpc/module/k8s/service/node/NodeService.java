package cn.wp.hpc.module.k8s.service.node;

import cn.wp.hpc.module.k8s.model.cluster.K8SNode;
import cn.wp.hpc.module.k8s.model.cluster.NodeResource;
import io.fabric8.kubernetes.api.model.Node;

import java.util.List;
import java.util.Map;

/**
 * k8s节点操作接口
 */
public interface NodeService {
    /**
     * 获取k8s节点列表
     * @param cluster
     */
    Map<String, Object> getNodeList(String cluster, int pageNo, int pageSize);

    /**
     * 查看节点列表，支持模糊搜索
     * @param cluster
     * @param pageNo
     * @param pageSize，以下都是可选字段
     * @param name
     * @param osImage
     * @param cpuBrand
     * @param accelerator
     */
    Map<String, Object> queryNodeList(String cluster, int pageNo, int pageSize,
                                      String name, String osImage, String cpuBrand, String accelerator);

    /**
     * 获取节点详情
     * @param cluster  集群名
     * @param nodeName
     */
    Node getNodeDetail(String cluster, String nodeName);

    /**
     * 根据nodeName删除节点
     * @param cluster
     * @param name
     */
    void deleteNode(String cluster, String name);

    /**
     * 编辑节点，启停调度、节点打标、打污点 本质都是patch node，一个restful api只是通过Request payload区分
     * @param cluster
     * @param name
     * @param payload ，停止调度 {"spec":{"unschedulable":true}}
     */
    void patchNode(String cluster, String name, String payload);

    /**
     * 编辑节点后 回显详情，突出label和taint
     * @param cluster  集群名
     * @param nodeName
     */
    K8SNode getNodeFeedback(String cluster, String nodeName);

    /**
     * 获取节点资源，包括Allocatable、Allocated Request/Limit
     * @param cluster  集群名
     * @param nodeName
     */
    List<NodeResource> getNodeResource(String cluster, String nodeName);

    /**
     * 添加节点，异步执行ansible-playbook
     * @param node
     */
    void addNodeAsync(K8SNode node);

    /**
     * 检查nodeName合法性
     * @param cluster
     * @param name
     */
    boolean checkNodeName(String cluster, String name);

    /**
     * 查询ansible执行进度
     * @param ip 目前一个ip对应一个文件，后续考虑一个文件对应多ip
     */
    String queryAnsibleLog(String ip);
}
