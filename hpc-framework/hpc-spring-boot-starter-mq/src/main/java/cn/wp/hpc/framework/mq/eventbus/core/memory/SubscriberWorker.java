package cn.wp.hpc.framework.mq.eventbus.core.memory;

import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Message;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Topic;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.TopicSubscriber;
import lombok.NonNull;
import lombok.SneakyThrows;

public class SubscriberWorker implements Runnable {
    private final Topic topic;
    private final TopicSubscriber topicSubscriber;

    public SubscriberWorker(@NonNull final Topic topic, @NonNull final TopicSubscriber topicSubscriber) {
        this.topic = topic;
        this.topicSubscriber = topicSubscriber;
    }

    @SneakyThrows
    @Override
    public void run() {
        synchronized (topicSubscriber) {
            do {
                int curOffset = topicSubscriber.getOffset().get();
                while (curOffset >= topic.getMessages().size()) {
                    topicSubscriber.wait();
                }
                Message message = topic.getMessages().get(curOffset);
                topicSubscriber.getSubscriber().consume(message);

                // 由于subscriber offset可能在consuming过程中被reset，所以只有当前是curOffset时才需要+1
                topicSubscriber.getOffset().compareAndSet(curOffset, curOffset + 1);
                // 当所有订阅者都消费完topic消息就删除，防止内存堆积
                if (isAllSubscribersConsumed(topic, curOffset)) {
                    topic.removeMessage(curOffset+1);
                    // 由于队列消息删除，需要重置offset为队列长度
                    topicSubscriber.getOffset().compareAndSet(curOffset+1, topic.getMessages().size());
                }
            } while (true);
        }
    }

    synchronized public void wakeUpIfNeeded() {
        synchronized (topicSubscriber) {
            topicSubscriber.notify();
        }
    }

    // 检查消息是否被所有订阅者都已消费，如果是则删除
    private boolean isAllSubscribersConsumed(Topic topic, int messageOffset) {
        for (TopicSubscriber subscriber : topic.getSubscribers()) {
            if (subscriber.getOffset().get() <= messageOffset) {
                return false;
            }
        }
        return true;
    }
}
