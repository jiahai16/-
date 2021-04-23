package junit.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActiviti {
    ApplicationContext ioc=new ClassPathXmlApplicationContext("spring/spring-*.xml");
    ProcessEngine processEngine=(ProcessEngine)ioc.getBean("processEngine");
    //1.创建流程引擎，创建23张表
    @Test
    public void test1(){
        System.out.println(processEngine);
    }
    //2.部署流程定义
    @Test
    public void test2(){

        System.out.println("processEngine: "+processEngine);
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Deployment deployment=repositoryService.createDeployment().addClasspathResource("MyProcess7.bpmn").deploy();
        System.out.println("deployment: "+deployment);

    }
    //3.查询部署流程定义
    @Test
    public void test3(){
        System.out.println("processEngine="+processEngine);

        RepositoryService repositoryService = processEngine.getRepositoryService();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> list = processDefinitionQuery.list(); //查询所有流程定义

        for (ProcessDefinition processDefinition : list) {
            System.out.println("Id="+processDefinition.getId());
            System.out.println("Key="+processDefinition.getKey());
            System.out.println("Name="+processDefinition.getName());
            System.out.println("Version="+processDefinition.getVersion());
            System.out.println("-----------------------");
        }

        long count = processDefinitionQuery.count(); //查询流程定义记录数
        System.out.println("count="+count);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        //查询最后一次部署的流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.latestVersion().singleResult();
        System.out.println("Id="+processDefinition.getId());
        System.out.println("Key="+processDefinition.getKey());
        System.out.println("Name="+processDefinition.getName());
        System.out.println("Version="+processDefinition.getVersion());
        System.out.println("******************************");

        //排序查询流程定义,分页查询流程定义.
        ProcessDefinitionQuery definitionQuery = processDefinitionQuery.orderByProcessDefinitionVersion().desc();
        List<ProcessDefinition> listPage = definitionQuery.listPage(0, 2);
        for (ProcessDefinition processDefinition2 : listPage) {
            System.out.println("Id="+processDefinition2.getId());
            System.out.println("Key="+processDefinition2.getKey());
            System.out.println("Name="+processDefinition2.getName());
            System.out.println("Version="+processDefinition2.getVersion());
            System.out.println("-----------------------");
        }

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        //根据流程定义的 Key 查询流程定义对象
        ProcessDefinition processDefinition2 = processDefinitionQuery.processDefinitionKey("myProcess").latestVersion().singleResult();
        System.out.println("Id="+processDefinition2.getId());
        System.out.println("Key="+processDefinition2.getKey());
        System.out.println("Name="+processDefinition2.getName());
        System.out.println("Version="+processDefinition2.getVersion());
    }
    //4.创建流程实例
    @Test
    public void test4(){
        RepositoryService repositoryService=processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        System.out.println("processInstance="+processInstance);
    }

    //5.查询流程实例的任务数据
    @Test
    public void test5(){
        ProcessDefinition processDefinition=processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        TaskService taskService=processEngine.getTaskService();
        TaskQuery taskQuery=taskService.createTaskQuery();
        List<Task> list1=taskQuery.taskAssignee("tl").list();
        //List<Task> list2=taskQuery.taskAssignee("lisi").list();

        //张三的任务
        for (Task task:list1){
            System.out.println("id= "+task.getId());
            System.out.println("name= "+task.getName());
            //张三完成任务
            //taskService.complete(task.getId());
            System.out.println("张三完成任务");
            System.out.println("--------------------");
        }

        //李四的任务
        /*for(Task task:list2){
            System.out.println("id= "+task.getId());
            System.out.println("name= "+task.getName());
            taskService.complete(task.getId());
            System.out.println("李四完成任务");
        }*/
    }
    //6.历史数据查询
    @Test
    public void test6(){
        ProcessDefinition processDefinition=processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
        HistoryService historyService=processEngine.getHistoryService();
        HistoricProcessInstanceQuery historicProcessInstanceQuery=historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance historicProcessInstance=historicProcessInstanceQuery.processInstanceId("201").finished().singleResult();
        System.out.println("historicProcessInstance= "+historicProcessInstance);
    }

    //7.领取任务
    @Test
    public void test7(){
        ProcessDefinition processDefinition=processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();

        TaskService taskService=processEngine.getTaskService();
        TaskQuery taskQuery=taskService.createTaskQuery();
        List<Task> list=taskQuery.taskCandidateGroup("tl").list();

        long count=taskQuery.taskAssignee("zhangsan").count();
        System.out.println("zhangsan领取任务前的任务数量： "+count);

        for (Task task:list){
            System.out.println("id= "+task.getId());
            System.out.println("name= "+task.getName());
            taskService.claim(task.getId(),"zhangsan");
        }

        taskQuery=taskService.createTaskQuery();
        count=taskQuery.taskAssignee("zhangsan").count();
        System.out.println("zhangsan领取任务后的任务数量是："+count);
    }
    //8.流程变量创建流程实例
    @Test
    public void test8(){

        Map<String,Object> varibales=new HashMap<>();
        varibales.put("tl","zhangsan");
        varibales.put("pm","lisi");
        RepositoryService repositoryService=processEngine.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibales);

        System.out.println("processInstance="+processInstance);

    }

    //9.网关 - 排他网关（互斥）
    @Test
    public void test9(){

        Map<String,Object> varibales=new HashMap<>();
        varibales.put("days","4");
        RepositoryService repositoryService=processEngine.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibales);

        System.out.println("processInstance="+processInstance);

    }
    //9-1
    @Test
    public void test91(){
        RepositoryService repositoryService=processEngine.getRepositoryService();
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();
        TaskService taskService=processEngine.getTaskService();
        TaskQuery taskQuery=taskService.createTaskQuery();
        List<Task> list1=taskQuery.taskAssignee("zhangsan").list();


        for (Task task:list1){
            System.out.println("zhangsan完成了任务");
            taskService.complete(task.getId());
        }
        taskQuery=taskService.createTaskQuery();
        List<Task> list2=taskQuery.taskAssignee("lisi").list();
        for (Task task:list2){
            System.out.println("lisi完成了任务");
            taskService.complete(task.getId());
        }
    }

    //10.网关 - 并行网关(会签) - 项目经理和财务经理都审批后，流程结束；如果只有一个经理审批，流程需要等待
    @Test
    public void test10(){
        RepositoryService repositoryService=processEngine.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());


    }
    @Test
    public void test101(){
        RepositoryService repositoryService=processEngine.getRepositoryService();
        TaskService taskService=processEngine.getTaskService();
        TaskQuery taskQuery=taskService.createTaskQuery();
        List<Task> list1=taskQuery.taskAssignee("zhangsan").list();
        List<Task> list2=taskQuery.taskAssignee("lisi").list();
        for (Task task:list1){
            System.out.println("id= "+task.getId());
            System.out.println("name= "+task.getName());
            System.out.println("zhangsan开始执行任务....");
            taskService.complete(task.getId());
            System.out.println("------------------");
        }
        for(Task task:list2) {
            System.out.println("id= " + task.getId());
            System.out.println("name= " + task.getName());
            System.out.println("lisi开始执行任务....");
            taskService.complete(task.getId());
            System.out.println("------------------");
        }
    }

    //11.网关 - 包含网关（排他 + 并行）
    @Test
    public void test11(){

        Map<String,Object> varibales=new HashMap<>();
        varibales.put("days","4");
        varibales.put("cost","6000");
        RepositoryService repositoryService=processEngine.getRepositoryService();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),varibales);

        System.out.println("processInstance="+processInstance);

    }
    @Test
    public void test111(){
        RepositoryService repositoryService=processEngine.getRepositoryService();
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();
        TaskService taskService=processEngine.getTaskService();
        TaskQuery taskQuery=taskService.createTaskQuery();
        List<Task> list=taskQuery.taskAssignee("lisi").list();
        for (Task task:list){
            taskService.complete(task.getId());
        }
    }
}
