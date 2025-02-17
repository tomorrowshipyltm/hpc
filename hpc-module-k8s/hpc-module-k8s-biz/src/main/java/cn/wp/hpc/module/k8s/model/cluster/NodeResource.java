package cn.wp.hpc.module.k8s.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeResource {
    private String key;
    private String value;
    // 中文描述
    private String mark;
}
