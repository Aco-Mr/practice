package com.aco.practice.demo1.task;

import com.aco.practice.demo1.domain.entity.ScheduledConfigEntity;
import com.aco.practice.demo1.mapper.ScheduledConfigMapper;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: HaoJianXu
 * @Date: 2020/7/26 15:02
 */
@Slf4j
public class ScheduledTask implements Runnable {

    private ScheduledConfigEntity scheduledConfigEntity;

    @Autowired
    private ScheduledConfigMapper scheduledConfigMapper;

    public ScheduledTask(ScheduledConfigEntity scheduledConfigEntity){
        this.scheduledConfigEntity = scheduledConfigEntity;
    }

    @Override
    public void run() {
        log.info("定时任务执行中，执行任务信息：{}", JSONObject.toJSONString(scheduledConfigEntity));
        int rows = scheduledConfigMapper.updateById(scheduledConfigEntity);
        log.info("更新定时任务,影响行数：{}",rows);
    }
}
