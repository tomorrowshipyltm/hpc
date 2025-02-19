package cn.wp.hpc.framework.mq.eventbus.core.interceptor;


/**
 * 订阅者接口
 */
public interface ISubscriber {
    /** 订阅者Id **/
    String getId();

    /** 消费 **/
    void consume(Message message) throws InterruptedException;
}
