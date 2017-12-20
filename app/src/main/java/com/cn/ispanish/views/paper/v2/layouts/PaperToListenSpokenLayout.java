package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.ChildViewPager;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperAudioView;
import com.cn.ispanish.views.paper.v2.views.PaperSpokenView;
import com.cn.ispanish.views.paper.v2.views.PaperTitleView;
import com.cn.ispanish.views.paper.v2.views.PaperTypeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Hua on 17/7/31.
 */
public class PaperToListenSpokenLayout extends PaperContentView {

    @ViewInject(R.id.paperListenSpoken_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperListenSpoken_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperListenSpoken_audioView)
    private PaperAudioView audioView;
    @ViewInject(R.id.paperListenSpoken_spokenView)
    private PaperSpokenView spokenView;

    public PaperToListenSpokenLayout(Context context, Question question, int position, OnQuestionListener onQuestion, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        super(context, question, position, onQuestion, callbackForComment);

        view = inflater.inflate(R.layout.layout_paper_listen_spoken, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }

    @Override
    public void initViewData() {
        typeView.initQuestion(question.getQuestionType(), position);
        titleView.initQuestion(question, position);
        titleView.goneTitleLayout();

        audioView.initQuestion(question);
        spokenView.initQuestion(question);

    }

    @Override
    public void onStop() {
        audioView.stopSound();
    }

    @Override
    public void showExplain() {
    }

    @Override
    public void showDoing() {
    }

}
