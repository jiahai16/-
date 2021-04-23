package com.xjh.atcrowdfunding.manager.service.impl;

import com.xjh.atcrowdfunding.bean.Permission;
import com.xjh.atcrowdfunding.bean.Role;
import com.xjh.atcrowdfunding.bean.User;
import com.xjh.atcrowdfunding.exception.LoginFailException;
import com.xjh.atcrowdfunding.manager.dao.UserMapper;
import com.xjh.atcrowdfunding.manager.service.UserService;
import com.xjh.atcrowdfunding.util.Const;
import com.xjh.atcrowdfunding.util.MD5Util;
import com.xjh.atcrowdfunding.util.Page;
import com.xjh.atcrowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserlogin(Map<String, Object> paramMap) {

        User user=userMapper.queryUserlogin(paramMap);

        if(user==null){

            throw new LoginFailException("账户或密码不正确！");
        }

        return user;
    }

//    ajax异步请求
    @Override
    public Page queryPage(Map<String, Object> paramMap) {

        Page page=new Page((Integer)paramMap.get("pageno"),(Integer)paramMap.get("pagesize"));
        Integer startIndex=page.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<User> data=userMapper.queryList(paramMap);
        Integer count=userMapper.queryCount(paramMap);
        page.setData(data);
        page.setTotalsize(count);
        return page;
    }

//    @Override
//    public Page queryPage(Integer pageno, Integer pagesize) {
//
//        Page page=new Page(pageno,pagesize);
//        Integer startIndex=page.getStartIndex();
//        List<User> data=userMapper.queryList(startIndex,pagesize);
//
//        Integer totalsize=userMapper.queryCount();
//
//        page.setData(data);
//        page.setTotalsize(totalsize);
//        return page;
//    }

    @Override
    public int saveUser(User user) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String createtime=simpleDateFormat.format(new Date());
        user.setCreatetime(createtime);
        user.setUserpswd(MD5Util.digest(Const.PASSWORD));
        return userMapper.insert(user);
    }

    @Override
    public User getUserById(Integer id) {
        User user=userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public int updateUser(User user) {

        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBatchUser(Integer[] ids) {
        int totalCount=0;
        for (Integer id:ids){
            int count=userMapper.deleteByPrimaryKey(id);
            totalCount+=count;
        }
        if(totalCount!=ids.length){
            throw new RuntimeException("批量删除失败!");
        }
        return totalCount;
    }

   /* @Override
    public int deleteBatchUserByVO(Data data) {
        return userMapper.deleteBatchUserByVO(data);
    }*/
   @Override
   public int deleteBatchUserByVO(Data data) {
       return userMapper.deleteBatchUserByVO(data.getDatas());
   }

    @Override
    public List<Role> queryAllRole() {
        return userMapper.queryAllRole();
    }

    @Override
    public List<Integer> queryRoleByUserid(Integer id) {
        return userMapper.querRoleByUserid(id);
    }

    @Override
    public Integer saveUserRoleRelationship(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationship(userid, data);
    }

    @Override
    public Integer deleteUserRoleRelationship(Integer userid, Data data) {
        return userMapper.deleteUserRoleRelationship(userid, data);
    }

    @Override
    public List<Permission> queryPermissionByUserId(Integer id) {
        return userMapper.queryPermissionByUserId(id);
    }
}
