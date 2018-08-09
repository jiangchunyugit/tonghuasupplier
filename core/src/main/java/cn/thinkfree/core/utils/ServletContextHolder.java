package cn.thinkfree.core.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * servlet上下文钩子
 */
public class ServletContextHolder {
    private static ServletContext servletContext;

    public static ServletContext getSevletContext(){
        if(servletContext == null){
            setServletContext();
        }
        return servletContext;
    }

    private static void setServletContext(){
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext nowServletContext = webApplicationContext.getServletContext();
        servletContext = nowServletContext;
    }

    public static String getUpload(String path){
        return  getSevletContext().getRealPath(path);
    }
}
