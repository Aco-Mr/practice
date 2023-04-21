package com.aco.spring.v2.servlet;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author A.co
 * @date 2023/4/20 17:11
 */
public class AcoViewResolver {

    private File templateRootDir;

    /**
     * Velocity .vm
     * Freemark .ftl
     * Jsp      .jsp
     */
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    public AcoViewResolver(String templateRoot) throws Exception {
        String templateRootPath = URLDecoder.decode(this.getClass().getClassLoader().getResource(templateRoot).getFile(),"UTF-8");
        this.templateRootDir = new File(templateRootPath);
    }

    public AcoView resolverViewName(String viewName) {
        if (null == viewName || "".equals(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((this.templateRootDir.getPath() + "/" + viewName).replaceAll("/+","/"));

        return new AcoView(templateFile);
    }
}
