package com.aco.practice.demo1.mq;

import com.aco.practice.demo1.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: HaoJianXu
 * @Date: 2020/8/24 21:14
 */
@Slf4j
public class SendDemoMq {
    private static final String QUEUE_NAME = "aco_worker";

    public static final String EXCHANGE_NAME = "aco_exchange";

    /**
     * 向指定队列发布消息
     * @param object
     * @throws Exception
     */
    public static void sendQueueMq(Object object) throws Exception{
        // 获取连接
        Connection connection = ConnectionUtil.getRabbitMqConnectionFactory();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 创建队列，声明并创建一个队列，如果队列存在，则使用这个队列
        /**
         * 1.队列名称
         * 2.是否持久化  true：持久化，false：非持久化
         * 3.是否启用独占模式  true：独占模式，false非独占
         * 4.是否自动删除队列中的消息   true：断开连接并删除消息，false：断开连接不会删除消息
         * 5.其它额外参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 发送消息
        // 消息内容
        String message = "Hello Aco " + String.valueOf(object);
        /**
         * 1.交换机名称
         * 2.队列名称
         * 3.BasicProperties 基础参数
         * 4.消息内容的字节数组
         */
        channel.basicPublish("", QUEUE_NAME,null,message.getBytes());
        log.info("发送消息：{}",message);
        // 关闭连接和通道，先关闭通道，再关闭连接
        channel.close();
        connection.close();

    }

    /**
     * 发布订阅模式，对绑定到同一个交换机的队列同时发送信息
     * @param object
     * @throws Exception
     */
    public static void sendFanoutMq(Object object) throws Exception {
        // 获取连接
        Connection connection = ConnectionUtil.getRabbitMqConnectionFactory();
        // 创建通道
        Channel channel = connection.createChannel();
        // 定义交换机，若交换机不存在则创建，存在则使用
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        // 发送信息
        String message = "Hello Aco Exchange " + String.valueOf(object);
        /**
         * 对指定的交换机发送消息
         * 1.交换机名称
         * 2.队列名称
         * 3.基础参数
         * 4.消息内容
         */
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
        //关闭通道
        channel.close();
        //关闭连接
        connection.close();
    }
}
