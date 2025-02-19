package cn.wp.hpc.framework.mq.eventbus.core.memory;

import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Message;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.Topic;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.TopicSubscriber;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TopicImpl implements Topic {
    private final String topicName;
    private final String topicId;
    private final List<Message> messages;
    private final List<TopicSubscriber> subscribers;

    public TopicImpl(@NonNull final String topicName, @NonNull final String topicId) {
        this.topicName = topicName;
        this.topicId = topicId;
        this.messages = new CopyOnWriteArrayList<>();
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    @Override
    public synchronized void addMessage(@NonNull final Message message) {
        messages.add(message);
    }

    @Override
    public synchronized void removeMessage(int index) {
        //  删除index以及之前的所有消息
        if (index <= messages.size()) {
            messages.subList(0, index).clear();
        }
    }

    @Override
    public void addSubscriber(@NonNull final TopicSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * 根据订阅者Id删除,一个订阅者id只对应一个TopicSubscriber实例
     * @param subscriberId
     */
    @Override
    public void removeSubscriber(String subscriberId) {
        for (TopicSubscriber ts : subscribers) {
            if (ts.getSubscriber().getId().equals(subscriberId)) {
                subscribers.remove(ts);
                break;
            }
        }
    }

}
