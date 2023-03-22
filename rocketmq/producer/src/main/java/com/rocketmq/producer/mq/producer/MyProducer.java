package com.rocketmq.producer.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

}
