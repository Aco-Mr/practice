package com.aco.spring.v2.beans;

/**
 * 对原生对象和代理对象的统一封装
 * @author A.co
 * @version 1.0
 * @date 2023/4/16 11:04
 */
public class AcoBeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public AcoBeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrapperClass = this.wrapperInstance.getClass();
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(Class<?> wrapperClass) {
        this.wrapperClass = wrapperClass;
    }
}
