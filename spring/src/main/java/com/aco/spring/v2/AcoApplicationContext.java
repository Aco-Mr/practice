package com.aco.spring.v2;

import com.aco.spring.annotation.AcoAutowired;
import com.aco.spring.annotation.AcoController;
import com.aco.spring.annotation.AcoService;
import com.aco.spring.v2.beans.AcoBeanWrapper;
import com.aco.spring.v2.beans.config.AcoBeanDefinition;
import com.aco.spring.v2.beans.support.AcoBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring主入口
 * @author A.co
 * @version 1.0
 * @date 2023/4/14 11:00
 */
public class AcoApplicationContext {

    private String[] configLocations;

    private AcoBeanDefinitionReader reader;

    private Map<String,AcoBeanDefinition> beanDefinitionMap = new HashMap<>();

    private Map<String,AcoBeanWrapper> factoryBeanInstanceCache = new HashMap<>();

    private Map<String,Object> factoryBeanObjectCache = new HashMap<>();

    public AcoApplicationContext(String... configLocations) {
        this.configLocations = configLocations;

        try {
            //1、加载配置文件
            this.reader = new AcoBeanDefinitionReader(this.configLocations);
            List<AcoBeanDefinition> beanDefinitions = this.reader.doLoadBeanDefinitions();

            //2、将BeanDefinition对象缓存起来
            doRegistryBeanDefinition(beanDefinitions);

            //3、创建Ioc容器
            doCreateBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doCreateBean() {
        for (Map.Entry<String, AcoBeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            getBean(beanName);
        }
    }

    private void doRegistryBeanDefinition(List<AcoBeanDefinition> beanDefinitions) throws Exception{
        for (AcoBeanDefinition beanDefinition : beanDefinitions) {
            if (this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists!!");
            }
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
            this.beanDefinitionMap.put(beanDefinition.getBeanClassName(),beanDefinition);
        }
    }

    /**
     * 两个职责
     * 1、创建Bean的实例
     * 2、完成依赖注入
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        //1、拿到beanName对应的配置信息，即BeanDefinition对象，根据配置信息创建对象
        AcoBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        //2、拿到BeanDefinition对象以后，进行实例化
        Object instance = instantiateBean(beanName,beanDefinition);
        if (instance == null){return null;}

        //3、将实例封装成BeanWrapper
        AcoBeanWrapper beanWrapper = new AcoBeanWrapper(instance);

        //4、将beanWrapper对象缓存到Ioc容器中
        this.factoryBeanInstanceCache.put(beanName,beanWrapper);

        //5、完成依赖注入
        populateBean(beanName,beanDefinition,beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrapperInstance();
    }

    private void populateBean(String beanName, AcoBeanDefinition beanDefinition, AcoBeanWrapper beanWrapper) {

        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrapperClass();

        //获取类的所有属性，包括public/private/protect/default，但是不包含继承的字段
        Field[] fields = clazz.getDeclaredFields();
        //遍历所有字段，填充属性
        for (Field field : fields) {
            if (!field.isAnnotationPresent(AcoAutowired.class)) {
                continue;
            }
            AcoAutowired autowired = field.getAnnotation(AcoAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(beanName)) {
                autowiredBeanName = field.getType().getName();
            }

            try {
                //打开强制访问
                field.setAccessible(true);
                if (!this.factoryBeanObjectCache.containsKey(autowiredBeanName)){continue;}

                //根据beanName从ioc容器中找到对应的实例
                //找到了@AcoAutowired这个注解的字段进行自动赋值
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object instantiateBean(String beanName, AcoBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;

        try {
            Class<?> clazz = Class.forName(className);
            if (!(clazz.isAnnotationPresent(AcoService.class) || clazz.isAnnotationPresent(AcoController.class))){
                return null;
            }

            //创建原生对象
            instance = clazz.newInstance();

            //AOP会配置一个切面表达式
            //excutions(public * com.aco.*.query*(..*)) 切面表达式
            //如果匹配上，就要创建代理对象，如果匹配不上，用直接返回原生对象
            //这里就是AOP的入口

            //三级缓存
            this.factoryBeanObjectCache.put(beanName,instance);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public Object getBean(Class className){
        return getBean(className.getName());
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }
}
