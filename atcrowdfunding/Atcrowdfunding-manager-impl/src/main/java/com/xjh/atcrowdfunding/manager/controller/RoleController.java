package com.xjh.atcrowdfunding.manager.controller;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.manager.service.PermissionService;
import com.xjh.atcrowdfunding.manager.service.RoleService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import com.xjh.atcrowdfunding.util.Page;
import com.xjh.atcrowdfunding.util.StringUtil;
import com.xjh.atcrowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "role/index";
    }

    @RequestMapping("/toAssignPermission")
    public String toAssignPermission(){
        return "role/assignPermission";
    }

    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                          @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize,
                          String queryText){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Map map=new HashMap();
            map.put("pageno",pageno);
            map.put("pagesize",pagesize);
            if(StringUtil.isNotEmpty(queryText)){
                if(queryText.contains("%")){
                    queryText = queryText.replaceAll("%", "\\\\%");
                }

                map.put("queryText", queryText);
            }
            Page page=roleService.queryAllRole(map);
            ajaxResult.setPage(page);
            ajaxResult.setSuccess(true);

        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("数据加载异常");
            e.printStackTrace();
        }
        return ajaxResult;
    }


    @ResponseBody
    @RequestMapping("/loadDataAsync")
    public Object loadDataAsync(Integer roleid){
        List<Permission> root=new ArrayList<Permission>();
        Map<Integer,Permission> map=new HashMap<>();

        try{
            List<Integer> permissionIdsForRoleid=permissionService.queryPermissionIdsByRoleid(roleid);

            List<Permission> childrenPermission=permissionService.queryAllPermission();
            for (Permission permission:childrenPermission){

                if(permissionIdsForRoleid.contains(permission.getId())){
                    permission.setChecked(true);
                }

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
        }catch (Exception e){
            e.printStackTrace();
        }

        return root;
    }


    @ResponseBody
    @RequestMapping("/doAssignPermission")
    public Object doAssignPermission(Integer roleid, Data datas){
        AjaxResult ajaxResult=new AjaxResult();
        try{

            int count=roleService.saveRolePermissionRelationship(roleid,datas);
            ajaxResult.setSuccess(count==datas.getIds().size());
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("操作执行异常");
            e.printStackTrace();
        }
        return ajaxResult;
    }
}
