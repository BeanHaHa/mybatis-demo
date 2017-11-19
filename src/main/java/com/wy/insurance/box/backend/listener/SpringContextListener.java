package com.wy.insurance.box.backend.listener;

import com.wy.insurance.box.backend.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class SpringContextListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(SpringContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SpringContextHolder.getInstance().setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}