package com.aco.spring.v2.beans.config;

/**
 * Bean定义，封装Bean的配置信息
 * @author A.co
 * @version 1.0
 * @date 2023/4/16 11:01
 */
public class AcoBeanDefinition {
    private String factoryBeanName;
    private String beanClassName;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}
