package com.cn.ispanish.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.SystemHandle;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

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
 * Created by Hua on 17/3/6.
 */
public class BaseActivity extends Activity {

    public Context context;

    public boolean isCheckLuck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

//        setFlagSecure(isFlagSecure());
        showIsDebug();
        isCheckLuck = true;
    }

    private void showIsDebug() {
        if(SystemHandle.isDubug(context)){
            MessageHandler.showToast(context, "内部测试模式~");
        }
    }

    public boolean isFlagSecure() {
        return SystemHandle.isFlagSecure(context);
    }

    public void setFlagSecure(boolean b) {
//        if (b) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//禁止截屏
//        } else {
//            MessageHandler.showToast(context, "可截图模式~");
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (isCheckLuck) {
            chsekLuck();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isCheckLuck = false;
    }

    public void chsekLuck() {
        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.GET, UrlHandle.getBackExtractTime(context),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            int code = JsonHandle.getInt(json, "code");
                            if (code == 1) {
                                long luckTime = JsonHandle.getLong(json, "data");
//                                logLuck(luckTime);
                                if (SystemHandle.getLong(context, SystemHandle.LUCK_TIME) != luckTime) {
                                    if (SystemHandle.getLong(context, SystemHandle.LUCK_TIME) != 0) {
                                        jumpLuckMessActivity();
                                    }
                                    SystemHandle.saveLongMessage(context, SystemHandle.LUCK_TIME, luckTime);
                                }
                            }
                        }

                    }
                });
    }

    private void logLuck(long luckTime) {
        MessageHandler.showToast(context, "luck time = " + luckTime + " ; save time = " + SystemHandle.getLong(context, SystemHandle.LUCK_TIME));

    }

    private void jumpLuckMessActivity() {
        PassagewayHandler.jumpActivity(context, LuckMessagActivity.class);
    }
}

