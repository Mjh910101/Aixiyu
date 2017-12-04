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
import com.cn.ispanish.views.paper.v2.views.PaperInputView;
import com.cn.ispanish.views.paper.v2.views.PaperRadioView;
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
public class PaperToRadioLayout extends PaperContentView {

    @ViewInject(R.id.paperRadio_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperRadio_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperRadio_radioView)
    private PaperRadioView radioView;
    @ViewInject(R.id.paperRadio_inputView)
    private PaperInputView inputView;
    @ViewInject(R.id.paperRadio_explainView)
    private PaperExplainView explainView;

    public PaperToRadioLayout(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_to_radio, null);

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
        radioView.initQuestion(question);
        radioView.setOnQuestion(new OnQuestionListener() {
            @Override
            public void onQuestion(Question question, boolean b) {
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, b);
                }
                if (!radioView.isGame()) {
                    radioView.setShow(true);
                    showExplain();
                }
            }
        });
        radioView.setOnCheckListener(new CallbackForBoolean() {
            @Override
            public void callback(boolean b) {
                showExplain();
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, b);
                }
            }
        });
        inputView.initQuestion(question);
        if (!radioView.isHaveAnswer()) {
            inputView.setVisibility(VISIBLE);
            inputView.setOnCheckListener(callback);
        }
    }

    public void setInChild() {
        titleView.goneTypeLayout();
        titleView.setTitle(Html.fromHtml("<font color=\"#3685D6\">" + "题目：" + "</font>" +
                "<font color=\"#565656\">" + question.getName() + "</font>"));
    }

    public void setGame(boolean b) {
        radioView.setGame(b);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void showExplain() {
        if (radioView.isGame()) {
            radioView.show();
        }
        explainView.setVisibility(VISIBLE);
    }

    @Override
    public void showDoing() {
        radioView.showDoing();
        inputView.showDoing();
    }

    CallbackForBoolean callback = new CallbackForBoolean() {
        @Override
        public void callback(boolean b) {
            showExplain();
            if (onQuestion != null) {
                onQuestion.onQuestion(question, b);
            }
        }
    };

    public void showRight() {
        radioView.showRight();
    }
}
