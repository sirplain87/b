package com.springapp.mvc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springapp.mvc.config.HsrateConfig;
import com.springapp.mvc.config.HttpClientUtils;
import com.springapp.mvc.dao.*;
import com.springapp.mvc.model.*;
import com.springapp.mvc.utils.DataUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yanzhao on 15/8/6.
 */
@Service
public class HsrateStatisticsService {


    private final Logger logger = LoggerFactory.getLogger(HsrateStatisticsService.class);

    @Autowired
    private HsrateDao hsrateDao;
    @Autowired
    private UserIdDao userIdDao;
    @Autowired
    private OwnsDao ownsDao;
    @Autowired
    private PriceDao priceDao;
    @Autowired
    private BaseDataDao baseDataDao;
    @Autowired
    private MorningpDao morningpDao;

    @Autowired
    private FivemDao fivemDao;

    public Hsrate getHsrateById(long id){
        return hsrateDao.getHsrateById(id);
    }

    public List<Hsrate> getHsratesByCode(String code){
        return hsrateDao.getHsrateByCode(code);
    }

    public List<Hsrate> getHsratesByCodeAndDate(String code, Date start, Date end){
        return hsrateDao.getHsrateByCodeAndTime(code, start, end);
    }

    public List<Hsrate> getHsrateOfToday(String code)throws Exception{
        System.out.println(HsrateConfig.getZeroofToday());
        return hsrateDao.getHsrateByCodeAndTime(code, HsrateConfig.getZeroofToday(), new Date());
    }

