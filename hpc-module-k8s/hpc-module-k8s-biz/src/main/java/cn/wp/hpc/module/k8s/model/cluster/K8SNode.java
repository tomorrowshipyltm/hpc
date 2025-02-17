package cn.wp.hpc.module.k8s.model.cluster;

import cn.hutool.core.util.StrUtil;
import io.fabric8.kubernetes.api.model.Taint;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.wp.hpc.module.k8s.enums.K8SConstant.NODE_STATUS_UNKNOWN;

/**
 * 使用builder构建实例
 */
@Data
@NoArgsConstructor
public class K8SNode {
    private String name;
    // 内部ip
    private String internalIP;
    // master or worker
    private String role;
    // 节点状态 ready
    private String status;
    private String os;
    // cpu厂商：Intel、海光、鲲鹏
    private String cpuBrand;
    // 加速卡，例如 寒武纪
    private String accelerator;
    // 资源容量
    private String capacity;
    // 是否可调度
    private boolean schedule;
    /** 以下属性用于节点回显 **/
    // 操作系统版本
    private String osImage;
    // 系统架构
    private String architecture;
    private String kernelVersion;
    private String containerRuntimeVersion;
    private String kubeletVersion;
    private String kubeProxyVersion;
    private String creationTimestamp;
    // label
    private Map<String, String> labels;
    // taint
    private List<Taint> taints;

    /** 登录信息，仅用于添加节点 **/
    private String cluster;
    private String user;
    private String password;
    private String port;

    public static class Builder {
        // 必传参数
        private String name;
        private String internalIP;
        // 可选参数
        private String role;
        private String os;
        private String cpuBrand;
        private String accelerator;

        private String status = NODE_STATUS_UNKNOWN;
        private boolean schedule = true;
        private String capacity;

        private String osImage;
        private String architecture;
        private String kernelVersion;
        private String containerRuntimeVersion;
        private String kubeletVersion;
        private String kubeProxyVersion;
        private String creationTimestamp;
        private Map<String, String> labels = new HashMap<>();
        private List<Taint> taints = new ArrayList<>();
        public Builder(String name, String internalIP) {
            this.name = name;
            this.internalIP = internalIP;
        }
        public Builder role(String val) {
            role = val;
            return this;
        }
        public Builder status(String val) {
            if (StrUtil.isNotBlank(val)) {
                status = val;
            }
            return this;
        }
        public Builder os(String val) {
            os = val;
            return this;
        }
        public Builder cpuBrand(String val) {
            cpuBrand = val;
            return this;
        }
        public Builder accelerator(String val) {
            accelerator = val;
            return this;
        }
        public Builder capacity(String val) {
            capacity = val;
            return this;
        }
        public Builder schedule(Boolean val) {
            if (val != null) {
                schedule = val;
            }
            return this;
        }

        public Builder osImage(String val) {
            osImage = val;
            return this;
        }
        public Builder architecture(String val) {
            architecture = val;
            return this;
        }
        public Builder kernelVersion(String val) {
            kernelVersion = val;
            return this;
        }
        public Builder containerRuntimeVersion(String val) {
            containerRuntimeVersion = val;
            return this;
        }
        public Builder kubeletVersion(String val) {
            kubeletVersion = val;
            return this;
        }
        public Builder kubeProxyVersion(String val) {
            kubeProxyVersion = val;
            return this;
        }
        public Builder creationTimestamp(String val) {
            creationTimestamp = val;
            return this;
        }
        public Builder labels(Map<String, String> val) {
            labels = val;
            return this;
        }
        public Builder taints(List<Taint> val) {
            taints = val;
            return this;
        }
        public K8SNode build() {
            return new K8SNode(this);
        }
    }

    private K8SNode(Builder builder) {
        name = builder.name;
        internalIP = builder.internalIP;
        role = builder.role;
        status = builder.status;
        os = builder.os;
        cpuBrand = builder.cpuBrand;
        accelerator = builder.accelerator;
        capacity = builder.capacity;
        schedule = builder.schedule;
        labels = builder.labels;
        taints = builder.taints;
        osImage = builder.osImage;
        architecture = builder.architecture;
        kernelVersion = builder.kernelVersion;
        containerRuntimeVersion = builder.containerRuntimeVersion;
        kubeletVersion = builder.kubeletVersion;
        kubeProxyVersion = builder.kubeProxyVersion;
        creationTimestamp = builder.creationTimestamp;
    }
}
