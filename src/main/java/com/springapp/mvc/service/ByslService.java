package com.springapp.mvc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springapp.mvc.config.HttpClientUtils;
import com.springapp.mvc.dao.OwnsDao;
import com.springapp.mvc.dao.PriceDao;
import com.springapp.mvc.dao.TradeDao;
import com.springapp.mvc.dao.UserIdDao;
import com.springapp.mvc.model.Owns;
import com.springapp.mvc.model.Price;
import com.springapp.mvc.model.Trade;
import com.springapp.mvc.model.Userid;
import com.springapp.mvc.utils.DataUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yanzhao on 15/8/14.
 */
@Service
public class ByslService {

    private final Logger logger = LoggerFactory.getLogger(ByslService.class);

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private UserIdDao userIdDao;

    @Autowired
    private OwnsDao ownsDao;

    @Autowired
    private PriceDao priceDao;

    //撤单

    //查询委托



    public void buyStkByCodeAndMoney(String cd, Double m){
        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();
        if(userid!=null){
            sb.append("uid=").append(userid.getUid());
            sb.append("&cssweb_type=STOCK_BUY&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$110.96.137.179;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=302&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&");
            if(cd.startsWith("6")){
                sb.append("exchange_type=1&");
                sb.append("stock_account=A782341997&");
            }else if(cd.startsWith("0")){
                sb.append("exchange_type=2&");
                sb.append("stock_account=0164498009&");

            }
            sb.append("stock_code=").append(cd).append("&");

            String [] params = DataUtil.getCurScData(cd);
            if(params == null || params.length == 0){
                return ;
            }

            if(StringUtils.equals(params[4], "0.00")){
                logger.error("the stock {} current hightest price is 0.00", cd);
                return;
            }

            Double curP = Double.parseDouble(params[3]);

            Double ysp = curP*100;

            int zs1 = (m.intValue()/(ysp.intValue()));


            StringBuffer sb1 = new StringBuffer();

            sb1.append("entrust_amount=").append(zs1*100).append("&").append("entrust_price=").append(curP).append("&entrust_prop=0&entrust_bs=1&").
                    append("ram=").append(String.valueOf(Math.random()));

            String decry = doRequest(sb.toString() + sb1.toString());
            doInsertTrade(cd, curP, zs1, decry, 1, false);
        }
    }

    public void doRevertTd(int id){
        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();

        Trade trade = tradeDao.findById(id);

        if(userid!=null && trade!=null){

            if(trade.getEntrustno() == null || trade.getEntrustno()<0){
                logger.error("entrustno is illegal");
                return;

            }
            sb.append("uid=").append(userid.getUid());
            sb.append("&cssweb_type=STOCK_CANCEL&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$111.161.20.14;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=304&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&batch_flag=0&exchange_type=&");

            sb.append("entrust_no=").append(trade.getEntrustno())
                    .append("&ram=").append(String.valueOf(Math.random()));

            String decry = doRequest(sb.toString());
        }
    }

    public void buyStkByCodeAndPrice(String cd, Double m, Double p){
        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();
        if(userid!=null){
            sb.append("uid=").append(userid.getUid());
            sb.append("&cssweb_type=STOCK_BUY&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$110.96.137.179;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=302&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&");
            if(cd.startsWith("6")){
                sb.append("exchange_type=1&");
                sb.append("stock_account=A782341997&");
            }else if(cd.startsWith("0")){
                sb.append("exchange_type=2&");
                sb.append("stock_account=0164498009&");

            }
            sb.append("stock_code=").append(cd).append("&");


            Double ysp = p*100;

            int zs1 = (m.intValue()/(ysp.intValue()));

            StringBuffer sb1 = new StringBuffer();

            sb1.append("entrust_amount=").append(zs1*100).append("&").append("entrust_price=").append(p).append("&entrust_prop=0&entrust_bs=1&").
                    append("ram=").append(String.valueOf(Math.random()));

            String decry = doRequest(sb.toString() + sb1.toString());
            doInsertTrade(cd, p, zs1, decry, 1, false);
        }
    }

