package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.dialog.ListDialog;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.views.SwitchView;
import com.easefun.polyvsdk.BitRateEnum;
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
 * Created by Hua on 17/3/16.
 */
public class SetingActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.setting_bitRateText)
    private TextView bitRateText;
    @ViewInject(R.id.setting_swithButton)
    private SwitchView swithButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.setting_aboutButton)
    public void onAbout(View view) {
        PassagewayHandler.jumpActivity(context, AboutUsActivity.class);
    }

    @OnClick(R.id.setting_clearButton)
    public void onClear(View view) {
        progress.setVisibility(View.VISIBLE);
        DownloadImageLoader.clearDiscCache();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
            }
        }, 3 * 1000);
    }

    @OnClick(R.id.setting_bitRateButton)
    public void onBitRate(View view) {
        showBitRateDialog();
    }

    @OnClick(R.id.setting_logoutButton)
    public void onLogout(View view) {
        if (User.isLogin(context)) {
            User.logout(context);
            PassagewayHandler.jumpToActivity(context, MainActivity.class);
        }
    }

    private void showBitRateDialog() {
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(VideoInfo.getBitRateArray());
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoInfo.saveBitRate(context, BitRateEnum.getBitRate(i));
                setBitRateText();
                dialog.dismiss();
            }
        });
    }

    private void initActivity() {
        titleText.setText("设置");
        setBitRateText();
        initSwith();
    }

    private void initSwith() {
        swithButton.setOnSwitchStateChangeListener(new SwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onStateSwitched(boolean isOn) {
                VideoInfo.saveAutoContinue(context, isOn);
            }
        });
//        swithButton.setOn(true);
        swithButton.setOn(VideoInfo.getAutoContinue(context));
    }

    private void setBitRateText() {
        bitRateText.setText(VideoInfo.getBitRate(context));
    }

}
