package com.cn.ispanish.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.interfaces.CallbackForString;
import com.cn.ispanish.webview.MyWebChromeClient;
import com.cn.ispanish.webview.MyWebViewClient;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

public class WebContentActivity extends Activity {

    private Context context;

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.web_contentWeb)
    private WebView contentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);
        context = this;
        ViewUtils.inject(this);

        initAcitvity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick(R.id.title_backIcon)
    public void onBlick(View view) {
        finish();
    }

    private void initAcitvity() {

        Bundle b = getIntent().getExtras();
        if (b != null) {
            contentWeb.setWebViewClient(new MyWebViewClient(context));
            contentWeb.setWebChromeClient(new MyWebChromeClient(
                    new CallbackForString() {
                        @Override
                        public void callback(String result) {
//                            titleText.setText(result);
                        }
                    }));

            contentWeb.getSettings().setJavaScriptEnabled(true);
            contentWeb.getSettings().setUseWideViewPort(true);
            contentWeb.getSettings().setLoadWithOverviewMode(true);

            titleText.setText(b.getString(MyWebViewClient.TITLE));
            String url = b.getString(MyWebViewClient.KEY);
            Log.e("web", "URL : " + url);
            if (url.contains("tencent://")) {
                jumpQQ(url);
            } else {
                contentWeb.loadUrl(url);
            }
        } else {
            finish();
        }

    }

    private void jumpQQ(String url) {
        boolean isCan = false;
        try {
            String str = url.substring(url.indexOf("uin=") + 4, url.length());
            String uin = str.substring(0, str.indexOf("&"));
            Log.e("", "uin = " + uin);
            if (isQQClientAvailable(context)) {
                isCan = true;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + uin)));
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (!isCan) {
            MessageHandler.showToast(context, "您的QQ版本过低或您当前未安装QQ,请安装最新版QQ后再试");
        }
        finish();
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
