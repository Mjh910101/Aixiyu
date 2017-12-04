package com.cn.ispanish.box;

import android.util.Log;

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
 * Created by Hua on 17/6/14.
 */
public class VipInformation {


    private String id;
    private String name;
    private String pag;
    private String time;
    private String price;
    private String discount;
    private String images;
    private String detail;
    private String buynum;
    private String classify;
    private String typetchang;
    private String introduction;
    private int demandcount;
    private String buydeadline;
    private String demanddeadline;
    private String fit;
    private boolean isBuy;
    private boolean isColling;

    private List<String> imgList;

    public VipInformation(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        pag = JsonHandle.getString(json, "pag");
        time = JsonHandle.getString(json, "time");
        price = JsonHandle.getString(json, "price");
        discount = JsonHandle.getString(json, "discount");
        images = JsonHandle.getString(json, "images");
        detail = JsonHandle.getString(json, "detail");
        buynum = JsonHandle.getString(json, "buynum");
        classify = JsonHandle.getString(json, "classify");
        typetchang = JsonHandle.getString(json, "typetchang");
        introduction = JsonHandle.getString(json, "introduction");
        demandcount = JsonHandle.getInt(json, "demandcount");
        buydeadline = JsonHandle.getString(json, "buydeadline");
        demanddeadline = JsonHandle.getString(json, "demanddeadline");
        fit = JsonHandle.getString(json, "fit");
        isBuy = JsonHandle.getInt(json, "buy") == 1;
        isColling = JsonHandle.getInt(json, "colling") == 1;

        setImageList(JsonHandle.getArray(json, "imgtit"));
    }

    public boolean isBuy() {
        return isBuy;
    }

    public List<String> getImgList() {
        return imgList;
    }

    private void setImageList(JSONArray array) {
        imgList = new ArrayList<>();
        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {
            String url = getImages(JsonHandle.getString(array, i));
            Log.e("", url);
            imgList.add(url);
        }
    }

    public String getImages(String img) {
        if (img.contains("http://") || img.contains("https://")) {
            return img;
        }
        if (img.length() > 1 && img.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + img.substring(1, img.length());
        }
//        return "http://www.ispanish.cn/Public/Uploads/addimginformation/" + img;
        return "http://www.ispanish.cn/Public/Uploads/" + img;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return getImages(images);
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

    public String getTypetchang() {
        return typetchang;
    }

    public String getFit() {
        return fit;
    }

    public String getIntroduction() {
        return introduction;
    }

    public int getDemandcount() {
        return demandcount;
    }

    public void setDemandcount(int demandcount) {
        this.demandcount = demandcount;
    }

    public String getBuydeadline() {
        return buydeadline;
    }

    public void setBuydeadline(String buydeadline) {
        this.buydeadline = buydeadline;
    }

    public String getDemanddeadline() {
        return demanddeadline;
    }

    public void setDemanddeadline(String demanddeadline) {
        this.demanddeadline = demanddeadline;
    }

    public String getTime4Text() {
        if (time == null) {
            return "";
        }
        try {
            long t = Long.valueOf(time) * 1000;
            return DateHandle.getTimeString(new Date(t), DateHandle.DATESTYP_11);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isImage() {
        if (typetchang == null) {
            return false;
        }
        return typetchang.equals("1");
    }

    public boolean isColl() {
        return isColling;
    }

    public boolean isFeer() {
        if (getDiscount() == null) {
            return false;
        }
        if (getDiscount().equals("0.00")) {
            return true;
        }
        try {
            double m = Double.valueOf(getDiscount());
            if (m == 0) {
                return true;
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return false;
    }
}
