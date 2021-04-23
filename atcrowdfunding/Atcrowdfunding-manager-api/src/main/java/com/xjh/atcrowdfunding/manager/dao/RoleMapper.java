package com.xjh.atcrowdfunding.manager.dao;

import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.bean.RolePermission;
import com.xjh.atcrowdfunding.bean.User;
import com.xjh.atcrowdfunding.util.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    List<Role> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    void deleteRolePermissionRelationship(Integer roleid);

    int insertRolePermission(RolePermission rolePermission);
}