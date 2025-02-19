package cn.wp.hpc.module.datacollector.framework.web.config;

import cn.wp.hpc.framework.swagger.config.HpcSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * datacollector 模块的 web 组件的 Configuration
 *
 *
 */
@Configuration(proxyBeanMethods = false)
public class DatacollectorWebConfiguration {

    /**
     * API 分组
     */
    @Bean
    public GroupedOpenApi k8sGroupedOpenApi() {
        return HpcSwaggerAutoConfiguration.buildGroupedOpenApi("datacollector");
    }

}
