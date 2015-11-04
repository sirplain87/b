package com.springapp.mvc.dao;

import com.springapp.mvc.model.Basedata;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yanzhao on 15/8/23.
 */
@Service
public class BaseDataDao {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List<Basedata> getBasedataAll(){
        List<Basedata> basedataList = sqlSessionTemplate.selectList("mybatis/mapper.BasedataMapper.selectAll");
        return basedataList;
    }
}
