package com.cn.ispanish.box;

import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.converter.DoubleColumnConverter;

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
@Table(name = "tbl_index_block")
public class IndexBlock {

    @Id(column = "course_id")
    private String courseid;
    @Column(column = "price")
    private String price;
    @Column(column = "original")
    private String original;
    @Column(column = "name")
    private String name;
    @Column(column = "images")
    private String images;
    @Column(column = "num")
    private String num;
    @Column(column = "time")
    private String time;
    @Column(column = "uid")
    private String uid;
    @Column(column = "content")
    private String content;
    @Column(column = "digest")
    private String digest;
    @Column(column = "is_free")
    private boolean isFree;
    @Column(column = "is_download")
    private boolean isDownload;

    public IndexBlock() {

    }

    public IndexBlock(JSONObject json) {
        String jsonText = json.toString();
        if (jsonText.indexOf("courseid") != -1) {
            courseid = JsonHandle.getString(json, "courseid");
        } else if (jsonText.indexOf("cid") != -1) {
            courseid = JsonHandle.getString(json, "cid");
        } else {
            courseid = JsonHandle.getString(json, "id");
        }
        name = JsonHandle.getString(json, "name");
        images = JsonHandle.getString(json, "images");

        price = JsonHandle.getString(json, "price");
        original = JsonHandle.getString(json, "original");
        num = JsonHandle.getString(json, "num");
        time = JsonHandle.getString(json, "time");
        uid = JsonHandle.getString(json, "uid");
        content = JsonHandle.getString(json, "content");
        digest = JsonHandle.getString(json, "digest");
        isFree = JsonHandle.getString(json, "free").equals("1");
    }

    public String getCourseid() {
        return courseid;
    }

    public String getPrice() {
        return price;
    }

    public boolean isNoMoney() {
        double m = 0;
        try {
            m = Double.valueOf(price);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return m == 0;
    }

    public String getOriginal() {
        return original;
    }

    public String getName() {
        return name;
    }

    public String getImages() {
        if (images.contains("http://")) {
            return images;
        }
        if (images.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + images.substring(1, images.length());
        }
        return "http://www.ispanish.cn/Public/Uploads/" + images;
    }

    public String getNum() {
        return num;
    }

    public String getTime() {
        return time;
    }

    public String getTimeToText() {
        try {
            long t = Long.valueOf(time) * 1000;
            return "发布于：" + DateHandle.format(t, DateHandle.DATESTYP_11);
        } catch (Exception e) {
            return "";
        }
    }


    public String getUid() {
        return uid;
    }

    public String getDigest() {
        return digest;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
