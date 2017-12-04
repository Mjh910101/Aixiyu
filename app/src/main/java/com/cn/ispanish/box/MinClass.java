package com.cn.ispanish.box;

import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
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
 * Created by Hua on 2017/10/9.
 */

public class MinClass implements Serializable {

    private String id;
    private String title;
    private String content;
    private String price;
    private String classtime;
    private String tid;
    private String imgindex;
    private String tname;
    private String original;
    private List<String> outLine;
    private boolean isBuy;
    private boolean isColl;

    public MinClass(JSONObject json) {

        id = JsonHandle.getString(json, "id");
        title = JsonHandle.getString(json, "title");
        content = JsonHandle.getString(json, "content");
        classtime = JsonHandle.getString(json, "classtime");
        tid = JsonHandle.getString(json, "tid");
        imgindex = JsonHandle.getString(json, "imgindex");
        price = JsonHandle.getString(json, "price");
        tname = JsonHandle.getString(json, "tname");
        original = JsonHandle.getString(json, "original");
        isBuy = JsonHandle.getInt(json, "isbuy") == 1;

        outLine = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClasstime() {
        return classtime;
    }

    public void setClasstime(String classtime) {
        this.classtime = classtime;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getImgindex() {
        if (imgindex.equals("")) {
            return "";
        }
        if (imgindex.contains("http://")) {
            return imgindex;
        }
        if (imgindex.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + imgindex.substring(1, imgindex.length());
        }
        return "http://www.ispanish.cn/Public/Uploads/" + imgindex;
    }

    public void setImgindex(String imgindex) {
        this.imgindex = imgindex;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTeacherInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("总时长：");
        sb.append(getClasstime());
        sb.append(" | ");
        sb.append("讲师：");
        sb.append(getTname());
        return sb.toString();
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public List<String> getOutLine() {
        return outLine;
    }

    public void setOutLine(JSONArray array) {
        if (array == null) {
            return;
        }
        for (int i = 0; i < array.length(); i++) {
            outLine.add(JsonHandle.getString(array, i));
        }
    }

    public void removeAllOutLine(){
        outLine.removeAll(outLine);
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public boolean isColl() {
        return isColl;
    }

    public void setColl(int c) {
        setColl(c == 1);
    }

    public void setColl(boolean coll) {
        isColl = coll;
    }

    public String getCollectType() {
        if(!isColl()){
            return "1";
        }
        return "2";
    }
}

