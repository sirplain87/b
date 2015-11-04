package com.springapp.mvc.dao;

import com.springapp.mvc.config.HsrateConfig;
import com.springapp.mvc.model.Userid;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yanzhao on 15/8/15.
 */
@Service
public class UserIdDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insert(Userid userid){
        sqlSessionTemplate.insert("mybatis/mapper.UseridMapper.insert", userid);
    }

    public Userid findTodayLatest(){
        try{
            Date today = HsrateConfig.getZeroofToday();
            Userid userid = sqlSessionTemplate.selectOne("mybatis/mapper.UseridMapper.selectTodayLatest", today);
            return userid;
        }catch (Exception e){
            return null;
        }
    }
}
