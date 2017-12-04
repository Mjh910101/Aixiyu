package com.cn.ispanish.box;

import android.content.Context;
import android.view.View;

import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONObject;

import mlxy.utils.S;

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
 * Created by Hua on 17/6/23.
 */
public class Ranking {


    private String id;
    private String average;
    private String usetime;
    private String name;
    private String portrait;
    private String putime;
    private int orders;
    private int going;
    private int luck;
    private int sharetype;
    private String userid;

    public Ranking(JSONObject json) {
        average = JsonHandle.getString(json, "average");
        usetime = JsonHandle.getString(json, "usetime");
        name = JsonHandle.getString(json, "name");
        portrait = JsonHandle.getString(json, "portrait");
        id = JsonHandle.getString(json, "id");
        putime = JsonHandle.getString(json, "putime");
        orders = JsonHandle.getInt(json, "orders");
        going = JsonHandle.getInt(json, "going");
        luck = JsonHandle.getInt(json, "lucky");
        sharetype = JsonHandle.getInt(json, "sharetype");
        userid = JsonHandle.getString(json, "userid");
    }

    public String getAverage() {
        return average;
    }

    public String getUsetime() {
        return usetime;
    }

    public long getUseTimeLong() {
        try {
            return Long.valueOf(usetime);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        if (name.equals("null")) {
            return "";
        }
        return name;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        StringBuilder sb = new StringBuilder();
        try {
            int s = Integer.valueOf(usetime);
            int time_m = (int) (s / 60);
            int time_s = (int) (s % 60);

            sb.append(time_m);
            sb.append("分");
            if (time_s < 10) {
                sb.append("0");
            }
            sb.append(time_s);
            sb.append("秒");
        } catch (Exception e) {
            sb.append("");
        }
        return sb.toString();
    }

    public String getPutime() {
        try {
            return DateHandle.format(Long.valueOf(putime) * 1000, DateHandle.DATESTYP_11);
        } catch (Exception e) {
            return "";
        }
    }

    public int getOrders() {
        if (orders <= 0) {
            return 0;
        }
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public int getGoing() {
        return going;
    }

    public void setGoing(int going) {
        this.going = going;
    }

    public String getGoingText() {
        switch (getGoing()) {
            case 1:
                return "正在进行";
            case 2:
                return "结束了";
            case 3:
                return "还没开始";
            default:
                return "";
        }
    }

    public boolean isEnd() {
        return going == 2;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isMe(Context context) {
        String userId = User.getUserId(context);
        if (userId == null) {
            return false;
        }
        if (userId.equals("")) {
            return false;
        }
        return userId.equals(getUserid());
    }

    public boolean isShare() {
        return sharetype == 1;
    }

    public int getSharetype() {
        return sharetype;
    }

    public void setSharetype(int sharetype) {
        this.sharetype = sharetype;
    }

    public String getAverageText() {
        return getAverageText(average);
    }

    public static String getAverageText(String average) {
        try {
            double m = Double.valueOf(average);
            if (m > 99) {
                return "爱西语学魔";
            } else if (m >= 90) {
                return "爱西语学神";
            } else if (m >= 80) {
                return "爱西语学霸";
            } else if (m >= 70) {
                return "爱西语学民";
            } else if (m >= 60) {
                return "爱西语学渣";
            } else {
                return "爱西语学沫";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public int getLuck() {
        return luck;
    }

    public boolean isLuck() {
        return luck == 1;
    }

    public void setId(String id) {
        this.id = id;
    }
}
