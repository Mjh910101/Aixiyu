package com.cn.ispanish.dao;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;

public class DBHandler {

    public final static int DB_LEVER = 8;

    public static DbUtils getDbUtils(Context context) {
        DaoConfig config = new DaoConfig(context);
        config.setDbName("DB_ISPANISH"); // db名
        config.setDbVersion(DB_LEVER); // db版本
        DbUtils db = DbUtils.create(config);
        return db;
    }
}
