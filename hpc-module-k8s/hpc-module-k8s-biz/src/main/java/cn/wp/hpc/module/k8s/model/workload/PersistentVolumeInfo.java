package cn.wp.hpc.module.k8s.model.workload;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * PersistentVolume信息前端展示
 */
@Data
@NoArgsConstructor
public class PersistentVolumeInfo {
    private String name;
    private String capacity;
    private String status;
    // 访问模式
    private String accessMode;
    // 回收机制
    private String reclaim;
    private String gmtCreate;

    public PersistentVolumeInfo(String name, String status, String gmtCreate) {
        this.name = name;
        this.status = status;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
    }

}
