package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperExplainView;
import com.cn.ispanish.views.paper.v2.views.PaperRadioView;
import com.cn.ispanish.views.paper.v2.views.PaperSpokenView;
import com.cn.ispanish.views.paper.v2.views.PaperTitleView;
import com.cn.ispanish.views.paper.v2.views.PaperTypeView;
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
 * Created by Hua on 17/5/26.
 */
public class PaperToSpokenLayout extends PaperContentView {

    @ViewInject(R.id.paperSpoken_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperSpoken_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperSpoken_spokenView)
    private PaperSpokenView spokenView;
    @ViewInject(R.id.paperSpoken_explainView)
    private PaperExplainView explainView;

    public PaperToSpokenLayout(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_to_spoken, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }

    @Override
    public void initViewData() {
        typeView.initQuestion(question.getQuestionType(), position);
        titleView.initQuestion(question, position);
        explainView.initQuestion(question, position);
        spokenView.initQuestion(question);
        spokenView.setOnStop(new CallbackForBoolean() {
            @Override
            public void callback(boolean b) {
                showExplain();
            }
        });
    }

    public void setInChild() {
        titleView.goneTypeLayout();
        titleView.setTitle(Html.fromHtml("<font color=\"#3685D6\">" + "题目：" + "</font>" +
                "<font color=\"#565656\">" + question.getName() + "</font>"));
    }


    @Override
    public void onStop() {

    }

    @Override
    public void showExplain() {
        explainView.setVisibility(VISIBLE);
    }

    @Override
    public void showDoing() {
    }
}
