package com.sunshine.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

import static com.sunshine.rocketmq.RocketMqConstant.namesrvAddr;
@Slf4j
public class AsyncProducer {

    /**
     * 1.创建消费者Consumer，制定消费者组名
     * 2.指定Nameserver地址
     * 3.订阅主题Topic和Tag
     * 4.设置回调函数，处理消息
     * 5.启动消费者consumer
     * @param args
     * @throws MQClientException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, UnsupportedEncodingException {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(RocketMqConstant.Group.GROUP_ORDER);
        // 设置NameServer的地址
        producer.setNamesrvAddr(namesrvAddr);
        // 启动Producer实例
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(RocketMqConstant.Topic.LoadBalanceConsumer,
                    RocketMqConstant.Tag.TAG_ORDER,
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {

                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
