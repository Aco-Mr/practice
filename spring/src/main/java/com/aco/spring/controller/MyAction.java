package com.aco.spring.controller;

import com.aco.spring.annotation.AcoController;
import com.aco.spring.annotation.AcoRequestMapping;
import com.aco.spring.annotation.AcoRequestParameter;
import com.aco.spring.v2.servlet.AcoModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author A.co
 * @date 2023/4/20 17:39
 */
@AcoController
@AcoRequestMapping("/web")
public class MyAction {

    @AcoRequestMapping("query.json")
    public AcoModelAndView query(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return out(response,"");
    }

    @AcoRequestMapping("add*.json")
    public AcoModelAndView add(HttpServletRequest request, HttpServletResponse response, @AcoRequestParameter("name") String name,@AcoRequestParameter("addr") String addr) throws Exception{
        throw new Exception("Aco 没事干，随便抛个异常");
//        return out(response,"");
    }

    @AcoRequestMapping("remove.json")
    public AcoModelAndView remove(HttpServletRequest request, HttpServletResponse response, @AcoRequestParameter("id") String id){
        return out(response,"id:" + id);
    }

    @AcoRequestMapping("calc.json")
    public AcoModelAndView calc( HttpServletResponse response, @AcoRequestParameter("a") Integer a,@AcoRequestParameter("b") Integer b){
        return out(response,"a:" + a + "，b:" + b);
    }

    @AcoRequestMapping("edit.json")
    public AcoModelAndView edit( HttpServletResponse response, @AcoRequestParameter("id") Integer id){
        return out(response,"id:" + id);
    }

    private AcoModelAndView out(HttpServletResponse response,String str){
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
