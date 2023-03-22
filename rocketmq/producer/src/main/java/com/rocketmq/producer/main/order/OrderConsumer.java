package com.rocketmq.producer.main.order;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 顺序消息-消费者
 * @author A.co
 * @version 1.0
 * @date 2023/3/16 16:44
 */
public class OrderConsumer {
    public static void main(String[] args) throws Exception {
        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_consumer_group1");
        //指定namesrv地址
        consumer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //指定消费者先消费队列中的第一个偏移量，即先进先出
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //订阅主题
        consumer.subscribe("OrderTopicTest","*");
        //注册监听器(序列监听器,保证一个队列一个线程)
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                //自动提交
                context.setAutoCommit(true);
                for (MessageExt msg : msgs) {
                    System.out.println("消息内容：" + new String(msg.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        //并发监听器，为了做测试
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                //遍历消息
//                for (MessageExt messageExt : list) {
//                    System.out.println("消息内容：" + new String(messageExt.getBody()));
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
        //启动消费者
        consumer.start();
        System.out.println("消费者已启动......");
    }
}
