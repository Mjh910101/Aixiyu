package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PlayHandler;
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
 * Created by Hua on 17/7/28.
 */
public class PaperAudioView extends LinearLayout {


    @ViewInject(R.id.paperAudio_playSeekBar)
    private SeekBar playSeekBar;
    @ViewInject(R.id.paperAudio_playButton)
    private ImageView palyButton;
    @ViewInject(R.id.paperAudio_playTimeText)
    private TextView playTimeText;

    private Context context;

    private PlayHandler playHandler;

    private View view;
    private LayoutInflater inflater;

    private Question question;

    public PaperAudioView(Context context) {
        super(context);
        initView(context);
    }

    public PaperAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperAudioView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_audio, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    @OnClick(R.id.paperAudio_playButton)
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
    }

    private void initViewData() {
        initPlayHandler();
    }

    private void initPlayHandler() {
        playHandler = new PlayHandler(playSeekBar);
        playHandler.initTimeText(playTimeText);
        playHandler.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                setStopSoundIcon();
            }
        });
    }

    public void playSound() {
        try {
            playHandler.playUrl(question.getVideourl());
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

    public void stopSound() {
        playHandler.pause();
        setStopSoundIcon();
    }

    private void setStopSoundIcon() {
        palyButton.setImageResource(R.drawable.audio_play_icon);
        playHandler.setIsPlay(false);
    }
}
