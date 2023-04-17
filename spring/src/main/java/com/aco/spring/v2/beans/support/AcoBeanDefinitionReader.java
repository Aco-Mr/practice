package com.aco.spring.v2.beans.support;


import com.aco.spring.annotation.AcoController;
import com.aco.spring.annotation.AcoService;
import com.aco.spring.v2.beans.config.AcoBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 用来读取配置文件的工具类
 * @author A.co
 * @version 1.0
 * @date 2023/4/14 11:05
 */
public class AcoBeanDefinitionReader {
    private Properties contextConfig = new Properties();
    
    private List<String> registerBeanClasses = new ArrayList<>();

    public AcoBeanDefinitionReader(String[] configLocations) {
        //1、加载配置文件(暂时只有一个)
        doLoadConfig(configLocations[0]);

        //2、解析配置文件，将配置信息封装成BeanDefinition
        doScanner(contextConfig.getProperty("scanPackage"));
    }

    /**
     * 将配置信息封装成BeanDefinition
     * @return
     */
    public List<AcoBeanDefinition> doLoadBeanDefinitions() {
        List<AcoBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registerBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()){continue;}

                //不是交给spring管理的类不加载，防止重复bean信息加载
                if (!(beanClass.isAnnotationPresent(AcoService.class) || beanClass.isAnnotationPresent(AcoController.class))){
                    continue;
                }

                //1、默认用类名首字母小写作为beanName
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));

                //2、接口的全名作为beanName
                for (Class<?> i : beanClass.getInterfaces()) {
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }


            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private AcoBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        AcoBeanDefinition beanDefinition = new AcoBeanDefinition();
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


    private void doScanner(String scanPackage) {
//        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        String url = null;
        try {
            url = URLDecoder.decode(this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/")).getPath(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //包路径对应的文件路径，ClassPath
        File classPath = new File(url);
        //获取所有类名
        for (File file : classPath.listFiles()) {
            if (!file.isDirectory()){
                //非类文件不加载
                if (!file.getName().endsWith(".class")){continue;}
                String className = scanPackage + "." + file.getName().replaceAll(".class","");
                registerBeanClasses.add(className);
                continue;
            }
            doScanner(scanPackage + "." + file.getName());
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation.replaceAll("classpath:",""));
        try {
            contextConfig.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resource != null){
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
