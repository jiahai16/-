package com.xjh.atcrowdfunding.manager.dao;

import com.xjh.atcrowdfunding.bean.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(Integer id);

    List<Permission> selectAll();

    int updateByPrimaryKey(Permission record);

    Permission getRootPermission();

    List<Permission> getChildrenPermissionByPid(Integer id);

    List<Permission> queryAllPermission();

    List<Integer> queryPermissionIdsByRoleid(Integer roleid);
}