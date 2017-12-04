package com.cn.ispanish.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.BitmapHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import mlxy.utils.T;

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
 * Created by Hua on 17/9/4.
 */
public class SharePaperActivity extends BaseActivity {

    public final static String TITLE_NAME = "title";
    //    public final static String TIME = "time";
    public final static String TIME_LONG = "time_long";
    public final static String AVERAGER_TEXT = "averageText";
    public final static String AVERAGER_NUM = "averageNum";
    public final static String MATCH_ID = "matchId";
    public final static int RequestCode = 1122001;
    public final static String Request_TEXT = "request";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.title_toolIcon)
    private ImageView toolIcon;
    @ViewInject(R.id.sharePaper_shareImage)
    private RelativeLayout shareImage;
    @ViewInject(R.id.sharePaper_uesrPic)
    private ImageView uesrPic;
    @ViewInject(R.id.sharePaper_uesrName)
    private TextView uesrName;
    @ViewInject(R.id.sharePaper_paperTitleText)
    private TextView paperTitleText;
    @ViewInject(R.id.sharePaper_averageText)
    private TextView averageText;
    @ViewInject(R.id.sharePaper_shareButton)
    private TextView shareButton;
    @ViewInject(R.id.sharePaper_paperTimeText)
    private TextView paperTimeText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private String matchId, title, averageNum;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_paper);

        ViewUtils.inject(this);

        initActivity();
    }


    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick({R.id.sharePaper_shareButton, R.id.title_toolIcon})
    public void onShare(View view) {
//        averageText.setVisibility(View.VISIBLE);
//        shareButton.setVisibility(View.GONE);
//        Bitmap image = BitmapHandler.getViewBitmap(shareImage);

        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Wechat.SHARE_WEBPAGE);
        sp.setTitle("震惊！我居然得了这点分，你敢来挑战我吗？");
        sp.setText("爱西语搞事情你真的不来看看吗？");
        sp.setUrl("http://www.ispanish.cn/index.php/Home/Api/sharesel?sign=" + User.getAppKey(context) + "&matid=" + matchId);
        sp.setImageData(BitmapHandler.getLogo(context));

        Platform wcshat = ShareSDK.getPlatform(WechatMoments.NAME);
        wcshat.share(sp);
        wcshat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                MessageHandler.showToast(context, "分享成功");
//                averageText.setVisibility(View.VISIBLE);
//                shareButton.setVisibility(View.GONE);
                showCompleteDialog();
                uploadData();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                averageText.setVisibility(View.GONE);
//                shareButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                averageText.setVisibility(View.GONE);
//                shareButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showCompleteDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("分享成功");
        dialog.setCommitStyle("查看成绩");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
//                close();
                juzmpRankingActivity();
            }
        });
    }

    private void juzmpRankingActivity() {
        PassagewayHandler.jumpToActivity(context, CompetitionRankingActivity.class);
    }

    private void uploadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("matchid", matchId);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getSvaematShare(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
//                                close();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "error"));
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }


                });
    }

    private void close() {
        Bundle b = new Bundle();
        b.putBoolean(Request_TEXT, true);

        Intent i = new Intent();
        i.putExtras(b);

        setResult(RequestCode, i);
        finish();
    }

    private void initActivity() {
        titleText.setText("有奖竞猜");

        toolIcon.setImageResource(R.drawable.tool_share_icon);
        toolIcon.setVisibility(View.VISIBLE);

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        time = b.getLong(TIME_LONG);
        title = b.getString(TITLE_NAME);
        averageNum = b.getString(AVERAGER_NUM);
        matchId = b.getString(MATCH_ID);

        Log.e("", "matchId : " + matchId);

        uesrName.setText(User.getName(context) + "，恭喜你");
        DownloadImageLoader.loadImage(uesrPic, User.getPortrait(context), WinHandler.dipToPx(context, 30));

        averageText.setText(Html.fromHtml("<font color=\"#333333\">" + "总分：" + "</font>" +
                "<font color=\"#F9AD58\">" + "？" + "</font>" +
                "<font color=\"#333333\">" + "分" + "</font>"));

        paperTimeText.setText(Html.fromHtml("<font color=\"#333333\">" + "用时：" + "</font>" + getTimeInHtml()));

        paperTitleText.setText(Html.fromHtml("<font color=\"#333333\">" + "您于" + "</font>" +
                "<b>" + "<font color=\"#000000\">" + title + "</font>" + "</b>" +
                "<font color=\"#333333\">" + "中" + "</font>" +
                "<br>" +
                "<font color=\"#333333\">" + "获得" + "</font>" +
                "<b>" + "<font color=\"#3685D6\">" + b.getString(AVERAGER_TEXT) + "</font>" + "</b>" +
                "<font color=\"#333333\">" + "称号" + "</font>"));
    }

    public String getTimeInHtml() {
        StringBuilder sb = new StringBuilder();

        int time_m = (int) (time / 60);
        int time_s = (int) (time % 60);

        sb.append("<font color=\"#F9AD58\">");
        sb.append(time_m);
        sb.append("</font>");

        sb.append("<font color=\"#333333\">");
        sb.append("分");
        sb.append("</font>");

        sb.append("<font color=\"#F9AD58\">");
        if (time_s < 10) {
            sb.append("0");
        }
        sb.append(time_s);
        sb.append("</font>");

        sb.append("<font color=\"#333333\">");
        sb.append("秒");
        sb.append("</font>");
        return sb.toString();

    }
}
