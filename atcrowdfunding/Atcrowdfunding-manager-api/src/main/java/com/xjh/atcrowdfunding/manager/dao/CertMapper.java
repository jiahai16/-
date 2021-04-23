package com.xjh.atcrowdfunding.manager.dao;

import com.xjh.atcrowdfunding.bean.Cert;
import com.xjh.atcrowdfunding.bean.MemberCert;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    List<Cert> queryCertByAccttype(String accttype);

    void insertMemberCert(MemberCert memberCert);
}