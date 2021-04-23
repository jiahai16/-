package com.xjh.atcrowdfunding.manager.service;

import com.xjh.atcrowdfunding.util.Page;
import com.xjh.atcrowdfunding.vo.Data;

import java.util.Map;

public interface RoleService {
    Page queryAllRole(Map<String,Object> map);

    int saveRolePermissionRelationship(Integer roleid, Data datas);
}
