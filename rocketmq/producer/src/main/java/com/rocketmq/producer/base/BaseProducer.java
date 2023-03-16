package com.rocketmq.producer.base;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author A.co
 * @date 2023/3/9 15:55
 */
public class BaseProducer {
    public static void main(String[] args) throws Exception {
        //1.创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("my-producer-group1");
        //2.指定namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //3.启动生产者
        producer.start();
        //4.创建消息
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Mytopic1", "TagA", ("Hello RocketMq" + i).getBytes(StandardCharsets.UTF_8));
            //5.发送消息
            SendResult result = producer.send(message);
            System.out.println(result);
        }
        //6.关闭生产者
        producer.shutdown();
    }
}
