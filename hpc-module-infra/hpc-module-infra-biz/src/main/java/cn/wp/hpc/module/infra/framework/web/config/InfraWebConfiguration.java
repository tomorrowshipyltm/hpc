package cn.wp.hpc.module.infra.framework.web.config;

import cn.wp.hpc.framework.swagger.config.HpcSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * infra 模块的 web 组件的 Configuration
 *
 *
 */
@Configuration(proxyBeanMethods = false)
public class InfraWebConfiguration {

    /**
     * infra 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi infraGroupedOpenApi() {
        return HpcSwaggerAutoConfiguration.buildGroupedOpenApi("infra");
    }

}
