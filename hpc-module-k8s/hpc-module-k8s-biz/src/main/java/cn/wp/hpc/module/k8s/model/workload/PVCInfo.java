package cn.wp.hpc.module.k8s.model.workload;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cn.wp.hpc.module.k8s.enums.K8SConstant.PVC_PHASE_BOUND;


/**
 * PersistentVolumeClaim信息前端展示
 */
@Data
@NoArgsConstructor
public class PVCInfo {
    private String name;
    private String namespace;
    private String pv;
    private String status;
    // 访问模式
    private String accessMode;
    // 挂载状态
    private String mountStatus = "未挂载";
    private String gmtCreate;

    public PVCInfo(String name, String namespace, String status, String gmtCreate) {
        this.name = name;
        this.namespace = namespace;
        this.status = status;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
        if (status.equals(PVC_PHASE_BOUND)) {
            mountStatus = "已挂载";
        }
    }

}
