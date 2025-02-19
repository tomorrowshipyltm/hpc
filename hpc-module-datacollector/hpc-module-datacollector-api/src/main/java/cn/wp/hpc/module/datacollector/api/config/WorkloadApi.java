package cn.wp.hpc.module.datacollector.api.config;

/**
 * 对外暴露 API 接口, 暂时不用
 *
 */
public interface WorkloadApi {

    /**
     * 根据参数键查询参数值
     *
     * @param key 参数键
     * @return 参数值
     */
    String getConfigValueByKey(String key);

}
