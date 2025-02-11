package cn.wp.hpc.module.iot.framework.web.config;

import cn.wp.hpc.framework.swagger.config.HpcSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * iot 模块的 web 组件的 Configuration
 *
 * @author ahh
 */
@Configuration(proxyBeanMethods = false)
public class IotWebConfiguration {

    /**
     * iot 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi iotGroupedOpenApi() {
        return HpcSwaggerAutoConfiguration.buildGroupedOpenApi("iot");
    }

}
