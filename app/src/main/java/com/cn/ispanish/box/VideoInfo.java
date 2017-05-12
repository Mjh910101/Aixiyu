package com.cn.ispanish.box;

import android.content.Context;

import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.SystemHandle;
import com.easefun.polyvsdk.BitRateEnum;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Hua on 17/3/14.
 */
@Table(name = "tbl_video_info")
public class VideoInfo implements Comparable {

    private final static String KEY = "Video_";


    @Column(column = "id")
    private String id;
    @Column(column = "name")
    private String name;
    @Id(column = "vid")
    private String vid;
    @Column(column = "sort")
    private int sort;
    @Column(column = "custom_tip")
    private String customTip;
    @Column(column = "is_free")
    private boolean isFree;
    @Column(column = "is_download")
    private boolean isDownload;
    @Column(column = "course_id")
    private String courseid;
    @Column(column = "image")
    private String image;
    @Transient
    private boolean isBuy;

    public VideoInfo() {

    }

    public VideoInfo(JSONObject json) {
        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        vid = JsonHandle.getString(json, "vid");
        sort = JsonHandle.getInt(json, "sort");
        customTip = JsonHandle.getString(json, "customTip");
        isFree = JsonHandle.getString(json, "free").equals("1");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVid() {
        return vid;
    }

    public int getSort() {
        return sort;
    }

    public String getCustomTip() {
        return customTip;
    }

    public boolean isFree() {
        return isFree;
    }

    public static void saveBitRate(Context context, BitRateEnum bitRate) {
        SystemHandle.saveIntMessage(context, KEY + "BitRate", bitRate.getNum());
    }

    public static BitRateEnum getBitRateEnum(Context context) {
        int num = SystemHandle.getInt(context, KEY + "BitRate");
        return BitRateEnum.getBitRate(num);
    }

    public static String getBitRate(Context context) {
        int num = SystemHandle.getInt(context, KEY + "BitRate");
        return BitRateEnum.getBitRateName(num);
    }

    public static List<String> getBitRateArray() {
        List<String> list = new ArrayList<>();
        list.add(BitRateEnum.ziDongName);
        list.add(BitRateEnum.liuChangName);
        list.add(BitRateEnum.gaoQingName);
        list.add(BitRateEnum.chaoQingName);
        return list;
    }

    public static void saveAutoContinue(Context context, boolean b) {
        SystemHandle.saveBooleanMessage(context, KEY + "auto_continue", b);
    }

    public static boolean getAutoContinue(Context context) {
        return SystemHandle.getBoolean(context, KEY + "auto_continue");
    }

    public boolean equals(String vid) {
        return getVid().equals(vid);
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setCustomTip(String customTip) {
        this.customTip = customTip;
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

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    @Override
    public int compareTo(Object o) {
        VideoInfo info = (VideoInfo) o;
        return this.getSort() - info.getSort();
    }
}
