package com.rocketmq.producer.main.base;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author A.co
 * @date 2023/3/13 11:01
 */
public class BaseConsumer {
    public static void main(String[] args) throws MQClientException {
        //1.创建消费者对象
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-consumer-group1");
        //2.指明namesrv地址
        consumer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //3.订阅主题：topic 和过滤消息用的tag表达式
        consumer.subscribe("Mytopic1","*");
        //4.创建一个监听器，当broker把消息推送过来的时候调用(并发监听器)
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //遍历消息
                for (MessageExt messageExt : list) {
                    System.out.println("接受的消息：" + new String(messageExt.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者
        consumer.start();
        System.out.println("消费者已启动");
    }
}
