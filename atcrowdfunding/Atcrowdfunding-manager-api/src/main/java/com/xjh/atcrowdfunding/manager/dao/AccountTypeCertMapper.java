package com.xjh.atcrowdfunding.manager.dao;

import com.xjh.atcrowdfunding.bean.AccountTypeCert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AccountTypeCertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountTypeCert record);

    AccountTypeCert selectByPrimaryKey(Integer id);

    List<AccountTypeCert> selectAll();

    int updateByPrimaryKey(AccountTypeCert record);

    List<Map<String, Object>> queryCertAccttype();

    int insertAcctTypeCert(Map<String, Object> map);

    int deleteAcctTypeCert(Map<String, Object> paramMap);
}