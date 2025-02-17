package cn.wp.hpc.module.k8s.service.workload;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;

import java.util.Map;

/**
 * 应用负载接口, 包括deployment、statefulset、daemonset等
 */
public interface WorkloadService {
    /**
     * 获取工作负载列表
     * @param cluster
     * @param type 包括3种：deployment、statefulset、daemonset
     * @param pageNo
     * @param pageSize
     * @param keyword
     * @return
     */
    Map<String, Object> getWorkloads(String cluster, String type, int pageNo, int pageSize, String keyword);

    /**
     * 获取指定namespace下workload详情
     * @param cluster
     * @param type : deployment 、statefulset、daemonset
     * @param namespace
     * @param name
     */
    Object getWorkloadDetail(String cluster, String type, String namespace, String name);

    /**
     * 获取pod list
     * @param cluster
     * @param nodeName 可选字段
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     * @return
     */
    Map<String, Object> getPods(String cluster, String nodeName, int pageNo, int pageSize, String keyword);

    /**
     * 获取pod详情
     * @param cluster
     * @param namespace
     * @param name
     */
    Pod getPodDetail(String cluster, String namespace, String name);


    /**
     * 获取service list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    Map<String, Object> getServices(String cluster, int pageNo, int pageSize, String keyword);

    /**
     * 获取service详情
     *
     * @param cluster
     * @param namespace
     * @param name
     */
    Service getServiceDetail(String cluster, String namespace, String name);


    /**
     * 获取event list
     * @param cluster
     * @param node
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> getEvents(String cluster, String node, int pageNo, int pageSize);

}
