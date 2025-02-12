package cn.wp.hpc.framework.pay.config;

import cn.wp.hpc.framework.pay.core.client.PayClientFactory;
import cn.wp.hpc.framework.pay.core.client.impl.PayClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 支付配置类
 *
 *
 */
@AutoConfiguration
public class HpcPayAutoConfiguration {

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
