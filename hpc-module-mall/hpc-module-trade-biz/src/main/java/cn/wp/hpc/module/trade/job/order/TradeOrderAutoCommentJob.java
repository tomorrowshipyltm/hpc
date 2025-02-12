package cn.wp.hpc.module.trade.job.order;

import cn.wp.hpc.framework.quartz.core.handler.JobHandler;
import cn.wp.hpc.framework.tenant.core.job.TenantJob;
import cn.wp.hpc.module.trade.service.order.TradeOrderUpdateService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 交易订单的自动评论 Job
 *
 *
 */
@Component
public class TradeOrderAutoCommentJob implements JobHandler {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;

    @Override
    @TenantJob
    public String execute(String param) {
        int count = tradeOrderUpdateService.createOrderItemCommentBySystem();
        return String.format("评论订单 %s 个", count);
    }

}
