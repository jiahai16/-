package com.xjh.atcrowdfunding.manager.controller;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.manager.service.PermissionService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @RequestMapping("/toIndex")
    public String toIndex(){
        return "permission/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "permission/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){
        Permission permission=permissionService.queryPermissionById(id);
        map.put("permission",permission);
        return "permission/update";
    }

    //一次查询，少量循环
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadPermission(){
        AjaxResult ajaxResult=new AjaxResult();
        List<Permission> root=new ArrayList<Permission>();
        Map<Integer,Permission> map=new HashMap<>();
        try{
            List<Permission> childrenPermission=permissionService.queryAllPermission();
            for (Permission permission:childrenPermission){
                map.put(permission.getId(),permission);
            }
            for(Permission permission:childrenPermission){
                Permission child=permission;
                if(child.getPid()==null){
                    root.add(child);
                }else{
                    Permission parent=map.get(child.getPid());
                    parent.getChildren().add(child);
                    parent.setOpen(true);
                }
            }
            ajaxResult.setData(root);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("数据加载异常");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(Permission permission){
        AjaxResult ajaxResult=new AjaxResult();
        try{

            Integer count=permissionService.saveByPermission(permission);
            ajaxResult.setSuccess(count==1);

        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("数据保存失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(Permission permission){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=permissionService.updatePermission(permission);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改权限树信息异常");
            e.printStackTrace();
        }

        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/deletePermission")
    public Object deletePermission(Integer id){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Integer count=permissionService.deletePermissionById(id);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除许可信息失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    /*一次查询，多次循环
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadPermission(){
        AjaxResult ajaxResult=new AjaxResult();
        List<Permission> root=new ArrayList<Permission>();
        try{
            List<Permission> childrenPermission=permissionService.queryAllPermission();
            for(Permission permission:childrenPermission){
                Permission child=permission;
                if(child.getPid()==null){
                    root.add(child);
                }else{
                    for (Permission innerPermission:childrenPermission){
                        if(child.getPid()==innerPermission.getId()){
                            Permission parent=innerPermission;
                            parent.getChildren().add(child);
                            break;
                        }
                    }
                }
            }
            ajaxResult.setData(root);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("数据加载异常");
            e.printStackTrace();
        }
        return ajaxResult;
    }*/

    /*
    @ResponseBody
    @RequestMapping("/loadData")
    public Object loadPermission(){
        AjaxResult ajaxResult=new AjaxResult();
        List<Permission> root=new ArrayList<Permission>();
        try{
            //父节点
            Permission permission=permissionService.getRootPermission();
            //子节点
            List<Permission> children=permissionService.getChildrenPermissionByPid(permission.getId());
            for (Permission child:children){
                child.setOpen(true);
                List<Permission> innerChildren=permissionService.getChildrenPermissionByPid(child.getId());
                child.setChildren(innerChildren);
            }
            permission.setChildren(children);
            root.add(permission);
            ajaxResult.setSuccess(true);
            ajaxResult.setData(root);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("数据加载异常");
            e.printStackTrace();
        }
        return ajaxResult;
    }*/

    //采用递归，多次查询
    /*@ResponseBody
    @RequestMapping("/loadData")
    public Object loadPermission(){
        AjaxResult ajaxResult=new AjaxResult();
        List<Permission> root=new ArrayList<Permission>();
        try{
            //父节点
            Permission permission=permissionService.getRootPermission();

            queryChildPermission(permission);
            root.add(permission);
            ajaxResult.setSuccess(true);
            ajaxResult.setData(root);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("数据加载异常");
            e.printStackTrace();
        }
        return ajaxResult;
    }
    private void queryChildPermission(Permission permission){
        List<Permission> innerChildren=permissionService.getChildrenPermissionByPid(permission.getId());
        permission.setChildren(innerChildren);
        for (Permission permission1:innerChildren){
            permission1.setOpen(true);
            queryChildPermission(permission1);
        }
    }*/
}
