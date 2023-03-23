package com.rocketmq.producer;

import com.rocketmq.producer.mq.producer.MyProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author A.co
 * @date 2023/3/22 17:36
 */
@SpringBootTest
public class ProducerApplicationTest {
    @Autowired
    MyProducer myProducer;

    @Test
    void test(){
        myProducer.sendMessage("my-boot-topic1","Hello My RocketMq Consumer");
    }

    @Test
    void test2(){
        myProducer.sendMessageInTransaction("my-boot-topic1","Hello My RocketMq Consumer");
    }
}
