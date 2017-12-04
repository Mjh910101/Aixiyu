package com.cn.ispanish.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admaster.sdk.api.AdmasterSdk;
import com.cn.ispanish.R;
import com.cn.ispanish.box.Live;
import com.cn.ispanish.box.LivePlayBack;
import com.cn.ispanish.box.User;
import com.cn.ispanish.fragments.LiveRoomFragment;
import com.cn.ispanish.fragments.PlayBackFragment;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.live.AdmasterSdkUtils;
import com.cn.ispanish.live.PolyvChatFragment;
import com.cn.ispanish.live.PolyvDanmuFragment;
import com.cn.ispanish.live.PolyvPlayerLightView;
import com.cn.ispanish.live.PolyvPlayerMediaController;
import com.cn.ispanish.live.PolyvPlayerVolumeView;
import com.cn.ispanish.live.PolyvScreenUtils;
import com.easefun.polyvsdk.live.PolyvLiveSDKUtil;
import com.easefun.polyvsdk.live.video.PolyvLiveMediaInfoType;
import com.easefun.polyvsdk.live.video.PolyvLivePlayErrorReason;
import com.easefun.polyvsdk.live.video.PolyvLiveVideoView;
import com.easefun.polyvsdk.live.video.PolyvLiveVideoViewListener;
import com.easefun.polyvsdk.live.video.auxiliary.PolyvLiveAuxiliaryVideoView;
import com.easefun.polyvsdk.live.vo.PolyvLiveChannelVO;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Hua on 2017/9/25.
 */

public class LivePlayBackActivity extends BaseActivity {

    public final static String CID_KEY = "cid";
    public final static String LIVE_KEY = "live";

    private static final String TAG = "";
    private PolyvChatFragment polyvChatFragment;
    private LiveRoomFragment liveRoomFragment;
    private PlayBackFragment playbackFragment;
    private PolyvDanmuFragment danmuFragment;
    private String uid = "d8e5a03063", cid = "130113";

    @ViewInject(R.id.liveRoon_viewLayout)
    private RelativeLayout viewLayout;
    @ViewInject(R.id.liveRoon_liveVideoView)
    private PolyvLiveVideoView videoView;//播放器主类
    @ViewInject(R.id.liveRoon_playerMediaController)
    private PolyvPlayerMediaController mediaController;//播放器控制栏
    @ViewInject(R.id.liveRoon_loadingProgress)
    private ProgressBar loadingProgress;
    @ViewInject(R.id.liveRoon_noStream)
    private ImageView noStream;
    @ViewInject(R.id.liveRoon_liveAuxiliaryVideoView)
    private PolyvLiveAuxiliaryVideoView auxiliaryVideoView;//辅助播放器类，用于播放视频片头广告
    @ViewInject(R.id.liveRoon_auxiliaryLoadingProgress)
    private ProgressBar auxiliaryLoadingProgress;
    @ViewInject(R.id.liveRoon_playerLightView)
    private PolyvPlayerLightView lightView;//手势亮度指示标志
//    @ViewInject(R.id.liveRoon_playerVolumeView)
//    private PolyvPlayerVolumeView volumeView;//手势音量指示标志
    @ViewInject(R.id.liveRoon_countDown)
    private TextView advertCountDown;//用于显示广告倒计时
    @ViewInject(R.id.liveRoomContent_dataText)
    private TextView dataText;
    @ViewInject(R.id.liveRoomContent_imText)
    private TextView imText;
    @ViewInject(R.id.liveRoomContent_oldText)
    private TextView oldText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private boolean isPlay = false;
    private FragmentManager fragmentManager;

    private List<LivePlayBack> playBackList;
    private User teacher;

    private Live live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        ViewUtils.inject(this);


