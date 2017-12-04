package com.cn.ispanish.topup.zhifubao.util;


import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.topup.zhifubao.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import mlxy.utils.S;

public class OrderInfoUtil2_0 {

    public final static String Course_Notify_Url = "http://www.ispanish.cn/index.php/Home/Api/alipayclass.html";
    public final static String Course_Balance_Notify_Url = "http://www.ispanish.cn/index.php/Home/Api/alipayclassbalance.html";
    public final static String Paper_Notify_Url = "http://www.ispanish.cn/index.php/Home/Api/bankalipayback.html";
    public final static String Min_Class_Notify_Url = "http://www.ispanish.cn/index.php/Home/Api/alipayminiclassback.html";
    public final static String Gift_Notify_Url = "http://www.ispanish.cn/index.php/Home/Api/alipaygiftback.html";

    /**
     * 构造授权参数列表
     *
     * @param pid
     * @param app_id
     * @param target_id
     * @return
     */
    public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<String, String>();

        // 商户签约拿到的app_id，如：2013081700024223
        keyValues.put("app_id", app_id);

        // 商户签约拿到的pid，如：2088102123816631
        keyValues.put("pid", pid);

        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");

        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");

        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");

        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");

        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");

        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);

        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");

        // 签名类型
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

//        keyValues.put("hb_fq_param", "{\"hb_fq_num\":\"3\",\"hb_fq_seller_percent\":\"1.80\"}");

        return keyValues;
    }

    /**
     * 构造支付订单参数列表
     *
     * @param app_id
     * @param rsa2
     * @return
     */
    public static Map<String, String> buildOrderParamMap(String app_id, boolean rsa2, String title, String price, String orderId, String notifyUrl) {
        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("app_id", app_id);

        keyValues.put("biz_content",
                "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + price
                        + "\",\"subject\":\"" + title + "\",\"body\":\"" + title + "\",\"out_trade_no\":\"" + orderId + "\"}");
//                        + "\",\"extend_params\":{\"hb_fq_num\":\"3\",\"hb_fq_seller_percent\":\"100\"}}");
//                +"\"hb_fq_param\":\"{\\\"hb_fq_num\\\":\\\"3\\\",\\\"hb_fq_seller_percent\\\":\\\"1.80\\\"}\""
//                +"\"extend_params\":\"{\\\"hb_fq_num\\\":\\\"3\\\",\\\"hb_fq_seller_percent\\\":\\\"1.80\\\"}\""


        keyValues.put("charset", "utf-8");

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        keyValues.put("timestamp", DateHandle.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

        keyValues.put("version", "1.0");

        keyValues.put("notify_url", notifyUrl);

        keyValues.put("enable_pay_channels", "balance,moneyFund,pcredit,pcreditpayInstallment,bankPay,debitCardExpress");

//        keyValues.put("hb_fq_param", "{\"hb_fq_num\":\"3\",\"hb_fq_seller_percent\":\"100\"}");
//        keyValues.put("extend_params", "{\"sys_service_provider_id\":\""+orderId+"\"}");
//        keyValues.put("extend_params", "{\"hb_fq_num\":\"3\",\"hb_fq_seller_percent\":\"0\",\"needBuyerRealnamed\":\"T\",\"sys_service_provider_id\":\"2088511833207846\"}");
//        keyValues.put("extend_params", "{\"hb_fq_num\":\"3\",\"hb_fq_seller_percent\":\"0\",\"needBuyerRealnamed\":\"T\"}");

        keyValues.put("goods_type", "1");

        return keyValues;
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

}
