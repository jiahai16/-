package com.xjh.atcrowdfunding.potal.service;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.bean.Ticket;

import java.util.List;
import java.util.Map;

public interface MemberService{
    Member queryMemberlogin(Map<String, Object> paramMap);

    void updateAccttype(Member loginMember);

    void updateBasicinfo(Member loginMember);

    void updateEmail(Member loginMember);

    void updateAuthstatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);
}
