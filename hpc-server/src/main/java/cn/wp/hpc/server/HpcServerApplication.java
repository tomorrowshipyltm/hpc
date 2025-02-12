package cn.wp.hpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 * SpringBootApplication作为springboot项目的唯一入口
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${hpc.info.base-package}
@SpringBootApplication(scanBasePackages = {"${hpc.info.base-package}.server", "${hpc.info.base-package}.module"})
public class HpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HpcServerApplication.class, args);
//        new SpringApplicationBuilder(HpcServerApplication.class)
//                .applicationStartup(new BufferingApplicationStartup(20480))
//                .run(args);

    }

}
