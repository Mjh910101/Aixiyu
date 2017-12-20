package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PlayHandler;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.sound.SoundMeter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

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
 * Created by Hua on 17/8/2.
 */
public class PaperSpokenView extends LinearLayout {

    private static final long POLL_INTERVAL = 300;
    private static final int SOUND_TOTAL = 30;

    @ViewInject(R.id.paperSpokenView_soundIcon)
    private ImageView soundIcon;
    @ViewInject(R.id.paperSpokenView_soundBtn)
    private Button soundBtn;
    @ViewInject(R.id.paperSpokenView_timeBox)
    private RelativeLayout timeBox;
    @ViewInject(R.id.paperSpokenView_timedown)
    private Chronometer timedown;
    @ViewInject(R.id.paperSpokenView_timeImage)
    private ImageView timeImage;
    @ViewInject(R.id.paperSpokenView_againButton)
    private TextView againButton;
    @ViewInject(R.id.paperSpokenView_playSeekBar)
    private SeekBar playSeekBar;
    @ViewInject(R.id.paperSpokenView_playButton)
    private ImageView palyButton;
    @ViewInject(R.id.paperSpokenView_playLayout)
    private RelativeLayout playLayout;
    @ViewInject(R.id.paperSpokenView_explainView)
    private PaperExplainView explainView;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private Question question;

    private CallbackForBoolean callback;

    private long startVoiceT, endVoiceT;
    private int flag = 1;
    private String voiceName;
    private long timeTotalInS = 0;
    private long timeLeftInS = 0;
    private long timeTotal = 0;

    private Handler mHandler = new Handler();
    //    private Mp3Recorder recorder;
    private SoundMeter recorder;
    private PlayHandler playHandler;

    public PaperSpokenView(Context context) {
        super(context);
        initView(context);
    }

