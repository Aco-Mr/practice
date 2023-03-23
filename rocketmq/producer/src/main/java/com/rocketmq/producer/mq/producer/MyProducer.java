package com.rocketmq.producer.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 生产者
 * @author A.co
 * @date 2023/3/22 17:32
 */
@Slf4j
@Component
public class MyProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送消息
     * @param topic 主题
     * @param message   内容
     */
    public void sendMessage(String topic,String message){
        rocketMQTemplate.convertAndSend(topic,message);
        log.info("发送消息成功！！！");
    }

    /**
     * 发送事务消息
     * @param topic
     * @param msg
     */
    public void sendMessageInTransaction(String topic,String msg){
        //tag组
        String[] tags = new String[]{"TagA","TagB","TagC","TagD","TagE"};
        //发送事务消息
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload(msg).build();
            //topic和tag整合在一起
            String destination = topic + ":" + tags[i % tags.length];
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, destination);
            log.info("发送事务消息成功：{}",sendResult);

        }
    }

}
