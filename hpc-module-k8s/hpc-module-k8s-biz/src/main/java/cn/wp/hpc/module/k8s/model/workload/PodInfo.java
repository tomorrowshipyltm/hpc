package cn.wp.hpc.module.k8s.model.workload;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Pod信息前端展示
 */
@Data
@NoArgsConstructor
public class PodInfo {
    private String name;
    private String namespace;
    private String gmtCreate;
    private String status;
    // pod所在节点
    private String node;
    private String podIp;

    public PodInfo(String name, String namespace, String status, String gmtCreate) {
        this.name = name;
        this.namespace = namespace;
        this.status = status;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
    }

}
