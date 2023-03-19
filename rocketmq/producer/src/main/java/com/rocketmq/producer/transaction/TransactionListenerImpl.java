package com.rocketmq.producer.transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 事务监听器实现
 * @author A.co
 * @date 2023/3/19 20:42
 */
public class TransactionListenerImpl implements TransactionListener {
    /**
     * 执行本地事务
     * @param message
     * @param o
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        String tags = message.getTags();
        if (StringUtils.contains(tags,"TagA")){
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        else if (StringUtils.contains(tags,"TagB")){
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        else {
            return LocalTransactionState.UNKNOW;
        }
    }

    /**
     * 回查本地事务-当执行的本地事务返回值是 UNKNOW 时，会执行此方法。若还是 UNKNOW 则会重复回查，一直到回查15次，RocketMq会把此消息给丢弃掉
     * @param messageExt
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        String tags = messageExt.getTags();
        if (StringUtils.contains(tags,"TagC")){
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        else if (StringUtils.contains(tags,"TagD")){
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        else {
            return LocalTransactionState.UNKNOW;
        }
    }
}
