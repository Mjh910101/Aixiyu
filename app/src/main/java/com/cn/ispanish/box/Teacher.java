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
 * Created by Hua on 17/3/10.
 */
public class Teacher {

    private String id;
    private String name;
    private String portrait;
    private String synopsis;

    public Teacher(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        portrait = JsonHandle.getString(json, "portrait");
        synopsis = JsonHandle.getString(json, "synopsis");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
