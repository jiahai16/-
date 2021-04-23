package com.xjh.atcrowdfunding.manager.controller;

import com.xjh.atcrowdfunding.bean.Cert;
import com.xjh.atcrowdfunding.manager.service.CertService;
import com.xjh.atcrowdfunding.manager.service.CertTypeService;
import com.xjh.atcrowdfunding.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/certtype")
public class CertTypeController {

    @Autowired
    private CertService certService;

    @Autowired
    private CertTypeService certTypeService;

    @RequestMapping("/index")
    public String index(Map<String,Object> map){
        //查询所有资质
        List<Cert> queryAllCert=certService.queryAllCert();
        map.put("queryAllCert",queryAllCert);

        //查询资质与账户类型之间的关系（账户类型分配过的资质）
        List<Map<String,Object>> certAccttypeList=certTypeService.queryCertAccttype();
        map.put("certAccttypeList",certAccttypeList);
        return "certtype/index";
    }

    @ResponseBody
    @RequestMapping("/insertAcctTypeCert")
    public Object insertAcctTypeCert(Integer certid,String accttype){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Map<String,Object> map=new HashMap<>();
            map.put("certid",certid);
            map.put("accttype",accttype);
            int count=certTypeService.insertAcctTypeCert(map);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping("/deleteAcctTypeCert")
    public Object deleteAcctTypeCert(Integer certid,String accttype){
        AjaxResult ajaxResult=new AjaxResult();
        try{
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("certid",certid);
            paramMap.put("accttype",accttype);
            int count=certTypeService.deleteAcctTypeCert(paramMap);
            ajaxResult.setSuccess(count==1);
        }catch (Exception e){
            ajaxResult.setSuccess(false);
            e.printStackTrace();
        }
        return ajaxResult;
    }
}
