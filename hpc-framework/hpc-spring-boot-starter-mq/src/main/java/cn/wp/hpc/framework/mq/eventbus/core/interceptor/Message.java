package cn.wp.hpc.framework.mq.eventbus.core.interceptor;

/**
 * 消息接口，用户自定义实现
 */
public interface Message {
    /** 消息类型 **/
    String getType();
    void setType(String type);

    /** 消息内容字符串 **/
    String getMsg();
}
