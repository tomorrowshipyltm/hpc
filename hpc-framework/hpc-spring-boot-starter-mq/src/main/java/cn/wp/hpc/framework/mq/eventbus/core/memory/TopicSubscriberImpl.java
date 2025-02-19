package cn.wp.hpc.framework.mq.eventbus.core.memory;

import cn.wp.hpc.framework.mq.eventbus.core.interceptor.ISubscriber;
import cn.wp.hpc.framework.mq.eventbus.core.interceptor.TopicSubscriber;
import lombok.Data;
import lombok.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class TopicSubscriberImpl implements TopicSubscriber {
    private final AtomicInteger offset;
    private final ISubscriber subscriber;

    public TopicSubscriberImpl(@NonNull final ISubscriber subscriber) {
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
    }
}
