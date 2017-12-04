package com.cn.ispanish.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.Teacher;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.dialog.ListDialog;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.video.MediaController;
import com.cn.ispanish.video.VideoViewContainer;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.cn.ispanish.video.server.PolyvDLNotificationService;
import com.cn.ispanish.views.InsideListView;
import com.easefun.polyvsdk.BitRateEnum;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.easefun.polyvsdk.util.ScreenTool;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 17/3/28.
 */
public class OfflineVideoPlayActivity extends BaseActivity {

    private static final String TAG = "OfflineVideoPlayActivity";

    public static final String VID_KEY = "vid";
    public static final String TITLE_KEY = "title";
    public static final String COURSE_ID_KEY = "courseId";

    @ViewInject(R.id.videoPlay_videoView)
    private IjkVideoView videoView;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.videoPlay_videoLayout)
    private VideoViewContainer videoLayout;
    @ViewInject(R.id.videoPlay_danmaku)
    private IDanmakuView mDanmakuView;
    @ViewInject(R.id.videoPlayList_titleText)
    private TextView titleText;
    @ViewInject(R.id.videoPlayList_sumText)
    private TextView sumText;
    @ViewInject(R.id.videoPlay_teacherPic)
    private ImageView teacherPic;
    @ViewInject(R.id.videoPlay_teacherName)
    private TextView teacherName;
    @ViewInject(R.id.videoPlay_teacherTime)
    private TextView teacherTime;
    @ViewInject(R.id.videoPlay_videoDataList)
    private InsideListView videoDataList;
    @ViewInject(R.id.videoPlay_collicIcon)
    private ImageView collicIcon;
    @ViewInject(R.id.videoPlay_buttonLayout)
    private RelativeLayout buttonLayout;
    @ViewInject(R.id.videoPlay_runText)
    private TextView runText;

    private String vid, courseId, title;

    private int fastForwardPos = 0;

    private boolean startNow = true;

    int w = 0, h = 0, adjusted_h = 0;

    private MediaController mediaController = null;

    private PolyvDBservice dBservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ViewUtils.inject(this);

        initActivity();

    }

    private void initActivity() {

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }
        runText.setText(getRunText());
        title = b.getString(TITLE_KEY);
        vid = b.getString(VID_KEY);
        courseId = b.getString(COURSE_ID_KEY);

        dBservice = new PolyvDBservice(context);
        buttonLayout.setVisibility(View.INVISIBLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        int[] wh = ScreenTool.getNormalWH(this);
        w = wh[0];
        h = wh[1];
        // 小窗口的比例
        float ratio = (float) 16 / 9;
        adjusted_h = (int) Math.ceil((float) w / ratio);
        videoLayout.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));

        initMessage(courseId);

        List<VideoDownloadInfo> list = dBservice.getDownloadFilesForVid(vid);
        initVideo(list);
    }

    private void initMessage(String id) {
        try {
            IndexBlock obj = DBHandler.getDbUtils(context).findById(IndexBlock.class, id);
            if (obj != null) {
                initBlock(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBlock(IndexBlock block) {
        titleText.setText(block.getName());
        sumText.setText(block.getNum());
//        teacherTime.setText(block.getTimeToText());
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
            initVideo(list.get(0));
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
                initVideo(list.get(i));
            }
        });
    }

    private void initVideo(VideoDownloadInfo info) {
        videoView.setMediaBufferingIndicator(progress);
        videoView.setOpenTeaser(true);
        videoView.setOpenAd(false);
        videoView.setOpenQuestion(false);
        videoView.setOpenSRT(true);
        videoView.setNeedGestureDetector(true);
        videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
        videoView.setAutoContinue(VideoInfo.getAutoContinue(context));//是否继续播放
        videoView.switchLevel(VideoInfo.getBitRateEnum(context).getNum());//分辨率

        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
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
                        sendMessage("未开始播放视频不能切换码率");
                        break;
                    case CAN_NOT_CHANGE_HLS_SPEED:
                        sendMessage("未开始播放视频不能切换播放速度");
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
                mDanmakuView.pause();
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

        videoView.setVid(info.getVid(), info.getBitrate(), true);
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

    public String getRunText() {
        StringBuilder sb = new StringBuilder();
        sb.append("【id:" + User.getUserId(context) + "】                                                         ");
        sb.append("【id:" + User.getUserId(context) + "】                                                         ");
        sb.append("【id:" + User.getUserId(context) + "】                                                         ");
        sb.append("【id:" + User.getUserId(context) + "】                                                         ");
        return sb.toString();
    }

}
