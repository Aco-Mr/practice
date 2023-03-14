package com.rocketmq.producer.simple;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 简单消息-异步消息
 * @author A.co
 * @date 2023/3/14 22:46
 */
public class AsyncProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducerGroup1");
        //指定namesrv地址
        producer.setNamesrvAddr("10.33.39.24:9876");
        //启动生产者
        producer.start();
        //设置异步发送消息失败重试时间
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //设置线程屏障
        int messageCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(100);
        //发送消息
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                Message message = new Message("AsyncTopicTest", "TagA", "OrderId188", ("Hello RocketMq" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(message, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        //成功回调
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s %n",index,sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        //失败回调
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n",index,throwable);
                        throwable.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //等待消息回调
        countDownLatch.await(5, TimeUnit.SECONDS);
        //关闭生产者
        producer.shutdown();

    }
}
