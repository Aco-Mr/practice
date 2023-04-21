package com.aco.spring.v2.servlet;

import com.aco.spring.annotation.AcoRequestParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author A.co
 * @date 2023/4/20 17:05
 */
public class AcoHandlerAdapter {
    /**
     * 完成动态参数的匹配
     * @param req
     * @param resp
     * @param mappingHandler
     * @return
     */
    public AcoModelAndView handle(HttpServletRequest req, HttpServletResponse resp, AcoHandlerMapping mappingHandler) throws Exception {
        //获取地址请求参数
        Map<String, String[]> params = req.getParameterMap();
        //获取接口层方法
        Method method = mappingHandler.getMethod();

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
        Object result = method.invoke(mappingHandler.getController(), paramValues);
        if (result == null || result instanceof Void){ return null; }

        boolean isModelAndView = mappingHandler.getMethod().getReturnType() == AcoModelAndView.class;
        if (isModelAndView){
            return (AcoModelAndView) result;
        }

        return null;
    }
}
