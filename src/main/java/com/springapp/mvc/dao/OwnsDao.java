package com.springapp.mvc.dao;

import com.springapp.mvc.config.HsrateConfig;
import com.springapp.mvc.model.Owns;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanzhao on 15/8/16.
 */
@Service
public class OwnsDao {
    @Autowired
    private SqlSessionTemplate template;
    public Owns findTodayLatest(String code){
        try{
            Date today = HsrateConfig.getZeroofToday();
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("today", today);
            param.put("code", code);
            return template.selectOne("mybatis/mapper.OwnsMapper.selectTodayLatest", param);
        }catch (Exception e){
            return null;
        }

    }

    public void insert(Owns owns){
        template.insert("mybatis/mapper.OwnsMapper.insert", owns);
    }
}
