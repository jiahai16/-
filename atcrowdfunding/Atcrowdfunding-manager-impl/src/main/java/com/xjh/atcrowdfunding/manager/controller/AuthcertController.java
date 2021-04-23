package com.xjh.atcrowdfunding.manager.controller;

import com.xjh.atcrowdfunding.bean.Member;
import com.xjh.atcrowdfunding.bean.Ticket;
import com.xjh.atcrowdfunding.potal.service.MemberService;
import com.xjh.atcrowdfunding.potal.service.TicketService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import com.xjh.atcrowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authcert")
public class AuthcertController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index(){
        return "authcert/index";
    }


    @RequestMapping("/show")
    public String show(Integer memberid,Map<String,Object> map){

        Member member=memberService.getMemberById(memberid);
        List<Map<String,Object>> list=memberService.queryCertByMemberid(memberid);
        map.put("member",member);
        map.put("certimgs",list);
        return "authcert/show";
    }

    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(@RequestParam(value="pageno",required = false,defaultValue = "1") Integer pageno,
                            @RequestParam(value="pagesize",required = false,defaultValue = "10")Integer pagesize){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey("auth")
                    .taskCandidateGroup("backuser");
            Page page=new Page(pageno,pagesize);
            //查询后台backuser委托组的任务
            List<Task> tasks=taskQuery.listPage(page.getStartIndex(), pagesize);
            List<Map<String,Object>> data=new ArrayList<>();

            for (Task task:tasks){
                //通过任务表的流程定义id查询流程定义
                ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();

                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("taskid",task.getId());
                taskMap.put("taskName",task.getName());

                taskMap.put("procDefName",processDefinition.getName());
                taskMap.put("procDefVersion",processDefinition.getVersion());

                // 通过流程查找流程审批单，再查询会员信息
                Member member=ticketService.getMemberByPiid(task.getProcessInstanceId());

                taskMap.put("member",member);
                data.add(taskMap);
            }
            page.setData(data);
            Long count=taskQuery.count();
            page.setTotalsize(count.intValue());
            ajaxResult.setPage(page);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("任务查询失败！");
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/pass")
    public Object pass( String taskid, Integer memberid ) {
        AjaxResult result = new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", true);
            taskService.setVariable(taskid, "memberid", memberid);
// 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/refuse")
    public Object refuse(String taskid, Integer memberid) {
        AjaxResult result = new AjaxResult();
        try {
            taskService.setVariable(taskid, "flag", false);
            taskService.setVariable(taskid, "memberid", memberid);
// 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}
