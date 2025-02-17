package cn.wp.hpc.module.k8s.controller.admin.node;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.module.k8s.model.cluster.K8SNode;
import cn.wp.hpc.module.k8s.model.cluster.NodeResource;
import cn.wp.hpc.module.k8s.service.node.NodeService;
import io.fabric8.kubernetes.api.model.Node;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.wp.hpc.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.wp.hpc.framework.common.pojo.CommonResult.error;
import static cn.wp.hpc.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - k8s集群管理")
@RestController
@RequestMapping("/kubernetes/node")
@Validated
public class K8SNodeController {
    private final Logger logger = LoggerFactory.getLogger(K8SNodeController.class);

    @Autowired
    NodeService nodeService;

    /**
     * 查看节点列表，支持按照name、osImage、cpuBrand、accelerator模糊搜索
     * @param cluster
     * @param pageNo
     * @param name
     * @param osImage
     * @param cpuBrand
     * @param accelerator
     * @param pageSize
     */
    @GetMapping("/list")
    @Operation(summary = "获取节点列表")
    @PreAuthorize("@ss.hasPermission('infra:redis:get-monitor-info')")
    public CommonResult<Object> getNodes(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "no") Integer pageNo,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "osImage", required = false) String osImage,
            @RequestParam(value = "cpuBrand", required = false) String cpuBrand,
            @RequestParam(value = "accelerator", required = false) String accelerator,
            @RequestParam(value = "size") Integer pageSize)
    {
        try {
            if (StrUtil.isBlank(cluster)) {
                return error(BAD_REQUEST, "cluster is required");
            }
            Map<String, Object> nodeList = nodeService.queryNodeList(cluster, pageNo, pageSize, name, osImage, cpuBrand, accelerator);
            return success(nodeList);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/detail")
    @Operation(summary = "获取节点详情")
    public CommonResult<Object> getNodeDetail(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "node")  String nodeName)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(nodeName)) {
            return error(BAD_REQUEST,"cluster or node is empty");
        }
        try {
            Node result = nodeService.getNodeDetail(cluster, nodeName);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /** 批量删除节点列表 **/
    @DeleteMapping("/delete")
    @Operation(summary = "批量删除节点")
    public CommonResult<Object> deleteNodeList(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "ids") String idStr)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(idStr)) {
            return error(BAD_REQUEST, "ids can't be empty");
        }
        String[] ids = idStr.split(",");
        for (String id : ids) {
            try {
                nodeService.deleteNode(cluster, id);
            } catch (Exception e) {
                logger.error("failed to delete node {}: {}", id, e.getMessage());
            }
        }
        return success(idStr);
    }

    /** patch node，包括启停调度、节点打标、打污点 **/
    @PostMapping("/patch")
    @Operation(summary = "节点打标")
    public CommonResult<Object> patchNode(@RequestBody Map<String, Object> requestBody) {
        String cluster = (String) requestBody.get("cluster");
        String nodeName = (String) requestBody.get("node");
        Object payloadMap = requestBody.get("payload");
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(nodeName) || payloadMap == null) {
            return error(BAD_REQUEST, "Invalid request body");
        }

        // Keep null values in serialization，比如删除label 需要保留 "key": null
        JSONConfig jsonConfig = new JSONConfig();
        jsonConfig.setIgnoreNullValue(false);
        JSON payloadJson = JSONUtil.parse(payloadMap, jsonConfig);
        String payload = payloadJson.toString();

        // Validate if the JSON string is valid
        if (!JSONUtil.isJson(payload)) {
            return error(BAD_REQUEST, "Invalid JSON payload");
        }
        try {
            nodeService.patchNode(cluster, nodeName, payload);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
        return success("");
    }


    /**
     * 编辑节点之后 回显，即返回节点详情里的label/taint list
     * @param cluster
     * @param nodeName
     * @return
     */
    @GetMapping("/feedback")
    @Operation(summary = "编辑节点之后回显")
    public CommonResult<Object> getNodeFeedback(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "node")  String nodeName)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(nodeName)) {
            return error(BAD_REQUEST,   "cluster or node is empty");
        }
        try {
            K8SNode result = nodeService.getNodeFeedback(cluster, nodeName);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 获取节点资源容量和使用量
     * @param cluster
     * @param nodeName
     * @return
     */
    @GetMapping("/resource")
    @Operation(summary = "获取节点资源容量")
    public CommonResult<Object> getNodeResource(
            @RequestParam(value = "cluster")  String cluster,
            @RequestParam(value = "node")  String nodeName)
    {
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(nodeName)) {
            return error(BAD_REQUEST,   "cluster or node is empty");
        }
        try {
            List<NodeResource> result = nodeService.getNodeResource(cluster, nodeName);
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 检查nodeName合法性
     * @param requestBody
     * @return
     */
    @PostMapping("/checkname")
    @Operation(summary = "检查nodeName合法性")
    public CommonResult<Object> checkNodeName(@RequestBody Map<String, Object> requestBody) {
        String cluster = (String) requestBody.get("cluster");
        String name = (String) requestBody.get("name");
        if (StrUtil.isBlank(cluster) || StrUtil.isBlank(name)) {
            return error(BAD_REQUEST, "Invalid request body");
        }
        try {
            Map<String, Boolean> result = new HashMap<>();
            result.put("valid", nodeService.checkNodeName(cluster, name));
            return success(result);
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 添加节点，异步执行节点部署
     * @param node
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "添加节点")
    public CommonResult<Object> addNode(@RequestBody K8SNode node) {
        try {
            nodeService.addNodeAsync(node);
            return success("");
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * 查询ansible 安装进度
     * 目前是一个节点对应一个日志文件
     * 后续考虑一个日志文件包含多个ip
     * @param ip
     * @return
     */
    @GetMapping("/progress")
    @Operation(summary = "查询节点ansible安装进度")
    public CommonResult<Object> queryAnsibleProgress(@RequestParam(value = "ip")  String ip) {
        if (StrUtil.isBlank(ip)) {
            return error(BAD_REQUEST,   "ip is null");
        }
        try {
            return success(nodeService.queryAnsibleLog(ip));
        } catch (Exception e) {
            return error(BAD_REQUEST, e.getMessage());
        }
    }
}
