package com.xjh.atcrowdfunding.potal.listener;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.potal.service.MemberService;
import com.xjh.atcrowdfunding.potal.service.TicketService;
import com.xjh.atcrowdfunding.util.ApplicationContextUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.context.ApplicationContext;

//流程审批拒绝
public class RefuseListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        Integer memberid=(Integer)delegateExecution.getVariable("memberid") ;

        ApplicationContext applicationContext= ApplicationContextUtils.applicationContext;
        //获取TicketService对象和MemberService对象

        TicketService ticketService=applicationContext.getBean(TicketService.class);
        MemberService memberService=applicationContext.getBean(MemberService.class);

        //更新t_member中的authstatus字段  1->0  未实名认证
        Member member=memberService.getMemberById(memberid);
        member.setAuthstatus("0");
        memberService.updateAuthstatus(member);
        //更新t_ticket表的字段status  0->1  表示流程结束
        ticketService.updateStatus(member);
    }
}
