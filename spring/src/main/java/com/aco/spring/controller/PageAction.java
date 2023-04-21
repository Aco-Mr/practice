package com.aco.spring.controller;

import com.aco.spring.annotation.AcoController;
import com.aco.spring.annotation.AcoRequestMapping;
import com.aco.spring.annotation.AcoRequestParameter;
import com.aco.spring.v2.servlet.AcoModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author A.co
 * @date 2023/4/20 17:45
 */
@AcoController
@AcoRequestMapping("/")
public class PageAction {

    @AcoRequestMapping("/first.html")
    public AcoModelAndView query(@AcoRequestParameter("teacher") String teacher){
        String result = "";
        Map<String,Object> model = new HashMap<>();
        model.put("teacher",teacher);
        model.put("data",result);
        model.put("token","123456");
        return new AcoModelAndView("first.html",model);
    }
}
