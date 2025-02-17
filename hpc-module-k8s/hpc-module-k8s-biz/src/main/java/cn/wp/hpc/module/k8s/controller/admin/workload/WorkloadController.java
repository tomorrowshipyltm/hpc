package cn.wp.hpc.module.k8s.controller.admin.workload;

import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.module.k8s.service.workload.WorkloadService;
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
public class WorkloadController {

    @Autowired
    WorkloadService workloadService;

    /**
     * 获取workload列表
     * @param cluster
     * @param type: deployment、daemonset、statefulset
     * @param keyword  搜索关键字
     */
    @GetMapping("/workload/list")
    @Operation(summary = "获取deploy/sts/daemonset列表")
    public CommonResult<Object> getWorkloads(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster) || StrUtil.isBlank(type)) {
                return error(BAD_REQUEST, "cluster and type is required");
            }
            Map<String, Object> result = workloadService.getWorkloads(cluster, type, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取workload详情
     * @param cluster
     * @param type: deployment、daemonset、statefulset
     * @param namespace
     * @param name
     */
    @GetMapping("/workload/detail")
    @Operation(summary = "获取deployment详情")
    public CommonResult<Object> getWorkloadDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name") String name)
    {
        try {
            if (StrUtil.isBlank(cluster) || StrUtil.isBlank(type)) {
                return error(BAD_REQUEST, "cluster and type is required");
            }
            Object result = workloadService.getWorkloadDetail(cluster, type, namespace, name);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取pod list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param node 可选字段，用于节点上podlist
     * @param keyword 搜索关键字
     */
    @GetMapping("/pod/list")
    @Operation(summary = "获取pod列表")
    public CommonResult<Object> getPods(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "node", required = false)  String node,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = workloadService.getPods(cluster, node, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/pod/detail")
    @Operation(summary = "获取pod详情")
    public CommonResult<Object> getPodDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(workloadService.getPodDetail(cluster, namespace, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取service list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    @GetMapping("/service/list")
    @Operation(summary = "获取service列表")
    public CommonResult<Object> getServices(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = workloadService.getServices(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/service/detail")
    @Operation(summary = "获取service详情")
    public CommonResult<Object> getServiceDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(workloadService.getServiceDetail(cluster, namespace, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }


    /**
     * 获取节点event事件
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param node
     * @return
     */
    @GetMapping("/event/list")
    @Operation(summary = "获取event列表")
    public CommonResult<Object> getEvents(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "node") String node,
            @RequestParam(value = "no", required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize)
    {
        try {
            if (StrUtil.isBlank(cluster) || StrUtil.isBlank(node)) {
                return error(BAD_REQUEST, "cluster or node is required");
            }
            Map<String, Object> result = workloadService.getEvents(cluster, node, pageNo, pageSize);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

}
