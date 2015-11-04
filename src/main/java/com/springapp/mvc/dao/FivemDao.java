package com.springapp.mvc.dao;

import com.springapp.mvc.model.Fivem;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanzhao on 15/10/31.
 */
@Repository
public class FivemDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    public List<Fivem> getTodayLatestHalfHour(Date start) {

        return sqlSessionTemplate.selectList("mybatis/mapper.FivemMapper.getHsrateMoreThanOne", start);
    }

    public List<Fivem> getSomeDayLatestHalfHour(Date start, Date end) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("end", end);
        return sqlSessionTemplate.selectList("mybatis/mapper.FivemMapper.getSomeDayLatestHalfHour", params);
    }
}
