package com.xjh.atcrowdfunding.manager.dao;

import com.xjh.atcrowdfunding.bean.Dictionary;
import java.util.List;

public interface DictionaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dictionary record);

    Dictionary selectByPrimaryKey(Integer id);

    List<Dictionary> selectAll();

    int updateByPrimaryKey(Dictionary record);
}