package com.sunshine.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

import static com.sunshine.rocketmq.RocketMqConstant.namesrvAddr;

@Slf4j
public class SyncProducer {


    /**
     * 1.创建消息生产者producer，并制定生产者组名
     * 2.指定Nameserver地址
     * 3.启动producer
     * 4.创建消息对象，指定主题Topic、Tag和消息体
     * 5.发送消息
     * 6.关闭生产者producer
     *
     * @param args
     * @throws UnsupportedEncodingException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQClientException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException, MQClientException {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConstant.Group.GROUP_ORDER);
        // 设置NameServer的地址
        producer.setNamesrvAddr(namesrvAddr);
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(RocketMqConstant.Topic.LoadBalanceConsumer /* Topic */,
                    RocketMqConstant.Tag.TAG_ORDER /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );

            // org.apache.rocketmq.common.message.MessageConst  添加属性
            // msg.putUserProperty("");

            // 发送消息到一个Broker
            SendResult sendResult = producer.send(msg);
            print(sendResult);
            // 通过sendResult返回消息是否成功送达
            System.out.printf("%s%n", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    public static final void print(SendResult sendResult) {
        MessageQueue messageQueue = sendResult.getMessageQueue();
        int queueId = messageQueue.getQueueId();
        String topic = messageQueue.getTopic();
        String brokerName = messageQueue.getBrokerName();
        SendStatus sendStatus = sendResult.getSendStatus();
        String msgId = sendResult.getMsgId();
        String transactionId = sendResult.getTransactionId();
        String regionId = sendResult.getRegionId();
        long queueOffset = sendResult.getQueueOffset();
        log.info(" msgId = {}.", msgId);
    }
}