    public void buyStkByRate(String cd){

        List<String> byStrs = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();
        if(userid!=null){
            sb.append("uid=").append(userid.getUid());
            sb.append("&cssweb_type=STOCK_BUY&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$110.96.137.179;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=302&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&exchange_type=1&");
            if(cd.startsWith("6")){
                sb.append("stock_account=A782341997&");
            }else if(cd.startsWith("0")){
                sb.append("stock_account=0164498009&");

            }
            sb.append("stock_code=").append(cd).append("&");

            String [] params = DataUtil.getCurScData(cd);
            if(params == null || params.length == 0){
                return ;
            }

            if(StringUtils.equals(params[4], "0.00")){
                logger.error("the stock {} current hightest price is 0.00", cd);
                return;
            }

            Double curP = Double.parseDouble(params[3]);

            Double ysp = curP*100;

            int zs1 = (10000/(ysp.intValue()))/3;

            int zs2 = zs1*2;

            if(zs1<1){
                zs1 = 1;
                zs2 = 1;
            }
            Double curP2 = curP*(1-0.03);

            //保留一位小数
            DecimalFormat df = new DecimalFormat("#0.0");
            StringBuffer sb1 = new StringBuffer();

            StringBuffer sb2 = new StringBuffer();

            sb1.append("entrust_amount=").append(zs1*100).append("&").append("entrust_price=").append(curP).append("&entrust_prop=0&entrust_bs=1&").
                    append("ram=").append(String.valueOf(Math.random()));
            sb2.append("entrust_amount=").append(zs2*100).append("&").append("entrust_price=").append(df.format(curP2)).append("&entrust_prop=0&entrust_bs=1&").
                    append("ram=").append(String.valueOf(Math.random()));


            byStrs.add(sb.toString() + sb1.toString());
            byStrs.add(sb.toString() + sb2.toString());

            String decry = doRequest(sb.toString() + sb1.toString());
            doInsertTrade(cd, curP, zs1, decry, 1, true);

            try{
                Thread.sleep(20*1000);
            }catch (Exception e){
                logger.error("buy thread sleep catch exception:{}", e);
            }

            decry = doRequest(sb.toString() + sb2.toString());
            doInsertTrade(cd, curP2, zs2, decry, 1, true);
        }
    }

    public void buyStkByDesc(String cd){

        logger.info("buy {} stk by desc", cd);
        List<String> byStrs = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();
        if(userid!=null){
            sb.append("uid=").append(userid.getUid());
            sb.append("&cssweb_type=STOCK_BUY&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$110.96.137.179;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=302&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&exchange_type=1&");
            if(cd.startsWith("6")){
                sb.append("stock_account=A782341997&");
            }else if(cd.startsWith("0")){
                sb.append("stock_account=0164498009&");

            }
            sb.append("stock_code=").append(cd).append("&");

            String [] params = DataUtil.getCurScData(cd);
            if(params == null || params.length == 0){
                return ;
            }

            if(StringUtils.equals(params[4], "0.00")){
                logger.error("the stock {} current hightest price is 0.00", cd);
                return;
            }

            //获取当前sell price
            Double curP = Double.parseDouble(params[7]);

            Double ysp = curP*100;

            int zs1 = (5000/(ysp.intValue()));



            StringBuffer sb1 = new StringBuffer();

            sb1.append("entrust_amount=").append(zs1*100).append("&").append("entrust_price=").append(curP).append("&entrust_prop=0&entrust_bs=1&").
                    append("ram=").append(String.valueOf(Math.random()));

            byStrs.add(sb.toString() + sb1.toString());

//            String decry = doRequest(sb.toString() + sb1.toString());
//            logger.info("buy by desc:{}, response:{}", sb.toString() + sb1.toString(), decry);
//            doInsertTrade(cd, curP, zs1, decry, 1);

            logger.info("buy by desc:{}, response:{}", sb.toString() + sb1.toString(), "");
            doInsertTrade(cd, curP, zs1, "", 1, true);

        }
    }

    private void doInsertTrade(String code, double p, int quan, String response, int type, boolean isDebug){

        Trade trade = new Trade();
        trade.setCode(code);
        trade.setDay(new Date());
        trade.setQuan(quan);
        trade.setDone(false);
        trade.setTradep(p);
        trade.setType(type);
        if(!isDebug){
            JSONObject jsonObject = JSON.parseObject(response);

            String retCode = jsonObject.getString("cssweb_code");
            if(StringUtils.equals(retCode, "success")){
                String items = jsonObject.getString("item");

                JSONArray itemArr = JSON.parseArray(items);

                if(itemArr!=null && itemArr.size()>0){
                    String item = itemArr.getString(0);
                    JSONObject itm = JSON.parseObject(item);
                    trade.setEntrustno(itm.getInteger("entrust_no"));
                }
            }

        }

        tradeDao.insert(trade);
    }

