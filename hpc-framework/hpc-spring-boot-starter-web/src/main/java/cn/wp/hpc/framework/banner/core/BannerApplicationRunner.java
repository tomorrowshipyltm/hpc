package cn.wp.hpc.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 *
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            log.info("\n----------------------------------------------------------\n\t" +
                            "项目启动成功！\n\t" +
                            "开发文档:  https://www.yuque.com/u1862689/pesoto/nu2aa8p0igxyx2zd  \n\t" +
                            "----------------------------------------------------------"
                    );

            // 数据报表
            if (isNotPresent("cn.wp.hpc.module.report.framework.security.config.SecurityConfiguration")) {
                System.out.println("[报表模块 hpc-module-report - 已禁用]");
            }
            // 工作流
            if (isNotPresent("cn.wp.hpc.module.bpm.framework.flowable.config.BpmFlowableConfiguration")) {
                System.out.println("[工作流模块 hpc-module-bpm - 已禁用]");
            }
            // 商城系统
            if (isNotPresent("cn.wp.hpc.module.trade.framework.web.config.TradeWebConfiguration")) {
                System.out.println("[商城系统 hpc-module-mall - 已禁用]");
            }
            // ERP 系统
            if (isNotPresent("cn.wp.hpc.module.erp.framework.web.config.ErpWebConfiguration")) {
                System.out.println("[ERP 系统 hpc-module-erp - 已禁用]");
            }
            // CRM 系统
            if (isNotPresent("cn.wp.hpc.module.crm.framework.web.config.CrmWebConfiguration")) {
                System.out.println("[CRM 系统 hpc-module-crm - 已禁用]");
            }
            // 微信公众号
            if (isNotPresent("cn.wp.hpc.module.mp.framework.mp.config.MpConfiguration")) {
                System.out.println("[微信公众号 hpc-module-mp - 已禁用]");
            }
            // 支付平台
            if (isNotPresent("cn.wp.hpc.module.pay.framework.pay.config.PayConfiguration")) {
                System.out.println("[支付系统 hpc-module-pay - 已禁用]");
            }
            // AI 大模型
            if (isNotPresent("cn.wp.hpc.module.ai.framework.web.config.AiWebConfiguration")) {
                System.out.println("[AI 大模型 hpc-module-ai - 已禁用]");
            }
            // IOT 物联网
            if (isNotPresent("cn.wp.hpc.module.iot.framework.web.config.IotWebConfiguration")) {
                System.out.println("[IOT 物联网 hpc-module-iot - 已禁用]");
            }
            // K8S dashboard
            if (isNotPresent("cn.wp.hpc.module.k8s.framework.web.config.K8SWebConfiguration")) {
                System.out.println("[K8S dashboard hpc-module-k8s - 已禁用]");
            }
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
