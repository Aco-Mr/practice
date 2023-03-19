package com.rocketmq.producer.batch;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息-超出最大发送消息生产者
 * @author A.co
 * @date 2023/3/19 11:35
 */
public class MaxBatchProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("batch_producer_group");
        //设置namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //主题
        String topic = "BatchTest";
        //模拟创建最大消息
        List<Message> messages = new ArrayList<>(100*1000);
        for (int i = 0; i < 100 * 1000; i++) {
            messages.add(new Message(topic,"Tag","OrderId" + i,("Hello World " + i).getBytes()));
        }
//        producer.send(messages);

        //分割消息，防止超出最大消息容量
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()){
            List<Message> next = splitter.next();
            SendResult sendResult = producer.send(next);
            System.out.println(sendResult);
        }
        //关闭消息
        producer.shutdown();
    }
}
