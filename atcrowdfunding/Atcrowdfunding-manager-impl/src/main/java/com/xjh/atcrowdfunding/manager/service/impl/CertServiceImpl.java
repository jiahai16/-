package com.xjh.atcrowdfunding.manager.service.impl;

import com.xjh.atcrowdfunding.bean.Cert;
import com.xjh.atcrowdfunding.bean.MemberCert;
import com.xjh.atcrowdfunding.manager.dao.CertMapper;
import com.xjh.atcrowdfunding.manager.service.CertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertServiceImpl implements CertService {

    @Autowired
    public CertMapper certMapper;

    @Override
    public List<Cert> queryAllCert() {

        return certMapper.selectAll();
    }

    @Override
    public List<Cert> queryCertByAccttype(String accttype) {

        return certMapper.queryCertByAccttype(accttype);
    }

    @Override
    public void saveMemberCert(List<MemberCert> certimgs) {
        for (MemberCert memberCert:certimgs){
            certMapper.insertMemberCert(memberCert);
        }
    }
}
