package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
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
 * Created by Hua on 17/8/28.
 */
public class GameResultActivity extends BaseActivity {

    public final static String SCORE = "score";
    public final static String TIME = "time";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.gameResult_bgView)
    private RelativeLayout bgView;
    @ViewInject(R.id.gameResult_imageView)
    private ImageView imageView;
    @ViewInject(R.id.gameResult_gameTitle)
    private TextView gameTitle;
    @ViewInject(R.id.gameResult_scoreText)
    private TextView scoreText;
    @ViewInject(R.id.gameResult_timeText)
    private TextView timeText;
    @ViewInject(R.id.gameResult_getCouponButton)
    private TextView couponButton;

    private int score;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.gameResult_getCouponButton)
    public void onCouponButton(View view) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getGetCoupon(context), params,
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

                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
                            MessageHandler.showToast(context, JsonHandle.getString(json, "data"));
                            finish();
                        } else {
                            MessageHandler.showException(context, json);
                        }

                    }
                });
    }

    private void initActivity() {
        titleText.setText("");
        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        score = b.getInt(SCORE);
        time = b.getLong(TIME);

        downloadData();
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getITestPaper(context), params,
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
                        JSONObject json = JsonHandle.getJSON(result);
                        setData(json);
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void setData(JSONObject json) {

        if (json == null) {
            return;
        }

        DownloadImageLoader.loadImage(imageView, "http://www.ispanish.cn/Public/Uploads/" + JsonHandle.getString(json, "image"));
        bgView.setBackgroundColor(ColorHandle.getColor(context, JsonHandle.getString(json, "bgcolors")));

        gameTitle.setText("感谢您参加了本次" + "\n" + JsonHandle.getString(json, "name"));
        scoreText.setText("得分：" + score + "分");
        timeText.setText("耗时：" + getTime(time));

        couponButton.setVisibility(View.VISIBLE);
    }

    private String getTime(long time) {
        StringBuilder sb = new StringBuilder();
        long h = time / 60;
        long m = time % 60;
        if (h < 10) {
            sb.append("0");
        }
        sb.append(h);
        sb.append("分");

        if (m < 10) {
            sb.append("0");
        }
        sb.append(m);
        sb.append("秒");

        return sb.toString();
    }


}
