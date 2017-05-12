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
 * Created by Hua on 17/3/6.
 */
public class Banner {

    private String title;
    private String image;
    private String value;
    private String type;

    public Banner(JSONObject json) {
        title = JsonHandle.getString(json, "title");
        image = JsonHandle.getString(json, "image");
        value = JsonHandle.getString(json, "value");
        type = JsonHandle.getString(json, "type");
    }

    public String getTitle() {
        return title;
    }

    public String getImages() {
        if (image.contains("http://")) {
            return image;
        }
        return "http://www.ispanish.cn/Public/Uploads/" + image;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
