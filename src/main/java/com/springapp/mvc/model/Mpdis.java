package com.springapp.mvc.model;

import java.util.Date;

/**
 * Created by yanzhao on 15/10/12.
 */
public class Mpdis {
    private String code;
    private Date day;
    private Double curp;
    private Double rate;
    private Long byonesh;
    private Double byonert;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Double getCurp() {
        return curp;
    }

    public void setCurp(Double curp) {
        this.curp = curp;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getByonesh() {
        return byonesh;
    }

    public void setByonesh(Long byonesh) {
        this.byonesh = byonesh;
    }

    public Double getByonert() {
        return byonert;
    }

    public void setByonert(Double byonert) {
        this.byonert = byonert;
    }
}
