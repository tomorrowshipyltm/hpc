package cn.wp.hpc.module.k8s.model.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单传入的clusterId，需要获取对应clusterName
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class K8SCluster {
    private Long id;
    private String name;
}
