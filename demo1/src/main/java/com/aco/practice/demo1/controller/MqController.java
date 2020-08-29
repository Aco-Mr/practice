package com.aco.practice.demo1.controller;

import com.aco.practice.demo1.service.SendRabbitMqService;
import com.aco.practice.demo1.util.ApiResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: HaoJianXu
 * @Date: 2020/7/4 20:49
 */
@Api(tags = {"消息队列接口"})
@RestController
public class MqController {

    @Autowired
    private SendRabbitMqService sendRabbitMqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "测试RabbitMq")
    @GetMapping(value = "/test/rabbitmq")
    public ResponseEntity testMq(String str){
        Map<String,Object> map = new HashMap<>();
        map.put("message",str);
        rabbitTemplate.convertAndSend("TestDirectExchange","TestDirectRouting",map);
        return ResponseEntity.ok().body(ApiResponseResult.ok(str));
    }
}
