package com.aco.spring.v2.servlet;

import java.util.Map;

/**
 * @author A.co
 * @date 2023/4/20 17:18
 */
public class AcoModelAndView {
    private String viewName;
    private Map<String,?> model;

    public AcoModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public AcoModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
