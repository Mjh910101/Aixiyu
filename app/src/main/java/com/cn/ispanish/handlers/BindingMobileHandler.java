package com.cn.ispanish.handlers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.cn.ispanish.activitys.BindingMobileActivity;
import com.cn.ispanish.box.User;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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
 * Created by Hua on 17/7/11.
 */
public class BindingMobileHandler {

    public static void isBindingMobile(final Context context) {
        if (!User.isLogin(context)) {
            return;
        }

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getCheckuserPhone(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") != 1) {
                                jumpBindingMobile(context);
                            }
                        }
                    }
                });

    }

    public static void jumpBindingMobile(Context context) {
        PassagewayHandler.jumpActivity(context, BindingMobileActivity.class);

    }

}
