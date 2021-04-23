package com.xjh.atcrowdfunding.manager.controller;

import com.xjh.atcrowdfunding.util.AjaxResult;
import com.xjh.atcrowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;
    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }

    @RequestMapping("/showimg")
    public String showimg(){
        return "process/showimg";
    }
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                          @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize){

        AjaxResult ajaxResult=new AjaxResult();

        try {

            Page page=new Page(pageno,pagesize);

            ProcessDefinitionQuery processDefinitionQuery=repositoryService.createProcessDefinitionQuery();

            List<ProcessDefinition> listPage=processDefinitionQuery.listPage(page.getStartIndex(),pagesize);

            List<Map<String,Object>> myListPage=new ArrayList<>();

            for (ProcessDefinition processDefinition:listPage){
                Map<String,Object> map=new HashMap<>();

                map.put("id",processDefinition.getId());

                map.put("name",processDefinition.getName());

                map.put("key",processDefinition.getKey());

                map.put("version",processDefinition.getVersion());

                myListPage.add(map);
            }

            Long totalsize=processDefinitionQuery.count();

            page.setData(myListPage);

            page.setTotalsize(totalsize.intValue());

            ajaxResult.setPage(page);

            ajaxResult.setSuccess(true);
        }catch (Exception e){

            ajaxResult.setSuccess(false);

            ajaxResult.setMessage("查询流程定义失败!");

            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/deploy")
    public Object deploy(HttpServletRequest request){
        AjaxResult ajaxResult=new AjaxResult();
        try {
            MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)(request);
            //MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart(request);
            MultipartFile multipartFile=multipartHttpServletRequest.getFile("processDefFile");
            repositoryService.createDeployment().addInputStream(multipartFile.getOriginalFilename(),
                    multipartFile.getInputStream()).deploy();
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("部署流程失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(String id){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("删除流程失败");
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除流程失败");
            e.printStackTrace();
        }
        return ajaxResult;
    }
    @ResponseBody
    @RequestMapping("/showimgProDef")
    public void showimgProDef(String id, HttpServletResponse response) throws IOException {
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        InputStream inputStream=repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getDiagramResourceName());
        ServletOutputStream outputStream=response.getOutputStream();
        IOUtils.copy(inputStream,outputStream);
    }
}
