package com.cn.ispanish.box;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.cn.ispanish.activitys.LoginActivity;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.SystemHandle;

import org.json.JSONObject;

import java.security.MessageDigest;

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
 * Created by Hua on 17/3/7.
 */
public class User {

    private static final String TAP = "user_";

    private String id;
    private String name;
    private String password;
    private String title;
    private String level;
    private String birthdate;
    private String sex;
    private String phone;
    private String email;
    private String qq;
    private String portrait;
    private String money;
    private String courseId;
    private String attentionId;
    private String collectedId;
    private String studyId;
    private String real;
    private String address;
    private String time;
    private String isnav;
    private String content;
    private String openid;
    private String lasttime;
    private String isLevel;
    private String isBbsUid;
    private String undergo;
    private String introduce;
    private String synopsis;
    private String good;
    private String harvest;
    private String photo;
    private String age;
    private String grade;
    private String school;
    private String clicknum;
    private String work;
    private String littlePortrait;
    private String bindTeacher;
    private String oxgold;
    private String lottery;
    private String recharge;
    private String logincode;
    private String vipbuy;
    private String viplastTime;
    private String vipbuyTime;
    private String phoneKey;
    private String phoneSet;
    private String classcount;
    private String key;

    public User(JSONObject json) {
        key = JsonHandle.getString(json, "key");
        classcount = JsonHandle.getString(json, "classcount");
        phoneSet = JsonHandle.getString(json, "phoneset");
        phoneKey = JsonHandle.getString(json, "phonekey");
        vipbuyTime = JsonHandle.getString(json, "vipbuytime");
        viplastTime = JsonHandle.getString(json, "viplasttime");
        vipbuy = JsonHandle.getString(json, "vipbuy");
        logincode = JsonHandle.getString(json, "logincode");
        recharge = JsonHandle.getString(json, "recharge");
        lottery = JsonHandle.getString(json, "lottery");
        oxgold = JsonHandle.getString(json, "oxgold");
        bindTeacher = JsonHandle.getString(json, "bind_teacher");
        littlePortrait = JsonHandle.getString(json, "little_portrait");
        work = JsonHandle.getString(json, "work");
        clicknum = JsonHandle.getString(json, "clicknum");
        school = JsonHandle.getString(json, "school");
        grade = JsonHandle.getString(json, "grade");
        age = JsonHandle.getString(json, "age");
        photo = JsonHandle.getString(json, "photo");
        harvest = JsonHandle.getString(json, "harvest");
        good = JsonHandle.getString(json, "good");
        synopsis = JsonHandle.getString(json, "synopsis");
        introduce = JsonHandle.getString(json, "introduce");
        undergo = JsonHandle.getString(json, "undergo");
        isBbsUid = JsonHandle.getString(json, "is_bbs_uid");
        isLevel = JsonHandle.getString(json, "is_level");
        lasttime = JsonHandle.getString(json, "lasttime");
        openid = JsonHandle.getString(json, "openid");
        content = JsonHandle.getString(json, "content");
        isnav = JsonHandle.getString(json, "isnav");
        time = JsonHandle.getString(json, "time");
        address = JsonHandle.getString(json, "address");
        real = JsonHandle.getString(json, "real");
        studyId = JsonHandle.getString(json, "studyid");
        collectedId = JsonHandle.getString(json, "Collectedid");
        attentionId = JsonHandle.getString(json, "attentionid");
        courseId = JsonHandle.getString(json, "courseid");
        money = JsonHandle.getString(json, "money");
        portrait = JsonHandle.getString(json, "portrait");
        qq = JsonHandle.getString(json, "QQ");
        email = JsonHandle.getString(json, "email");
        phone = JsonHandle.getString(json, "phone");
        sex = JsonHandle.getString(json, "sex");
        birthdate = JsonHandle.getString(json, "birthdate");
        level = JsonHandle.getString(json, "level");
        title = JsonHandle.getString(json, "title");
        password = JsonHandle.getString(json, "password");
        name = JsonHandle.getString(json, "name");
        id = JsonHandle.getString(json, "id");
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getQq() {
        return qq;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getMoney() {
        return money;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getAttentionId() {
        return attentionId;
    }

    public String getCollectedId() {
        return collectedId;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getReal() {
        return real;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }

    public String getIsnav() {
        return isnav;
    }

    public String getContent() {
        return content;
    }

    public String getOpenid() {
        return openid;
    }

    public String getLasttime() {
        return lasttime;
    }

    public String getIsLevel() {
        return isLevel;
    }

    public String getIsBbsUid() {
        return isBbsUid;
    }

    public String getUndergo() {
        return undergo;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getGood() {
        return good;
    }

    public String getHarvest() {
        return harvest;
    }

    public String getPhoto() {
        return photo;
    }

    public String getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    public String getSchool() {
        return school;
    }

    public String getClicknum() {
        return clicknum;
    }

    public String getWork() {
        return work;
    }

    public String getLittlePortrait() {
        return littlePortrait;
    }

    public String getBindTeacher() {
        return bindTeacher;
    }

    public String getOxgold() {
        return oxgold;
    }

    public String getLottery() {
        return lottery;
    }

    public String getRecharge() {
        return recharge;
    }

    public String getLogincode() {
        return logincode;
    }

    public String getVipbuy() {
        return vipbuy;
    }

    public String getViplastTime() {
        return viplastTime;
    }

    public String getVipbuyTime() {
        return vipbuyTime;
    }

    public String getPhoneKey() {
        return phoneKey;
    }

    public String getPhoneSet() {
        return phoneSet;
    }

    public String getClasscount() {
        return classcount;
    }

    public String getAppKey() {
        return key;
    }

    public static void saveUser(Context context, User user) {
        saveKey(context, user.getAppKey());
        SystemHandle.saveStringMessage(context, TAP + "classcount", user.getClasscount());
        SystemHandle.saveStringMessage(context, TAP + "phoneSet", user.getPhoneSet());
        SystemHandle.saveStringMessage(context, TAP + "phoneKey", user.getPhoneKey());
        SystemHandle.saveStringMessage(context, TAP + "vipbuyTime", user.getVipbuyTime());
        SystemHandle.saveStringMessage(context, TAP + "viplastTime", user.getViplastTime());
        SystemHandle.saveStringMessage(context, TAP + "vipbuy", user.getVipbuy());
        SystemHandle.saveStringMessage(context, TAP + "logincode", user.getLogincode());
        SystemHandle.saveStringMessage(context, TAP + "lottery", user.getLottery());
        SystemHandle.saveStringMessage(context, TAP + "recharge", user.getRecharge());
        SystemHandle.saveStringMessage(context, TAP + "oxgold", user.getOxgold());
        SystemHandle.saveStringMessage(context, TAP + "bindTeacher", user.getBindTeacher());
        SystemHandle.saveStringMessage(context, TAP + "littlePortrait", user.getLittlePortrait());
        SystemHandle.saveStringMessage(context, TAP + "work", user.getWork());
        SystemHandle.saveStringMessage(context, TAP + "clicknum", user.getClicknum());
        SystemHandle.saveStringMessage(context, TAP + "school", user.getSchool());
        SystemHandle.saveStringMessage(context, TAP + "grade", user.getGrade());
        SystemHandle.saveStringMessage(context, TAP + "age", user.getAge());
        SystemHandle.saveStringMessage(context, TAP + "photo", user.getPhoto());
        SystemHandle.saveStringMessage(context, TAP + "harvest", user.getHarvest());
        SystemHandle.saveStringMessage(context, TAP + "good", user.getGood());
        SystemHandle.saveStringMessage(context, TAP + "synopsis", user.getSynopsis());
        SystemHandle.saveStringMessage(context, TAP + "introduce", user.getIntroduce());
        SystemHandle.saveStringMessage(context, TAP + "undergo", user.getUndergo());
        SystemHandle.saveStringMessage(context, TAP + "isBbsUid", user.getIsBbsUid());
        SystemHandle.saveStringMessage(context, TAP + "isLevel", user.getIsLevel());
        SystemHandle.saveStringMessage(context, TAP + "lasttime", user.getLasttime());
        SystemHandle.saveStringMessage(context, TAP + "openid", user.getOpenid());
        SystemHandle.saveStringMessage(context, TAP + "content", user.getContent());
        SystemHandle.saveStringMessage(context, TAP + "isnav", user.getIsnav());
        SystemHandle.saveStringMessage(context, TAP + "time", user.getTime());
        saveAddress(context, user.getAddress());
        SystemHandle.saveStringMessage(context, TAP + "real", user.getReal());
        SystemHandle.saveStringMessage(context, TAP + "studyId", user.getStudyId());
        SystemHandle.saveStringMessage(context, TAP + "collectedId", user.getCollectedId());
        SystemHandle.saveStringMessage(context, TAP + "attentionId", user.getAttentionId());
        SystemHandle.saveStringMessage(context, TAP + "courseId", user.getCourseId());
        SystemHandle.saveStringMessage(context, TAP + "money", user.getMoney());
        savePortrait(context, user.getPortrait());
        SystemHandle.saveStringMessage(context, TAP + "qq", user.getQq());
        SystemHandle.saveStringMessage(context, TAP + "email", user.getEmail());
        SystemHandle.saveStringMessage(context, TAP + "phone", user.getPhone());
        SystemHandle.saveStringMessage(context, TAP + "sex", user.getSex());
        saveBirthdate(context, user.getBirthdate());
        SystemHandle.saveStringMessage(context, TAP + "level", user.getLevel());
        SystemHandle.saveStringMessage(context, TAP + "title", user.getTitle());
        saveName(context, user.getName());
        SystemHandle.saveStringMessage(context, TAP + "id", user.getId());
    }

    public static void logout(Context context) {
        SystemHandle.saveStringMessage(context, TAP + "app_key", "");
    }

    public static void savePortrait(Context context, String v) {
        SystemHandle.saveStringMessage(context, TAP + "portrait", v);
    }


    public static void saveKey(Context context, String v) {
        SystemHandle.saveStringMessage(context, TAP + "app_key", v);
    }

    public static void saveBirthdate(Context context, String v) {
        SystemHandle.saveStringMessage(context, TAP + "birthdate", v);
    }

    public static void saveAddress(Context context, String v) {
        SystemHandle.saveStringMessage(context, TAP + "address", v);
    }

    public static void saveName(Context context, String v) {
        SystemHandle.saveStringMessage(context, TAP + "name", v);
    }

    public static String getBirthdate(Context context) {
        return SystemHandle.getString(context, TAP + "birthdate");
    }

    public static String getAddress(Context context) {
        return SystemHandle.getString(context, TAP + "address");
    }

    public static String getAppKey(Context context) {
        String key = SystemHandle.getString(context, TAP + "app_key");
        Log.e("", "key : " + key);
        return key;
    }

    public static String getName(Context context) {
        return SystemHandle.getString(context, TAP + "name");
    }

    public static String getPortrait(Context context) {
        return SystemHandle.getString(context, TAP + "portrait");
    }

    public static boolean isLogin(Context context) {
        return !getAppKey(context).equals("");
    }

    public static boolean isLoginAndShowMessage(final Context context) {
        return isLoginAndShowMessage(context, new MessageDialog.CallBackListener() {
            @Override
            public void callback() {

            }
        });
    }

    public static boolean isLoginAndShowMessage(final Context context, MessageDialog.CallBackListener callback) {
        boolean isLogin = isLogin(context);
        if (!isLogin) {
            MessageDialog dialog = new MessageDialog(context);
            dialog.setMessage("请先登录!");
            dialog.setCommitStyle("登录");
            dialog.setCommitListener(new MessageDialog.CallBackListener() {
                @Override
                public void callback() {
                    PassagewayHandler.jumpActivity(context, LoginActivity.class, LoginActivity.RequestCode);
                }
            });
            dialog.setCancelStyle("取消");
            dialog.setCancelListener(callback);
        }
        return isLogin;
    }
}
