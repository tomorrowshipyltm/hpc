package cn.wp.hpc.framework.mq.eventbus.core.interceptor;

public interface MessageHandler<T>
{
    void handle(T object);
}
