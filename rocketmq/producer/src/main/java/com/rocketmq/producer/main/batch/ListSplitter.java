package com.rocketmq.producer.main.batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 消息分割器
 * @author A.co
 * @date 2023/3/19 11:37
 */
public class ListSplitter implements Iterator<List<Message>> {
    /**
     * 限制的消息总长度
     */
    private final int sizeLimit = 1000 * 1000;
    /**
     * 分割的消息
     */
    private final List<Message> messages;
    /**
     * 起始下标
     */
    private int currIndex;

    public ListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;
        for (; nextIndex < messages.size(); nextIndex++){
            Message message = messages.get(nextIndex);
            //获取消息的主题、内容和配置的字节长度
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String,String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            tmpSize = tmpSize + 20;
            if (tmpSize > sizeLimit){
                //单个消息超出sizeLimit是意外的
                //这里就让它去吧，否则它会阻塞拆分过程
                if (nextIndex - currIndex == 0){
                    //如果下一个子列表没有元素，添加这个元素，然后中断，否则就断了
                    nextIndex++;
                }
                break;
            }
            if (tmpSize + totalSize > sizeLimit){
                break;
            }
            else{
                totalSize += tmpSize;
            }
        }
        List<Message> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
