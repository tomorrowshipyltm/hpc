package cn.wp.hpc.module.member.framework.web.config;

import cn.wp.hpc.framework.swagger.config.HpcSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * member 模块的 web 组件的 Configuration
 *
 *
 */
@Configuration(proxyBeanMethods = false)
public class MemberWebConfiguration {

    /**
     * member 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi memberGroupedOpenApi() {
        return HpcSwaggerAutoConfiguration.buildGroupedOpenApi("member");
    }

}
