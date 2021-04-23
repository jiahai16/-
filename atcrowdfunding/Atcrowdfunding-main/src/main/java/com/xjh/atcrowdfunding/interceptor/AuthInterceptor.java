package com.xjh.atcrowdfunding.interceptor;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.manager.service.PermissionService;
import com.xjh.atcrowdfunding.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private PermissionService permissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.查询所有许可
        Set<String> allURIs=(Set<String>) request.getSession().getServletContext().getAttribute(Const.ALL_PERMISSION_URI);
        //2.判断q请求路径是否在所有许可范围之内
        String servletPath=request.getServletPath();
        if (allURIs.contains(servletPath)){
            Set<String> myURIs=new HashSet<>();
            myURIs=(Set<String>)request.getSession().getAttribute(Const.MY_URIS);
            if(myURIs.contains(servletPath)){

                return true;
            }else {
                response.sendRedirect(request.getContextPath()+"/login.htm");
                return false;
            }
        }else {
            return true;
        }

    }
}
