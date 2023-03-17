package com.rocketmq.producer.scheduled;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * 延迟消息-生产者
 * @author A.co
 * @version 1.0
 * @date 2023/3/17 16:27
 */
public class ScheduledProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("scheduled_producer_group");
        //指定namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //创建消息
        for (int i = 0; i < 10; i++) {
            //发送消息
            Message message = new Message("scheduledTopicTest", ("Hello World" + i).getBytes());
            //指定延迟等级（开源的RocketMq提供了18个等级延迟，其它的只能买商业版才能提供）
            message.setDelayTimeLevel(3);
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult);
        }
        //关闭生产者
        producer.shutdown();
    }
}
