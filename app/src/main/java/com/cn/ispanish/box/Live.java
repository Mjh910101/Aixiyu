package com.cn.ispanish.box;

import android.content.Context;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;

import org.json.JSONObject;

import java.io.Serializable;

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
 * Created by Hua on 2017/9/26.
 */

public class Live implements Serializable {

    private String id;
    private String title;
    private String liveVid;
    private long time;
    private long closeTime;
    private int onlineNum;
    private int readNum;
    private String channel;//直播cid
    private String cid;
    private String imgindex;
    private String content;
    private String tid;
    private int status;

    public Live(){}

    public Live(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        title = JsonHandle.getString(json, "title");
        liveVid = JsonHandle.getString(json, "livevid");
        time = JsonHandle.getLong(json, "time") * 1000;
        closeTime = JsonHandle.getLong(json, "closetime") * 1000;
        onlineNum = JsonHandle.getInt(json, "online_num");
        readNum = JsonHandle.getInt(json, "readnum");
        channel = JsonHandle.getString(json, "channel");
        imgindex = JsonHandle.getString(json, "imgindex");
        cid = JsonHandle.getString(json, "cid");
        status = JsonHandle.getInt(json, "status");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLiveVid() {
        return liveVid;
    }

    public void setLiveVid(String liveVid) {
        this.liveVid = liveVid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDay() {
        return DateHandle.format(time, "MM月dd日");
    }

    public String getLengthTime() {
        return DateHandle.format(time, "HH:mm") + " - " + DateHandle.format(closeTime, "HH:mm");
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImgindex() {
        if (imgindex.equals("")) {
            return "";
        }
        if (imgindex.contains("http://")) {
            return imgindex;
        }
        if (imgindex.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + imgindex.substring(1, imgindex.length());
        }
        return "http://www.ispanish.cn/Public/Uploads/" + imgindex;
    }

    public void setImgindex(String imgindex) {
        this.imgindex = imgindex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        switch (status) {
            case 1:
                return "回放";
            case 2:
                return "预约";
            case 3:
                return "直播中";
            default://异常
                return "";
        }
    }

    public void setStatusText(Context context, TextView view) {
        view.setText(getStatusText());

        switch (status) {
            case 1:
                view.setBackgroundResource(R.drawable.orange_orange_rounded_5);
                break;
            case 2:
                view.setBackgroundResource(R.drawable.green00_green00_rounded_5);
                break;
            case 3:
                view.setBackgroundResource(R.drawable.redff_redff_rounded_5);
                break;
            default://异常
                view.setBackgroundResource(R.drawable.gray_transparent_rounded_5);
                break;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(JSONObject json) {
        this.content = JsonHandle.getString(json, "content");
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setTid(JSONObject json) {
        this.tid = JsonHandle.getString(json, "tid");
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public static Live copy(Live obj) {
        Live l = new Live();
        l.setId(obj.getId());
        l.setTitle(obj.getTitle());
        l.setLiveVid(obj.getLiveVid());
        l.setTime(obj.getTime());
        l.setCloseTime(obj.getCloseTime());
        l.setOnlineNum(obj.getOnlineNum());
        l.setReadNum(obj.getReadNum());
        l.setChannel(obj.getChannel());
        l.setCid(obj.getCid());
        l.setImgindex(obj.getImgindex());
        l.setContent(obj.getContent());
        l.setTid(obj.getTid());
        l.setStatus(obj.getStatus());
        return l;
    }
}
