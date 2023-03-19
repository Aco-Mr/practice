package com.rocketmq.producer.filter;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 过滤消息-tag过滤-生产者
 * @author A.co
 * @date 2023/3/19 14:48
 */
public class TagFilterProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("tag_producer_group");
        //指定namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //发送消息
        String[] tags = new String[]{"TagA","TagB","TagC"};
        for (int i = 0; i < 15; i++) {
            Message message = new Message("tagFilterTopic", tags[i % tags.length], ("Hello World " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult result = producer.send(message);
            System.out.println(result);
        }
        //关闭生产者
        producer.shutdown();
    }
}
