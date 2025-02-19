package cn.wp.hpc.framework.mq.eventbus.core.memory;

import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Topic;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.TopicSubscriber;
import lombok.NonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * topic处理器
 */
public class TopicHandler {
    private final Topic topic;
    private final Map<String, SubscriberWorker> subscriberWorkers;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public TopicHandler(@NonNull final Topic topic) {
        this.topic = topic;
        subscriberWorkers = new HashMap<>();
    }

    /** 并发通知topic下所有订阅者 **/
    public void publish() {
        for (TopicSubscriber topicSubscriber : topic.getSubscribers()) {
            threadPool.execute(() -> startSubscribeWorker(topicSubscriber));
        }
    }

    // 开子线程通知，如果subscriber不存在，那么就创建线程，然后调用对应的consumer()
    public void startSubscribeWorker(@NonNull final TopicSubscriber topicSubscriber) {
        final String subscribeId = topicSubscriber.getSubscriber().getId();
        if (!subscriberWorkers.containsKey(subscribeId)) {
            final SubscriberWorker subscriberWorker = new SubscriberWorker(topic, topicSubscriber);
            subscriberWorkers.put(subscribeId, subscriberWorker);
            new Thread(subscriberWorker).start();
        }
        final SubscriberWorker subscriberWorker = subscriberWorkers.get(subscribeId);
        subscriberWorker.wakeUpIfNeeded();
    }

}