    public String doRequest(String s){
        try{
            String encry = Base64.encodeBase64String(s.getBytes("gb2312"));
            String requestUrl = "https://tradegw.htsc.com.cn/?" + encry;
            logger.info("stock trade, request:{}", requestUrl);
            String response = null;
            try{
                response = HttpClientUtils.doGet(requestUrl, null);
            }catch (Exception e){
                logger.error("trade catch exception", e);
            }
            logger.info("stock trade, response:{}", response);

            String decry = new String(Base64.decodeBase64(response), "gb2312");

            logger.info("stock trade, decrypted response:{}", decry);

            return decry;
        }catch (Exception e){
            logger.error("dotrade catch exception", e);
            return null;
        }
    }


    public  void sellStk(String cd){

        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();
        if(userid!=null){

            Owns o = ownsDao.findTodayLatest(cd);
            if(o != null){

                sb.append("uid=").append(userid.getUid());
                sb.append("&cssweb_type=STOCK_SALE&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$110.96.137.179;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=302&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&exchange_type=");
                if(cd.startsWith("6")){
                    sb.append("1&");
                    sb.append("stock_account=A782341997&");
                }else if(cd.startsWith("0")){
                    sb.append("2&");
                    sb.append("stock_account=0164498009&");

                }
                sb.append("stock_code=").append(cd).append("&");

                String [] params = DataUtil.getCurScData(cd);
                if(params == null || params.length == 0){
                    logger.error("the stock data format is illegal, code:{}", cd);
                    return ;
                }

                Double curP = Double.parseDouble(params[3]);

                Double salepoint = o.getSuggp();

                if(curP > salepoint){
                    sb.append("entrust_amount=").append(o.getQuan()).append("&").append("entrust_price=").append(curP).append("&entrust_prop=0&entrust_bs=2&")
                            .append("ram=").append(String.valueOf(Math.random()));

                }else {
                    sb.append("entrust_amount=").append(o.getQuan()).append("&").append("entrust_price=").append(salepoint).append("&entrust_prop=0&entrust_bs=2&")
                            .append("ram=").append(String.valueOf(Math.random()));

                }
                String response = doRequest(sb.toString());

                doInsertTrade(cd, curP>salepoint? curP: salepoint, o.getQuan(), response, 2, false);

            }

        }
    }

    public void stslByLastPrice(String cd) {

        StringBuffer sb = new StringBuffer();
        Userid userid = userIdDao.findTodayLatest();
        if(userid!=null){

            Owns o = ownsDao.findTodayLatest(cd);
            if(o != null){

                sb.append("uid=").append(userid.getUid());
                sb.append("&cssweb_type=STOCK_SALE&version=1&custid=666600961929&op_branch_no=1320&branch_no=1320&op_entrust_way=7&op_station=IP$110.96.137.179;MAC$08-00-27-AE-8C-4C;HDD$VB1fbba643-1fd7e619 &function_id=302&fund_account=666600961929&password=V8qK1Vt0w8mRsloC7iM4Ww$$&identity_type=&exchange_type=");
                if(cd.startsWith("6")){
                    sb.append("1&");
                    sb.append("stock_account=A782341997&");
                }else if(cd.startsWith("0")){
                    sb.append("2&");
                    sb.append("stock_account=0164498009&");

                }
                sb.append("stock_code=").append(cd).append("&");

                Price price = priceDao.findLatestOne(cd);
                if(price!=null){
                    if(price.getHang()){
                        logger.error("sell by price is hang");
                        return;
                    }else {
                        String [] params = DataUtil.getCurScData(cd);
                        if(params == null || params.length == 0){
                            logger.error("the stock data format is illegal, code:{}", cd);
                            return ;
                        }

                        Double curP = Double.parseDouble(params[3]);



                        Double salepoint = o.getSuggp();

                        if(curP > salepoint){
                            sb.append("entrust_amount=").append(o.getQuan()).append("&").append("entrust_price=").append(curP).append("&entrust_prop=0&entrust_bs=2&")
                                    .append("ram=").append(String.valueOf(Math.random()));

                        }else {
                            sb.append("entrust_amount=").append(o.getQuan()).append("&").append("entrust_price=").append(salepoint).append("&entrust_prop=0&entrust_bs=2&")
                                    .append("ram=").append(String.valueOf(Math.random()));

                        }
                        String response = doRequest(sb.toString());

                        doInsertTrade(cd, curP>salepoint? curP: salepoint, o.getQuan(), response, 2, false);
                    }
                }


            }

        }
    }
}
