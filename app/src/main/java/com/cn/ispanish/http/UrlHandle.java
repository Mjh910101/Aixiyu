package com.cn.ispanish.http;


import android.content.Context;

import com.cn.ispanish.handlers.SystemHandle;

public class UrlHandle {

    private static String INDEX = "http://www.ispanish.cn";
    private static String DUBUG_INDEX = "http://39.108.246.159";

//    private static String INDEX_URL= INDEX + "/index.php/Home/Api";

    private static String getIndexUrl(Context context) {
        if (SystemHandle.isDubug(context)) {
            return DUBUG_INDEX + "/index.php/Home/Api";
        }
        return INDEX + "/index.php/Home/Api";
    }

    /*
    * 首页全部课程
    * */
    public static String getIndexblock(Context context) {
        return getIndexUrl(context) + "/indexblock.html";
    }

    /*
    * 课程列表
    * */
    public static String getClasstype(Context context) {
        return getIndexUrl(context) + "/classtype.html";
    }

    /*
    * 冲刺课程
    * */
    public static String getBlock2Class(Context context) {
        return getIndexUrl(context) + "/blocktoclass.html";
    }

    /*
    * 幻灯
    * */
    public static String getBanner(Context context) {
        return getIndexUrl(context) + "/banner.html";
    }

    public static String getBackBanner(Context context) {
        return getIndexUrl(context) + "/backbanner.html";
    }

    /*
    * 幻灯
    * */
    public static String getTitBanner(Context context) {
        return getIndexUrl(context) + "/titbanner.html";
    }

    public static String getBackExtractTime(Context context) {
        return getIndexUrl(context) + "/backextracttime.html";
    }

    /*
    * 检查显示隐藏
    * */
    public static String getBankdisPlay(Context context) {
        return getIndexUrl(context) + "/bankdisplay.html";
    }

    /*
    * 发短信
    * */
    public static String getSendNote(Context context) {
        return getIndexUrl(context) + "/SendNote.html";
    }

    /*
    * 发短信
    * */
    public static String getSendMessage(Context context) {
        return getIndexUrl(context) + "/sendmessage.html";
    }

    /*
    * 注册
    * */
    public static String getRegister(Context context) {
        return getIndexUrl(context) + "/register.html";
    }

    /*
    * 登录
    * */
    public static String getLogin(Context context) {
        return getIndexUrl(context) + "/login.html";
    }

    /*
    * 修改用户资料
    * */
    public static String getUserInfoset(Context context) {
        return getIndexUrl(context) + "/userinfoset.html";
    }

    /*
    * 修改用户头像
    * */
    public static String getUpportrait(Context context) {
        return getIndexUrl(context) + "/upportrait.html";
    }

    /*
    * QQ登录
    * */
    public static String getQjogg(Context context) {
        return getIndexUrl(context) + "/qjogg.html";
    }

    /*
    * Weibo登录
    * */
    public static String getWbdl(Context context) {
        return getIndexUrl(context) + "/wbdl.html";
    }

    /*
    * 微信登录
    * */
    public static String getWxdl(Context context) {
        return getIndexUrl(context) + "/wxdl.html";
    }

    /*
    * 查看个人中心信息
    * */
    public static String getUserinfo(Context context) {
        return getIndexUrl(context) + "/uinfo.html";
    }

    /*
    * 视频播放页信息
    * */
    public static String getVideoPlay(Context context) {
        return getIndexUrl(context) + "/videoplay.html";
    }

    /*
    * 视频评论
    * */
    public static String getVideoComment(Context context) {
        return getIndexUrl(context) + "/spcomment.html";
    }

    /*
    * 视频评论
    * */
    public static String getVideoReply(Context context) {
        return getIndexUrl(context) + "/classpl.html";
    }

    /*
    * 查看收藏
    * */
    public static String getCollection(Context context) {
        return getIndexUrl(context) + "/scback.html";
    }

    /*
    * 取消收藏
    * */
    public static String getCleanCollect(Context context) {
        return getIndexUrl(context) + "/cleancollect.html";
    }

    /*
    * 收藏
    * */
    public static String getCollectVideo(Context context) {
        return getIndexUrl(context) + "/collectvideo.html";
    }

    /*
    * 获取订单ID
    * */
    public static String getAndpayali(Context context) {
        return getIndexUrl(context) + "/andpayali.html";
    }

    /*
    * 获取订单ID
    * */
    public static String getAliPayBalance(Context context) {
        return getIndexUrl(context) + "/alipaybalance.html";
    }

