package com.sunshine.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.net.SocketAddress;
@Slf4j
 public class LoadBalanceConsumer {



    public static void main(String[] args) throws Exception {
        // 实例化消息生产者,指定组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConstant.Group.GROUP_ORDER);
        // 指定Namesrv地址信息.
        consumer.setNamesrvAddr(RocketMqConstant.namesrvAddr);
        // 订阅Topic
        consumer.subscribe(RocketMqConstant.Topic.LoadBalanceConsumer, "1");
        //负载均衡模式消费  集群模式  广播模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 注册回调函数，处理消息
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n",
                    Thread.currentThread().getName(), msgs);
            for (MessageExt messageExt: msgs){
                printLog(messageExt);
                handler(new String(messageExt.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消息者
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

    /**
     * 处理业务
     * @param message
     */
    private static void handler(String message) {
        log.info("mes = {}",message);
    }

    /**
     * 打印消息属性
     * @param messageExt
     */
    private static void printLog(MessageExt messageExt) {
        String brokerName = messageExt.getBrokerName();
        int queueId = messageExt.getQueueId();
        int storeSize = messageExt.getStoreSize();
        long queueOffset = messageExt.getQueueOffset();
        int sysFlag = messageExt.getSysFlag();
        long bornTimestamp = messageExt.getBornTimestamp();
        SocketAddress storeHost = messageExt.getStoreHost();
        String msgId = messageExt.getMsgId();
        long commitLogOffset = messageExt.getCommitLogOffset();
        int bodyCRC = messageExt.getBodyCRC();
        int reconsumeTimes = messageExt.getReconsumeTimes();
        long preparedTransactionOffset = messageExt.getPreparedTransactionOffset();
        String topic = messageExt.getTopic();
        int flag = messageExt.getFlag();
        String tags = messageExt.getTags();
        String uniqKey = messageExt.getProperty("UNIQ_KEY");
        String wait = messageExt.getProperty("WAIT");
        String transactionId = messageExt.getTransactionId();
        log.info("uniqKey = {}",uniqKey);

    }
}
