package cn.wp.hpc.framework.mq.eventbus.core.interceptor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * topic订阅者
 */
public interface TopicSubscriber {
    /** 获取消费进度 **/
    AtomicInteger getOffset();

    /** 获取订阅者 **/
    ISubscriber getSubscriber();
}
