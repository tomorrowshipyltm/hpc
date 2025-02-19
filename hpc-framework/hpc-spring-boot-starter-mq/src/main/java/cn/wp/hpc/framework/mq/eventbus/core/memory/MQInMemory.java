package cn.wp.hpc.framework.mq.eventbus.core.memory;

import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * java基于内存的消息队列，同时支持队列模型和发布-订阅模型，不支持高级功能
 * 队列模型：一个消息一个消费者, 适用于consumer主动poll
 * 发布-订阅模型：一个消息多个消费者, 适用于consumer被动push
 * 注：有内存泄露风险，生产环境禁用
 */
@Slf4j
public class MQInMemory implements MQService {
    // 每个topic有一个内部处理器TopicHandler，handler里面包含多个Subscriber
    private final Map<String, TopicHandler> topicProcessors;
    // 维护topicName和实例对应关系
    private final Map<String, Topic> topicNames;
    // 维护队列名和P2P队列关系
    private final Map<String, BlockingQueue<Message>> p2pQueues;

    public MQInMemory() {
        // 使用线程安全容器
        this.topicProcessors = new ConcurrentHashMap<>();
        this.topicNames = new ConcurrentHashMap<>();
        this.p2pQueues = new ConcurrentHashMap<>();
    }

    @Override
    public Topic createTopic(@NonNull final String topicName) {
        if (topicNames.containsKey(topicName)) {
            return topicNames.get(topicName);
        }
        final Topic topic = new TopicImpl(topicName, UUID.randomUUID().toString());
        TopicHandler topicHandler = new TopicHandler(topic);
        topicProcessors.put(topic.getTopicId(), topicHandler);
        topicNames.put(topicName, topic);
        log.info("Create topic: {}", topicName);
        return topic;
    }

    @Override
    public void subscribe(@NonNull final ISubscriber subscriber, final String topicName) {
        Topic topic = topicNames.get(topicName);
        if (topic == null) {
            topic = createTopic(topicName);
        }
        topic.addSubscriber(new TopicSubscriberImpl(subscriber));
        log.info("{} subscrib to topic: {} ,cur size: {}", subscriber.getId() , topicName, topic.getMessages().size());
    }

    /** 取消对某个topic的订阅 **/
    @Override
    public void unSubscribe(@NonNull final ISubscriber subscriber, final String topicName) {
        Topic topic = topicNames.get(topicName);
        if (topic != null) {
            topic.removeSubscriber(subscriber.getId());
            log.info("{} unSubscrib to topic: {} ,cur size: {}", subscriber.getId(), topicName, topic.getMessages().size());
        }
    }

    /** 一旦发布，就通知该topic下所有订阅者 **/
    @Override
    public void publish(final String topicName, @NonNull final Message message) {
        Topic topic = topicNames.get(topicName);
        if (topic != null) {
            topic.addMessage(message);
            log.info("{} published to topic: {}", message.getMsg(), topicName);
            new Thread(() -> topicProcessors.get(topic.getTopicId()).publish()).start();
        }
    }

    /**
     * 同一个topic, 不同Subscriber各自维护offset, 每次消费offset+1
     * 重置topic的offset=0
     */
    public void resetOffset(@NonNull final Topic topic, @NonNull final ISubscriber subscriber, @NonNull final Integer newOffset) {
        for (TopicSubscriber topicSubscriber : topic.getSubscribers()) {
            if (topicSubscriber.getSubscriber().equals(subscriber)) {
                topicSubscriber.getOffset().set(newOffset);
                log.info("{} offset reset to: {}", topicSubscriber.getSubscriber().getId(), newOffset);
                new Thread(() -> topicProcessors.get(topic.getTopicId()).startSubscribeWorker(topicSubscriber)).start();
                break;
            }
        }
    }

    /**
     * 创建点对点 队列
     * @param queueName
     */
    @Override
    public void createQueue(String queueName) {
        if (StrUtil.isNotBlank(queueName) && !p2pQueues.containsKey(queueName)) {
            p2pQueues.put(queueName, new LinkedBlockingQueue<>());
            log.info("Create p2p queue: {}", queueName);
        }
    }

    /**
     * 向p2p队列 发送消息
     * @param queueName
     * @param message
     */
    @Override
    public void sendMessage(String queueName, Message message) {
        BlockingQueue<Message> queue = p2pQueues.get(queueName);
        if (queue != null && message != null) {
            queue.add(message);
            log.info("Message sent to p2p queue {}: {}", queueName, message.getMsg());
        }
    }

    /**
     * 从p2p队列 接收消息
     * @param queueName
     */
    @Override
    public Message receiveMessage(String queueName) {
        BlockingQueue<Message> queue = p2pQueues.get(queueName);
        if (queue != null) {
            Message message = queue.poll();
            if (message != null) {
                return message;
            }
        } else {
            log.error("P2P queue {} does not exist", queueName);
        }
        return null;
    }

}
