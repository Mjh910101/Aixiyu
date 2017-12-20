package com.cn.ispanish.box.question;

import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * Created by Hua on 2017/10/13.
 */

public class QuestionComment {

    private String id;
    private String uid;
    private String goodnum;
    private String conten;
    private String lid;
    private String fid;
    private String tid;
    private String time;
    private String username;
    private String userimg;
    private List<QuestionComment> list;
    private boolean isGood;


    public QuestionComment(JSONObject json) {
        list = new ArrayList<>();

        id = JsonHandle.getString(json, "id");
        uid = JsonHandle.getString(json, "uid");
        goodnum = JsonHandle.getString(json, "goodnum");
        lid = JsonHandle.getString(json, "lid");
        fid = JsonHandle.getString(json, "fid");
        tid = JsonHandle.getString(json, "tid");
        time = JsonHandle.getString(json, "time");
        conten = JsonHandle.getString(json, "conten");
        userimg = JsonHandle.getString(json, "portrait");
        username = JsonHandle.getString(json, "name");
        isGood = JsonHandle.getInt(json, "isgood") == 1;

        JSONArray array = JsonHandle.getArray(json, "list");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(new QuestionComment(JsonHandle.getJSON(array, i)));
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(String goodnum) {
        this.goodnum = goodnum;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<QuestionComment> getList() {
        return list;
    }

    public void setList(List<QuestionComment> list) {
        this.list = list;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }

    public String getUploadTime() {
        try {
            long date = Long.valueOf(time) * 1000;
            return DateHandle.getTimeString(new Date(date), DateHandle.DATESTYP_11);
        } catch (Exception e) {
            return "";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
