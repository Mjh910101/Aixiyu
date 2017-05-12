package com.cn.ispanish.activitys;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.VideoMaxTextAdapter;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.Teacher;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.box.VideoHistory;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.dialog.ListDialog;
import com.cn.ispanish.fragments.DataFragment;
import com.cn.ispanish.fragments.DataNoStartFragment;
import com.cn.ispanish.fragments.ReplyFragment;
import com.cn.ispanish.fragments.VideoListFragment;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.video.MediaController;
import com.cn.ispanish.video.VideoViewContainer;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.cn.ispanish.video.server.PolyvDLNotificationService;
import com.easefun.polyvsdk.BitRateEnum;
import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.easefun.polyvsdk.util.ScreenTool;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
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

import java.util.ArrayList;
import java.util.Collections;
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
 * Created by Hua on 17/3/13.
 */
public class VideoPlayContentNoStartActivity extends BaseActivity {

    private static final String TAG = "VideoPlayActivity";

    public static final String VID_KEY = "vid";
    public static final String TITLE_KEY = "title";
    public static final String COURSE_ID_KEY = "courseId";
    public static final String OFFLINE = "offline";

    @ViewInject(R.id.videoPlayContent_videoView)
    private IjkVideoView videoView;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.videoPlayContent_videoLayout)
    private VideoViewContainer videoLayout;
    @ViewInject(R.id.videoPlayContent_danmaku)
    private IDanmakuView mDanmakuView;

    @ViewInject(R.id.videoPlayContent_dataText)
    private TextView dataText;
    @ViewInject(R.id.videoPlayContent_dataLine)
    private View dataLine;
    @ViewInject(R.id.videoPlayContent_videoText)
    private TextView videoText;
    @ViewInject(R.id.videoPlayContent_videoLine)
    private View videoLine;
    @ViewInject(R.id.videoPlayContent_replyText)
    private TextView replyText;
    @ViewInject(R.id.videoPlayContent_replyLine)
    private View replyLine;

    @ViewInject(R.id.videoPlayContent_collectionButton)
    private ImageView collectionButton;
    @ViewInject(R.id.videoPlayContent_buyButton)
    private ImageView buyButton;

    private String vid, courseId, title;

    private int fastForwardPos = 0;

    private boolean startNow = true, isOffline = false;
    boolean isBuy;

    int w = 0, h = 0, adjusted_h = 0;

    private MediaController mediaController = null;

    private IndexBlock block;
    private VideoInfo videoInfo;
    private Teacher teacher;
    private List<VideoInfo> videoList;

    private PolyvDBservice service;

    private ServiceConnection serconn;
    private PolyvDLNotificationService downloadService;

    private FragmentManager fragmentManager;

    private DataNoStartFragment dataFragment;
    private ReplyFragment replyFragment;
    private VideoListFragment videoListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_content_no_start);

        ViewUtils.inject(this);

        Bundle b;
        if (savedInstanceState != null) {
            b = savedInstanceState;
        } else {
            b = getIntent().getExtras();
        }
        if (b == null) {
            finish();
            return;
        }

        initActivity(b);

    }

    @Override
    public void onResume() {
        super.onResume();
        // 恢复弹幕
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused() && videoView != null
                && videoView.isPlaying())
            mDanmakuView.resume();

        if (videoView != null && videoView.isPausState()) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            unbindService(serconn);

            VideoHistory history = new VideoHistory();
            history.setVid(vid);
            history.setCourseId(courseId);
            history.setImageUrl(videoView.getVideo() != null ? videoView.getVideo().getFirstImage() : "");
            history.setLastTime(System.currentTimeMillis());
            history.setTitle(title);
            history.setWatchTime(mediaController.getCurrentTime());

            DBHandler.getDbUtils(context).saveOrUpdate(history);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initActivity(Bundle b) {

        initButton();

        title = b.getString(TITLE_KEY);
        vid = b.getString(VID_KEY, "");
        courseId = b.getString(COURSE_ID_KEY);
        isOffline = b.getBoolean(OFFLINE, false);

        fragmentManager = getFragmentManager();

        service = new PolyvDBservice(context);

        serconn = PolyvDLNotificationService.bindDownloadService(context, new PolyvDLNotificationService.BindListener() {

            @Override
            public void bindSuccess(PolyvDLNotificationService downloadService) {
                VideoPlayContentNoStartActivity.this.downloadService = downloadService;
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        int[] wh = ScreenTool.getNormalWH(this);
        w = wh[0];
        h = wh[1];
        // 小窗口的比例
        float ratio = (float) 16 / 9;
        adjusted_h = (int) Math.ceil((float) w / ratio);
        videoLayout.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));

        initVideo();
        if (!isOffline) {
            downloadData(courseId);
            if (!vid.equals("")) {
                setVid(vid);
            }
        } else {
            List<VideoDownloadInfo> list = service.getDownloadFilesForVid(vid);
            initVideo(list);
        }
    }

    private void initVideo(List<VideoDownloadInfo> list) {
        if (list == null) {
            finish();
            return;
        }
        if (list.isEmpty()) {
            finish();
            return;
        }
        if (list.size() == 1) {
            setVideo(list.get(0));
        } else {
            showDialog(list);
        }
    }

    private void showDialog(final List<VideoDownloadInfo> list) {
        List<String> bitRateList = new ArrayList<>();
        for (VideoDownloadInfo obj : list) {
            bitRateList.add(BitRateEnum.getBitRate(obj.getBitrate()).getName());
        }

        final ListDialog dialog = new ListDialog(context);
        dialog.setTitle("视频格式");
        dialog.setList(bitRateList);
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                setVideo(list.get(i));
            }
        });
    }

    public void setVideo(VideoDownloadInfo info) {
        videoView.setVid(info.getVid(), info.getBitrate(), true);
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

        mediaController.setOnCollectionListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isCollic(block)) {
                        DBHandler.getDbUtils(context).deleteById(IndexBlock.class, block.getCourseid());
                    } else {
                        DBHandler.getDbUtils(context).saveOrUpdate(block);
                    }
                    initCollic(block);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        videoLayout.setVideoView(videoView);
    }

    private void setVid(String vid) {
        setVid(vid, VideoInfo.getBitRateEnum(context).getNum(), false);
    }

    private void setVid(String vid, int bitRate, boolean isOffLine) {
        if (vid == null) {
            return;
        }
        if (vid.equals("")) {
            return;
        }
        if (vid.equals("null")) {
            return;
        }
        videoView.setVid(vid, bitRate, isOffLine);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(VID_KEY, vid);
        outState.putString(TITLE_KEY, title);
        outState.putString(COURSE_ID_KEY, courseId);
        outState.putBoolean(OFFLINE, isOffline);
        super.onSaveInstanceState(outState);
        Log.i(TAG, "@@Activity onSaveInstanceState@@" + this.toString());
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        vid = savedInstanceState.getString(VID_KEY);
//        Log.i(TAG, "@@Activity onRestoreInstanceState@@" + this.toString());
//    }

    /**
     * 切换到横屏
     */
    public void changeToLandscape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 由于切换到横屏获取到的宽高可能和竖屏的不一样，所以需要重新获取宽高
        int[] wh = ScreenTool.getNormalWH(this);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(wh[0], wh[1]);
        videoLayout.setLayoutParams(p);
    }

    /**
     * 切换到竖屏
     */
    public void changeToPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, adjusted_h);
        videoLayout.setLayoutParams(p);
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

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", id);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getVideoPlay(), params,
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
                        if (json != null) {

                            isBuy = JsonHandle.getString(json, "buy").equals("YES");

                            initBlock(JsonHandle.getJSON(json, "sp"));
                            initTeasher(JsonHandle.getJSON(json, "teacher"));
                            initVideoList(JsonHandle.getArray(json, "splist"), isBuy);

                            onButton(findViewById(R.id.videoPlayContent_dataButton));
                            initToolButton();
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initToolButton() {
        if (isBuy) {
            buyButton.setImageResource(R.drawable.buy_yellow_icon);
        } else {
            buyButton.setImageResource(R.drawable.bug_gray_icon);
        }
        initCollic(block);
    }

    private void initVideoList(JSONArray array, boolean isBuy) {
        if (array == null) {
            return;
        }
        videoList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            VideoInfo video = new VideoInfo(JsonHandle.getJSON(array, i));
            video.setBuy(isBuy);
            videoList.add(video);
            if (vid.equals("")) {
                if (video.isBuy() || video.isFree()) {
                    uploadVidoe(video);
//                    vid = video.getVid();
//                    videoInfo = video;
//                    setVid(vid);
                }
            } else {
                if (video.equals(vid)) {
                    videoInfo = video;
                }
            }
        }
        Collections.sort(videoList);
    }

    private void initTeasher(JSONObject json) {
        if (json == null) {
            return;
        }
        teacher = new Teacher(json);
    }

    private void initBlock(JSONObject json) {
        if (json == null) {
            return;
        }
        block = new IndexBlock(json);
    }

    private boolean isCollic(IndexBlock block) {
        try {
            DBHandler.getDbUtils(context).createTableIfNotExist(IndexBlock.class);
            IndexBlock obj = DBHandler.getDbUtils(context).findById(IndexBlock.class, block.getCourseid());
//            MessageHandler.showToast(context, obj.getCourseid());
            return obj != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @OnClick(R.id.videoPlayContent_downloadButton)
    public void onDownload(View view) {
//        Video.loadVideo(vid, new PolyvVideoVO.OnVideoLoaded() {
//            @Override
//            public void onloaded(Video video) {
//                // 码率数
//                String[] items = BitRateEnum.getBitRateNameArray(video.getDfNum());
//                if (items != null) {
//                    showDownloadDialog(video, items);
//                }
//            }
//        });
    }

    @OnClick(R.id.videoPlayContent_collectionButton)
    public void onCollic(View view) {
        try {
            if (isCollic(block)) {
                DBHandler.getDbUtils(context).deleteById(IndexBlock.class, block.getCourseid());
            } else {
                DBHandler.getDbUtils(context).saveOrUpdate(block);
            }
            initCollic(block);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCollic(IndexBlock block) {
//        if (isCollic(block)) {
//            collectionButton.setImageResource(R.drawable.star_red_icon);
//        } else {
//            collectionButton.setImageResource(R.drawable.star_gray_icon);
//        }
        if (mediaController != null) {
            mediaController.setCollection(isCollic(block));
        }
    }

    private void showDownloadDialog(final VideoInfo videoInfo, final Video video, String[] items) {
        final ListDialog dialog = new ListDialog(context);
        dialog.setList(items);
        dialog.setTitle("请选择您要下载的分辨率");
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                downloadVideo(videoInfo, video, i);
                dialog.dismiss();
            }
        });
    }

    private void downloadVideo(VideoInfo videoInfo, Video video, int which) {

        Video.HlsSpeedType hlsSpeedType = Video.HlsSpeedType.SPEED_1X;

        final int bitrate = which + 1;

        final VideoDownloadInfo downloadInfo = new VideoDownloadInfo(videoInfo.getVid(), videoInfo.getCourseid(), video.getDuration(),
                video.getFileSizeMatchVideoType(bitrate), bitrate);
        downloadInfo.setTitle(videoInfo.getName());
        downloadInfo.setSpeed(hlsSpeedType.getName());

        String pic = "";
        if (video != null) {
            pic = video.getFirstImage();
        } else {
            if (block != null) {
                pic = block.getImages();
            }
        }

        downloadInfo.setPic(pic);

        if (service != null && !service.isAdd(downloadInfo)) {

            addDownloadFile(downloadInfo);

            final int id = PolyvDLNotificationService.getId(video.getVid(), bitrate,
                    hlsSpeedType.getName());
            PolyvDownloader polyvDownloader = PolyvDownloaderManager.getPolyvDownloader(videoInfo.getVid(),
                    bitrate, hlsSpeedType);

            polyvDownloader
                    .setPolyvDownloadProressListener(new PolyvDownloadProgressListener() {
                        private long total;

                        @Override
                        public void onDownloadSuccess() {
                            service.updatePercent(downloadInfo, total, total);
                            if (downloadService != null) {
                                downloadService.updateFinishNF(id);
                            }
                        }

                        @Override
                        public void onDownloadFail(PolyvDownloaderErrorReason errorReason) {
                            if (downloadService != null)
                                downloadService.updateErrorNF(id, false);

                            showMessageOnUiThread("下载失败");
                        }

                        @Override
                        public void onDownload(long current, long total) {
                            this.total = total;
                            if (downloadService != null)
                                downloadService.updateDownloadingNF(id,
                                        (int) (current * 100 / total), false);
                        }
                    });

            // 先执行
            if (downloadService != null)
                downloadService.updateStartNF(id, videoInfo.getVid(), bitrate, hlsSpeedType.getName(),
                        videoInfo.getName(), 0);
            polyvDownloader.start();

            showMessageOnUiThread("开始下载");

        } else {
            showMessageOnUiThread("下载任务已经增加到队列");
        }

    }

    private void addDownloadFile(VideoDownloadInfo info) {
        try {
            service.addDownloadFile(info);

            block.setDownload(true);
            videoInfo.setDownload(true);
            videoInfo.setImage(videoView.getVideo() != null ? videoView.getVideo().getFirstImage() : "");

            DBHandler.getDbUtils(context).saveOrUpdate(block);
            DBHandler.getDbUtils(context).saveOrUpdate(videoInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessageOnUiThread(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                MessageHandler.showToast(context, message);
            }
        });
    }

    @OnClick({R.id.videoPlayContent_dataButton, R.id.videoPlayContent_videoButton, R.id.videoPlayContent_replyButton})
    public void onButton(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initButton();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.videoPlayContent_dataButton:
                onDataButton(transaction);
                break;
            case R.id.videoPlayContent_videoButton:
                onVideoButton(transaction);
                break;
            case R.id.videoPlayContent_replyButton:
                onReplyButton(transaction);
                break;
        }
        transaction.commit();
    }

    private void onReplyButton(FragmentTransaction transaction) {
        replyText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        replyLine.setVisibility(View.VISIBLE);

        if (block == null) {
            return;
        }

        if (replyFragment == null) {
            replyFragment = new ReplyFragment(block.getCourseid(), progress);
            transaction.add(R.id.videoPlayContent_contentLayout, replyFragment);
        } else {
            transaction.show(replyFragment);
        }

    }

    private void onVideoButton(FragmentTransaction transaction) {
        videoText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        videoLine.setVisibility(View.VISIBLE);

        if (videoInfo == null || videoList == null || block == null) {
            return;
        }

        for (VideoInfo videoInfo : videoList) {
            videoInfo.setCourseid(block.getCourseid());
            Log.e("video", "download file " + videoInfo.getCourseid());
        }

        if (videoListFragment == null) {
            videoListFragment = new VideoListFragment(videoInfo, videoList, new VideoMaxTextAdapter.OnVideoListener() {
                @Override
                public void callback(final VideoInfo finalVideoInfo, boolean isDown) {
                    if (isDown) {
                        Video.loadVideo(finalVideoInfo.getVid(), new PolyvVideoVO.OnVideoLoaded() {
                            @Override
                            public void onloaded(Video videoData) {
                                // 码率数
                                String[] items = BitRateEnum.getBitRateNameArray(videoData.getDfNum());
                                if (items != null) {
                                    showDownloadDialog(finalVideoInfo, videoData, items);
                                }
                            }
                        });
                    } else {
                        uploadVidoe(finalVideoInfo);
                    }
                }
            });
            transaction.add(R.id.videoPlayContent_contentLayout, videoListFragment);
        } else {
            transaction.show(videoListFragment);
        }

    }

    private void uploadVidoe(VideoInfo finalVideoInfo) {
        videoInfo = finalVideoInfo;
        vid = finalVideoInfo.getVid();
        setVid(finalVideoInfo.getVid());
        if (dataFragment != null) {
            dataFragment.setTitleText(finalVideoInfo);
        }
    }

    private void onDataButton(FragmentTransaction transaction) {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        dataLine.setVisibility(View.VISIBLE);

        if (block == null || videoInfo == null) {
            return;
        }

        if (dataFragment == null) {
            dataFragment = new DataNoStartFragment(block, teacher, videoInfo, isBuy);
            transaction.add(R.id.videoPlayContent_contentLayout, dataFragment);
        } else {
            transaction.show(dataFragment);
        }
    }

    private void initButton() {

        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));
        videoText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));
        replyText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));

        dataLine.setVisibility(View.INVISIBLE);
        videoLine.setVisibility(View.INVISIBLE);
        replyLine.setVisibility(View.INVISIBLE);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (dataFragment != null) {
            transaction.hide(dataFragment);
        }
        if (replyFragment != null) {
            transaction.hide(replyFragment);
        }
        if (videoListFragment != null) {
            transaction.hide(videoListFragment);
        }
    }


}
