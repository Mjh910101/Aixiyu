package com.cn.ispanish.services;

import android.content.Intent;
import android.util.Log;

import com.easefun.polyvsdk.server.AndroidService;

public class PolyvService extends AndroidService {

    private static final String TAG = "PolyvService";

    // 无参数构造函数，调用父类的super(String name)
    public PolyvService() {
        super();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // Log.i("TAG","service started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log.i("TAG","service onStartCommand");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "server destory");
    }
}
