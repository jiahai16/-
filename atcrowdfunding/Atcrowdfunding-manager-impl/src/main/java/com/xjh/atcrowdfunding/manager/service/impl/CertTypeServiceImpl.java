package com.xjh.atcrowdfunding.manager.service.impl;

import com.xjh.atcrowdfunding.manager.dao.AccountTypeCertMapper;
import com.xjh.atcrowdfunding.manager.service.CertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CertTypeServiceImpl implements CertTypeService {

    @Autowired
    private AccountTypeCertMapper accountTypeCertMapper;

    @Override
    public List<Map<String, Object>> queryCertAccttype() {

        return accountTypeCertMapper.queryCertAccttype();
    }

    @Override
    public int insertAcctTypeCert(Map<String, Object> map) {
        return accountTypeCertMapper.insertAcctTypeCert(map);
    }

    @Override
    public int deleteAcctTypeCert(Map<String, Object> paramMap) {
        return accountTypeCertMapper.deleteAcctTypeCert(paramMap);
    }
}
