package com.cn.ispanish.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.MainActivity;
import com.cn.ispanish.activitys.MainLodingActivity;
import com.cn.ispanish.activitys.VideoPlayListActivity;
import com.cn.ispanish.activitys.WebContentActivity;
import com.cn.ispanish.box.PushInfo;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.webview.MyWebViewClient;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

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
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE, "");
        String message = bundle.getString(JPushInterface.EXTRA_ALERT, "");
        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID, 0);
        JSONObject json = JsonHandle.getJSON(bundle.getString(JPushInterface.EXTRA_EXTRA));

        if (json != null) {
            try {
                PushInfo info = new PushInfo(json);
                showNotification(context, title, message, notifactionId, info);
                DBHandler.getDbUtils(context).saveOrUpdate(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey : " + key + ", value : " + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void showNotification(Context context, String title, String message, int id, PushInfo info) {
        int code = id;

        Intent notifyIntent = getNotificationIntent(context, info);

        PendingIntent pi = PendingIntent.getActivity(context, code, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title)
                .setContentText(message)
                .setTicker("")
                .setSmallIcon(R.mipmap.app_logo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pi);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(code, builder.build());
    }

    private Intent getNotificationIntent(Context context, PushInfo info) {
        return info.getJumpIntent(context);
    }

}
