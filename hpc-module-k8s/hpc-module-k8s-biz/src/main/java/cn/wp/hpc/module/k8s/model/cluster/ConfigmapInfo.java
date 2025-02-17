package cn.wp.hpc.module.k8s.model.cluster;

import cn.wp.hpc.module.k8s.util.K8SNodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 配置信息前端展示
 */
@Data
@NoArgsConstructor
public class ConfigmapInfo {
    private String name;
    private String namespace;
    private String gmtCreate;
    // secret类型
    private String type;
    // 字段
    private String data;
    // 字段数量
    private int dataNum;
    // serviceAccount.secret
    private String secretName;

    public ConfigmapInfo(String name, String namespace, String gmtCreate) {
        this.name = name;
        this.namespace = namespace;
        this.gmtCreate = K8SNodeUtil.convertTimestamp(gmtCreate);
    }
}
