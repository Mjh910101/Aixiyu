package com.cn.ispanish.topup.zhifubao;

import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.topup.zhifubao.util.OrderInfoUtil2_0;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/3/7.
 */
public class ZhiFuBao {


    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016062101539723";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088121398226912";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMOE/FLUB//QU8UCdOJDl9kOYJR1s39aHQbodg2vZ34AKFGANSSgTBtInRSvshtQPOB272pzIk04HlqxDgZWkxh2ZFPZ8EGGQXzyAfLF03x31P/iFo/Rkj5kNwI9OdCNTcN/YUSVolPcU/kDDpNIOxA6HSvSfE2dIKskR7Sey3MbAgMBAAECgYA8HgzTjvxyrOTFA89arvvmsqQBofsyIkwgjqOV66P/ux6qiKhOfbdJAFfEP42dFlvRjOBqs8INSj8TFbPBQSdy6VmvW6UhoEldcM5n4+WK6Z64wOqOwUO271TVYdFscC9bTK2LF7dY/eoNZkP/nxsdcptTxV/VXwQiG1uazzOjYQJBAOtncvoInXacQK/KeSkfLBQqdxnjkWjZMGKgBtxP8OrcApIJBiCcrbQNGkUoE6RMzLt2aU30OJNiXFy2vj3gOOsCQQDUoDSxJckqGHCCJUYhnybEx+OBqYOtW2kOSLp6Uv8iCLkJln8036EiycGbd55oh57NceszMT2D4wId62nStCKRAkBIu6DZwjmswSB3cC65VulOncRI2ng+FgUOI/WaspR0bF/a8TDUeLRu0jGQto5DEHudXDuzG0czuothFhF3msTbAkEAq23LbfRAH496zoOM6ritgkSDOlxYzSGBL0IWD1/xUhhkT8WmJBQVj73JOjaUYkTOFWg+sJSHu1kAsOzH8ljH8QJBAIDMmsDxWBPE+7egdiH/AqJFPoX88eDhlZ0iyngSC69Qxxl9CKgcHW2tIqrIJvJ2tJITiHhp7N1/eAOYgWm3GtU=";

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;

//    public static String getOrderParam(String orderId) {
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//        return orderParam;
//    }

//    public static String getSin() {
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//
//        return sign;
//    }

    public static String getOrderInfo(String title, String price, String orderId) {
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, title, price, orderId);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);

        final String orderInfo = orderParam + "&" + sign;

        return orderInfo;
    }

}
