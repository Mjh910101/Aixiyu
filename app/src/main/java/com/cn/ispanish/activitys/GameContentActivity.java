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
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.views.TimeTextView;
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
 * Created by Hua on 17/6/13.
 */
public class GameContentActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.gameContent_imageView)
    private ImageView imageView;
    @ViewInject(R.id.gameContent_titleTopView)
    private TextView titleTopView;
    @ViewInject(R.id.gameContent_contentTopView)
    private TextView contentTopView;
    @ViewInject(R.id.gameContent_titleBottomView)
    private TextView titleBottomView;
    @ViewInject(R.id.gameContent_contentBottomView)
    private TextView contentBottomView;
    @ViewInject(R.id.gameContent_timeTitle)
    private TextView timeTitle;
    @ViewInject(R.id.gameContent_timeText)
    private TimeTextView timeText;
    @ViewInject(R.id.gameContent_bgView)
    private RelativeLayout bgView;

    private String id, title;
    private boolean isStart, isWork;
    private int kstime, startType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_cntent);

        ViewUtils.inject(this);

        initActivity();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        downloadData();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.gameContent_startButton)
    public void onStart(View view) {
        if (!User.isLoginAndShowMessage(context)) {
            return;
        }
        if (isWork) {
            MessageHandler.showToast(context, "您已经参与过了~");
            jumpRankingActivity();
            return;
        }
        switch (startType) {
            case 2:
                MessageHandler.showToast(context, "结束了");
                jumpRankingActivity();
                return;
            case 3:
                MessageHandler.showToast(context, "还没开始");
                return;
            case 0:
                MessageHandler.showToast(context, "出现异常了");
                return;
        }
        showStartDialog();
    }

    private void jumpRankingActivity() {
        PassagewayHandler.jumpActivity(context, MyselfRankingActivity.class);
        finish();
    }

    private void showStartDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("您准备好参加“" + title + "”");
        dialog.setMessage("比赛时长：" + kstime + "分钟");
        dialog.setCommitStyle("马上参加");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                Bundle b = new Bundle();
                b.putInt(GamePaperActivity.TIME_KEY, kstime);
                b.putString(GamePaperActivity.ID_KEY, id);
                b.putString(GamePaperActivity.TITLE_KEY, title);
                PassagewayHandler.jumpActivity(context, GamePaperActivity.class, b);
                finish();
            }
        });
        dialog.setCancelStyle("再等等");
        dialog.setCancelListener(null);
    }

    private void initActivity() {
        titleText.setText("有奖竞赛");

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

        id = JsonHandle.getString(json, "id");
        title = JsonHandle.getString(json, "name");
        startType = JsonHandle.getInt(json, "going");
        kstime = JsonHandle.getInt(json, "kstime");
        isWork = JsonHandle.getInt(json, "working") == 1;

        DownloadImageLoader.loadImage(imageView, "http://www.ispanish.cn/Public/Uploads/" + JsonHandle.getString(json, "image"));

        titleTopView.setText(JsonHandle.getString(json, "titone"));
        contentTopView.setText(JsonHandle.getString(json, "contenone"));
        titleBottomView.setText(JsonHandle.getString(json, "tittwo"));
        contentBottomView.setText(JsonHandle.getString(json, "contentwo"));

        bgView.setBackgroundColor(ColorHandle.getColor(context, JsonHandle.getString(json, "bgcolors")));


        long statsTime = JsonHandle.getLong(json, "statstime");
        long endTime = JsonHandle.getLong(json, "endtime");
        long nowTime = DateHandle.getTime();
        switch (startType) {
            case 1:
                timeTitle.setText("距离活动结束还有");
                timeText.setTimes(DateHandle.getTime(endTime - nowTime));
                if (!timeText.isRun()) {
                    timeText.run();
                }
                break;
            case 2:
                timeTitle.setText("本活动已结束" + "\n" + "敬请留意下一场比赛");
                return;
            case 3:
                timeTitle.setText("距离活动开始还有");
                timeText.setTimes(DateHandle.getTime(statsTime - nowTime));
                if (!timeText.isRun()) {
                    timeText.run();
                }
                return;
        }
        timeText.setEndCallback(new CallbackForBoolean() {
            @Override
            public void callback(boolean b) {
                if (b) {
                    downloadData();
                }
            }
        });
    }
}
