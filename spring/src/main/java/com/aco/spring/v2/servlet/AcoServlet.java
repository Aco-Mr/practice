package com.aco.spring.v2.servlet;

import com.aco.spring.annotation.*;
import com.aco.spring.v2.AcoApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

/**
 * 循环依赖问题：是因为构造器填充的时候出现的，因为两个互相注入的类都通过构造器注入，导致注入的时候实例化对象时，构造器无法找到对应的实例化对象从而导致循环依赖。
 * @author A.co
 * @version 1.0
 * @date 2023/4/9 16:45
 */
public class AcoServlet extends HttpServlet {

    private Map<String,Method> handlerMapping = new HashMap<>();

    private AcoApplicationContext context;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用接口
        doDispatch(req,resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");

        if (!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 Not Found!");
            return;
        }

        //获取地址请求参数
        Map<String, String[]> params = req.getParameterMap();
        //获取接口层方法
        Method method = handlerMapping.get(url);

        //拿到Method的形参列表
        Class<?>[] parameterTypes = method.getParameterTypes();
        //定义Method的实参列表
        Object[] paramValues = new Object[parameterTypes.length];

        Annotation[][] pa = method.getParameterAnnotations();

        for (int i = 0; i < parameterTypes.length; i++) {
            Class paramType = parameterTypes[i];
            //请求类
            if (paramType == HttpServletRequest.class){
                paramValues[i] = req;
            }
            //响应类
            else if (paramType == HttpServletRequest.class){
                paramValues[i] = resp;
            }
            //字符串
            else if (paramType == String.class){
                for (Annotation a : pa[i]) {
                    if (a instanceof AcoRequestParameter){
                        String paramName = ((AcoRequestParameter) a).value();
                        String value = Arrays.toString(params.get(paramName))
                                .replaceAll("\\[|\\]","")
                                .replaceAll("\\s","");
                        paramValues[i] = value;
                    }
                }
            }
            //其它类
            else {
                paramValues[i] = null;
            }
        }

        //通过类名从ioc中获取对象实例
        try {
            Object re = method.invoke(this.context.getBean(method.getDeclaringClass()), paramValues);
            resp.getWriter().print(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new AcoApplicationContext(config.getInitParameter("contextConfigLocation"));

        //===========MVC============
        //5.初始化HandlerMapping
        doInitHandlerMapping();
        System.out.println("A Spring Framework is init...");
    }

    private void doInitHandlerMapping() {
        if (this.context.getBeanDefinitionCount() == 0){return;}

        //对接口层进行接口地址映射
        String[] beanNames = this.context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object instance = this.context.getBean(beanName);
            Class<?> clazz = instance.getClass();
            if (!clazz.isAnnotationPresent(AcoController.class)){continue;}

            //接口类上的路径前缀
            String baseUrl = "";
            if (clazz.isAnnotationPresent(AcoRequestMapping.class)){
                baseUrl = clazz.getAnnotation(AcoRequestMapping.class).value();
            }
            //处理方法路径
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(AcoRequestMapping.class)){continue;}
                AcoRequestMapping mapping = method.getAnnotation(AcoRequestMapping.class);
                String url = ("/" + baseUrl + "/" + mapping.value()).replaceAll("/+","/");
                handlerMapping.put(url,method);
                System.out.println("Mapped:" + url + "," + method);
            }
        }
    }

}
