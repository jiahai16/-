package com.xjh.atcrowdfunding.manager.dao;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.bean.User;
import com.xjh.atcrowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

	User queryUserlogin(Map<String, Object> paramMap);

//	List<User> queryList(@Param("startIndex") Integer startIndex, @Param("pagesize") Integer pagesize);
//
//	Integer queryCount();
//    ajax异步请求
    List<User> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);
    //int deleteBatchUserByVO(Data data);

    //int deleteBatchUserByVO(List<User> userList);

    //int deleteBatchUserByVO(User[] userList);

    int deleteBatchUserByVO(@Param("userList") List<User> userList);

    List<Role> queryAllRole();

    List<Integer> querRoleByUserid(Integer id);

    Integer saveUserRoleRelationship(@Param("userid") Integer userid, @Param("data") Data data);

    Integer deleteUserRoleRelationship(@Param("userid") Integer userid,@Param("data") Data data);

    List<Permission> queryPermissionByUserId(Integer id);
}