package com.cn.ispanish.box;

import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONObject;

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
 * Created by Hua on 2017/9/28.
 */

public class LivePlayBack {

    private String id;
    private String title;
    private String time;
    private String img;
    private String fid;
    private String vid;
    private String cid;

    public LivePlayBack(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        title = JsonHandle.getString(json, "title");
        time = JsonHandle.getString(json, "time");
        img = JsonHandle.getString(json, "img");
        fid = JsonHandle.getString(json, "fid");
        vid = JsonHandle.getString(json, "vid");
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return "http://www.ispanish.cn/Public/Uploads/" + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
