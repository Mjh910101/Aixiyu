package com.cn.ispanish.box;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cn.ispanish.activitys.MainActivity;
import com.cn.ispanish.activitys.VideoPlayContentNoStartActivity;
import com.cn.ispanish.activitys.VideoPlayListActivity;
import com.cn.ispanish.activitys.WebContentActivity;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.webview.MyWebViewClient;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

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
 * Created by Hua on 17/3/17.
 */
@Table(name = "tbl_push_history")
public class PushInfo {

    @Id(column = "id")
    private int id;

    /**
     * 视频id
     */
    @Column(column = "vid")
    private String vid;

    /**
     * 课程id
     */
    @Column(column = "course_id")
    private String courseid;

    @Column(column = "title")
    private String title;
    /**
     * 推送内容
     */
    @Column(column = "content")
    private String content;

    /**
     * 简介图片
     */
    @Column(column = "img")
    private String briefImg;

    /**
     * 跳转网址的url
     */
    @Column(column = "url")
    private String url;

    /**
     * 推送的时间
     */
    @Column(column = "time")
    private String time;

    public PushInfo() {

    }

    public PushInfo(JSONObject json) {
        vid = JsonHandle.getString(json, "vid");
        courseid = JsonHandle.getString(json, "courseid");
        title = JsonHandle.getString(json, "title");
        content = JsonHandle.getString(json, "content");
        briefImg = JsonHandle.getString(json, "briefImg");
        url = JsonHandle.getString(json, "url");
        time = JsonHandle.getString(json, "time");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBriefImg() {
        if (briefImg == null) {
            return "";
        }
        if (briefImg.contains("http://")) {
            return briefImg;
        }
        if (briefImg.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + briefImg.substring(1, briefImg.length());
        }
        return "http://www.ispanish.cn/Public/Uploads/" + briefImg;
    }

    public void setBriefImg(String briefImg) {
        this.briefImg = briefImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCourse() {
        if (vid == null) {
            return false;
        }
        if (vid.equals("")) {
            return false;
        }
        if (vid.equals("null")) {
            return false;
        }
        if (courseid == null) {
            return false;
        }
        if (courseid.equals("")) {
            return false;
        }
        if (courseid.equals("null")) {
            return false;
        }
        return true;
    }

    public boolean isWebMessage() {
        if (url == null) {
            return false;
        }
        if (url.equals("")) {
            return false;
        }
        if (url.equals("null")) {
            return false;
        }
        return true;
    }

    public Intent getJumpIntent(Context context) {
        Bundle b = new Bundle();
        Intent intent;
        if (isCourse()) {
//            b.putString(VideoPlayListActivity.COURSE_ID_KEY, getCourseid());
//            intent = new Intent(context, VideoPlayListActivity.class);
            b.putString(VideoPlayContentNoStartActivity.TITLE_KEY, getTitle());
            b.putString(VideoPlayContentNoStartActivity.COURSE_ID_KEY, getCourseid());
            intent = new Intent(context, VideoPlayContentNoStartActivity.class);
        } else if (isWebMessage()) {
            b.putString(MyWebViewClient.KEY, getUrl());
            b.putString(MyWebViewClient.TITLE, getTitle());
            intent = new Intent(context, WebContentActivity.class);
        } else {
            intent = new Intent(context, MainActivity.class);
        }
        intent.putExtras(b);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent;
    }
}
