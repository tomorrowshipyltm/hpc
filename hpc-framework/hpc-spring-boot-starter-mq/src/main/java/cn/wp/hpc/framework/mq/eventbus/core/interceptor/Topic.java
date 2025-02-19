package cn.wp.hpc.framework.mq.eventbus.core.interceptor;

import java.util.List;
/**
 * 主题
 */
public interface Topic {
    /** 获取主题名 **/
    String getTopicName();

    /** 获取主题Id **/
    String getTopicId();

    /** 获取消息 **/

    List<Message> getMessages();

    /** 获取主题订阅者 **/
    List<TopicSubscriber> getSubscribers();

    /** 向主题添加消息 **/
    void addMessage(Message message);

    /** 删除主题中index之前的所有消息 **/
    void removeMessage(int index);

    /** 添加主题订阅者 **/
    void addSubscriber(TopicSubscriber subscriber);

    /** 删除订阅者 **/
    void removeSubscriber(String subscriberId);

}
