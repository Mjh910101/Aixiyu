package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.CollectionListActivity;
import com.cn.ispanish.activitys.CourseListActivity;
import com.cn.ispanish.activitys.LoginActivity;
import com.cn.ispanish.activitys.PushHistoryListActivity;
import com.cn.ispanish.activitys.SetingActivity;
import com.cn.ispanish.activitys.UserCenterActivity;
import com.cn.ispanish.activitys.VideoHistoryListActivity;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_myself,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initActivity();

        return contactsLayout;
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
        PassagewayHandler.jumpActivity(context, CourseListActivity.class);
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
    }
}
