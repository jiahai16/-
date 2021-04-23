package com.xjh.atcrowdfunding.listener;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.manager.service.PermissionService;
import com.xjh.atcrowdfunding.util.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StartSystemListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext application=servletContextEvent.getServletContext();
        ApplicationContext ioc= WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService=ioc.getBean(PermissionService.class);

        List<Permission> allPermission=permissionService.queryAllPermission();
        Set<String> allURIs=new HashSet<>();
        for (Permission permission:allPermission){
            allURIs.add("/"+permission.getUrl());
        }
        application.setAttribute(Const.ALL_PERMISSION_URI,allURIs);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
