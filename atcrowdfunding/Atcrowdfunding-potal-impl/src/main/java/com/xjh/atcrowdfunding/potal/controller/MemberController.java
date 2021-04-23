package com.xjh.atcrowdfunding.potal.controller;

import com.xjh.atcrowdfunding.bean.Cert;
import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.bean.MemberCert;
import com.xjh.atcrowdfunding.bean.Ticket;
import com.xjh.atcrowdfunding.manager.service.CertService;
import com.xjh.atcrowdfunding.potal.listener.PassListener;
import com.xjh.atcrowdfunding.potal.listener.RefuseListener;
import com.xjh.atcrowdfunding.potal.service.MemberService;
import com.xjh.atcrowdfunding.potal.service.TicketService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import com.xjh.atcrowdfunding.util.Const;
import com.xjh.atcrowdfunding.vo.Data;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.jfree.chart.axis.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CertService certService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/accttype")
    public String accttype(){
        return "member/accttype";
    }

    @RequestMapping("/basicinfo")
    public String basicinfo(){
        return "member/basicinfo";
    }

    @RequestMapping("/uploadCert")
    public String uploadCert(){ return "member/uploadCert"; }

    @RequestMapping("/checkEmail")
    public String checkEmail(){ return "member/checkEmail"; }

    @RequestMapping("/checkAuthCode")
    public String checkAuthCode(){ return "member/checkAuthCode"; }
    @ResponseBody
    @RequestMapping("/updateAcctType")
    public Object updateAcctType(HttpSession session, String accttype){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Member loginMember=(Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setAccttype(accttype);
            memberService.updateAccttype(loginMember);

            Ticket ticket=ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("accttype");
            ticketService.updatePstep(ticket);

            session.setAttribute(Const.LOGIN_MEMBER,loginMember);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/updateBasicinfo")
    public Object updateBasicinfo(HttpSession session,Member member){
        AjaxResult ajaxResult=new AjaxResult();
        try {
            Member loginMember=(Member) session.getAttribute(Const.LOGIN_MEMBER);
            loginMember.setCardnum(member.getCardnum());
            loginMember.setRealname(member.getRealname());
            loginMember.setTel(member.getTel());
            memberService.updateBasicinfo(loginMember);

            Ticket ticket=ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("basicinfo");
            ticketService.updatePstep(ticket);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @RequestMapping("/apply")
    public String apply(HttpSession session){
            Member loginMember=(Member)session.getAttribute(Const.LOGIN_MEMBER);
            Ticket ticket=ticketService.getTicketByMemberId(loginMember.getId());
            if (ticket == null){
                ticket = new Ticket();
                ticket.setMemberid(loginMember.getId());
                ticket.setPstep("apply");
                ticket.setStatus("0");
                ticketService.saveTicket(ticket);
            }else {
                String pstep=ticket.getPstep();
                if("accttype".equals(pstep)){
                    return "redirect:basicinfo.htm";
                }else if("basicinfo".equals(pstep)){
                    List<Cert> queryCertByAccttype=certService.queryCertByAccttype(loginMember.getAccttype());
                    session.setAttribute("queryCertByAccttype",queryCertByAccttype);
                    return "redirect:uploadCert.htm";
                }else if("uploadCert".equals(pstep)){
                    return "redirect:checkEmail.htm";
                }else if ("checkEmail".equals(pstep)){
                    return "redirect:checkAuthCode.htm";
                }
            }
        return "redirect:accttype.htm";
    }

    @ResponseBody
    @RequestMapping("/doUploadCert")
    public Object doUploadCert(HttpSession session, Data ds){
        AjaxResult ajaxResult=new AjaxResult();
        try {
            //获取登录会员信息
            Member loginMember=(Member)session.getAttribute(Const.LOGIN_MEMBER);

            //保存会员与资质关键数据
            String realPath=session.getServletContext().getRealPath("/pics");
            List<MemberCert> certimgs=ds.getCertimgs();
            for (MemberCert memberCert:certimgs){
                MultipartFile fileImg=memberCert.getFileImg();
                String extName=fileImg.getOriginalFilename().substring(fileImg.getOriginalFilename().lastIndexOf("."));
                String tmpName=UUID.randomUUID().toString()+extName;
                String filename=realPath+"/cert"+"/"+ tmpName;

                fileImg.transferTo(new File(filename));//资质文件上传

                //准备数据
                memberCert.setIconpath(tmpName);//封装数据，保存数据库
                memberCert.setMemberid(loginMember.getId());
            }

            //保存会员与资质的关系
            certService.saveMemberCert(certimgs);

            //记录流程步骤
            Ticket ticket=ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("uploadCert");
            ticketService.updatePstep(ticket);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }


    @ResponseBody
    @RequestMapping("/startProcess")
    public Object startProcess(HttpSession session, String email){

        AjaxResult ajaxResult=new AjaxResult();
        try {
            //获取登录会员信息
            Member loginMember=(Member)session.getAttribute(Const.LOGIN_MEMBER);

            //如果用户输入新的Email，将旧的邮箱地址替换
            if (!loginMember.getEmail().equals(email)){
                loginMember.setEmail(email);
                memberService.updateEmail(loginMember);
            }

            //启动实名认证流程 - 系统自动发送邮件，生成验证码，验证邮箱地址是否合法（模拟银行账户是否合法）
            ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionKey("auth").singleResult();

            StringBuilder authcode=new StringBuilder();
            for (int i=0;i<4;i++){
                authcode.append(new Random().nextInt(10));
            }

            Map<String,Object> variables=new HashMap<>();
            variables.put("toEmail",email);
            variables.put("authcode",authcode);
            variables.put("loginacct",loginMember.getLoginacct());
            variables.put("passListener",new PassListener());
            variables.put("refuseListener",new RefuseListener());

            //ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("auth");
            ProcessInstance processInstance=runtimeService.startProcessInstanceById(processDefinition.getId(),variables);

            //记录流程步骤
            Ticket ticket=ticketService.getTicketByMemberId(loginMember.getId());
            ticket.setPstep("checkEmail");
            ticket.setPiid(processInstance.getId());
            ticket.setAuthcode(authcode.toString());
            ticketService.updatePiidAndPstep(ticket);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/finishApply")
    public Object finishApply(HttpSession session, String authcode){

        AjaxResult ajaxResult=new AjaxResult();
        try {
            //获取登录会员信息
            Member loginMember=(Member)session.getAttribute(Const.LOGIN_MEMBER);

            //让当前系统用户完成验证码审核功能
            Ticket ticket=ticketService.getTicketByMemberId(loginMember.getId());

            if (ticket.getAuthcode().equals(authcode)){
                //完成审核验证码任务
                Task task=taskService.createTaskQuery().processInstanceId(ticket.getPiid()).taskAssignee(loginMember.getLoginacct()).singleResult();
                taskService.complete(task.getId());

                //更新用户申请状态
                loginMember.setAuthstatus("1");
                memberService.updateAuthstatus(loginMember);
                //记录流程步骤
                ticket.setPstep("finishApply");
                ticketService.updatePstep(ticket);
                ajaxResult.setSuccess(true);
            }else{
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("验证码不正确，请重新输入！");
            }
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }
}