    public PaperSpokenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperSpokenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperSpokenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_spoken_view, null);

        ViewUtils.inject(this, view);
        addView(view);

    }

    @OnClick(R.id.paperSpokenView_againButton)
    public void onAgain(View view) {
        againSound();
    }

    @OnClick(R.id.paperSpokenView_playButton)
    public void onPlay(View view) {
        if (playHandler == null) {
            return;
        }
        if (playHandler.isPlay()) {
            stopSound();
        } else {
            playSound();
        }
    }

    public void initQuestion(Question question) {
        this.question = question;
        initViewData();
        initPlayHandler();
        setSoundBtnOnTouchListener();
        explainView.initQuestion(question, 0,null);
    }

    private void initViewData() {

    }

    private void initPlayHandler() {
        voiceName = DateHandle.getTime() + ".amr";
        try {
            recorder = new SoundMeter();
            playHandler = new PlayHandler(playSeekBar);
            playHandler.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    setStopSoundIcon();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            playLayout.setVisibility(GONE);
            soundIcon.setVisibility(GONE);
            soundBtn.setVisibility(GONE);
        }
    }

    private void setSoundBtnOnTouchListener() {
        soundBtn.setOnTouchListener(new View.OnTouchListener() {
            boolean isDown = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (Utils.isFastClick()) {
                            MessageHandler.showToast(context, "点击太快");
                            return true;
                        }
                        Log.e("", "ACTION_DOWN+ACTION_DOWN+ACTION_DOWN+ACTION_DOWN+ACTION_DOWN+ACTION_DOWN");
                        soundIcon.setImageResource(R.drawable.sound_on_icon);
                        int[] location = new int[2];
                        soundBtn.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
                        int btn_rc_Y = location[1];
                        int btn_rc_X = location[0];

                        isDown = true;

                        if (flag == 1) {
                            if (event.getY() < btn_rc_Y && event.getX() > btn_rc_X) {
                                mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        if (isDown) {
                                            timeBox.setVisibility(View.VISIBLE);
                                            startVoiceT = System.currentTimeMillis();
                                            start(voiceName);
                                            //设置录音时间
                                            initTimer(SOUND_TOTAL);
                                            timedown.start();
                                            flag = 2;
                                        }
                                    }
                                }, 300);
                            }
                        }

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.e("", "ACTION_UP+ACTION_UP+ACTION_UP+ACTION_UP+ACTION_UP+ACTION_UP");
                        isDown = false;

                        soundIcon.setImageResource(R.drawable.sound_off_icon);
                        timeBox.setVisibility(View.GONE);

                        timedown.stop();
                        stop();

                        flag = 1;
                        soundUse(voiceName);

                        endVoiceT = System.currentTimeMillis();
                        int time = (int) ((endVoiceT - startVoiceT) / 1000);
                        System.out.println(time);

                        if (callback != null) {
                            callback.callback(true);
                        }
                        explainView.setVisibility(VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    againSound();
                    return true;
                }
                return false;
            }
        });
    }

    private void playSound() {
        try {
            playHandler.playInPath(SoundMeter.getSoundPath() + "/" + voiceName);
            palyButton.setImageResource(R.drawable.audio_stop_icon);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopSound() {
        playHandler.pause();
        setStopSoundIcon();
    }

    private void setStopSoundIcon() {
        palyButton.setImageResource(R.drawable.audio_play_icon);
        playHandler.setIsPlay(false);
    }

    private void againSound() {
        if (playHandler.isPlay()) {
            stopSound();
        }
        deleteSound();
        timeTotal = 0;
        soundIcon.setVisibility(View.VISIBLE);
        playLayout.setVisibility(View.GONE);
        againButton.setVisibility(View.GONE);
    }

    private void deleteSound() {
        File f = new File(SoundMeter.getSoundPath() + "/" + voiceName);
//        File f = new File(Mp3Recorder.getSoundPath() + "/" + voiceName);
        if (f.exists()) {
            f.delete();
        }
    }

    private void start(String name) {
        try {
            recorder.start(name);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Recorder", "Recorder Start error ： " + name);
        }
    }

    private void stop() {
        mHandler.removeCallbacks(mPollTask);
        try {
            recorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Recorder", "Recorder Stop error");
        }
        timeImage.setImageResource(R.drawable.sound_amp_001);
    }

    private void soundUse(String fileName) {
        //判断sd卡上是否有声音文件，有的话就显示名称并播放
        final String path = SoundMeter.getSoundPath() + "/" + voiceName;
//        final String path = Mp3Recorder.getSoundPath() + "/" + voiceName;
        File file = new File(path);
        if (file.exists()) {
            soundIcon.setVisibility(View.GONE);
            playLayout.setVisibility(View.VISIBLE);
            againButton.setVisibility(View.VISIBLE);
            String soundName = file.getName();
            setSoundTime(timeTotal);
        }
    }

    private void refreshTimeLeft() {
        timeTotal = timeTotalInS - timeLeftInS;
        this.timedown.setText(getTimeText(timeTotal));
    }

    public void setSoundTime(long t) {
//        txtName.setText(getTimeText(t));
    }

    int amp = 0;

    private Runnable mPollTask = new Runnable() {

        public void run() {
            updateDisplay(amp);
            amp = (amp + 1) % 4;
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    private void updateDisplay(int signalEMA) {

        switch (signalEMA) {
            case 0:
                timeImage.setImageResource(R.drawable.sound_amp_001);
                break;
            case 1:
                timeImage.setImageResource(R.drawable.sound_amp_002);
                break;
            case 2:
                timeImage.setImageResource(R.drawable.sound_amp_003);
                break;
            default:
                timeImage.setImageResource(R.drawable.sound_amp_004);
                break;
        }
    }

    private String getTimeText(long t) {
        if (t < 10) {
            return "00:0" + t;
        } else {
            return "00:" + t;
        }
    }

    /**
     * 初始化计时器，计时器是通过widget.Chronometer来实现的
     *
     * @param total 一共多少秒
     */
    private void initTimer(long total) {
        this.timeTotalInS = total;
        this.timeLeftInS = total;
        timedown.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (timeLeftInS <= 0) {
                    MessageHandler.showToast(context, "录音时间到");
                    timedown.stop();
                    //录音停止
                    stop();
                    timeBox.setVisibility(View.GONE);
                    return;
                }
                timeLeftInS--;
                refreshTimeLeft();
            }
        });
    }

    static class Utils {
        private static long lastClickTime;

        public synchronized static boolean isFastClick() {
            long time = System.currentTimeMillis();
            if (time - lastClickTime < 300) {
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }

    public void setOnStop(CallbackForBoolean callback) {
        this.callback = callback;
    }
}
