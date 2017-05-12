package com.cn.ispanish.box;

import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 17/3/6.
 */
public class IndexBlockCollection {

    private String id;
    private String name;
    private List<IndexBlock> bodyList;

    public IndexBlockCollection(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        bodyList = new ArrayList<>();
        JSONArray array = JsonHandle.getArray(json, "body");

        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject dataJson = JsonHandle.getJSON(array, i);
                if (dataJson != null) {
                    bodyList.add(new IndexBlock(dataJson));
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<IndexBlock> getBodyList() {
        return bodyList;
    }

    public List<IndexBlock> getBodyListTopFour() {
        return getBodyListTop(4);
    }

    public List<IndexBlock> getBodyListTop(int top) {
        if (bodyList.size() < top) {
            return bodyList;
        }

        List<IndexBlock> list = new ArrayList<>();
        for (int i = 0; i < top; i++) {
            list.add(bodyList.get(i));
        }
        return list;
    }
}
