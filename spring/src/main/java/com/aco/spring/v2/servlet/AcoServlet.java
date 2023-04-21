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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 循环依赖问题：是因为构造器填充的时候出现的，因为两个互相注入的类都通过构造器注入，导致注入的时候实例化对象时，构造器无法找到对应的实例化对象从而导致循环依赖。
 * @author A.co
 * @version 1.0
 * @date 2023/4/9 16:45
 */
public class AcoServlet extends HttpServlet {

    private List<AcoHandlerMapping> handlerMappings = new ArrayList<>();

    private Map<AcoHandlerMapping,AcoHandlerAdapter> handlerAdapters = new HashMap<>();

    private List<AcoViewResolver> viewResolvers = new ArrayList<>();

    private AcoApplicationContext context;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //调用接口
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String,Object> model = new HashMap<>();
            model.put("detail","500 Exception Detail");
            model.put("stackTrace",Arrays.toString(e.getStackTrace()));
            try {
                processDispatchResult(req,resp,new AcoModelAndView("500",model));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //1、根据url去找到一个HandlerMapping对象
        AcoHandlerMapping mappingHandler = getHandler(req);
        if (mappingHandler == null){
            //返回404
            processDispatchResult(req,resp,new AcoModelAndView("404"));
            return;
        }

        //2、根据HandlerMapping获取一个HandlerAdapter
        AcoHandlerAdapter ha = getHandlerAdapter(mappingHandler);

        //3、根据HandlerAdapter的方法动态匹配参数，并且得到返回值ModelAndView
        AcoModelAndView mv = ha.handle(req,resp,mappingHandler);

        //4、根据ModelAndView决定选择哪个ViewResolver进行解析和渲染
        processDispatchResult(req,resp,mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, AcoModelAndView mv) throws Exception{
        if (null == mv){return;}

        if (this.viewResolvers.isEmpty()){return;}

        for (AcoViewResolver viewResolver : this.viewResolvers) {
            AcoView view = viewResolver.resolverViewName(mv.getViewName());
            view.render(mv.getModel(),req,resp);
            return;
        }
    }

    private AcoHandlerAdapter getHandlerAdapter(AcoHandlerMapping mappingHandler) {
        if (this.handlerAdapters.isEmpty()){return null;}
        return this.handlerAdapters.get(mappingHandler);
    }

    private AcoHandlerMapping getHandler(HttpServletRequest req) {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");

        for (AcoHandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()){continue;}
            return handlerMapping;
        }
        return null;
    }

//    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String url = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        url = url.replaceAll(contextPath,"").replaceAll("/+","/");
//
//        if (!this.handlerMapping.containsKey(url)){
//            resp.getWriter().write("404 Not Found!");
//            return;
//        }
//
//        //获取地址请求参数
//        Map<String, String[]> params = req.getParameterMap();
//        //获取接口层方法
//        Method method = handlerMapping.get(url);
//
//        //拿到Method的形参列表
//        Class<?>[] parameterTypes = method.getParameterTypes();
//        //定义Method的实参列表
//        Object[] paramValues = new Object[parameterTypes.length];
//
//        Annotation[][] pa = method.getParameterAnnotations();
//
//        for (int i = 0; i < parameterTypes.length; i++) {
//            Class paramType = parameterTypes[i];
//            //请求类
//            if (paramType == HttpServletRequest.class){
//                paramValues[i] = req;
//            }
//            //响应类
//            else if (paramType == HttpServletRequest.class){
//                paramValues[i] = resp;
//            }
//            //字符串
//            else if (paramType == String.class){
//                for (Annotation a : pa[i]) {
//                    if (a instanceof AcoRequestParameter){
//                        String paramName = ((AcoRequestParameter) a).value();
//                        String value = Arrays.toString(params.get(paramName))
//                                .replaceAll("\\[|\\]","")
//                                .replaceAll("\\s","");
//                        paramValues[i] = value;
//                    }
//                }
//            }
//            //其它类
//            else {
//                paramValues[i] = null;
//            }
//        }
//
//        //通过类名从ioc中获取对象实例
//        try {
//            Object re = method.invoke(this.context.getBean(method.getDeclaringClass()), paramValues);
//            resp.getWriter().print(re);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //===============Ioc和DI====================
        context = new AcoApplicationContext(config.getInitParameter("contextConfigLocation"));

        //===========MVC============
        try {
            initStrategies(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.初始化HandlerMapping
//        doInitHandlerMapping();
        System.out.println("A Spring Framework is init...");
    }

    protected void initStrategies(AcoApplicationContext context) throws Exception{
        //多文件上传的组件
//        initMultipartResolver(context);
        //初始化本地语言环境
//        initThemeResolver(context);
        //handlerMapping
        initHandlerMapping(context);
        //初始化参数适配器
        initHandlerAdapters(context);
        //初始化异常拦截器
//        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
//        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        //FlashMap管理器
//        initFlashMapManager(context);

    }

    private void initViewResolvers(AcoApplicationContext context) throws Exception{
        //获取默认的文件模板路径
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = URLDecoder.decode(this.getClass().getClassLoader().getResource(templateRoot).getFile(),"UTF-8");

        //保存模板文件根目录
        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new AcoViewResolver(templateRoot));
            break;
        }
    }

    private void initHandlerAdapters(AcoApplicationContext context) {
        //将handlerMapping和handlerAdapter建立一对一的关系
        for (AcoHandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapters.put(handlerMapping,new AcoHandlerAdapter());
        }
    }

    private void initHandlerMapping(AcoApplicationContext context) {
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
                String regx = ("/" + baseUrl + "/" + mapping.value())
                        .replaceAll("\\*",".*")
                        .replaceAll("/+","/");
                Pattern pattern = Pattern.compile(regx);
                handlerMappings.add(new AcoHandlerMapping(pattern,instance,method));
                System.out.println("Mapped:" + pattern + "," + method);
            }
        }
    }

}
