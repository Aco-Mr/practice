package com.aco.spring.v2.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author A.co
 * @date 2023/4/20 16:56
 */
public class AcoHandlerMapping {
    private Pattern pattern;
    private Object controller;
    private Method method;

    public Pattern getPattern() {
        return pattern;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    public AcoHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }
}
