package com.sunshine.rocketmq;

import com.sunshine.rocketmq.util.ListSplitter;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

import static com.sunshine.rocketmq.RocketMqConstant.namesrvAddr;

/**
 * 批量发送消息能显著提高传递小消息的性能。限制是这些批量消息应该有相同的topic，
 * 相同的waitStoreMsgOK，而且不能是延时消息。此外，这一批消息的总大小不应超过4MB。
 */
public class BatchProducer {
    public static void main(String[] args) throws MQClientException {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConstant.Group.GROUP_ORDER);
        // 设置NameServer的地址
        producer.setNamesrvAddr(namesrvAddr);
        // 启动Producer实例
        producer.start();

        List<Message> messages = new ArrayList<>();
        messages.add(new Message(RocketMqConstant.Topic.LoadBalanceConsumer, "TagA", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(RocketMqConstant.Topic.LoadBalanceConsumer, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(RocketMqConstant.Topic.LoadBalanceConsumer, "TagA", "OrderID003", "Hello world 2".getBytes()));
        ListSplitter splitter = new ListSplitter(messages);

        while (splitter.hasNext()) {
            try {
                List<Message>  listItem = splitter.next();
                producer.send(listItem);
            } catch (Exception e) {
                e.printStackTrace();
                //处理error
            }
        }
        producer.shutdown();

    }


}
