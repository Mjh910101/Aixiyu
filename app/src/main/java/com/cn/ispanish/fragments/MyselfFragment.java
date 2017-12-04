package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.CollectionListActivity;
import com.cn.ispanish.activitys.CourseFrozenListActivity;
import com.cn.ispanish.activitys.CourseListActivity;
import com.cn.ispanish.activitys.LoginActivity;
import com.cn.ispanish.activitys.OffLineActivity;
import com.cn.ispanish.activitys.PushHistoryListActivity;
import com.cn.ispanish.activitys.QuestionBankActivity;
import com.cn.ispanish.activitys.SetingActivity;
import com.cn.ispanish.activitys.UserCenterActivity;
import com.cn.ispanish.activitys.VideoHistoryListActivity;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForString;
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
 * Created by Hua on 17/3/6.
 */
public class MyselfFragment extends BaseFragment {

    @ViewInject(R.id.myself_uesrPic)
    private ImageView uesrPic;
    @ViewInject(R.id.myself_uesrNikeNameText)
    private TextView uesrNikeNameText;
    @ViewInject(R.id.myself_uesrMessageText)
    private TextView uesrMessageText;
    @ViewInject(R.id.myself_learnText)
    private TextView learnText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.myself_questionBankButton)
    private RelativeLayout questionBankButton;
    @ViewInject(R.id.myself_questionBankLine)
    private View questionBankLine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_myself,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initActivity();
        download();
        selectBalance();

        return contactsLayout;
    }

    private void selectBalance() {
        User.selectBalance(context, new CallbackForString() {
            @Override
            public void callback(String data) {
                learnText.setText(data);
                try {
                    double d = Double.valueOf(data);
//                    MessageHandler.showToast(context, "" + d);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.myself_setingButton)
    public void onSetting(View view) {
        PassagewayHandler.jumpActivity(context, SetingActivity.class);
    }

    @OnClick(R.id.myself_collectionButton)
    public void onCollection(View view) {
        PassagewayHandler.jumpActivity(context, CollectionListActivity.class);
    }

    @OnClick(R.id.myself_pushButton)
    public void onPush(View view) {
        PassagewayHandler.jumpActivity(context, PushHistoryListActivity.class);
    }

    @OnClick(R.id.myself_historyButton)
    public void onHistory(View view) {
        PassagewayHandler.jumpActivity(context, VideoHistoryListActivity.class);
    }

    @OnClick(R.id.myself_courseButton)
    public void onCourse(View view) {
//        PassagewayHandler.jumpActivity(context, CourseFrozenListActivity.class);
        PassagewayHandler.jumpActivity(context, CourseListActivity.class);
    }

    @OnClick(R.id.myself_questionBankButton)
    public void onQuestionBank(View view) {
        PassagewayHandler.jumpActivity(context, QuestionBankActivity.class);
    }

    @OnClick(R.id.myself_offLineButton)
    public void onOffLine(View view) {
        if (User.isLoginAndShowMessage(context)) {
            PassagewayHandler.jumpActivity(context, OffLineActivity.class);
        }
    }

    @OnClick({R.id.myself_uesrPic, R.id.myself_noLoginTextLayout})
    public void onLogin(View view) {
        if (User.isLogin(context)) {
            PassagewayHandler.jumpActivity(context, UserCenterActivity.class, UserCenterActivity.RequestCode);
        } else {
            PassagewayHandler.jumpActivity(context, LoginActivity.class, LoginActivity.RequestCode);
        }
    }

    private void initActivity() {
        initUserLayout();
    }

    private void initUserLayout() {
        if (User.isLogin(context)) {
            DownloadImageLoader.loadImage(uesrPic, User.getPortrait(context), WinHandler.dipToPx(context, 25));
            uesrNikeNameText.setText(User.getName(context));
            uesrMessageText.setText("个人信息 >");
        } else {
            DownloadImageLoader.loadImageForID(uesrPic, R.drawable.no_login_icon, WinHandler.dipToPx(context, 25));
            uesrNikeNameText.setText("游客模式");
            uesrMessageText.setText("请点击登录");
        }
    }

    public void uploadData() {
        initUserLayout();
        download();
        selectBalance();
    }

    private void download() {
//        progress.setVisibility(View.VISIBLE);
//
//        RequestParams params = HttpUtilsBox.getRequestParams();
//        params.addBodyParameter("set", "1");
//        params.addBodyParameter("key", User.getAppKey(context));
//
//        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBankdisPlay(), params,
//                new RequestCallBack<String>() {
//
//                    @Override
//                    public void onFailure(HttpException exception,
//                                          String msg) {
//                        progress.setVisibility(View.GONE);
//                        MessageHandler.showFailure(context);
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        String result = responseInfo.result;
//                        Log.d("", result);
//
//                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
//                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
//                            if (JsonHandle.getInt(json, "data") == 1) {
//                                questionBankButton.setVisibility(View.VISIBLE);
//                                questionBankLine.setVisibility(View.VISIBLE);
//                            } else {
//                                questionBankButton.setVisibility(View.GONE);
//                                questionBankLine.setVisibility(View.GONE);
//                            }
//                        }
//                        progress.setVisibility(View.GONE);
//                    }
//                });
    }
}
