package com.xjh.atcrowdfunding.manager.service;

import com.xjh.atcrowdfunding.bean.Permission;

import java.util.List;

public interface PermissionService {
    public Permission getRootPermission();
    public List<Permission> getChildrenPermissionByPid(Integer id);

    List<Permission> queryAllPermission();

    Integer saveByPermission(Permission permission);

    Permission queryPermissionById(Integer id);

    Integer updatePermission(Permission permission);

    Integer deletePermissionById(Integer id);

    List<Integer> queryPermissionIdsByRoleid(Integer roleid);
}
