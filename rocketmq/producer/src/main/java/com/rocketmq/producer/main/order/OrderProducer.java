package com.rocketmq.producer.main.order;

import com.rocketmq.producer.constant.RocketMqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 顺序消息-生产者
 * @author A.co
 * @version 1.0
 * @date 2023/3/16 16:32
 */
public class OrderProducer {
    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("order_producer_group1");
        //namesrv服务器可以从环境变量中配置
        producer.setNamesrvAddr(RocketMqConstant.NAME_SERVICE_ADDRESS);
        //启动生产者
        producer.start();
        //创建消息
        for (int i = 0; i < 10; i++) {
            int orderId = i;
            for (int j = 0; j < 5; j++) {
                Message message = new Message("OrderTopicTest", "order_" + orderId, "KEY" + orderId, ("order_" + orderId + " step " + j).getBytes(RemotingHelper.DEFAULT_CHARSET));
                //发送消息
                SendResult sendResult = producer.send(message, (list, msg, arg) -> {
                    //o是方法的第三个参数传进来的
                    Integer id = (Integer) arg;
                    //获取发送消息的队列，因为得保证顺序消息，所以只能把消息发送到同一个队列中进行消费，舍弃了负载均衡
                    int index = id % list.size();
                    return list.get(index);
                }, orderId);
                System.out.println(sendResult.toString());
            }
        }
    }
}
