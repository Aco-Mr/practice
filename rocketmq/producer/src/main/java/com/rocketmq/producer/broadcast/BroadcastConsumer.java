package com.rocketmq.producer.broadcast;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 广播消息-消费者
 * @author A.co
 * @version 1.0
 * @date 2023/3/17 16:06
 */
public class BroadcastConsumer {
    public static void main(String[] args) throws Exception {
        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("broadcast_consumer_group");
        //指定namesrv地址
        consumer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //指定消费者从第一个偏移量开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //设置为广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        //订阅主题
        consumer.subscribe("BroadcastTopicTest","*");
        //创建监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                //打印信息
                for (MessageExt msg : msgs) {
                    System.out.println("接收信息：" + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();
        System.out.println("消费者已启动......");
    }
}
