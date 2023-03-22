package com.rocketmq.producer.main.simple;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 简单消息-单向消息
 * @author A.co
 * @date 2023/3/14 23:00
 */
public class OneWayProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("OneWayProducerGroup1");
        //指定namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //发送消息
        for (int i = 0; i < 10; i++) {
            Message message = new Message("TopicTest", "TagA", ("Hello RocketMq" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(message);
        }
        //关闭生产者
        Thread.sleep(5000L);
        producer.shutdown();
    }
}
