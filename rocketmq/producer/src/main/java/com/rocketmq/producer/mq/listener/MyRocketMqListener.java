package com.rocketmq.producer.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.StringMessageConverter;

/**
 * 本地事务监听器
 * @author A.co
 * @date 2023/3/23 14:48
 */
@Slf4j
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class MyRocketMqListener implements RocketMQLocalTransactionListener {
    /**
     * 本地事务处理
     * @param message   消息体
     * @param o 参数
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        //生产者把目的地作为参数传过来了
        String destination = (String)o;
        //转换spring消息为rocketmq的消息体
        org.apache.rocketmq.common.message.Message convert = RocketMQUtil.convertToRocketMessage(
                new StringMessageConverter(),//消息转换器
                "UTF-8",    //字节编码
                destination,    //消息地址
                message //消息
        );
        String tags = convert.getTags();
        if (StringUtils.contains(tags,"TagA")){
            log.info("--------------------本地事务提交--------------------");
            return RocketMQLocalTransactionState.COMMIT;
        }
        else if (StringUtils.contains(tags,"TagB")){
            log.info("--------------------本地事务回滚--------------------");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        else {
            log.info("---------------------本地事务未知消息------------------------");
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        return null;
    }
}
