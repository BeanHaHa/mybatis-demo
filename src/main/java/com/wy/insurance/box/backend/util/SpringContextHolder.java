package com.wy.insurance.box.backend.util;

import org.springframework.context.ApplicationContext;

public class SpringContextHolder {

    private static SpringContextHolder springContextHolder = new SpringContextHolder();

    private ApplicationContext applicationContext;

    private SpringContextHolder() {
    }

    public static SpringContextHolder getInstance() {
        return springContextHolder;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据beanName 获得内存bean
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
