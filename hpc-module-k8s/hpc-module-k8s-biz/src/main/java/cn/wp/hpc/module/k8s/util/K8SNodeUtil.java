package cn.wp.hpc.module.k8s.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.module.k8s.model.cluster.K8SNode;
import cn.wp.hpc.module.k8s.model.cluster.NodeResource;
import io.fabric8.kubernetes.api.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static cn.wp.hpc.module.k8s.enums.K8SConstant.*;


/**
 * 将原始node 转换为前端所需K8SNode
 */
@Slf4j
public class K8SNodeUtil {
    static final Logger logger = LoggerFactory.getLogger(K8SNodeUtil.class);
    // cpuModel、加速卡和资源描述
    static final Map<String, String> cpuBrandMap = new HashMap<>();
    static final Map<String, String> acceleratorMap = new HashMap<>();
    static final Map<String, String> resourceMark = new HashMap<>();

    static {
        // 在静态代码块中初始化，合同只要求前2种
        cpuBrandMap.put("kunpeng", "鲲鹏");
        cpuBrandMap.put("phytium", "飞腾");
        cpuBrandMap.put("haiguang", "海光");
        cpuBrandMap.put("shenwei", "申威");
        cpuBrandMap.put("default", "arm64");

        /**
         * 昆仑芯 xpu-r200, 后续可能有xpu-r300
         * 寒武纪 mlu-370x8
         * 昇腾 npu-910prob
         */
        acceleratorMap.put("npu", "昇腾");
        acceleratorMap.put("xpu", "昆仑芯");
        acceleratorMap.put("mlu", "寒武纪");
        acceleratorMap.put("default", "无");

        /**
         * 资源Allocatable:
         * 昆仑芯 baidu.com/xpu
         * 寒武纪 cambricon.com/mlu370
         * 昇腾  huawei.com/Ascend910
         */
        resourceMark.put(NODE_RESOURCE_CPU, "CPU");
        resourceMark.put(NODE_RESOURCE_MEM, "内存");
        resourceMark.put(NODE_RESOURCE_DISK, "磁盘");
        resourceMark.put(NODE_RESOURCE_BAIDU, "昆仑芯");
        resourceMark.put(NODE_RESOURCE_HUAWEI, "昇腾");
        resourceMark.put(NODE_RESOURCE_CAMBRICON, "寒武纪");
    }

    public static K8SNode parseK8SNode(Node node) {
        if (node == null) {
            return null;
        }

        try {
            String name = node.getMetadata().getName();
            String internalIP = node.getStatus().getAddresses().get(0).getAddress();
            String role = "worker";
            Map<String, String> labelMap = node.getMetadata().getLabels();
            if (labelMap.containsKey(LABEL_NODE_ROLE_MASTER)) {
                role = "master";
            }
            // 节点真实状态
            String status = getNodeStatus(node);
            Boolean unschedulable = node.getSpec().getUnschedulable();
            boolean schdule = true;
            if (unschedulable != null && unschedulable == true) {
                schdule = false;
            }
            String os = node.getStatus().getNodeInfo().getOperatingSystem();
            String osImage = node.getStatus().getNodeInfo().getOsImage();

            // cpuModel和加速卡标签
            String cpuBrand = node.getStatus().getNodeInfo().getArchitecture();
            if (labelMap.containsKey(LABEL_CPU_BRAND)) {
                cpuBrand = cpuBrandMap.getOrDefault(labelMap.get(LABEL_CPU_BRAND), "");
            }
            String accelerator = labelMap.getOrDefault(LABEL_ACCELERATOR, LABEL_CPU_DEFAULT);
            String[] acceleratorArray = accelerator.split(LABEL_ACCELERATOR_SPLITER);
            if (acceleratorArray.length > 1) {
                accelerator = acceleratorArray[0];
            }
            // 资源capacity
            Map<String, Quantity> capacity = node.getStatus().getCapacity();
            // feedback
            String architecture = node.getStatus().getNodeInfo().getArchitecture();
            String kernelVersion = node.getStatus().getNodeInfo().getKernelVersion();
            String containerRuntimeVersion = node.getStatus().getNodeInfo().getContainerRuntimeVersion();
            String kubeletVersion = node.getStatus().getNodeInfo().getKubeletVersion();
            String kubeProxyVersion = node.getStatus().getNodeInfo().getKubeProxyVersion();
            String creationTimestamp = node.getMetadata().getCreationTimestamp();
            K8SNode result = new K8SNode.Builder(name, internalIP).role(role).status(status)
                    .schedule(schdule).os(os).cpuBrand(cpuBrand)
                    .accelerator(acceleratorMap.getOrDefault(accelerator, LABEL_VALUE_NULL)).capacity(capacity.toString())
                    .osImage(osImage).architecture(architecture).kernelVersion(kernelVersion)
                    .containerRuntimeVersion(containerRuntimeVersion).kubeletVersion(kubeletVersion)
                    .kubeProxyVersion(kubeProxyVersion).creationTimestamp(creationTimestamp)
                    .labels(node.getMetadata().getLabels())
                    .taints(node.getSpec().getTaints())
                    .build();
            return result;
        } catch (Exception e) {
            logger.error("Fail to parse node to k8sNode, err={}", e.getMessage());
            return null;
        }
    }

