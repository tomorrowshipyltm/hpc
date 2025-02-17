package cn.wp.hpc.module.k8s.model.workload;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StorageClass信息前端展示
 */
@Data
@NoArgsConstructor
public class StorageClassInfo {
    private String name;
    // 供应者
    private String provisioner;
    // 默认存储类
    private Boolean defaultClass = false;
    private String reclaimPolicy;
    private String volumeBindingMode;
    private String gmtCreate;

    public StorageClassInfo(String name, String provisioner, String gmtCreate) {
        this.name = name;
        this.provisioner = provisioner;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
    }
}
