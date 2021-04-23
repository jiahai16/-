package com.xjh.atcrowdfunding.manager.service;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.bean.User;
import com.xjh.atcrowdfunding.util.Page;
import com.xjh.atcrowdfunding.vo.Data;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserlogin(Map<String, Object> paramMap);
    //Page queryPage(Integer pageno,Integer pagesize);
    Page queryPage(Map<String,Object> paramMap);  //ajax异步请求
    int saveUser(User user);
    User getUserById(Integer id);
    int updateUser(User user);
    int deleteUser(Integer id);
    int deleteBatchUser(Integer[] ids);

    int deleteBatchUserByVO(Data data);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserid(Integer id);

    Integer saveUserRoleRelationship(Integer userid, Data data);

    Integer deleteUserRoleRelationship(Integer userid, Data data);

    List<Permission> queryPermissionByUserId(Integer id);
}