    /**
     * 分页，并且支持模糊匹配
     * @param list ，使用泛型接收任何list类型
     * @param pageNo
     * @param pageSize
     * @param keyword  搜索关键字
     * @return
     */
    public static Map<String, Object> pageHelper(List<?> list, int pageNo, int pageSize, String keyword) {
        Map<String, Object> result = new HashMap<>();
        if (CollUtil.isEmpty(list)) {
            result.put("list", Collections.emptyList());
            result.put("total", 0);
            result.put("pageNo", pageNo);
            result.put("pageSize", pageSize);
            return result;
        }

        // 模糊匹配
        List<?> filteredList = list;
        if (StrUtil.isNotBlank(keyword)) {
            filteredList = list.stream()
                    .filter(item -> StrUtil.contains(item.toString(), keyword))
                    .collect(Collectors.toList());
        }

        // 分页逻辑
        int total = filteredList.size();
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<?> pageList = filteredList.subList(start, end);
        result.put("list", pageList);
        result.put("total", total);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);

        return result;
    }
//
//    public static Map<String, Object> pageHelper(List<?> list, int pageNo, int pageSize, String keyword) {
//        Map<String, Object> result = new HashMap<>();
//        if (CollUtil.isEmpty(list)) {
//            return result;
//        }
//        List<?> filteredList = list;
//        if (StrUtil.isNotBlank(keyword)) {
//            // 由于workload 和 configmap 都重写了toString()，相当于全文匹配
//            filteredList = list.stream().filter(item -> item.toString().contains(keyword)).collect(Collectors.toList());
//        }
//        // 分页 TODO
//        Pagination pagination = new Pagination(filteredList.size(), pageNo, pageSize);
//        result.put("pagination",pagination);
//        if( pagination.getCount() == 0 || pageNo > pagination.getPageNumber() ) {
//            result.put("list", Collections.emptyList());
//        } else {
//            final int first = (pagination.getPageNo()-1)*pagination.getPageSize();
//            final int last = Math.min(first+pagination.getPageSize(), pagination.getCount());
//            result.put("list", filteredList.subList(first, last).stream().collect(Collectors.toList()));
//        }
//        return result;
//    }

    /**
     * 将 "2024-10-09T01:14:24Z"  转为 2024-10-09 09:14:24
     * @param isoTimestamp
     * @return
     */
    public static String convertTimestamp(String isoTimestamp) {
        // 将时间转换为东八区（北京时间）
        ZonedDateTime zdt = ZonedDateTime.parse(isoTimestamp);
        ZonedDateTime zdtBj = zdt.withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return zdtBj.format(formatter);
    }

    /**
     * 判断节点是否可被删除
     * @param node
     */
    public static boolean shouldDelete(Node node) {
        // 禁止删除master
        Map<String, String> labelMap = node.getMetadata().getLabels();
        if (CollUtil.isNotEmpty(labelMap) && labelMap.containsKey(LABEL_NODE_ROLE_MASTER)) {
            return false;
        }
        // other filter
        return true;
    }

    /**
     * 获取节点状态，包括Ready、NotReady、Unknown
     * 新增一种状态 Deploying 表示部署中
     * @param node
     */
    public static String getNodeStatus(Node node) {
        if (node == null) {
            return NODE_STATUS_DEPLOYING;
        }
        for (NodeCondition condition : node.getStatus().getConditions()) {
            if (CONDITION_TYPE_READY.equals(condition.getType())) {
                if (CONDITION_STATUS_TRUE.equals(condition.getStatus())) {
                    return NODE_STATUS_READY;
                } else if (CONDITION_STATUS_FALSE.equals(condition.getStatus())) {
                    return NODE_STATUS_NOTREADY;
                } else {
                    /**
                     *     reason: NodeStatusNeverUpdated
                     *     status: Unknown
                     *     type: Ready
                     */
                    return NODE_STATUS_DEPLOYING;
                }
            }
        }
        return NODE_STATUS_DEPLOYING;
    }

    /**
     * 特殊字符转义，例如 \#
     * @param password
     * @return
     */
    public static String escapeSpecialCharacters(String password) {
        StringBuilder sb = new StringBuilder();
        for (char c : password.toCharArray()) {
            if (isSpecialCharacter(c)) {
                sb.append("\\").append(c);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isSpecialCharacter(char c) {
        return c == '#';
    }

    /**
     * 替换文件中 placeholder
     * @param filePath
     * @param placeholder
     * @param replacement
     */
    public static void replaceInFile(String filePath, String placeholder, String replacement) {
        try {
            File file = new File(filePath);
            String content = FileUtils.readFileToString(file, "UTF-8");
            content = content.replace(placeholder, replacement);
            FileUtils.writeStringToFile(file, content, "UTF-8");
        } catch (IOException e) {
            logger.error("Failed to replace placeholder in file {}, {}", filePath, e.getMessage());
        }
    }

    /**
     * 获取节点资源，包括Allocatable、Allocated Request/Limit
     * @param node
     * @param podList
     */
    public static List<NodeResource> getNodeResource(Node node, List<Pod> podList){
        List<NodeResource> result = new ArrayList<>();
        // 资源可使用量
        Map<String, Quantity> allocatable = node.getStatus().getAllocatable();
        if (allocatable.isEmpty()) {
            logger.info("Allocatable of {} is empty", node.getMetadata().getName());
            return result;
        }
        for (Map.Entry<String, Quantity> entry : allocatable.entrySet()) {
            String mark = getResourceMark(entry.getKey(), NODE_RESOURCE_ALLOCATABLE);
            if (StrUtil.isNotBlank(mark)) {
                NodeResource nodeResource = new NodeResource(entry.getKey(), entry.getValue().toString(), mark);
                result.add(nodeResource);
            }
        }
        // 已分配资源
        Map<String, BigDecimal> totalRequests = new HashMap<>();
        Map<String, BigDecimal> totalLimits = new HashMap<>();
        for (Pod pod : podList) {
            for (Container container : pod.getSpec().getContainers()) {
                addResources(container.getResources().getRequests(), totalRequests);
                addResources(container.getResources().getLimits(), totalLimits);
            }
        }
        // Calculate resource usage percentages
        for (Map.Entry<String, Quantity> entry : allocatable.entrySet()) {
            String resourceType = entry.getKey();
            BigDecimal requested = totalRequests.getOrDefault(resourceType, BigDecimal.ZERO);
            BigDecimal limited = totalLimits.getOrDefault(resourceType, BigDecimal.ZERO);
            String requestPercentage = calculatePercentage(entry.getValue().getNumericalAmount(), requested);
            String limitPercentage = calculatePercentage(entry.getValue().getNumericalAmount(), limited);

            // Create NodeResource for requests
            String mark = getResourceMark(resourceType, NODE_RESOURCE_REQUEST);
            if (StrUtil.isNotBlank(mark)) {
                String requestValueWithPercentage = formatResourceValueWithPercentage(resourceType, requested, requestPercentage);
                NodeResource nodeResourceRequests = new NodeResource(
                        resourceType + "_Request",
                        requestValueWithPercentage,
                        getResourceMark(resourceType, NODE_RESOURCE_REQUEST)
                );
                result.add(nodeResourceRequests);
            }
            // Create NodeResource for limits
            mark = getResourceMark(resourceType, NODE_RESOURCE_LIMIT);
            if (StrUtil.isNotBlank(mark)) {
                String limitValueWithPercentage = formatResourceValueWithPercentage(resourceType, limited, limitPercentage);
                NodeResource nodeResourceLimits = new NodeResource(
                        resourceType + "_Limit",
                        limitValueWithPercentage,
                        getResourceMark(resourceType, NODE_RESOURCE_LIMIT)
                );
                result.add(nodeResourceLimits);
            }
        }
        return result;
    }

    static void addResources(Map<String, Quantity> resources, Map<String, BigDecimal> totalResources) {
        for (Map.Entry<String, Quantity> entry : resources.entrySet()) {
            BigDecimal current = totalResources.get(entry.getKey());
            if (current == null) {
                totalResources.put(entry.getKey(), entry.getValue().getNumericalAmount());
            } else {
                current = current.add(entry.getValue().getNumericalAmount());
                totalResources.put(entry.getKey(), current);
            }
        }
    }

    static String calculatePercentage(BigDecimal total, BigDecimal used) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return "0%";
        }
        BigDecimal percentage = used.divide(total, 2, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100));
        return percentage.setScale(0, RoundingMode.HALF_UP) + "%";
    }

    // 内存换算单位Mi，小数点后保留0位
    static BigDecimal convertMemoryToMi(BigDecimal memory) {
        return memory.divide(new BigDecimal(1024 * 1024), 0, RoundingMode.FLOOR);
    }

    static String formatResourceValueWithPercentage(String resourceType, BigDecimal value, String percentage) {
        String unit = getUnitForResourceType(resourceType);
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            unit = "";
        }
        String formattedValue = value + unit;
        if (NODE_RESOURCE_CPU.equals(resourceType)) {
            BigDecimal tmpValue = value.multiply(new BigDecimal(1000));
            // 不需要小数
            formattedValue = tmpValue.setScale(0, RoundingMode.FLOOR) + "m";
        }
        if (NODE_RESOURCE_MEM.equals(resourceType)) {
            formattedValue = convertMemoryToMi(value) + "Mi";
        }
        return formattedValue + " (" + percentage + ")";
    }
    static String getUnitForResourceType(String resourceType) {
        switch (resourceType) {
            case NODE_RESOURCE_CPU:
                return "m";
            case NODE_RESOURCE_MEM:
                return "Mi";
            case NODE_RESOURCE_DISK:
                return "Ki";
            default:
                return "";
        }
    }

    // 返回资源的中文描述
    static String getResourceMark(String key, String type) {
        // 过滤
        String[] skipAllocated = new String[]{"hugepage", "real-mlu"};
        for (String ele : skipAllocated) {
            if (key.contains(ele)) {
                return null;
            }
        }
        // pod和加速卡无需计算Request/Limit
        String[] skipRequests = new String[]{NODE_RESOURCE_PODS, NODE_RESOURCE_HUAWEI, NODE_RESOURCE_BAIDU, NODE_RESOURCE_CAMBRICON};
        if (!type.equals("allocatable")) {
            for (String ele : skipRequests) {
                if (key.contains(ele)) {
                    return null;
                }
            }
        }
        // 拼接
        if (type.equalsIgnoreCase("request")) {
            return resourceMark.getOrDefault(key, key) + "请求";
        } else if (type.equalsIgnoreCase("limit")) {
            return resourceMark.getOrDefault(key, key) + "上限";
        }

        // 当Allocatable中key=huawei.com/Ascend910，返回 昇腾/Ascend910容量
        String[] keyArray = key.split(NODE_RESOURCE_SPLITER);
        if (keyArray.length > 1) {
            key = keyArray[0];
            String suffix = keyArray[1];
            return resourceMark.getOrDefault(key, key) + "/" + suffix + "容量";
        } else {
            return resourceMark.getOrDefault(key, key) + "容量";
        }
    }

    /** 获取文件绝对路径 **/
    public static String transferLocalFile(String localFileName) {
        try {
            // 尝试从 classpath 加载资源
            Resource resource = new ClassPathResource(localFileName);
            if (resource.exists() && resource.isReadable()) {
                // 如果资源存在且可读，返回其文件路径
                return resource.getFile().getAbsolutePath();
            } else {
                // 如果资源不存在，尝试作为普通文件路径处理
                File file = new File(localFileName);
                if (file.isFile()) {
                    return file.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            // 记录异常信息
            log.error("Failed to get file path for: {}", localFileName, e);
        }
        // 如果无法获取路径，返回原始路径
        return localFileName;
    }
//    public static String transferLocalFile(String localFileName) {
//        File file = new File(localFileName);
//        if (file.isFile()) {
//            return localFileName;
//        }
//        if (!localFileName.startsWith(File.separator)) {
//            try {
//                LauncherService launcherService = SpringBeanUtil.getBean(LauncherService.class);
//                String realPath = launcherService.getRealPath(Initializer.MODNAME, localFileName);
//                file = new File(realPath);
//                if (file.isFile()) {
//                    return file.getAbsolutePath();
//                }
//            } catch (Exception e) {
//                return localFileName;
//            }
//        }
//        return localFileName;
//    }

}
