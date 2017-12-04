package com.cn.ispanish.handlers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.dialog.InputDialog;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForString;
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
 * Created by Hua on 17/8/10.
 */
public class FeelBackHandler {

    public static void showFeelBackDialog(final Context context, final Question question) {
        InputDialog dialog = new InputDialog(context);
        dialog.setTitle("问题反馈");
        dialog.setListener(new CallbackForString() {
            @Override
            public void callback(String result) {
//                MessageHandler.showToast(context, question.getId() + " : " + result);
                if (result.equals("")) {
                    MessageHandler.showToast(context,"写点什么！？");
                    return;
                }
                uploadFeelBack(context, question.getId(), result);
            }
        });
    }

    private static void uploadFeelBack(final Context context, String id, final String content) {
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("tid", id);
        params.addBodyParameter("conten", content);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getFeedBack(context), params,
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
                            if (JsonHandle.getInt(json, "code") == 1) {
//                                MessageHandler.showToast(context, "问题反馈成功");
                                MessageHandler.showToast(context, "谢谢小天使，您的反馈我们已经收到，技术哥正在没日没夜加班修复中，敬请期待~");
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }

                    }
                });
    }
}
