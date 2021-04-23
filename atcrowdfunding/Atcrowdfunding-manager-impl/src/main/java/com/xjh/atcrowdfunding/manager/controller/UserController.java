package com.xjh.atcrowdfunding.manager.controller;

import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.bean.User;
import com.xjh.atcrowdfunding.manager.service.UserService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import com.xjh.atcrowdfunding.util.Page;
import com.xjh.atcrowdfunding.util.StringUtil;
import com.xjh.atcrowdfunding.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //ajax异步请求
    @RequestMapping("/toIndex")
    public String toIndex(){
        return "user/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "user/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){
        User user=userService.getUserById(id);
        map.put("user",user);
        return "user/update";
    }

    @RequestMapping("/toAssignRole")
    public String toAssignRole(Integer id,Map map){
        //所有的角色
        List<Role> allListRole=userService.queryAllRole();
        //拥有的角色id
        List<Integer> roleIds=userService.queryRoleByUserid(id);
        //未分配角色
        List<Role> leftRoleList=new ArrayList<Role>();
        //已分配角色
        List<Role> rightRoleList=new ArrayList<Role>();
        for(Role role:allListRole){
            if(roleIds.contains(role.getId())){
                rightRoleList.add(role);
            }else{
                leftRoleList.add(role);
            }
        }
        map.put("leftRoleList",leftRoleList);
        map.put("rightRoleList",rightRoleList);
        return "user/assignRole";
    }

    //条件查询
    @ResponseBody
    @RequestMapping("/index")
    public Object index(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                        @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,
                        String queryText){
        AjaxResult ajaxResult = new AjaxResult();
        try {

            Map paramMap = new HashMap();
            paramMap.put("pageno", pageno);
            paramMap.put("pagesize", pagesize);

            if(StringUtil.isNotEmpty(queryText)){

                if(queryText.contains("%")){
                    queryText = queryText.replaceAll("%", "\\\\%");
                }

                paramMap.put("queryText", queryText); //   \%
            }
            Page page = userService.queryPage(paramMap);
            ajaxResult.setSuccess(true);
            ajaxResult.setPage(page);
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            e.printStackTrace();
            ajaxResult.setMessage("查询数据失败!");
        }
        return ajaxResult; //将对象序列化为JSON字符串,以流的形式返回.
    }


    //异步请求
//    @ResponseBody
//    @RequestMapping("/index")
//    public Object index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
//              @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize, Map map){
//        AjaxResult ajaxResult=new AjaxResult();
//        try{
//            Page page=userService.queryPage(pageno,pagesize);
//            ajaxResult.setSuccess(true);
//            ajaxResult.setPage(page);
//        }catch (Exception e){
//            ajaxResult.setSuccess(false);
//            ajaxResult.setMessage("查询数据失败！");
//            e.printStackTrace();
//        }
//        return ajaxResult;
//    }


    //同步请求
//    @RequestMapping("/index")
//    public String index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
//              @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize, Map map){
//
//        Page page=userService.queryPage(pageno,pagesize);
//
//        map.put("page",page);
//
//        return "user/index";
//    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(User user){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=userService.saveUser(user);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("保存数据失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(User user){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=userService.updateUser(user);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改数据失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping("/deleteUser")
    public Object deleteUser(Integer id){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=userService.deleteUser(id);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除用户失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Data data){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=userService.deleteBatchUserByVO(data);
            ajaxResult.setSuccess(count==data.getDatas().size());
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除用户失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }


    /*@ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Integer[] id){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=userService.deleteBatchUser(id);
            ajaxResult.setSuccess(count==id.length);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除用户失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }*/

    @ResponseBody
    @RequestMapping("/doAssignRole")
    public Object doAssignRole(Integer userid,Data data){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            userService.saveUserRoleRelationship(userid,data);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("分配角色数据失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doUnAssignRole")
    public Object doUnAssignRole(Integer userid,Data data){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            userService.deleteUserRoleRelationship(userid,data);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("取消分配角色数据失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }
}
