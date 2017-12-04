package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.BaseAdapter;

import com.cn.ispanish.R;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.SystemHandle;
import com.cn.ispanish.views.SwitchView;
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
 * Created by Hua on 2017/11/7.
 */

public class HiddenSettingActivity extends BaseActivity {


    @ViewInject(R.id.setting_cutButton)
    private SwitchView cutButton;
    @ViewInject(R.id.setting_debugButton)
    private SwitchView debugButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_seting);

        ViewUtils.inject(this);

        initActivity();
    }

    private void initActivity() {
        cutButton.setOnSwitchStateChangeListener(new SwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onStateSwitched(boolean isOn) {
                SystemHandle.saveIsFlagSecure(context, isOn);
            }
        });
        cutButton.setOn(SystemHandle.isFlagSecure(context));

        debugButton.setOnSwitchStateChangeListener(new SwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onStateSwitched(boolean isOn) {
                SystemHandle.setDebug(context, isOn);
            }
        });
        debugButton.setOn(SystemHandle.isDubug(context));
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    private void close() {
        PassagewayHandler.jumpToActivity(context, MainActivity.class);
    }

}