    public static String getAliPayMinClass(Context context) {
        return getIndexUrl(context) + "/alipayminiclass.html";
    }

    /*
       * 获取优惠券
       * */
    public static String getCoupon(Context context) {
        return getIndexUrl(context) + "/coupon.html";
    }

    /*
     * 获取订单ID--独家资料
     * */
    public static String getBankAlipay(Context context) {
        return getIndexUrl(context) + "/bankalipay.html";
    }

    /*
    * 获取订单ID
    * */
    public static String getWxpay(Context context) {
        return getIndexUrl(context) + "/wxpay.html";
    }

    /*
    * 获取订单ID
    * */
    public static String getWxPayBalance(Context context) {
        return getIndexUrl(context) + "/wxpaybalance.html";
    }

    public static String getWxPayMinClass(Context context) {
        return getIndexUrl(context) + "/wxpayminiclass.html";
    }

    /*
    * 获取订单ID
    * */
    public static String getWxpay2Page(Context context) {
        return getIndexUrl(context) + "/wxpaytopage.html";
    }

    /*
    * 用户已购买课程
    * */
    public static String getMyVedioList(Context context) {
        return getIndexUrl(context) + "/getVedioList.html";
    }

    /*
     * 试卷列表
     * */
    public static String getQusetionBack(Context context) {
        return getIndexUrl(context) + "/qusetionback.html";
    }

    /*
     * 题目列表
     * */
    public static String getBackQuestion(Context context) {
        return getIndexUrl(context) + "/backquestion.html";
    }

    public static String getBank2CollTit(Context context) {
        return getIndexUrl(context) + "/banktocolltit.html";
    }

    /*
     * 按题目类型获取试题
     * */
    public static String getTypeToBank(Context context) {
        return getIndexUrl(context) + "/typetobank.html";
    }

    /*
     * 收藏试卷
     * */
    public static String getCollbank(Context context) {
        return getIndexUrl(context) + "/collbank.html";
    }

    public static String getTitColl(Context context) {
        return getIndexUrl(context) + "/titcoll.html";
    }

    public static String getType2ErrTit(Context context) {
        return getIndexUrl(context) + "/typetoerrtit.html";
    }

    public static String getType2Tit(Context context) {
        return getIndexUrl(context) + "/typetotit.html";
    }

    public static String getSaveErrorTit(Context context) {
        return getIndexUrl(context) + "/saveerrortit.html";
    }

    /*
     * 获取收藏试卷
     * */
    public static String getBackCollbank(Context context) {
        return getIndexUrl(context) + "/backcollbank.html";
    }

    public static String getBackCollBanksel(Context context) {
        return getIndexUrl(context) + "/backcollbanksel.html";
    }

    /*
     * 获取独家资料列表
     * */
    public static String getImgInforMation(Context context) {
        return getIndexUrl(context) + "/imginformation.html";
    }

    public static String getBackBuyInformation(Context context) {
        return getIndexUrl(context) + "/backbuyinformation.html";
    }

    /*
     * 获取独家资料
     * */
    public static String getImgInforMationXi(Context context) {
        return getIndexUrl(context) + "/imginformationxi.html";
    }

    /*
     * 有奖竞猜
     * */
    public static String getITestPaper(Context context) {
        return getIndexUrl(context) + "/testpaper.html";
    }

    /*
     * 有奖竞猜试题
     * */
    public static String getMatchTotitle(Context context) {
        return getIndexUrl(context) + "/matchtotitle.html";
    }

    /*
     * 有奖竞猜试题
     * */
    public static String getSeluserTest(Context context) {
        return getIndexUrl(context) + "/selusertest.html";
    }

    /*
     * 交卷
     * */
    public static String getTakeranking(Context context) {
        return getIndexUrl(context) + "/takeranking.html";
    }

    /*
     * 问题反馈
     * */
    public static String getFeedBack(Context context) {
        return getIndexUrl(context) + "/feedback.html";
    }

    /*
     * 历届比赛
     * */
    public static String getAllTestPaper(Context context) {
        return getIndexUrl(context) + "/alltestpaper.html";
    }

    /*
     * 历届排名
     * */
    public static String getTestPaperRank(Context context) {
        return getIndexUrl(context) + "/testpaperrank.html";
    }

    /*
     * 我的成绩
     * */
    public static String getPersonalscore(Context context) {
        return getIndexUrl(context) + "/personalscore.html";
    }

