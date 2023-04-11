package com.aco.spring.controller;

import com.aco.spring.annotation.AcoAutowired;
import com.aco.spring.annotation.AcoController;
import com.aco.spring.annotation.AcoRequestMapping;
import com.aco.spring.annotation.AcoRequestParameter;
import com.aco.spring.service.AService;

/**
 * @author A.co
 * @date 2023/4/11 15:32
 */
@AcoController
@AcoRequestMapping("test")
public class DemoController {
    @AcoAutowired
    private AService aService;

    @AcoRequestMapping("aaa")
    public String test(@AcoRequestParameter("id")String id,@AcoRequestParameter("name")String name){
        return "Hello World " + id + ":" + name;
    }
}
