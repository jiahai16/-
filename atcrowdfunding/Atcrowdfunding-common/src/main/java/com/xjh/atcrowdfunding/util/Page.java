package com.xjh.atcrowdfunding.util;

import java.util.List;

public class Page {
    private Integer pageno;//当前页数
    private Integer pagesize;//当前页多少记录
    private Integer totalsize;//总共多少条记录
    private Integer totalno;//总共多少页
    private List<Object> data;//当前页的数据

    public Page(Integer pageno,Integer pagesize){
        if(pageno<=0){
            this.pageno=1;
        }else{
            this.pageno=pageno;
        }
        if(pagesize<=0){
            this.pagesize=10;
        }else {
            this.pagesize = pagesize;
        }
    }
    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Integer getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(Integer totalsize) {
        this.totalsize = totalsize;
        totalno=(totalsize % pagesize)==0? (totalsize/pagesize):(totalsize/pagesize+1);
    }

    public Integer getTotalno() {
        return totalno;
    }

    private void setTotalno(Integer totalno) {
        this.totalno = totalno;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
    public Integer getStartIndex(){
        return (this.pageno-1)*pagesize;
    }
}
