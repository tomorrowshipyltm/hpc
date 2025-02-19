package cn.wp.hpc.framework.mq.eventbus.core.interceptor;

public interface MessageQueueService
{
    <T> void registerSynchronousHandler(String destination, MessageHandler<T> handler);
    <T> void registerAsynchronousHandler(String destination, MessageHandler<T> handler);
    void postSynchronousEvent(String destination, Object object);
    void postAsynchronousEvent(String destination, Object object);
}
