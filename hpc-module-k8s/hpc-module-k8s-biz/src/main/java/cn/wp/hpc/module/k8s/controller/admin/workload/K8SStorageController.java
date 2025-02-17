package cn.wp.hpc.module.k8s.controller.admin.workload;

import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.module.k8s.service.workload.StorageService;
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
public class K8SStorageController {

    @Autowired
    StorageService storageService;

    /**
     * 获取persistentvolume list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    @GetMapping("/persistentvolume/list")
    @Operation(summary = "获取PV列表")
    public CommonResult<Object> getPersistentvolumes(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = storageService.getPersistentVolumes(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取persistentVolume详情
     * @param cluster
     * @param name
     */
    @GetMapping("/persistentvolume/detail")
    @Operation(summary = "获取PV详情")
    public CommonResult<Object> getPersistentvolumeDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(storageService.getPersistentvolumeDetail(cluster, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取persistentvolumeClaim list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    @GetMapping("/pvc/list")
    @Operation(summary = "获取PVC列表")
    public CommonResult<Object> getPersistentvolumeClaims(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = storageService.getPVCs(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取persistentVolumeClaim详情
     * @param cluster
     * @param namespace
     * @param name
     */
    @GetMapping("/pvc/detail")
    @Operation(summary = "获取PVC详情")
    public CommonResult<Object>  getPersistentvolumeClaimDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "namespace")  String namespace,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(storageService.getPVCDetail(cluster, namespace, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取storageclass list
     * @param cluster
     * @param pageNo
     * @param pageSize
     * @param keyword 搜索关键字
     */
    @GetMapping("/storageclass/list")
    @Operation(summary = "获取storageClass列表")
    public CommonResult<Object> getStorageClasss(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "size") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> result = storageService.getStorageClasss(cluster, pageNo, pageSize, keyword);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取storageClass详情
     * @param cluster
     * @param name
     */
    @GetMapping("/storageclass/detail")
    @Operation(summary = "获取storageClass详情")
    public CommonResult<Object> getStorageClassDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "name")  String name)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST,   "cluster or name is empty");
        }
        try {
            return success(storageService.getStorageClassDetail(cluster, name));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

}
