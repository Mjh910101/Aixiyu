package com.cn.ispanish.box.question;

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
 * Created by Hua on 2017/11/6.
 */

public class PaperTitle implements Serializable {


    private String id;
    private String name;
    private String count;
    private List<PaperTitle> childList;

    public PaperTitle(JSONObject json) {

        if (json == null) {
            return;
        }

        childList = new ArrayList<>();

        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        count = JsonHandle.getString(json, "count");

        JSONArray typeArray = JsonHandle.getArray(json, "list");
        if (typeArray != null) {
            for (int i = 0; i < typeArray.length(); i++) {
                childList.add(new PaperTitle(JsonHandle.getJSON(typeArray, i)));
            }
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PaperTitle> getTypeList() {
        return childList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isHaveChild() {
        if (childList == null) {
            return false;
        }
        if (childList.isEmpty()) {
            return false;
        }
        if (childList.size() < 1) {
            return false;
        }
        return true;
    }

}
