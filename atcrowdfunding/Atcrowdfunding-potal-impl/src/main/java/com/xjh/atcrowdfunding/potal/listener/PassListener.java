package com.xjh.atcrowdfunding.potal.listener;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.potal.service.MemberService;
import com.xjh.atcrowdfunding.potal.service.TicketService;
import com.xjh.atcrowdfunding.potal.service.impl.MemberServiceImpl;
import com.xjh.atcrowdfunding.potal.service.impl.TicketServiceImpl;
import com.xjh.atcrowdfunding.util.ApplicationContextUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//流程审批通过
public class PassListener implements ExecutionListener {
    
    //@Autowired  不能直接进行自动装配，因为流程监听器是自己创建的
    //MemberService memberService;
    
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        Integer memberid=(Integer)delegateExecution.getVariable("memberid");
        //不能自己创建ioc容器，保证容器的唯一性
        //ApplicationContext applicationContext=new ClassPathXmlApplicationContext("");
        //可行，需要获取application对象
        //ApplicationContext applicationContext= WebApplicationContextUtils.getWebApplicationContext(application)

        //通过自定义的工具类获取IOC容器，实现spring接口，一接口注入的方式获取容器
        ApplicationContext applicationContext= ApplicationContextUtils.applicationContext;
        //获取TicketService对象和MemberService对象

        TicketService ticketService=applicationContext.getBean(TicketService.class);
        MemberService memberService=applicationContext.getBean(MemberService.class);

        //更新t_member中的authstatus字段  1->2  已实名认证
        Member member=memberService.getMemberById(memberid);
        member.setAuthstatus("2");
        memberService.updateAuthstatus(member);
        //更新t_ticket表的字段status  0->1  表示流程结束
        ticketService.updateStatus(member);
    }
}