        initActivity();
    }

    private void initActivity() {

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        live = (Live) b.getSerializable(LIVE_KEY);
        cid = live.getChannel();

//        MessageHandler.showToast(context, live.getTitle());

        fragmentManager = getFragmentManager();
        // 初始化广告监测器
        AdmasterSdk.init(getApplicationContext(), "");

        findIdAndNew();
        initView();

        downloadData(live.getCid());
    }

    private void findIdAndNew() {
//        mediaController.initConfig(viewLayout);
        mediaController.setDanmuFragment(danmuFragment);
        videoView.setMediaController(mediaController);
        videoView.setAuxiliaryVideoView(auxiliaryVideoView);
        videoView.setPlayerBufferingIndicator(loadingProgress);
        videoView.setNoStreamIndicator(noStream);
        auxiliaryVideoView.setPlayerBufferingIndicator(auxiliaryLoadingProgress);
    }

    private void initView() {
        videoView.setOpenAd(true);
        videoView.setOpenPreload(true, 2);
        videoView.setNeedGestureDetector(true);

        videoView.setOnPreparedListener(new PolyvLiveVideoViewListener.OnPreparedListener() {
            @Override
            public void onPrepared() {
                Toast.makeText(context, "准备完毕，可以播放", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setOnInfoListener(new PolyvLiveVideoViewListener.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case PolyvLiveMediaInfoType.MEDIA_INFO_BUFFERING_START:
                        Toast.makeText(context, "开始缓冲", Toast.LENGTH_SHORT).show();
                        break;

                    case PolyvLiveMediaInfoType.MEDIA_INFO_BUFFERING_END:
                        Toast.makeText(context, "结束缓冲", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        videoView.setOnVideoPlayErrorListener(new PolyvLiveVideoViewListener.OnVideoPlayErrorListener() {
            @Override
            public void onVideoPlayError(@NonNull PolyvLivePlayErrorReason errorReason) {
                switch (errorReason.getType()) {
                    case NETWORK_DENIED:
                        Toast.makeText(context, "无法连接网络，请连接网络后播放", Toast.LENGTH_SHORT).show();
                        break;

                    case START_ERROR:
                        Toast.makeText(context, "播放错误，请重新播放(error code " + PolyvLivePlayErrorReason.ErrorType.START_ERROR.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;

                    case CHANNEL_NULL:
                        Toast.makeText(context, "频道信息获取失败，请重新播放(error code " + PolyvLivePlayErrorReason.ErrorType.CHANNEL_NULL.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;

                    case LIVE_UID_NOT_EQUAL:
                        Toast.makeText(context, "用户id错误，请重新设置(error code" + PolyvLivePlayErrorReason.ErrorType.LIVE_UID_NOT_EQUAL.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;

                    case LIVE_CID_NOT_EQUAL:
                        Toast.makeText(context, "频道号错误，请重新设置(error code " + PolyvLivePlayErrorReason.ErrorType.LIVE_CID_NOT_EQUAL.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;

                    case LIVE_PLAY_ERROR:
                        Toast.makeText(context, "播放错误，请稍后重试(error code " + PolyvLivePlayErrorReason.ErrorType.LIVE_PLAY_ERROR.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;

                    case RESTRICT_NULL:
                        Toast.makeText(context, "限制信息错误，请稍后重试(error code " + PolyvLivePlayErrorReason.ErrorType.RESTRICT_NULL.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;

                    case RESTRICT_ERROR:
                        Toast.makeText(context, errorReason.getErrorMsg() + "(error code " + PolyvLivePlayErrorReason.ErrorType.RESTRICT_ERROR.getCode() + ")", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        videoView.setOnAdvertisementOutListener(new PolyvLiveVideoViewListener.OnAdvertisementOutListener() {
            @Override
            public void onOut(@NonNull PolyvLiveChannelVO.ADMatter adMatter) {
            }

            @Override
            public void onClick(@NonNull PolyvLiveChannelVO.ADMatter adMatter) {
                // 发送广告点击监测
                AdmasterSdkUtils.sendAdvertMonitor(adMatter, AdmasterSdkUtils.MONITOR_CLICK);
                if (!TextUtils.isEmpty(adMatter.getAddrUrl())) {
                    try {
                        new URL(adMatter.getAddrUrl());
                    } catch (MalformedURLException e1) {
                        Log.e(TAG, PolyvLiveSDKUtil.getExceptionFullMessage(e1, -1));
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(adMatter.getAddrUrl()));
                    startActivity(intent);
                }
            }
        });

        videoView.setOnAdvertisementCountDownListener(new PolyvLiveVideoViewListener.OnAdvertisementCountDownListener() {
            @Override
            public void onCountDown(int num) {
                advertCountDown.setText(String.format("广告也精彩：%d秒", num));
                advertCountDown.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(@NonNull PolyvLiveChannelVO.ADMatter adMatter) {
                advertCountDown.setVisibility(View.GONE);
                // 发送广告曝光监测
                AdmasterSdkUtils.sendAdvertMonitor(adMatter, AdmasterSdkUtils.MONITOR_SHOW);
            }
        });

        videoView.setOnGestureLeftUpListener(new PolyvLiveVideoViewListener.OnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                videoView.setBrightness(brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureLeftDownListener(new PolyvLiveVideoViewListener.OnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                videoView.setBrightness(brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureRightUpListener(new PolyvLiveVideoViewListener.OnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                videoView.setVolume(volume);
//                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureRightDownListener(new PolyvLiveVideoViewListener.OnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                videoView.setVolume(volume);
//                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setLivePlay(uid, cid, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            videoView.onActivityResume();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PolyvScreenUtils.isLandscape(this)) {
                mediaController.changeToPortrait();
                return true;
            }
            if (polyvChatFragment != null) {
                if (PolyvScreenUtils.isPortrait(this) && polyvChatFragment.emoLayoutIsVisible()) {
                    polyvChatFragment.resetEmoLayout(true);
                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
        lightView.hide();
//        volumeView.hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //弹出去暂停
        isPlay = videoView.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.destroy();
        // 关闭广告监测器
        AdmasterSdk.terminateSDK();
    }

    @OnClick({R.id.liveRoomContent_dataButton, R.id.liveRoomContent_imButton, R.id.liveRoomContent_oldButton})
    public void onButton(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initButton();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.liveRoomContent_dataButton:
                onData(transaction);
                break;
            case R.id.liveRoomContent_imButton:
                onIm(transaction);
                break;
            case R.id.liveRoomContent_oldButton:
                onOld(transaction);
                break;
        }
        transaction.commit();
    }

    private void onOld(FragmentTransaction transaction) {
        oldText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        if (playbackFragment == null) {

            playbackFragment = new PlayBackFragment(teacher, playBackList);
            transaction.add(R.id.liveRoomContent_contentLayout, playbackFragment);
        } else {
            transaction.show(playbackFragment);
        }
    }

    private void onData(FragmentTransaction transaction) {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        if (liveRoomFragment == null) {

            liveRoomFragment = new LiveRoomFragment(live);
            transaction.add(R.id.liveRoomContent_contentLayout, liveRoomFragment);
        } else {
            transaction.show(liveRoomFragment);
        }
    }

    private void onIm(FragmentTransaction transaction) {
        imText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        if (polyvChatFragment == null) {

            polyvChatFragment = new PolyvChatFragment();
            polyvChatFragment.initChatConfig(uid, cid, User.getName(context), User.getPortrait(context),teacher);
            transaction.add(R.id.liveRoomContent_contentLayout, polyvChatFragment);
        } else {
            transaction.show(polyvChatFragment);
        }
    }

    private void initButton() {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        imText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        oldText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (polyvChatFragment != null) {
            transaction.hide(polyvChatFragment);
        }
        if (liveRoomFragment != null) {
            transaction.hide(liveRoomFragment);
        }
        if (playbackFragment != null) {
            transaction.hide(playbackFragment);
        }

    }

    private void downloadData(String cid) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
//        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("liveid", cid);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getLiveRoom(context), params,
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

                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                JSONObject dataJson = JsonHandle.getJSON(json, "data");
                                setLiveData(dataJson);
                                setPlayBack(dataJson);
                                setTeacher(dataJson);
                            }
                        }
                    }
                });
    }

    private void setTeacher(JSONObject dataJson) {
        JSONObject json = JsonHandle.getJSON(dataJson, "teacherinfo");
        if (json == null) {
            return;
        }
        teacher = new User(json);
        teacher.setId(JsonHandle.getString(json, "tid"));
    }

    private void setPlayBack(JSONObject dataJson) {
        playBackList = new ArrayList<>();
        if (dataJson == null) {
            return;
        }

        JSONArray array = JsonHandle.getArray(dataJson, "hblist");
        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {
            LivePlayBack obj = new LivePlayBack(JsonHandle.getJSON(array, i));
            playBackList.add(obj);
        }
    }

    private void setLiveData(JSONObject dataJson) {
        if (dataJson == null) {
            return;
        }

        live.setContent(dataJson);
        live.setTid(dataJson);

        onButton(findViewById(R.id.liveRoomContent_dataButton));
    }
}
