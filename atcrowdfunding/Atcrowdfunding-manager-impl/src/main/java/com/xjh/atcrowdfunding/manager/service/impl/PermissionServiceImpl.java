package com.xjh.atcrowdfunding.manager.service.impl;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.manager.dao.PermissionMapper;
import com.xjh.atcrowdfunding.manager.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Permission getRootPermission() {
        return permissionMapper.getRootPermission();
    }

    @Override
    public List<Permission> getChildrenPermissionByPid(Integer id) {
        return permissionMapper.getChildrenPermissionByPid(id);
    }

    @Override
    public List<Permission> queryAllPermission() {
        return permissionMapper.queryAllPermission();
    }

    @Override
    public Integer saveByPermission(Permission permission) {
        return permissionMapper.insert(permission);
    }

    @Override
    public Permission queryPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updatePermission(Permission permission) {
        return permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public Integer deletePermissionById(Integer id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Integer> queryPermissionIdsByRoleid(Integer roleid) {
        return permissionMapper.queryPermissionIdsByRoleid(roleid);
    }
}
