package com.rocketmq.producer.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 * @author A.co
 * @date 2023/3/22 17:41
 */
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "my-boot-consumer-group",topic = "my-boot-topic1")
public class MyConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        //消费消息
        log.info("接受消息：" + s);
    }
}
