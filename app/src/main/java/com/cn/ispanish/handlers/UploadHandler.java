package com.cn.ispanish.handlers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cn.ispanish.activitys.VersionActivity;
import com.cn.ispanish.box.User;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
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
 * Created by Hua on 17/9/5.
 */
public class UploadHandler {

    public static void checkUpload(final Context context) {

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getForceUpdata(context),
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
                            JSONObject dataJson = JsonHandle.getJSON(json, "data");
                            if (dataJson != null) {
                                jumpVersionActivity(context,
                                        JsonHandle.getInt(dataJson, "versionnum"),
                                        JsonHandle.getInt(dataJson, "isforce") == 1,
                                        JsonHandle.getString(dataJson, "forceinfo"),
                                        JsonHandle.getString(dataJson, "forceurl"));
                            }
                        }
                    }
                });

    }

    private static void jumpVersionActivity(Context context, int version, boolean versionShort, String changelog, String update_url) {
        if (SystemHandle.detectionVersion(context, version)) {

            Bundle b = new Bundle();
            b.putString("changelog", changelog);
            b.putString("update_url", update_url);
            b.putBoolean("must", versionShort);

            PassagewayHandler.jumpActivity(context, VersionActivity.class, VersionActivity.UPLOAD_REQUEST_CODE, b);
        }
    }

}
