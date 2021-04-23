package com.xjh.atcrowdfunding.potal.service.impl;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.bean.Ticket;
import com.xjh.atcrowdfunding.exception.LoginFailException;
import com.xjh.atcrowdfunding.potal.dao.MemberMapper;
import com.xjh.atcrowdfunding.potal.service.MemberService;
import com.xjh.atcrowdfunding.potal.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;
    @Override
    public Member queryMemberlogin(Map<String, Object> paramMap) {
        Member member=memberMapper.queryMebmerlogin(paramMap);
        if (member==null){
            throw new LoginFailException("账号或密码不正确!");
        }
        return member;
    }

    @Override
    public void updateAccttype(Member loginMember) {
        memberMapper.updateAcctType(loginMember);

    }

    @Override
    public void updateBasicinfo(Member loginMember) {
        memberMapper.updateBasicinfo(loginMember);
    }

    @Override
    public void updateEmail(Member loginMember) {
        memberMapper.updateEmail(loginMember);
    }

    @Override
    public void updateAuthstatus(Member loginMember) {
        memberMapper.updateAuthstatus(loginMember);
    }

    @Override
    public Member getMemberById(Integer memberid) {
        return memberMapper.getMemberById(memberid);
    }

    @Override
    public List<Map<String, Object>> queryCertByMemberid(Integer memberid) {
        return memberMapper.queryCertByMemberid(memberid);
    }
}
