package com.cn.ispanish.box.question;

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
 * Created by Hua on 17/5/22.
 */
public class Paper {

    private String id;
    private String name;
    private String qid;
    private String pag;
    private String time;
    private String price;
    private String discount;
    private String images;
    private String detail;
    private String buynum;
    private String classify;
    private boolean isColl;
    private int titlenum;

    public Paper(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        qid = JsonHandle.getString(json, "qid");
        pag = JsonHandle.getString(json, "pag");
        time = JsonHandle.getString(json, "time");
        price = JsonHandle.getString(json, "price");
        discount = JsonHandle.getString(json, "discount");
        images = JsonHandle.getString(json, "images");
        detail = JsonHandle.getString(json, "detail");
        buynum = JsonHandle.getString(json, "buynum");
        classify = JsonHandle.getString(json, "classify");
        isColl = JsonHandle.getInt(json, "coll") == 1;
        titlenum = JsonHandle.getInt(json, "titlenum");
    }

    public void upload(Paper p) {
        isColl = p.isColl();
        buynum = p.getBuynum();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQid() {
        return qid;
    }

    public String getPag() {
        return pag;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getImages() {
        if (images.contains("http://")) {
            return images;
        }
        if (images.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + images.substring(1, images.length());
        }
        return "http://www.ispanish.cn/Public/Uploads/addimginformation/" + images;
    }

    public String getDetail() {
        return detail;
    }

    public String getBuynum() {
        return buynum;
    }

    public String getClassify() {
        return classify;
    }

    public boolean isColl() {
        return isColl;
    }

    public int getTitlenum() {
        return titlenum;
    }

    public boolean equals(Paper paper) {
        if (paper == null) {
            return false;
        }
        return paper.getId().equals(id);
    }
}
