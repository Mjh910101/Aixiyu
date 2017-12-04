package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.PlayHandler;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.paper.PaperContentView;
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
 * Created by Hua on 17/5/26.
 */
public class PaperToHearingView extends PaperContentView {

    @ViewInject(R.id.paperHearing_questionTypeText)
    private TextView questionTypeText;
    @ViewInject(R.id.paperHearing_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperHearing_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperHearing_questionExplainText)
    private TextView questionExplainText;
    @ViewInject(R.id.paperHearing_questionExplainLayout)
    private LinearLayout questionExplainLayout;
    @ViewInject(R.id.paperHearing_questionPic)
    private ImageView questionPic;
    @ViewInject(R.id.paperHearing_answerList)
    private InsideListView answerList;
    @ViewInject(R.id.paperHearing_playSeekBar)
    private SeekBar playSeekBar;
    @ViewInject(R.id.paperHearing_playButton)
    private ImageView palyButton;

    private PlayHandler playHandler;
//    private boolean isPlay = false;

    public PaperToHearingView(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_hearing, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);

        addView(view);

        initPlayHandler();
        initViewData();
    }

    private void initPlayHandler() {
        playHandler = new PlayHandler(playSeekBar);
        playHandler.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                setStopSoundIcon();
            }
        });
    }

    @Override
    public void initViewData() {
        setNum(questionNumText);
        setType(questionTypeText);
        setTitle(questionTitle);
        setExplain(questionExplainText);
        setPic(questionPic);
        setAnswerList(answerList);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void showExplain() {
        questionExplainLayout.setVisibility(VISIBLE);
    }

    @OnClick(R.id.paperHearing_playButton)
    public void onPlay(View view) {
        if (playHandler == null) {
            return;
        }
        if (playHandler.isPlay()) {
            stopSound();
//            MessageHandler.showToast(context,"stop");
        } else {
            playSound();
//            MessageHandler.showToast(context,"play");
        }
    }

    private void playSound() {
        try {
            playHandler.playUrl(question.getVideourl());
            palyButton.setImageResource(R.drawable.audio_stop_icon);
//                isPlay = true;
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
//        isPlay = false;
    }

    @Override
    public void showDoing() {
    }

}
