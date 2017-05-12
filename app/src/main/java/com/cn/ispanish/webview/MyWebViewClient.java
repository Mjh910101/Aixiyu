package com.cn.ispanish.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cn.ispanish.activitys.WebContentActivity;
import com.cn.ispanish.handlers.PassagewayHandler;


public class MyWebViewClient extends WebViewClient {

    public final static String KEY = "URL";
    public final static String TITLE = "title";
    public final static String COLOR = "COLOR";

    private Context context;
    private int color;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // view.loadUrl(url);// 如果不需要其他对点击链接事件的处理返回true，否则返回false
        Bundle b = new Bundle();
        b.putString(KEY, url);
        b.putString(TITLE, "");
        PassagewayHandler.jumpActivity(context, Intent.FLAG_ACTIVITY_CLEAR_TOP, WebContentActivity.class, b);

        return true;

    }
}
