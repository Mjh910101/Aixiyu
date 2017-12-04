package com.cn.ispanish.activitys;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.fragments.LiveRoomFragment;
import com.cn.ispanish.fragments.PlayBackFragment;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.live.AdmasterSdkUtils;
import com.cn.ispanish.live.PolyvChatFragment;
import com.cn.ispanish.live.PolyvDanmuFragment;
import com.cn.ispanish.live.PolyvPlayerLightView;
import com.cn.ispanish.live.PolyvPlayerMediaController;
import com.cn.ispanish.live.PolyvPlayerVolumeView;
import com.cn.ispanish.live.PolyvScreenUtils;
import com.cn.ispanish.video.MediaController;
import com.cn.ispanish.video.VideoViewContainer;
import com.cn.ispanish.views.TimeTextView;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.easefun.polyvsdk.live.PolyvLiveSDKUtil;
import com.easefun.polyvsdk.live.video.PolyvLiveMediaInfoType;
import com.easefun.polyvsdk.live.video.PolyvLivePlayErrorReason;
import com.easefun.polyvsdk.live.video.PolyvLiveVideoView;
import com.easefun.polyvsdk.live.video.PolyvLiveVideoViewListener;
import com.easefun.polyvsdk.live.video.auxiliary.PolyvLiveAuxiliaryVideoView;
import com.easefun.polyvsdk.live.vo.PolyvLiveChannelVO;
import com.easefun.polyvsdk.util.ScreenTool;
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

import master.flame.danmaku.controller.IDanmakuView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

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

public class LiveRoomActivity extends BaseActivity {

    public final static String CID_KEY = "cid";
    public final static String LIVE_KEY = "live";

    private static final String TAG = "";
    private PolyvChatFragment polyvChatFragment;
    private LiveRoomFragment liveRoomFragment;
    private PlayBackFragment playbackFragment;
    private PolyvDanmuFragment danmuFragment;
    private String uid = "d8e5a03063", cid = "130113";

    @ViewInject(R.id.liveRoon_videoViewLayout)
    private RelativeLayout videoViewLayout;
    @ViewInject(R.id.liveRoon_viewLayout)
    private RelativeLayout liveLayout;
    @ViewInject(R.id.liveRoon_liveVideoView)
    private PolyvLiveVideoView liveVideoView;//播放器主类
    @ViewInject(R.id.liveRoon_playerMediaController)
    private PolyvPlayerMediaController liveMediaController;//播放器控制栏
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


    @ViewInject(R.id.liveRoon_contentLayout)
    private RelativeLayout contentLayout;

    @ViewInject(R.id.liveRoon_videoLayout)
    private VideoViewContainer videoLayout;
    @ViewInject(R.id.liveRoon_videoView)
    private IjkVideoView videoView;
    @ViewInject(R.id.liveRoon_danmaku)
    private IDanmakuView mDanmakuView;

    private MediaController mediaController = null;

    private boolean startNow = true;
    private boolean isPlay = false;
    private FragmentManager fragmentManager;

    private List<LivePlayBack> playBackList;
    private User teacher;

    private Live live;

    private int fastForwardPos = 0;

    int w = 0, h = 0, adjusted_h = 0;


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
        initPlayBack();

        switch (live.getStatus()) {
            case 2:
                startTime(live.getTime());
//                break;
            case 3:
                setLiveid(cid);
                break;
            default:
                setVid(live.getLiveVid());
                break;
        }