    public String hsrttd(String code)throws Exception{
        List<Hsrate> hsrates = getHsrateOfToday(code);
        DecimalFormat df = new DecimalFormat("#0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        StringBuffer sb = new StringBuffer();
        for(Hsrate rt: hsrates){
            String hsl = df.format(rt.getRate());
            sb.append(hsl).append(" ").append(simpleDateFormat.format(rt.getDate())).append(" ");

        }
        return sb.toString();
    }

    public String hsrtofsmd(String code, String dt) throws Exception{
        Date start = HsrateConfig.getStartOfDate(dt);
        Date end = HsrateConfig.getEndOfDate(dt);
        List<Hsrate> hsrates = getHsratesByCodeAndDate(code, start, end);
        DecimalFormat df = new DecimalFormat("#0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        StringBuffer sb = new StringBuffer();
        for(Hsrate rt: hsrates){
            String hsl = df.format(rt.getRate());
            sb.append(hsl).append(" ").append(simpleDateFormat.format(rt.getDate())).append(" ");

        }
        return sb.toString();
    }

    public String fisrtHighSecondLow(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        List<Basedata> basedatas = baseDataDao.getBasedataAll();
        for(Basedata basedata : basedatas){
            String code = basedata.getCode();
            List<Price> prices = priceDao.findPriceofCode(code);
            for(int i=0;i<prices.size()-1;i++){
                Price prev = prices.get(i);
                Price after = prices.get(i+1);
                if(!prev.getHang() && !after.getHang() && prev.getPrevp()!=null){
                    if(prev.getHigh()> prev.getPrevp()*(1+0.097)){
                        if(after.getHigh()< prev.getHigh()*(1+0.03)){
                            sb.append(JSON.toJSONString(prev)).append("|").append(JSON.toJSONString(after)).append("---time:").append(simpleDateFormat.format(prev.getDay())).append("\n");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public String fisrtHighEndSecondLow(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        List<Basedata> basedatas = baseDataDao.getBasedataAll();
        for(Basedata basedata : basedatas){
            String code = basedata.getCode();
            List<Price> prices = priceDao.findPriceofCode(code);
            for(int i=0;i<prices.size()-1;i++){
                Price prev = prices.get(i);
                Price after = prices.get(i+1);
                if(!prev.getHang() && !after.getHang() && prev.getPrevp()!=null){
                    if(prev.getHigh()> prev.getPrevp()*(1+0.097) && prev.getHigh().equals(prev.getEnd())){
                        if(after.getHigh()< prev.getHigh()*(1+0.03)){
                            sb.append(JSON.toJSONString(prev)).append("|").append(JSON.toJSONString(after)).append("---time:").append(simpleDateFormat.format(prev.getDay())).append("\n");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public String fisrtHighEndSecondLowOfOne(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        List<Basedata> basedatas = baseDataDao.getBasedataAll();
        for(Basedata basedata : basedatas){
            String code = basedata.getCode();
            List<Price> prices = priceDao.findPriceofCode(code);
            for(int i=0;i<prices.size()-1;i++){
                Price prev = prices.get(i);
                Price after = prices.get(i+1);
                if(!prev.getHang() && !after.getHang() && prev.getPrevp()!=null){
                    if(prev.getHigh()> prev.getPrevp()*(1+0.097) && prev.getHigh().equals(prev.getEnd())){
                        if(after.getHigh()< prev.getHigh()*(1+0.01)){
                            sb.append(JSON.toJSONString(prev)).append("|").append(JSON.toJSONString(after)).append("---time:").append(simpleDateFormat.format(prev.getDay())).append("\n");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public String priceThatFit(String time)throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        List<Basedata> basedatas = baseDataDao.getBasedataAll();
        Date day = simpleDateFormat.parse(time);
        for(Basedata basedata : basedatas){
            String code = basedata.getCode();
            List<Price> prices = priceDao.findPriceThatFit(day, code);
            if(prices!=null && prices.size()==2){
                Price price1 = prices.get(0);
                Price price2 = prices.get(1);
                if(!price1.getHang() && !price2.getHang()){
                    if(price1.getEnd()< price1.getStartp() && price2.getEnd()< price2.getStartp())
                    {
                        if(price1.getZs()!=null && price2.getZs()!=null && price1.getZs()>price2.getZs()){
                            sb.append(JSON.toJSONString(price1)).append("|").append(JSON.toJSONString(price2)).append("\n");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public String hsrateNotFit(String start, String end, String compareTo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        try{
            Date startd = simpleDateFormat.parse(start);
            Date endd = simpleDateFormat.parse(end);
            String [] cs = compareTo.split(" ");
            Date comps = simpleDateFormat.parse(cs[0] + " 00:00:00");
            Date compe = simpleDateFormat.parse(cs[0] + " 23:59:59");
            List<Hsrate> hsrates = hsrateDao.getRateGT1(startd, endd);
            for(Hsrate hsrate : hsrates){
                Price price = priceDao.findPriceofTime(hsrate.getCode(), comps, compe);
                double expect = hsrate.getCurp()*(1+0.03);
                if(price!=null && expect> price.getHigh()){
                    double growth = (hsrate.getCurp()- hsrate.getPrevp())/hsrate.getPrevp();
                    sb.append(JSON.toJSONString(hsrate)).append("|").append(JSON.toJSONString(price)).append("---growth:").append(growth).append("\n");
                }
            }
            return sb.toString();
        }catch (Exception e){
            logger.error("find hsrate not fit catch exception", e);
        }
        return "";
    }

    public String hsrateThatFit(String start, String end, String compareTo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        try{
            Date startd = simpleDateFormat.parse(start);
            Date endd = simpleDateFormat.parse(end);
            String [] cs = compareTo.split(" ");
            Date comps = simpleDateFormat.parse(cs[0] + " 00:00:00");
            Date compe = simpleDateFormat.parse(cs[0] + " 23:59:59");
            List<Hsrate> hsrates = hsrateDao.getRateGT1(startd, endd);
            for(Hsrate hsrate : hsrates){
                Price price = priceDao.findPriceofTime(hsrate.getCode(), comps, compe);
                double expect = hsrate.getCurp()*(1+0.03);
                if(price!=null && expect< price.getHigh()){
                    double growth = (hsrate.getCurp()- hsrate.getPrevp())/hsrate.getPrevp();
                    sb.append(JSON.toJSONString(hsrate)).append("|").append(JSON.toJSONString(price)).append("---growth:").append(growth).append("\n");
                }
            }
            return sb.toString();
        }catch (Exception e){
            logger.error("find hsrate not fit catch exception", e);
        }
        return "";
    }

    //查询当前保证金

    public String queryCurrentMoney(){
        try{
            Userid userid = userIdDao.findTodayLatest();
            if(userid == null){
                logger.error("get uid failed");
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("uid=").append(userid.getUid());
            stringBuffer.append("&cssweb_type=GET_FUNDS&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$106.39.223.31;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619&function_id=405&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&money_type=&ram=");
            stringBuffer.append(String.valueOf(Math.random()));
            String request = stringBuffer.toString();
            logger.info("get fund ,before encrypt:{}", request);
            return doGetFunds(Base64.encodeBase64String(request.getBytes("gb2312")));
        }catch (Exception e){
            logger.error("query funds catch exception:{}", e);
            return null;
        }
    }

    //分析并保存
    public void analiseStore(String ipt) {
        String decodeStr = new String(Base64.decodeBase64(ipt));
        try{
            doGetOwns(ipt);
        }catch (Exception e){
            logger.error("get stock position failed", e);
            return;
        }
        String [] params = decodeStr.split("&");
        String [] keyvalue = params[0].split("=");
        if(StringUtils.equals(keyvalue[0],"uid")){
            Userid userid = new Userid();
            userid.setDay(new Date());
            userid.setParam(ipt);
            userid.setUid(keyvalue[1]);
            userIdDao.insert(userid);
            HttpClientUtils.doGet("http://localhost:8080/untitled1/loop?ipt=" + ipt, null);
        }
    }
    //循环请求
    public void loop(String ipt, HttpServletResponse response) {

        try{
            PrintWriter writer = response.getWriter();
        }catch (Exception e ){
            logger.error("response write fail");
        }
        logger.info("loop begin");

        while (true){
            String requestUrl = "https://tradegw.htsc.com.cn/?" + ipt;
            String res = HttpClientUtils.doGet(requestUrl, null);
            logger.info(res);


            try{
                String decry = new String(Base64.decodeBase64(res), "gb2312");

                JSONObject jsonObject = JSON.parseObject(decry);
                String retStatus = jsonObject.getString("cssweb_code");
                if(!StringUtils.equals(retStatus, "success")){
                    logger.error("get stock position failed, response:{}", decry);
                    break;
                }
                Thread.sleep(60*1000);
            }catch (Exception e){
                logger.error("loop exception");
            }
        }
    }
    //get stock position
    public void doGetOwns(String encry)throws Exception{

        String requestUrl = "https://tradegw.htsc.com.cn/?" + encry;
        String response = HttpClientUtils.doGet(requestUrl, null);
        String decry = new String(Base64.decodeBase64(response), "gb2312");

        JSONObject jsonObject = JSON.parseObject(decry);
        String retStatus = jsonObject.getString("cssweb_code");
        if(StringUtils.equals(retStatus, "success")){
            String items = jsonObject.getString("item");
            if(StringUtils.isNotBlank(items)){
                JSONArray itemArray = JSONArray.parseArray(items);
                if(itemArray!= null && itemArray.size()>0){
                    for(int i=0;i<itemArray.size()-1;i++){
                        Owns owns = new Owns();
                        String item = itemArray.getString(i);
                        JSONObject itemObj = JSON.parseObject(item);
                        String cd = itemObj.getString("stock_code");
                        owns.setCode(cd);
                        Double costp = itemObj.getDouble("cost_price");
                        owns.setCostp(costp);
                        DecimalFormat df = new DecimalFormat("#0.0");
                        owns.setSuggp(Double.parseDouble(df.format(costp*1.03)));
                        Double amount = itemObj.getDouble("enable_amount");
                        owns.setQuan(amount.intValue());
                        owns.setDay(new Date());
                        ownsDao.insert(owns);
                    }
                }
            }
        }

    }


    public String doGetFunds(String encry) throws Exception {
        String requestUrl = "https://tradegw.htsc.com.cn/?" + encry;
        String response = HttpClientUtils.doGet(requestUrl, null);
        String decry = new String(Base64.decodeBase64(response), "gb2312");
        logger.info("get funds response:{}", decry);

        JSONObject jsonObject = JSON.parseObject(decry);
        String retStatus = jsonObject.getString("cssweb_code");
        if (StringUtils.equals(retStatus, "success")) {
            String items = jsonObject.getString("item");
            if (StringUtils.isNotBlank(items)) {
                JSONArray itemArray = JSONArray.parseArray(items);
                if (itemArray != null && itemArray.size() > 0) {
                    String item = itemArray.getString(0);
                    JSONObject itemObj = JSON.parseObject(item);
                    String canUse = itemObj.getString("enable_balance");
                    return canUse;
                }
            }
        }
        return null;
    }

    public String getCurp(String cd) {


        StringBuffer stringBuffer = new StringBuffer();

        String [] params = DataUtil.getCurScData(cd);
        if(params == null || params.length == 0){
            logger.error("the stock data format is illegal, code:{}", cd);
            return null;
        }

        for(int i=1;i<params.length;i++){
            stringBuffer.append(params[i]).append(",");
        }
        stringBuffer.append("\n").append("<br>");

        return stringBuffer.toString();
    }

    public String getLatestPrice(String cd){
        List<Price> prices = priceDao.findLastTen(cd);
        if(CollectionUtils.isEmpty(prices)){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMM");
        for(Price price : prices){
            stringBuffer.append(price.getEnd()).append(" ")
                    .append(price.getStartp()).append(" ")
                    .append(simpleDateFormat.format(price.getDay())).append(" ")
                    .append((price.getEnd()-price.getPrevp())/price.getPrevp()).append("\n").append("<br>");
        }
        return stringBuffer.toString();

    }

    public String getData(String cd){
        String code = cd.substring(cd.length()-8, cd.length()-2);
        return getCurp(code) + getLatestPrice(code);
    }


    public List<Mpdis> getMorningp()throws Exception{
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String ds = simpleDateFormat.format(date);
        Date dt = simpleDateFormat.parse(ds.split(" ")[0] + " 06:00:00");
        List<Mpdis> mpdises = morningpDao.getTodayMorningp(dt);
        List<Mpdis> todis = new ArrayList<Mpdis>();
        for(Mpdis mpdis:mpdises){
            Double rt = mpdis.getRate()*100;
            if(rt<5){
                String code = mpdis.getCode();
                String r = String.valueOf(Math.random());
                mpdis.setCode( r.substring(0, r.length()-8) + code + "1");
                mpdis.setRate(rt);
                todis.add(mpdis);
            }
        }
        return todis;
    }


    public List<Price> getPrice(String cd) {
        List<Price> prices = priceDao.findLastTen(cd.substring(cd.length()-8, cd.length()-2));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM");
        for(Price price:prices){
            String t = simpleDateFormat.format(price.getDay());
            String [] ts = t.split(" ");
            price.setId(Long.parseLong(ts[0] + ts[1]));
            price.setLow((price.getEnd()-price.getPrevp())/price.getPrevp());
            price.setHigh((price.getEnd()-price.getStartp())/price.getPrevp());

        }
        return prices;
    }

    public List<Fivemdis> getFivems()throws Exception{
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date start = simpleDateFormat.parse(simpleDateFormat.format(date).split(" ")[0] + " 14:30:00");

        Date mor = simpleDateFormat.parse(simpleDateFormat.format(date).split(" ")[0] + " 09:51:00");

        DateUtils.addHours(date, -1);
        List<Fivem> fivems = fivemDao.getTodayLatestHalfHour(start);
        List<Fivemdis> todis = new ArrayList<Fivemdis>();
        for(Fivem fivem:fivems){
            Fivemdis fivemdis = new Fivemdis();
            String code = fivem.getCode();
            String r = String.valueOf(Math.random());
            fivemdis.setCode( r.substring(0, r.length()-8) + code + "1");
            fivemdis.setHsrate(String.valueOf(fivem.getHsrate() * 100));
            fivemdis.setByonep(String.valueOf(fivem.getCurzs() / (fivem.getZs() + 0.01) * 100));
            fivemdis.setDay(fivem.getDay());
            fivemdis.setHighp(r.substring(2, 4) + "0" + String.valueOf(fivem.getHighp()));
            fivemdis.setLowp(r.substring(4, 6) + "0" + String.valueOf(fivem.getLowp()));
            fivemdis.setCurp(r.substring(6, 8) + "0" + String.valueOf(fivem.getCurp()));
            fivemdis.setRate(String.valueOf(fivem.getRate() * 100));
            fivemdis.setStartp(r.substring(8, 10) + "0" + String.valueOf(fivem.getStartp()));
            todis.add(fivemdis);
        }
        return todis;
    }

    public Object statistics(String day) throws Exception{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date start = simpleDateFormat.parse(day + " 14:30:00");
        Date end = simpleDateFormat.parse(day + " 15:03:00");


        List<Fivem> fivems = fivemDao.getSomeDayLatestHalfHour(start, end);
        List<Stadis> todis = new ArrayList<Stadis>();
        for(Fivem fivem:fivems){
            Stadis stadis = new Stadis();
            String code = fivem.getCode();
            String r = String.valueOf(Math.random());
            stadis.setCode( r.substring(0, r.length()-8) + code + "1");
            stadis.setHsrate(String.valueOf(fivem.getHsrate() * 100));
            stadis.setByonep(String.valueOf(fivem.getCurzs() / (fivem.getZs() + 0.01) * 100));
            stadis.setDay(fivem.getDay());
            stadis.setHighp(String.valueOf(fivem.getHighp()));
            stadis.setLowp(String.valueOf(fivem.getLowp()));
            stadis.setCurp(String.valueOf(fivem.getCurp()));
            stadis.setRate(String.valueOf(fivem.getRate() * 100));
            stadis.setStartp(String.valueOf(fivem.getStartp()));

            Price price = priceDao.findPriceOfDay(simpleDateFormat.parse(day + " 17:30:00"), code);
            stadis.setNday(price.getDay());
            stadis.setNhighp(String.valueOf(price.getHigh()));
            stadis.setNlowp(String.valueOf(price.getLow()));
            stadis.setNstartp(String.valueOf(price.getStartp()));
            stadis.setIncre(String.valueOf((price.getHigh()-price.getPrevp())*100/price.getPrevp()));


            todis.add(stadis);
        }
        return todis;
    }
}
