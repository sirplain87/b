package com.springapp.mvc.dao;

import com.springapp.mvc.model.Mpdis;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yanzhao on 15/10/12.
 */
@Repository
public class MorningpDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Mpdis> getTodayMorningp(Date day){
        return sqlSessionTemplate.selectList("mybatis/mapper.MorningpMapper.selectTodayMorningp", day);
    }
}