        downloadData(live.getCid());
    }

    private void startTime(long time) {
        DownloadImageLoader.loadImage(noStream, live.getImgindex());
        noStream.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.GONE);
    }

    private void initPlayBack() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        int[] wh = ScreenTool.getNormalWH(this);
        w = wh[0];
        h = wh[1];
        // 小窗口的比例
        float ratio = (float) 16 / 9;
        adjusted_h = (int) Math.ceil((float) w / ratio);
        videoLayout.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));
        initVideo();
    }

    private void findIdAndNew() {
        liveMediaController.initConfig(liveLayout, videoViewLayout);
        liveMediaController.setDanmuFragment(danmuFragment);
        liveVideoView.setMediaController(liveMediaController);
        liveVideoView.setAuxiliaryVideoView(auxiliaryVideoView);
        liveVideoView.setPlayerBufferingIndicator(loadingProgress);
        liveVideoView.setNoStreamIndicator(noStream);
        auxiliaryVideoView.setPlayerBufferingIndicator(auxiliaryLoadingProgress);
    }

    private void initView() {
        liveVideoView.setOpenAd(true);
        liveVideoView.setOpenPreload(true, 2);
        liveVideoView.setNeedGestureDetector(true);

        liveVideoView.setOnPreparedListener(new PolyvLiveVideoViewListener.OnPreparedListener() {
            @Override
            public void onPrepared() {
                Toast.makeText(context, "准备完毕，可以播放", Toast.LENGTH_SHORT).show();
            }
        });

        liveVideoView.setOnInfoListener(new PolyvLiveVideoViewListener.OnInfoListener() {
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

        liveVideoView.setOnVideoPlayErrorListener(new PolyvLiveVideoViewListener.OnVideoPlayErrorListener() {
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


        liveVideoView.setOnAdvertisementOutListener(new PolyvLiveVideoViewListener.OnAdvertisementOutListener() {
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

        liveVideoView.setOnAdvertisementCountDownListener(new PolyvLiveVideoViewListener.OnAdvertisementCountDownListener() {
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

        liveVideoView.setOnGestureLeftUpListener(new PolyvLiveVideoViewListener.OnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, liveVideoView.getBrightness()));
                int brightness = liveVideoView.getBrightness() + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                liveVideoView.setBrightness(brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        liveVideoView.setOnGestureLeftDownListener(new PolyvLiveVideoViewListener.OnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, liveVideoView.getBrightness()));
                int brightness = liveVideoView.getBrightness() - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                liveVideoView.setBrightness(brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        liveVideoView.setOnGestureRightUpListener(new PolyvLiveVideoViewListener.OnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, liveVideoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = liveVideoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                liveVideoView.setVolume(volume);
//                volumeView.setViewVolumeValue(volume, end);
            }
        });

        liveVideoView.setOnGestureRightDownListener(new PolyvLiveVideoViewListener.OnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, liveVideoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = liveVideoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                liveVideoView.setVolume(volume);
//                volumeView.setViewVolumeValue(volume, end);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            liveVideoView.onActivityResume();
        }

        // 恢复弹幕
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused() && videoView != null
                && videoView.isPlaying())
            mDanmakuView.resume();

        if (videoView != null && videoView.isPausState()) {
            videoView.start();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PolyvScreenUtils.isLandscape(this)) {
                liveMediaController.changeToPortrait();
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
        isPlay = liveVideoView.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        liveVideoView.destroy();
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
            playbackFragment.setOnClick(new PlayBackFragment.OnClickCallback() {
                @Override
                public void callback(String vid) {
//                    liveVideoView.destroy();
//                    setVid(vid);

                    Live l = Live.copy(live);
                    l.setStatus(1);
                    l.setLiveVid(vid);

                    Bundle b = new Bundle();
                    b.putSerializable(LiveRoomActivity.LIVE_KEY, l);
                    PassagewayHandler.jumpToActivity(context, LiveRoomActivity.class, b);

                }
            });
            transaction.add(R.id.liveRoomContent_contentLayout, playbackFragment);
        } else {
            transaction.show(playbackFragment);
        }
    }

    private void onData(FragmentTransaction transaction) {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        if (liveRoomFragment == null) {
            liveRoomFragment = new LiveRoomFragment(live);
            liveRoomFragment.setTimeCallback(new CallbackForBoolean() {
                @Override
                public void callback(boolean b) {
                    if (b) {
                        noStream.setVisibility(View.GONE);
                        setLiveid(cid);
                    }
                }
            });
            transaction.add(R.id.liveRoomContent_contentLayout, liveRoomFragment);
        } else {
            transaction.show(liveRoomFragment);
        }
    }

    private void onIm(FragmentTransaction transaction) {
        imText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        if (polyvChatFragment == null) {

            polyvChatFragment = new PolyvChatFragment();
//            polyvChatFragment.initChatConfig(uid, cid, User.getName(context), User.getPortrait(context), teacher);
            polyvChatFragment.initChatConfig(User.getUserId(context), cid, User.getName(context), User.getPortrait(context), teacher);
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

    private void initVideo() {
        videoView.setMediaBufferingIndicator(progress);
        videoView.setOpenTeaser(true);
        videoView.setOpenAd(false);
        videoView.setOpenQuestion(false);
        videoView.setOpenSRT(true);
        videoView.setNeedGestureDetector(true);
        videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
        videoView.setAutoContinue(VideoInfo.getAutoContinue(context));//是否继续播放
//        videoView.switchLevel(VideoInfo.getBitRateEnum(context).getNum());//分辨率

        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
//                mediaController.toggleVisiblity();
                mediaController.show();
                // logo.setVisibility(View.VISIBLE);
                if (startNow == false) {
                    videoView.pause(true);
                }
                String msg = String.format("是否在线播放 %b", videoView.isLocalPlay() == false);
                Log.d(TAG, msg);
            }
        });

        videoView.setOnVideoStatusListener(new IjkVideoView.OnVideoStatusListener() {

            @Override
            public void onStatus(int status) {
                if (status < 60) {
                    Log.e(TAG, String.format("状态错误 %d", status));
                } else {
                    Log.d(TAG, String.format("状态正常 %d", status));
                }
            }
        });

        videoView.setOnVideoPlayErrorLisener(new IjkVideoView.OnVideoPlayErrorLisener() {

            @Override
            public boolean onVideoPlayError(IjkVideoView.ErrorReason errorReason) {
                // 播放错误，暂停弹幕
                if (mDanmakuView != null && mDanmakuView.isPrepared())
                    mDanmakuView.pause();

                switch (errorReason.getType()) {
                    case BITRATE_ERROR:
                        sendMessage("设置的码率错误");
                        break;
                    case CAN_NOT_CHANGE_BITRATE:
//                        sendMessage("未开始播放视频不能切换码率");
                        break;
                    case CAN_NOT_CHANGE_HLS_SPEED:
//                        sendMessage("未开始播放视频不能切换播放速度");
                        break;
                    case CHANGE_EQUAL_BITRATE:
                        sendMessage("切换码率相同");
                        break;
                    case CHANGE_EQUAL_HLS_SPEED:
                        sendMessage("切换播放速度相同");
                        break;
                    case HLS_15X_URL_ERROR:
                        sendMessage("1.5倍当前码率视频正在编码中");
                        break;
                    case HLS_15X_ERROR:
                        sendMessage("视频不支持1.5倍当前码率播放");
                        break;
                    case HLS_15X_INDEX_EMPTY:
                        sendMessage("视频不支持1.5倍自动码率播放");
                        break;
                    case HLS_SPEED_TYPE_NULL:
                        sendMessage("请设置播放速度");
                        break;
                    case LOCAL_VIEWO_ERROR:
                        sendMessage("本地视频文件损坏");
                        break;
                    case M3U8_15X_LINK_NUM_ERROR:
                        sendMessage("HLS 1.5倍播放地址服务器数据错误");
                        break;
                    case M3U8_LINK_NUM_ERROR:
                        sendMessage("HLS 播放地址服务器数据错误");
                        break;
                    case MP4_LINK_NUM_ERROR:
                        sendMessage("MP4 播放地址服务器数据错误");
                        break;
                    case NETWORK_DENIED:
                        sendMessage("无法连接网络");
                        break;
                    case NOT_LOCAL_VIDEO:
                        sendMessage("找不到本地下载的视频文件，请连网后重新下载或播放");
                        break;
                    case NOT_PERMISSION:
                        sendMessage("没有权限，不能播放该视频");
                        break;
                    case OUT_FLOW:
                        sendMessage("流量超标");
                        break;
                    case QUESTION_JSON_ERROR:
                        sendMessage("问答数据加载为空");
                        break;
                    case QUESTION_JSON_PARSE_ERROR:
                        sendMessage("问答数据格式化错误");
                        break;
                    case LOADING_VIDEO_ERROR:
                        sendMessage("视频信息加载过程中出错");
                        break;
                    case START_ERROR:
                        sendMessage("开始播放视频错误，请重试");
                        break;
                    case TIMEOUT_FLOW:
                        sendMessage("账号过期");
                        break;
                    case USER_TOKEN_ERROR:
                        sendMessage("没有设置用户数据");
                        break;
                    case VIDEO_NULL:
                        sendMessage("视频信息为空");
                        break;
                    case VIDEO_STATUS_ERROR:
                        sendMessage("视频状态错误");
                        break;
                    case VID_ERROR:
                        sendMessage("设置的vid错误");
                        break;
                    default:
                        break;

                }
                // 返回 false，sdk中会弹出一个默认的错误提示框
                // 返回 true，sdk中不会弹出一个错误提示框
                return true;
            }
        });

        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(IMediaPlayer arg0, int arg1, int arg2) {
                sendMessage("播放异常，请稍后再试");
                // 返回 false，sdk中会弹出一个默认的错误提示框
                // 返回 true，sdk中不会弹出一个错误提示框
                return true;
            }
        });

        videoView.setOnPlayPauseListener(new IjkVideoView.OnPlayPauseListener() {

            @Override
            public void onPlay() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onCompletion() {
                // logo.setVisibility(View.GONE);
                mediaController.setProgressMax();
                // 播放完成，暂停弹幕
                if (mDanmakuView != null) {
                    mDanmakuView.pause();
                }
            }
        });

        videoView.setClick(new IjkVideoView.Click() {

            @Override
            public void callback(boolean start, boolean end) {
                mediaController.toggleVisiblity();
            }
        });

        videoView.setLeftUp(new IjkVideoView.LeftUp() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                videoView.setBrightness(brightness);
            }
        });

        videoView.setLeftDown(new IjkVideoView.LeftDown() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                videoView.setBrightness(brightness);
            }
        });

        videoView.setRightUp(new IjkVideoView.RightUp() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                videoView.setVolume(volume);
            }
        });

        videoView.setRightDown(new IjkVideoView.RightDown() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                videoView.setVolume(volume);
            }
        });

        videoView.setSwipeLeft(new IjkVideoView.SwipeLeft() {

            @Override
            public void callback(boolean start, boolean end) {
                // TODO 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    videoView.seekTo(fastForwardPos);
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 500;
                }
            }
        });

        videoView.setSwipeRight(new IjkVideoView.SwipeRight() {

            @Override
            public void callback(boolean start, boolean end) {
                // TODO 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                    videoView.seekTo(fastForwardPos);
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 500;
                }
            }
        });

        // 设置缓存监听
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {

            @Override
            public boolean onInfo(IMediaPlayer arg0, int arg1, int arg2) {
                switch (arg1) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        // 缓存的时候暂停
                        if (mDanmakuView != null)
                            mDanmakuView.pause();
                        break;

                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        // 恢复
                        if (mDanmakuView != null)
                            // resume方法中已包含了start
                            mDanmakuView.resume();
                        // 把缓冲后的时间设置给控制器
                        mediaController.setNewtime(videoView.getCurrentPosition());
                        break;
                }
                return false;
            }
        });

        mediaController = new MediaController(this, false);
        mediaController.setIjkVideoView(videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setInstantSeeking(false);
        videoView.setMediaController(mediaController);

        // 设置切屏事件
        mediaController.setOnBoardChangeListener(new MediaController.OnBoardChangeListener() {

            @Override
            public void onPortrait() {
                changeToLandscape();
            }

            @Override
            public void onLandscape() {
                changeToPortrait();
            }
        });

        mediaController.setOnResetViewListener(new MediaController.OnResetViewListener() {

            @Override
            public void onReset() {

            }
        });

        mediaController.setOnUpdateStartNow(new MediaController.OnUpdateStartNow() {

            @Override
            public void onUpdate(boolean isStartNow) {
                startNow = isStartNow;
            }
        });

        videoLayout.setVideoView(videoView);
    }

    /**
     * 切换到横屏
     */
    public void changeToLandscape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 由于切换到横屏获取到的宽高可能和竖屏的不一样，所以需要重新获取宽高
        int[] wh = ScreenTool.getNormalWH(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(wh[0], wh[1]);
        videoLayout.setLayoutParams(p);
        videoViewLayout.setLayoutParams(p);
    }

    /**
     * 切换到竖屏
     */
    public void changeToPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, adjusted_h);
        videoLayout.setLayoutParams(p);
        videoViewLayout.setLayoutParams(p);
    }

    private void setLiveid(String cid) {
        videoLayout.setVisibility(View.GONE);

        liveLayout.setVisibility(View.VISIBLE);
        liveVideoView.setLivePlay(uid, cid, false);
    }

    private void setVid(String vid) {
        if (vid == null) {
            return;
        }
        if (vid.equals("")) {
            return;
        }
        if (vid.equals("null")) {
            return;
        }

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(1, 1);
        liveLayout.setLayoutParams(p);
//        liveVideoView.setLayoutParams(p);
        liveLayout.setVisibility(View.GONE);

        videoLayout.setVisibility(View.VISIBLE);
        videoView.setVid(vid);
    }

    private void sendMessage(String info) {
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("msg", info);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage(msg.getData().getString("msg"));
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });

            builder.setCancelable(false);
            builder.show();
        }
    };

}
