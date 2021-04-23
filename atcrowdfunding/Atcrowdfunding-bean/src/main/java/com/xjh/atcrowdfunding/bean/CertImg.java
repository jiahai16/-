package com.xjh.atcrowdfunding.bean;

import org.springframework.web.multipart.MultipartFile;

public class CertImg {
    private Integer memberid;
    private Integer certid;
    private MultipartFile imgfile;
    private String iconpath;

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Integer getCertid() {
        return certid;
    }

    public void setCertid(Integer certid) {
        this.certid = certid;
    }

    public MultipartFile getImgfile() {
        return imgfile;
    }

    public void setImgfile(MultipartFile imgfile) {
        this.imgfile = imgfile;
    }

    public String getIconpath() {
        return iconpath;
    }

    public void setIconpath(String iconpath) {
        this.iconpath = iconpath;
    }
}
