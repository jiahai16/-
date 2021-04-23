package com.xjh.atcrowdfunding.potal.dao;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.bean.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

	Member queryMebmerlogin(Map<String, Object> paramMap);

	void updateAcctType(Member loginMember);

	void updateBasicinfo(Member loginMember);

	void updateEmail(Member loginMember);

	void updateAuthstatus(Member loginMember);

	Member getMemberById(Integer memberid);

	List<Map<String, Object>> queryCertByMemberid(Integer memberid);

}