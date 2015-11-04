package com.springapp.mvc.dao;

import com.springapp.mvc.model.Hsrate;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanzhao on 15/8/6.
 */
@Service
public class HsrateDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Hsrate getHsrateById(long id){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", id);
        return sqlSessionTemplate.selectOne("mybatis/mapper.HsrateMapper.selectByPrimaryKey", params);
    }

    public List<Hsrate> getHsrateByCode(String code){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("code", code);
        return sqlSessionTemplate.selectList("mybatis/mapper.HsrateMapper.selectByCode", params);
    }

    public List<Hsrate> getHsrateByCodeAndTime(String code ,Date start, Date end){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("start", start);
        params.put("end", end);
        return sqlSessionTemplate.selectList("mybatis/mapper.HsrateMapper.selectByCodeAndDate", params);
    }


    public List<Hsrate> getRateGT1(Date startd, Date endd) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", startd);
        params.put("end", endd);
        return sqlSessionTemplate.selectList("mybatis/mapper.HsrateMapper.selectRateGtOne", params);
    }
}
