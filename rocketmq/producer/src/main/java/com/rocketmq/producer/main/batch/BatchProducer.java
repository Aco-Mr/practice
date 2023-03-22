package com.rocketmq.producer.main.batch;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息-生产者
 * @author A.co
 * @date 2023/3/19 11:31
 */
public class BatchProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("batch_producer_group");
        //设置namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //批量发送消息
        String topic = "BatchTest";
        List<Message> list = new ArrayList<>();
        list.add(new Message(topic,"TagA","OrderID001","Hello World0".getBytes()));
        list.add(new Message(topic,"TagA","OrderID002","Hello World1".getBytes()));
        list.add(new Message(topic,"TagA","OrderID003","Hello World2".getBytes()));
        SendResult sendResult = producer.send(list);
        System.out.println(sendResult);
        //关闭生产者
        producer.shutdown();
    }
}
