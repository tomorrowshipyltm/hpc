package cn.wp.hpc.module.crm.job.customer;

import cn.wp.hpc.framework.quartz.core.handler.JobHandler;
import cn.wp.hpc.framework.tenant.core.job.TenantJob;
import cn.wp.hpc.module.crm.service.customer.CrmCustomerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 客户自动掉入公海 Job
 *
 *
 */
@Component
public class CrmCustomerAutoPutPoolJob implements JobHandler {

    @Resource
    private CrmCustomerService customerService;

    @Override
    @TenantJob
    public String execute(String param) {
        int count = customerService.autoPutCustomerPool();
        return String.format("掉入公海客户 %s 个", count);
    }

}