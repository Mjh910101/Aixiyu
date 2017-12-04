package com.cn.ispanish.activitys;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import com.cn.ispanish.R;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.tencent.bugly.crashreport.CrashReport;

import cn.sharesdk.framework.ShareSDK;

public class MainLodingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loding);

        initActivity();
        initRun();
        isCheckLuck = false;

    }

    private void initActivity() {
//        ShareSDK.initSDK(this);
//        DownloadImageLoader.initLoader(context);
    }

    private void initRun() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PassagewayHandler.jumpActivity(context, MainActivity.class);
                finish();
            }
        }, 3 * 1000);
    }

    Handler handler = new Handler();

}
