package com.sunshine.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class BroadcastConsume {
    public static void main(String[] args) throws MQClientException {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConstant.Topic.LoadBalanceConsumer);
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(RocketMqConstant.namesrvAddr);
        // 订阅Topic
        consumer.subscribe(RocketMqConstant.Topic.LoadBalanceConsumer, "*");
        //广播模式消费
        consumer.setMessageModel(MessageModel.BROADCASTING);

        // 注册回调函数，处理消息
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n",
                    Thread.currentThread().getName(), msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消息者
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
