package com.cn.ispanish.http;


public class UrlHandle {

    private static String INDEX = "http://www.ispanish.cn";

    private static String INDEX_URL = INDEX + "/index.php/Home/Api";

    /*
    * 首页全部课程
    * */
    public static String getIndexblock() {
        return INDEX_URL + "/indexblock.html";
    }

    /*
    * 课程列表
    * */
    public static String getClasstype() {
        return INDEX_URL + "/classtype.html";
    }

    /*
    * 幻灯
    * */
    public static String getBanner() {
        return INDEX_URL + "/banner.html";
    }

    /*
    * 发短信
    * */
    public static String getSendNote() {
        return INDEX_URL + "/SendNote.html";
    }

    /*
    * 注册
    * */
    public static String getRegister() {
        return INDEX_URL + "/register.html";
    }

    /*
    * 登录
    * */
    public static String getLogin() {
        return INDEX_URL + "/login.html";
    }

    /*
    * 修改用户资料
    * */
    public static String getUserInfoset() {
        return INDEX_URL + "/userinfoset.html";
    }

    /*
    * 修改用户头像
    * */
    public static String getUpportrait() {
        return INDEX_URL + "/upportrait.html";
    }

    /*
    * QQ登录
    * */
    public static String getQjogg() {
        return INDEX_URL + "/qjogg.html";
    }

    /*
    * Weibo登录
    * */
    public static String getWbdl() {
        return INDEX_URL + "/wbdl.html";
    }

    /*
    * 微信登录
    * */
    public static String getWxdl() {
        return INDEX_URL + "/wxdl.html";
    }

    /*
    * 查看个人中心信息
    * */
    public static String getUserinfo() {
        return INDEX_URL + "/uinfo.html";
    }

    /*
    * 视频播放页信息
    * */
    public static String getVideoPlay() {
        return INDEX_URL + "/videoplay.html";
    }

    /*
    * 视频评论
    * */
    public static String getVideoComment() {
        return INDEX_URL + "/spcomment.html";
    }

    /*
    * 查看收藏
    * */
    public static String getCollection() {
        return INDEX_URL + "/scback.html";
    }

    /*
    * 获取订单ID
    * */
    public static String getAndpayali() {
        return INDEX_URL + "/andpayali.html";
    } /*
    * 获取订单ID
    * */

    public static String getWxpay() {
        return INDEX_URL + "/wxpay.html";
    }

    /*
    * 用户已购买课程
    * */
    public static String getMyVedioList() {
        return INDEX_URL + "/getVedioList.html";
    }

}
