package cn.wp.hpc.module.k8s.framework.web.config;

import cn.wp.hpc.framework.swagger.config.HpcSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * k8s 模块的 web 组件的 Configuration
 *
 *
 */
@Configuration(proxyBeanMethods = false)
public class K8sWebConfiguration {

    /**
     * k8s 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi k8sGroupedOpenApi() {
        return HpcSwaggerAutoConfiguration.buildGroupedOpenApi("k8s");
    }

}
