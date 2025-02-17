package cn.wp.hpc.module.k8s.controller.admin.workload;

import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.module.k8s.service.workload.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import static cn.wp.hpc.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.wp.hpc.framework.common.pojo.CommonResult.error;
import static cn.wp.hpc.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - k8s集群管理")
@RestController
@RequestMapping("/kubernetes")
@Validated
public class K8SConfigmapController {

    @Autowired
    ConfigService configService;

    /********* Restful api for configmaps ********/
    @GetMapping("/configmap/list")
    @Operation(summary = "获取configmap列表")
    public CommonResult<Object> getConfigmaps(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = configService.getConfigmaps(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/configmap/detail")
    @Operation(summary = "获取configmap详情")
    public CommonResult<Object> getConfigmapDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(configService.getConfigmapDetail(cluster, namespace, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /********* Restful api for secrets ********/
    @GetMapping("/secret/list")
    @Operation(summary = "获取secret列表")
    public CommonResult<Object> getSecrets(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = configService.getSecrets(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/secret/detail")
    @Operation(summary = "获取secret详情")
    public CommonResult<Object> getSecretDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(configService.getSecretDetail(cluster, namespace, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }


    /********* Restful api for serviceAccounts ********/
    @GetMapping("/serviceaccount/list")
    @Operation(summary = "获取serviceAccount列表")
    public CommonResult<Object> getServiceAccounts(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = configService.getServiceAccounts(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/serviceaccount/detail")
    @Operation(summary = "获取serviceAccount详情")
    public CommonResult<Object> getServiceAccountDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(configService.getServiceAccountDetail(cluster, namespace, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

}
