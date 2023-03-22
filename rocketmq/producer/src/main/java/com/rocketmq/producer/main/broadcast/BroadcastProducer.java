package com.rocketmq.producer.main.broadcast;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 广播消息-生产者
 * @author A.co
 * @version 1.0
 * @date 2023/3/17 16:01
 */
public class BroadcastProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("broadcast_producer_group");
        //指定namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //创建消息
        for (int i = 0; i < 100; i++) {
            Message message = new Message("BroadcastTopicTest", "TagA", ("Hello world" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //发送消息
            SendResult result = producer.send(message);
            System.out.println(result);
        }
        //关闭生产者
        producer.shutdown();
    }
}
