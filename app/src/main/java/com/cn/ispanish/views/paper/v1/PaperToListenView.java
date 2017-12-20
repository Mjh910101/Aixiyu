package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.PlayHandler;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.ChildViewPager;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v1.ListenToJudgeView;
import com.cn.ispanish.views.paper.v1.ListenToSingleView;
import com.cn.ispanish.views.paper.v1.ListenToTranslationView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

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
 * Created by Hua on 17/7/17.
 */
public class PaperToListenView extends PaperContentView {

    @ViewInject(R.id.paperListen_questionTypeText)
    private TextView questionTypeText;
    @ViewInject(R.id.paperListen_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperListen_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperListen_questionPic)
    private ImageView questionPic;
    @ViewInject(R.id.paperListen_playSeekBar)
    private SeekBar playSeekBar;
    @ViewInject(R.id.paperListen_playButton)
    private ImageView palyButton;
    @ViewInject(R.id.paperListen_questionViewPager)
    private ChildViewPager questionViewPager;

    private PlayHandler playHandler;

    private ListenQuestion question;

    public PaperToListenView(Context context, ListenQuestion question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion,null);
        view = inflater.inflate(R.layout.layout_paper_listen, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        this.question = question;

        ViewUtils.inject(this, view);

        initPlayHandler();
        initViewData();

        addView(view);
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
        setPic(questionPic);

        questionTitle.setVisibility(GONE);

        questionViewPager.setAdapter(new PagerContentAdapter());
        questionViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void showExplain() {
    }

    @Override
    public void onStop() {
        stopSound();
    }

    @OnClick(R.id.paperListen_playButton)
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


    private void playSound() {
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

    private void stopSound() {
        playHandler.pause();
        setStopSoundIcon();
    }

    private void setStopSoundIcon() {
        palyButton.setImageResource(R.drawable.audio_play_icon);
        playHandler.setIsPlay(false);
    }

    class PagerContentAdapter extends PagerAdapter {


        private Map<Integer, PaperContentView> viewMap;

        public PagerContentAdapter() {
            viewMap = new HashMap<>();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView(viewMap.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return question.getQuestionList().size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PaperContentView view;

            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(question.getQuestionList().get(position), position);
                viewMap.put(position, view);
            }
            container.addView(view);
            return view;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    private PaperContentView getContentView(Question question, int position) {
        position = position + 1;
        switch (question.getType()) {
            case Question.TingLi_PANDUAN:
                return new ListenToJudgeView(context, question, position, onQuestion);
            case Question.TingLi_DATI:
                return new ListenToTranslationView(context, question, position, onQuestion);
            case Question.TingLi_XUANZHE:
                return new ListenToSingleView(context, question, position, onQuestion);
            default:
                return new ListenToTranslationView(context, question, position, onQuestion);
        }
    }
    @Override
    public void showDoing() {
    }
}
