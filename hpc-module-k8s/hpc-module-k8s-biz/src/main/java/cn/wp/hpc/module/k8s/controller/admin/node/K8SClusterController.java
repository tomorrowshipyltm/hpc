package cn.wp.hpc.module.k8s.controller.admin.node;

import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.module.k8s.service.node.K8SClientFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static cn.wp.hpc.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - k8s集群管理")
@RestController
@RequestMapping("/kubernetes/cluster")
@Validated
public class K8SClusterController {

    @Autowired
    K8SClientFactory k8SClientFactory;

    @GetMapping("/list")
    @Operation(summary = "获取集群列表")
    @PreAuthorize("@ss.hasPermission('infra:redis:get-monitor-info')")
    public CommonResult<Object> getK8SClusters() {
        return success(k8SClientFactory.getK8SClusters());
    }

}
