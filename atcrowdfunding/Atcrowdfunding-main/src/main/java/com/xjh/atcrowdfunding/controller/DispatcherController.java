package com.xjh.atcrowdfunding.controller;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.bean.User;
import com.xjh.atcrowdfunding.manager.service.UserService;
import com.xjh.atcrowdfunding.potal.service.MemberService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import com.xjh.atcrowdfunding.util.Const;
import com.xjh.atcrowdfunding.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DispatcherController {
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/member")
    public String member(HttpSession session){
        return "member/member";
    }

    @RequestMapping("/main")
    public String main(HttpSession session){

        return "main";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();//销毁session对象或者清理session域
        return "redirect:/index.jsp";
    }

    //基于Ajax的异步请求
    //@ResponseBody：j结合Jackson组件，将返回结果z转换为字符串，将json串以流的形式返回给客户端
    @ResponseBody
    @RequestMapping("/doLogin")

    public Object doLogin(String loginacct, String userpswd, String type, HttpSession session){

        AjaxResult ajaxResult=new AjaxResult();

        try {
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("loginacct",loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type",type);
            if("member".equals(type)){

                Member member=memberService.queryMemberlogin(paramMap);
                session.setAttribute(Const.LOGIN_MEMBER,member);
            }else if ("user".equals(type)){
                User user= userService.queryUserlogin(paramMap);

                session.setAttribute(Const.LOGIN_USER,user);


                //-------------------------------------
                //加载当前用户的所拥有的许可权限
                List<Permission> myPermission=userService.queryPermissionByUserId(user.getId());
                Permission permissionRoot=null;
                Map<Integer,Permission> map=new HashMap();
                Set<String> myUris=new HashSet<>();//用于拦截器拦截许可权限
                for(Permission innerPermission:myPermission){
                    map.put(innerPermission.getId(),innerPermission);
                    myUris.add("/"+innerPermission.getUrl());
                }
                session.setAttribute(Const.MY_URIS,myUris);
                for (Permission permission:myPermission){
                    Permission child=permission;
                    if(child.getPid()==null){
                        permissionRoot=permission;
                    }else {
                        Permission parent=map.get(child.getPid());
                        parent.getChildren().add(child);
                    }
                }
                session.setAttribute("permissionRoot",permissionRoot);
                //--------------------
            }else {

            }
            ajaxResult.setSuccess(true);
            ajaxResult.setData(type);
            //{"success":true}
        }catch (Exception e){
            ajaxResult.setMessage("登录失败！");
            ajaxResult.setSuccess(false);
            //{"success":false,"message":"登录失败！"}
            e.printStackTrace();
        }

        return ajaxResult;
    }

    //同步请求
//    @RequestMapping("/doLogin")
//    public String doLogin(String loginacct, String userpswd, String type, HttpSession session){
//
//        Map<String,Object> paramMap=new HashMap<>();
//
//        paramMap.put("loginacct",loginacct);
//
//        paramMap.put("userpswd",userpswd);
//
//        paramMap.put("type",type);
//
//        User user= userService.queryUserlogin(paramMap);
//
//        session.setAttribute(Const.LOGIN_USER,user);
//
//        return "redirect::/main.htm";
//    }
}
