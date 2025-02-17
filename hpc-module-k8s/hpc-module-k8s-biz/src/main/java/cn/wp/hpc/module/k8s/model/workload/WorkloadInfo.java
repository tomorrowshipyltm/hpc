package cn.wp.hpc.module.k8s.model.workload;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 工作负载信息前端展示
 */
@Data
@NoArgsConstructor
public class WorkloadInfo {
    private String name;
    private String namespace;
    private String gmtCreate;
    // 状态
    private String status;

    public WorkloadInfo(String name, String namespace, String gmtCreate) {
        this.name = name;
        this.namespace = namespace;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
    }


    public WorkloadInfo(String name, String namespace, String gmtCreate, Integer replicas, Integer readReplicas) {
        this.name = name;
        this.namespace = namespace;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
        if (replicas == null) {
            replicas = 0;
        }
        if (readReplicas == null) {
            readReplicas = 0;
        }
        String status = String.format("更新中 (%d/%d)", readReplicas, replicas);
        if (readReplicas > 0 && readReplicas.equals(replicas)) {
            status = String.format("运行中 (%d/%d)", readReplicas, replicas);
        }
        this.status = status;
    }
}