    /*
    * 忘记密码
    * */
    public static String getLostPaseword(Context context) {
        return getIndexUrl(context) + "/lostpwdset.html";
    }

    /*
    * 绑定手机
    * */
    public static String getPhoneBinding(Context context) {
        return getIndexUrl(context) + "/phonebinding.html";
    }

    /*
    * 检查是否绑定手机
    * */
    public static String getCheckuserPhone(Context context) {
        return getIndexUrl(context) + "/checkuserphone.html";
    }

    /*
    * 查询余额
    * */
    public static String getSelectBalance(Context context) {
        return getIndexUrl(context) + "/selectbalance.html";
    }

    /*
     * 余额购买课程
     * */
    public static String getBuyClass(Context context) {
        return getIndexUrl(context) + "/BuyClass.html";
    }

    /*
     * 余额购买课程
     * */
    public static String getBuyClassBalance(Context context) {
        return getIndexUrl(context) + "/buyClassBalance.html";
    }

    public static String getMiniClassBalance(Context context) {
        return getIndexUrl(context) + "/miniclassbalance.html";
    }

    /*
     * 获取优惠券
     * */
    public static String getGetCoupon(Context context) {
        return getIndexUrl(context) + "/getcoupon.html";
    }

    /*
     * 提交分享
     * */
    public static String getSvaematShare(Context context) {
        return getIndexUrl(context) + "/svaematshare.html";
    }

    /*
     * 检查更新
     * */
    public static String getForceUpdata(Context context) {
        return getIndexUrl(context) + "/forceupdata.html";
    }

    /*
     * 直播列表
     * */
    public static String getLiveIndex(Context context) {
        return getIndexUrl(context) + "/liveindex.html";
    }

    /*
     *直播信息
     * */
    public static String getLiveRoom(Context context) {
        return getIndexUrl(context) + "/liveroom.html";
    }

    /*
    * 一对一
    * */
    public static String getOneByOne(Context context) {
        return getIndexUrl(context) + "/onetoonelist.html";
    }

    /*
    * 小班
    * */
    public static String getMiniClassList(Context context) {
        return getIndexUrl(context) + "/miniclasslist.html";
    }

    /*
    * 一对一简介
    * */
    public static String getOneByOneContent(Context context) {
        return getIndexUrl(context) + "/onetooneconten.html";
    }

    public static String getLiveCollect(Context context) {
        return getIndexUrl(context) + "/livecollect.html";
    }

    /*
   * 小班简介
   * */
    public static String getMinClassContent(Context context) {
        return getIndexUrl(context) + "/miniclass.html";
    }

    public static String getLiveConget(Context context) {
        return getIndexUrl(context) + "/liveconget.html";
    }

    public static String getLiveCongetPut(Context context) {
        return getIndexUrl(context) + "/liveconput.html";
    }

    public static String getLiveCommenGood(Context context) {
        return getIndexUrl(context) + "/livecommengood.html";
    }

    public static String getCleanGood(Context context) {
        return getIndexUrl(context) + "/cleangood.html";
    }

    public static String getBackGift(Context context) {
        return getIndexUrl(context) + "/backgift.html";
    }

    public static String getAlipayGift(Context context) {
        return getIndexUrl(context) + "/alipaygift.html";
    }

    public static String getWxpayGift(Context context) {
        return getIndexUrl(context) + "/wxpaygift.html";
    }

    public static String getGiftBalance(Context context) {
        return getIndexUrl(context) + "/giftbalance.html";
    }

    public static String getBackLanguage(Context context) {
        return getIndexUrl(context) + "/backlanguage.html";
    }

    public static String getChannelHistory(String channelId) {
        return "http://api.live.polyv.net/v2/chat/" + channelId + "/getHistory";
    }

    public static String getBackBankType(Context context) {
        return getIndexUrl(context) + "/backbanktype.html";
    }

    public static String getPointBuyPage(Context context) {
        return getIndexUrl(context) + "/pointbuypage.html";
    }

    public static String getDemandBank(Context context) {
        return getIndexUrl(context) + "/demandbank.html";
    }

    public static String getBackBankComment(Context context) {
        return getIndexUrl(context) + "/backbankcomment.html";
    }
    public static String getBankCommentGood(Context context) {
        return getIndexUrl(context) + "/bankcommentgood.html";
    }
    public static String getSaveBankComment(Context context) {
        return getIndexUrl(context) + "/savebankcomment.html";
    }
}
