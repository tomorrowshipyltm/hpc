package cn.wp.hpc.module.pay.framework.web.config;

import cn.wp.hpc.framework.swagger.config.HpcSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * pay 模块的 web 组件的 Configuration
 *
 *
 */
@Configuration(proxyBeanMethods = false)
public class PayWebConfiguration {

    /**
     * pay 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi payGroupedOpenApi() {
        return HpcSwaggerAutoConfiguration.buildGroupedOpenApi("pay");
    }

}
