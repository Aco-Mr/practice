package com.aco.spring.v1;

import com.aco.spring.annotation.*;

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * 循环依赖问题：是因为构造器填充的时候出现的，因为两个互相注入的类都通过构造器注入，导致注入的时候实例化对象时，构造器无法找到对应的实例化对象从而导致循环依赖。
 * @author A.co
 * @version 1.0
 * @date 2023/4/9 16:45
 */
public class AcoServlet extends HttpServlet {

    private Map<String,Object> ioc = new HashMap<>();

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String,Method> handlerMapping = new HashMap<>();

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

        //获取接口类名
        String beanName = toLowerFirstCase(method.getDeclaringClass().getName());
        //通过类名从ioc中获取对象实例
        try {
            Object re = method.invoke(ioc.get(beanName), paramValues);
            resp.getWriter().print(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //2.扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));
        //3.实例化相关的类，并且将实例对象缓存到IOC容器中
        doInstance();
        //4.完成自动赋值(依赖注入)
        doAutowired();
        //5.初始化HandlerMapping
        doInitHandlerMapping();
        System.out.println("A Spring Framework is init...");
        System.out.println(ioc.toString());
    }

    private void doInitHandlerMapping() {
        if (ioc.isEmpty()){return;}
        //对接口层进行接口地址映射
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
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

    private void doAutowired() {
        if (ioc.isEmpty()){return;}
        //对IOC里的实现类进行属性填充
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获取类的所有属性，包括public/private/protect/default，但是不包含继承的字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            //遍历所有字段，填充属性
            for (Field field : fields) {
                if (!field.isAnnotationPresent(AcoAutowired.class)){continue;}
                AcoAutowired autowired = field.getAnnotation(AcoAutowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)){
                    beanName = toLowerFirstCase(field.getType().getName());
                }

                try {
                    //打开强制填充
                    field.setAccessible(true);
                    //根据beanName从ioc容器中找到对应的实例
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()){return;}
        try {
            //遍历类名，实例化
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //实例化接口层
                if (clazz.isAnnotationPresent(AcoController.class)){
                    Object instance = clazz.newInstance();
                    //转换类名首字母小写
                    String beanName = toLowerFirstCase(className);
                    ioc.put(beanName,instance);
                }
                //实例化业务层
                else if (clazz.isAnnotationPresent(AcoService.class)){
                    //1.默认类名首字母小写
                    String beanName = toLowerFirstCase(className);
                    //2.如果不同报名类出现相同类名，利用注解
                    AcoService serviceAnno = clazz.getAnnotation(AcoService.class);
                    String serviceAnnoName = serviceAnno.value();
                    if (!"".equals(serviceAnnoName)){
                        beanName = serviceAnnoName;
                    }
                    //因为service注解是在class类上的，因此不会出现接口实例化，导致报错
                    Object instance = clazz.newInstance();
                    ioc.put(beanName,instance);
                    //3.获取类的接口，把接口对应的实现类给加载进去IOC里
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())){
                            throw new Exception("The beanName " + i.getName() + " is exists !!!");
                        }
                        ioc.put(i.getName(),instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                classNames.add(className);
                continue;
            }
            doScanner(scanPackage + "." + file.getName());
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
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
