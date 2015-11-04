package com.springapp.mvc.dao;

import com.springapp.mvc.model.Trade;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yanzhao on 15/8/16.
 */
@Service
public class TradeDao {
    @Autowired
    private SqlSessionTemplate template;

    public void insert(Trade trade){
        template.insert("mybatis/mapper.TradeMapper.insert",trade);
    }

    public Trade findById(int id){return template.selectOne("mybatis/mapper.TradeMapper.selectByPrimaryKey", id);}
}
