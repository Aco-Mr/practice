package com.rocketmq.producer.batch;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 批量消息-消费者
 * @author A.co
 * @date 2023/3/19 11:58
 */
public class BatchConsumer {
    public static void main(String[] args) throws Exception {
        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("batch_consumer_group");
        //指定namesrv地址
        consumer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //设置为广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        //订阅主题
        consumer.subscribe("BatchTest","*");
        //创建监听器
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            //打印信息
            for (MessageExt msg : msgs) {
                System.out.println("接收信息：" + new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
        System.out.println("消费者已启动......");
    }
}
