package cn.wp.hpc.framework.mq.eventbus.core.interceptor;

/**
 * MQService：简单内存消息队列, 不涉及网络通信, 适用于一个项目下多module内部调用
 * v1.0 特性：同时支持 队列模型  和 发布-订阅模型
 * 队列模型：一个消息一个消费者, 适用于consumer主动poll、按需拉取
 * 发布-订阅模型：一个消息多个消费者, 适用于consumer被动push、及时接收
 */
public interface MQService {
    /******************** 发布-订阅模型 ********************/
    /** 创建topic **/
    Topic createTopic(String topicName);

    /** 订阅topic **/
    void subscribe(ISubscriber subscriber, String topicName);

    /** 取消订阅topic **/
    void unSubscribe(ISubscriber subscriber, String topicName);

    /** 发布消息 **/
    void publish(String topicName, Message message);


    /********************    队列模型   ********************/
    /** Create a queue **/
    void createQueue(String queueName);

    /** Send a message to a specific queue **/
    void sendMessage(String queueName, Message message);

    /** Receive a message from a specific queue **/
    Message receiveMessage(String queueName);

}
