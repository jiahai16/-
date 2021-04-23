package com.xjh.atcrowdfunding.manager.service;

import com.xjh.atcrowdfunding.bean.Cert;
import com.xjh.atcrowdfunding.bean.MemberCert;

import java.util.List;

public interface CertService {
    public List<Cert> queryAllCert();

    List<Cert> queryCertByAccttype(String accttype);

    void saveMemberCert(List<MemberCert> certimgs);
}
