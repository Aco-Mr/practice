package com.rocketmq.producer.main.filter;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 过滤消息-tag过滤-消费者
 * @author A.co
 * @date 2023/3/19 14:53
 */
public class TagFilterConsumer {
    public static void main(String[] args) throws Exception {
        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("tag_consumer_group");
        //指定namesrv地址
        consumer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //订阅主题
        consumer.subscribe("tagFilterTopic","TagA || TagC");
        //创建监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //消费消息
                for (MessageExt msg : list) {
                    System.out.println("接收消息：" + msg.toString());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();
        System.out.println("消费者已启动......");
    }
}
