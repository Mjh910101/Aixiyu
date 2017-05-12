package com.cn.ispanish.box;

import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONObject;

import java.util.Date;

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
 * Created by Hua on 17/3/10.
 */
public class Comment {

    private String content;
    private String time;
    private String uid;
    private String name;
    private String portrait;

    public Comment(JSONObject json) {
        content = JsonHandle.getString(json, "content");
        time = JsonHandle.getString(json, "time");
        uid = JsonHandle.getString(json, "uid");
        name = JsonHandle.getString(json, "name");
        portrait = JsonHandle.getString(json, "portrait");
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        if (name.equals("")) {
            return "爱西语学员";
        }
        return name;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getUploadTime() {
        try {
            long date = Long.valueOf(time) * 1000;
            return DateHandle.getTimeString(new Date(date), DateHandle.DATESTYP_11);
        } catch (Exception e) {
            return "";
        }
    }
}
