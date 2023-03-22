package com.rocketmq.producer.main.filter;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 过滤消息-sql过滤-消费者
 * @author A.co
 * @date 2023/3/19 15:01
 */
public class SqlFilterConsumer {
    public static void main(String[] args) throws Exception {
        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("sql_consumer_group");
        //指定namesrv地址
        consumer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //订阅主题,需要在broker.conf添加enablePropertyFilter=true 支持sql过滤
        consumer.subscribe("sqlFilterTopic", MessageSelector.bySql("TAGS is not null and TAGS in ('TagA','TagB') and a between 0 and 3"));
        //创建监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //消费消息
                for (MessageExt msg : list) {
                    System.out.println("接收消息："  + msg.toString());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();
        System.out.println("消费者已启动......");
    }
}
