package cn.superfw.framework.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext appContext;

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        appContext = ctx;
    }

    public static Object getBean(String name) {
        return appContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return appContext.getBean(name, requiredType);
    }

}
