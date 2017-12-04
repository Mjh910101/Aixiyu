package com.cn.ispanish.dao;

import android.content.ContentValues;

import com.cn.ispanish.box.channel.ChannelItem;

import java.util.List;
import java.util.Map;

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
 * Created by Hua on 2017/11/15.
 */

public interface ChannelDaoInface {

    public boolean addCache(ChannelItem item);

    public boolean deleteCache(String whereClause, String[] whereArgs);

    public boolean updateCache(ContentValues values, String whereClause,
                               String[] whereArgs);

    public Map<String, String> viewCache(String selection,
                                         String[] selectionArgs);

    public List<Map<String, String>> listCache(String selection,
                                               String[] selectionArgs);

    public void clearFeedTable();

}
