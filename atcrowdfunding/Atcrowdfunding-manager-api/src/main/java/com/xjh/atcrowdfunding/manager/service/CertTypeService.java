package com.xjh.atcrowdfunding.manager.service;

import java.util.List;
import java.util.Map;

public interface CertTypeService {
    List<Map<String, Object>> queryCertAccttype();

    int insertAcctTypeCert(Map<String, Object> map);

    int deleteAcctTypeCert(Map<String, Object> paramMap);
}
