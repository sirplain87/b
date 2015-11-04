package com.springapp.mvc.dao;

import com.springapp.mvc.model.Price;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanzhao on 15/8/17.
 */
@Service
public class PriceDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Price findPriceofTime(String code, Date comps, Date comps1) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", code);
        params.put("start", comps);
        params.put("end", comps1);
        return sqlSessionTemplate.selectOne("mybatis/mapper.PriceMapper.selectPriceofTime", params);
    }

    public List<Price> findPriceofCode(String code){
        return sqlSessionTemplate.selectList("mybatis/mapper.PriceMapper.selectByCodeAsc",code);
    }

    public Price findLatestOne(String code){
        return sqlSessionTemplate.selectOne("mybatis/mapper.PriceMapper.selectLatestOne", code);
    }

    public List<Price> findPriceThatFit(Date day, String code){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", code);
        params.put("day", day);
        return sqlSessionTemplate.selectList("mybatis/mapper.PriceMapper.selectByTimeLimitTwo",params);
    }

    public List<Price> findLastTen(String code){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", code);
        return sqlSessionTemplate.selectList("mybatis/mapper.PriceMapper.selectListofTen",params);
    }

    public Price findPriceOfDay(Date start, String code) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", code);
        params.put("start", start);

        return sqlSessionTemplate.selectOne("mybatis/mapper.PriceMapper.findPriceOfDay", params);
    }
}
