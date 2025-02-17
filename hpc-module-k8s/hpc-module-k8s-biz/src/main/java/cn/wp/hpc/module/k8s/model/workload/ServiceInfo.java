package cn.wp.hpc.module.k8s.model.workload;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Service信息前端展示
 */
@Data
@NoArgsConstructor
public class ServiceInfo {
    private String name;
    private String namespace;
    private String gmtCreate;
    // 内部访问ip
    private String clusterIp;
    // 外部访问ip
    private String publicIp;

    public ServiceInfo(String name, String namespace, String clusterIp, String gmtCreate) {
        this.name = name;
        this.namespace = namespace;
        this.clusterIp = clusterIp;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
    }

}
