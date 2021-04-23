package com.xjh.atcrowdfunding.manager.service.impl;

import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.bean.RolePermission;
import com.xjh.atcrowdfunding.manager.dao.RoleMapper;
import com.xjh.atcrowdfunding.manager.service.RoleService;
import com.xjh.atcrowdfunding.util.Page;
import com.xjh.atcrowdfunding.vo.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Page queryAllRole(Map<String,Object> map) {
        Page page=new Page((Integer)map.get("pageno"),(Integer)map.get("pagesize"));
        Integer startIndex=page.getStartIndex();
        map.put("startIndex",startIndex);
        List<Role> data=roleMapper.queryList(map);
        Integer count=roleMapper.queryCount(map);
        page.setData(data);
        page.setTotalsize(count);
        return page;
    }

    @Override
    public int saveRolePermissionRelationship(Integer roleid, Data datas) {
        roleMapper.deleteRolePermissionRelationship(roleid);
        int totalCount=0;
        List<Integer> ids=datas.getIds();
        for (Integer permission:ids){
            RolePermission rolePermission=new RolePermission();
            rolePermission.setRoleid(roleid);
            rolePermission.setPermissionid(permission);
            int count=roleMapper.insertRolePermission(rolePermission);
            totalCount+=count;
        }
        return totalCount;
    }
}
