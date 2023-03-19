package com.rocketmq.producer.transaction;

import com.rocketmq.producer.RocketMqConstant;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事务消息-生产者
 * @author A.co
 * @date 2023/3/19 20:41
 */
public class TransactionProducer {
    public static void main(String[] args) throws Exception {
        //创建事务实现
        TransactionListener listener = new TransactionListenerImpl();
        //创建事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer_group");
        //指定namesrv地址
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("transaction-producer-thread-");
                return thread;
            }
        });
        //设置线程池
        producer.setExecutorService(executor);
        //设置事务监听实现
        producer.setTransactionListener(listener);
        //启动生产者
        producer.start();

        //定义tag列表
        String[] tags = new String[]{"TagA","TagB","TagC","TagD","TagE"};
        //发送事务消息
        for (int i = 0; i < 10; i++) {
            Message message = new Message("transactionTopicTest", tags[i % tags.length], "KEY" + i, ("Hello World " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            TransactionSendResult sendResult = producer.sendMessageInTransaction(message,null);
            System.out.println(sendResult);
            Thread.sleep(10);
        }
    }
}
