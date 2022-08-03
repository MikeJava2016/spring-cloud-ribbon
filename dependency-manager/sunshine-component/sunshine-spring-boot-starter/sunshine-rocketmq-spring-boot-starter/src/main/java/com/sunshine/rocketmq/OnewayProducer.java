package com.sunshine.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import static com.sunshine.rocketmq.RocketMqConstant.namesrvAddr;

public class OnewayProducer {
    public static void main(String[] args) throws Exception{
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConstant.Group.GROUP_ORDER);
        // 设置NameServer的地址
        producer.setNamesrvAddr(namesrvAddr);
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(RocketMqConstant.Topic.LoadBalanceConsumer /* Topic */,
//                    RocketMqConstant.Tag.TAG_ORDER /* Tag */,

                    "1",("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */

            );

//            msg.setKeys("");  // 业务的唯一标识
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);

        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
