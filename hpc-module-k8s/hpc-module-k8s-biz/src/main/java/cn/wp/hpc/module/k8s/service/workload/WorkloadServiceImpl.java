package cn.wp.hpc.module.k8s.service.workload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.module.k8s.model.workload.EventInfo;
import cn.wp.hpc.module.k8s.model.workload.PodInfo;
import cn.wp.hpc.module.k8s.model.workload.ServiceInfo;
import cn.wp.hpc.module.k8s.model.workload.WorkloadInfo;
import cn.wp.hpc.module.k8s.service.node.K8SClientFactory;
import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.wp.hpc.module.k8s.enums.K8SConstant.*;

@Service("k8sWorkloadService")
public class WorkloadServiceImpl implements WorkloadService {
    final static private Logger logger = LoggerFactory.getLogger(WorkloadService.class);

    @Autowired
    K8SClientFactory k8SClientFactory;

    @Override
    public Map<String, Object> getWorkloads(String cluster, String type, int pageNo, int pageSize, String keyword) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        List<?> itemList = null;
        List<WorkloadInfo> workloadInfoList = new ArrayList<>();
        if (WORKLOAD_DAEMONSET.equalsIgnoreCase(type)) {
            itemList = k8sClient.apps().daemonSets().list().getItems();
        } else if (WORKLOAD_STATEFULSET.equalsIgnoreCase(type)) {
            itemList = k8sClient.apps().statefulSets().list().getItems();
        } else {
            itemList = k8sClient.apps().deployments().list().getItems();
        }

        if (CollUtil.isNotEmpty(itemList)) {
            workloadInfoList = itemList.stream().map(item -> mapToWorkloadInfo(item)).collect(Collectors.toList());
        }
        return K8SNodeUtil.pageHelper(workloadInfoList, pageNo, pageSize, keyword);
    }

    private WorkloadInfo mapToWorkloadInfo(Object item) {
        // 根据实际情况转换特定类型的对象到 WorkloadInfo
        if (item instanceof DaemonSet) {
            DaemonSet ele = (DaemonSet) item;
            return new WorkloadInfo(ele.getMetadata().getName(), ele.getMetadata().getNamespace(),
                    ele.getMetadata().getCreationTimestamp(), ele.getStatus().getDesiredNumberScheduled(), ele.getStatus().getNumberReady());
        } else if (item instanceof StatefulSet) {
            StatefulSet ele = (StatefulSet) item;
            return new WorkloadInfo(ele.getMetadata().getName(), ele.getMetadata().getNamespace(),
                    ele.getMetadata().getCreationTimestamp(), ele.getStatus().getReplicas(), ele.getStatus().getReadyReplicas());
        }
        Deployment ele = (Deployment) item;
        return new WorkloadInfo(ele.getMetadata().getName(), ele.getMetadata().getNamespace(),
                ele.getMetadata().getCreationTimestamp(), ele.getStatus().getReplicas(), ele.getStatus().getReadyReplicas());
    }

    @Override
    public Object getWorkloadDetail(String cluster, String type, String namespace, String name) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            if (WORKLOAD_DAEMONSET.equalsIgnoreCase(type)) {
                return k8sClient.apps().daemonSets().inNamespace(namespace).withName(name).get();
            } else if (WORKLOAD_STATEFULSET.equalsIgnoreCase(type)) {
                return k8sClient.apps().statefulSets().inNamespace(namespace).withName(name).get();
            } else {
                return k8sClient.apps().deployments().inNamespace(namespace).withName(name).get();
            }
        } catch (Exception e) {
            logger.error("Failed to get workload detail, {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> getPods(String cluster, String nodeName, int pageNo, int pageSize, String keyword) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<Pod> podList = k8sClient.pods().list().getItems();
            // 指定节点查询
            if (StrUtil.isNotBlank(nodeName)) {
                podList = k8sClient.pods().withField(LABEL_SPEC_NODENAME, nodeName).list().getItems();
            }
            List<PodInfo> podInfoList = podList.stream().map(ele -> {
                PodInfo info = new PodInfo(ele.getMetadata().getName(), ele.getMetadata().getNamespace(),
                        ele.getStatus().getPhase(), ele.getMetadata().getCreationTimestamp());
                info.setPodIp(ele.getStatus().getPodIP());
                String node = ele.getSpec().getNodeName() + "(" + ele.getStatus().getHostIP() + ")";
                info.setNode(node);
                return info;
            }).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(podInfoList, pageNo, pageSize, keyword);
        } catch (Exception e) {
            logger.error("Failed to get pods for {}, {}", cluster, e.getMessage());
            return null;
        }
    }

    @Override
    public Pod getPodDetail(String cluster, String namespace, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.pods().inNamespace(namespace).withName(name).get();
    }

    @Override
    public Map<String, Object> getServices(String cluster, int pageNo, int pageSize, String keyword) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<io.fabric8.kubernetes.api.model.Service> serviceList = k8sClient.services().list().getItems();
            List<ServiceInfo> serviceInfoList = serviceList.stream().map(ele -> {
                ServiceInfo info = new ServiceInfo(ele.getMetadata().getName(), ele.getMetadata().getNamespace(),
                        ele.getSpec().getClusterIP(), ele.getMetadata().getCreationTimestamp());
                // todo 设置外部ip，比如nodePort、ingress
                return info;
            }).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(serviceInfoList, pageNo, pageSize, keyword);
        } catch (Exception e) {
            logger.error("Failed to get services for {}, {}", cluster, e.getMessage());
            return null;
        }
    }

    @Override
    public io.fabric8.kubernetes.api.model.Service getServiceDetail(String cluster, String namespace, String name) {
        KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
        return k8sClient.services().inNamespace(namespace).withName(name).get();
    }

    @Override
    public Map<String, Object> getEvents(String cluster, String node, int pageNo, int pageSize) {
        try {
            KubernetesClient k8sClient = k8SClientFactory.getClient(cluster);
            List<Event> eventList = k8sClient.v1().events().inAnyNamespace().withField("involvedObject.kind", "Node")
                    .withField("involvedObject.name", node).list().getItems();
            List<EventInfo> infoList = eventList.stream().map(ele ->
                new EventInfo(ele.getType(), ele.getReason(), ele.getLastTimestamp(),
                        ele.getSource().getComponent(), ele.getMessage())).collect(Collectors.toList());
            return K8SNodeUtil.pageHelper(infoList, pageNo, pageSize, null);
        } catch (Exception e) {
            logger.error("Failed to get events for {}, {}", node, e.getMessage());
            return null;
        }
    }

}
