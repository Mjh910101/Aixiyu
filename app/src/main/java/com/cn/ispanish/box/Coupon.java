package com.cn.ispanish.box;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.DateHandle;
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
 * Created by Hua on 17/8/25.
 */
public class Coupon {

    private String id;
    private String price;
    private String status;
    private String rangeSta;
    private long timeSta;
    private long timeEnd;
    private String classUse;
    private String cid;
    private int type;
    private boolean isOn;

    public Coupon(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        price = JsonHandle.getString(json, "price");
        status = JsonHandle.getString(json, "status");
        rangeSta = JsonHandle.getString(json, "rangeSta");
        timeSta = JsonHandle.getLong(json, "timeSta");
        timeEnd = JsonHandle.getLong(json, "timeEnd");
        classUse = JsonHandle.getString(json, "classUse");
        cid = JsonHandle.getString(json, "cid");
        type = JsonHandle.getInt(json, "type");
        isOn = JsonHandle.getInt(json, "isOn") == 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRangeSta() {
        return rangeSta;
    }

    public void setRangeSta(String rangeSta) {
        this.rangeSta = rangeSta;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getTimeSta() {
        return timeSta;
    }

    public void setTimeSta(long timeSta) {
        this.timeSta = timeSta;
    }

    public String getClassUse() {
        return classUse;
    }

    public void setClassUse(String classUse) {
        this.classUse = classUse;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getTypeText() {
        switch (getType()) {
            case 2:
                return "奖学金";
            default:
                return "优惠券";
        }
    }

    public int getCoupobBg() {
        switch (getType()) {
            case 2:
                return R.drawable.orange_orange_rounded_5;
            default:
                return R.drawable.red_red_rounded_5;
        }
    }

    public String getTimeEndText() {
        if (timeEnd <= 0) {
            return "";
        }
        return DateHandle.format(timeEnd * 1000, DateHandle.DATESTYP_4);
    }

    public boolean isRangeSta(double money) {
        try {
            double range = Double.valueOf(rangeSta);
            if (range < money) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
