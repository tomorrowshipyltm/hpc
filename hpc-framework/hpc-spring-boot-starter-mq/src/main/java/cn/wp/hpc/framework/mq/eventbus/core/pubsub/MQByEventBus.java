package cn.wp.hpc.framework.mq.eventbus.core.pubsub;

import cn.wp.hpc.framework.mq.eventbus.core.interceptor.ISubscriber;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.MQService;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Message;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Topic;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Service;

/**
 * 由于不需要消息持久化和分布式，通过EventBus模拟MQ，获取更好的性能和稳定性
 * producer --->    EventBus  ----event-----> Consumer1, Consumer2
 * 一个event被多个消费者消费，@Subscribe方法根据入参类型消费
 * 消费者通过消息type区分处理逻辑，无需topic区分
 */
@Service("MQByEventBus")
public class MQByEventBus implements MQService {
    // 同步队列，暂不需AsyncEventBus
    private EventBus eventBus;
    public MQByEventBus() {
        eventBus = new EventBus();
    }
    @Override
    public Topic createTopic(String topicName) {
        return null;
    }

    @Override
    public void subscribe(ISubscriber subscriber, String topicName) {
        eventBus.register(subscriber);
    }

    @Override
    public void unSubscribe(ISubscriber subscriber, String topicName) {
        eventBus.unregister(subscriber);
    }

    @Override
    public void publish(String topicName, Message message) {
        message.setType(topicName);
        eventBus.post(message);
    }

    @Override
    public void createQueue(String queueName) {}

    @Override
    public void sendMessage(String queueName, Message message) {}

    @Override
    public Message receiveMessage(String queueName) {
        return null;
    }
}
