package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.Live;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.SystemHandle;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.views.TimeTextView;
import com.cn.ispanish.views.VestrewWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 2017/9/28.
 */

public class LiveRoomFragment extends BaseFragment {

    @ViewInject(R.id.liveRoom_contentView)
    private VestrewWebView contentView;
    @ViewInject(R.id.liveRoom_timeText)
    private TimeTextView timeText;
    @ViewInject(R.id.liveRoom_toolButtoon)
    private TextView toolButtoon;

    Live live;
    CallbackForBoolean callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_live_room,
                container, false);
        ViewUtils.inject(this, contactsLayout);
        initData();
        return contactsLayout;
    }

    private void initData() {
        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setWebChromeClient(new WebChromeClient());
        contentView.setFocusable(false);
        contentView.loadData("http://www.ispanish.cn", live.getContent());

        switch (live.getStatus()) {
            case 2:
                timeText.setTimes(DateHandle.getTime((live.getTime() / 1000) - (DateHandle.getTime())));
                if (!timeText.isRun()) {
                    timeText.run();
                }
                timeText.setEndCallback(new CallbackForBoolean() {
                    @Override
                    public void callback(boolean b) {
                        if(b){
                            timeText.setText("直播中");
                            toolButtoon.setText("直播中");
                            timeText.setRun(false);
                            callback.callback(b);
                        }
                    }
                });
                break;
            case 3:
                timeText.setText("直播中");
                toolButtoon.setText("直播中");
                break;
            default:
                timeText.setText("已结束");
                toolButtoon.setText("已结束");
                break;

        }
    }

    public LiveRoomFragment(Live live) {
        this.live = live;
    }

    public void setTimeCallback(final CallbackForBoolean callback) {
        this.callback = callback;
    }

}
