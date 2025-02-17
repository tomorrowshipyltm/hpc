package cn.wp.hpc.module.k8s.enums;

public interface K8SConstant {
    /** k8s cluster **/
    String CLUSTER_TYPE_K8S = "kubernetes";
    String KUBECONFIG_KEY = "kubeconfig";

    /** k8s node list **/
    String LABEL_NODE_ROLE_MASTER = "node-role.kubernetes.io/master";
    String LABEL_NODE_ROLE_WORKER = "node-role.kubernetes.io/worker";
    String LABEL_SPEC_NODENAME = "spec.nodeName";
    // 执行命令lscpu，查看model.name
    /**  cpu厂商, lscpu 查看model  **/
    String LABEL_CPU_BRAND = "cpumodel";
    String LABEL_CPU_ARCH = "kubernetes.io/arch";
    String LABEL_CPU_DEFAULT = "default";
    String LABEL_VALUE_NULL = "无";

    // 加速卡
    String LABEL_ACCELERATOR = "accelerator";
    String LABEL_ACCELERATOR_SPLITER = "-";

    // 资源
    String NODE_RESOURCE_CPU = "cpu";
    String NODE_RESOURCE_MEM = "memory";
    String NODE_RESOURCE_DISK = "ephemeral-storage";
    String NODE_RESOURCE_PODS = "pods";
    String NODE_RESOURCE_BAIDU = "baidu.com";
    String NODE_RESOURCE_HUAWEI = "huawei.com";
    String NODE_RESOURCE_CAMBRICON = "cambricon.com";
    String NODE_RESOURCE_ALLOCATABLE = "allocatable";
    String NODE_RESOURCE_REQUEST = "request";
    String NODE_RESOURCE_LIMIT = "limit";
    String NODE_RESOURCE_SPLITER = "/";

    /**
     * 节点状态有3种：Unknown、Ready、NotReady
     * 为了区分 部署中，新增一种状态 Deploying, reason: NodeStatusNeverUpdated
     */
    String CONDITION_TYPE_READY = "Ready";
    String CONDITION_STATUS_TRUE = "True";
    String CONDITION_STATUS_FALSE = "False";
    String NODE_STATUS_READY = "Ready";
    String NODE_STATUS_NOTREADY = "NotReady";
    String NODE_STATUS_UNKNOWN = "Unknown";
    String NODE_STATUS_DEPLOYING = "Deploying";
    String CONDITION_REASON_NEVER_UPDATED = "NodeStatusNeverUpdated";
    String NODE_LOG_PREFIX = "/tmp/";
    String NODE_DEPLOY_PLACEHOLDER = "PLACEHOLDER";

    /** k8s workload list **/
    String WORKLOAD_DEPLOYMENT = "deployment";
    String WORKLOAD_DAEMONSET = "daemonset";
    String WORKLOAD_STATEFULSET = "statefulset";

    String PVC_PHASE_BOUND = "Bound";
    String STORAGE_CLASS_DEFAULT = "storageclass.beta.kubernetes.io/is-default-class";
}
