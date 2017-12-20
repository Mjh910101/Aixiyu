package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperExplainView;
import com.cn.ispanish.views.paper.v2.views.PaperInputView;
import com.cn.ispanish.views.paper.v2.views.PaperMultiSelecView;
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
public class PaperToMultiSelectLayout extends PaperContentView {

    @ViewInject(R.id.paperMultiSelect_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperMultiSelect_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperMultiSelect_radioView)
    private PaperMultiSelecView multiSelecView;
    @ViewInject(R.id.paperMultiSelect_explainView)
    private PaperExplainView explainView;

    public PaperToMultiSelectLayout(Context context, Question question, int position, OnQuestionListener onQuestion, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        super(context, question, position, onQuestion, callbackForComment);

        view = inflater.inflate(R.layout.layout_paper_to_multi_select, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }

    @Override
    public void initViewData() {
        typeView.initQuestion(question.getQuestionType(), position);
        titleView.initQuestion(question, position);
        explainView.initQuestion(question, position, callbackForComment);
        multiSelecView.initQuestion(question);
        multiSelecView.setOnQuestion(new OnQuestionListener() {
            @Override
            public void onQuestion(Question question, boolean b) {
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, b);
                }
                if (!multiSelecView.isGame()) {
                    multiSelecView.setShow(true);
                    showExplain();
                }
            }
        });
        multiSelecView.setOnCheckListener(new CallbackForBoolean() {
            @Override
            public void callback(boolean b) {
                showExplain();
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, b);
                }
            }
        });
    }

    public void setInChild() {
        titleView.goneTypeLayout();
        titleView.setTitle(Html.fromHtml("<font color=\"#3685D6\">" + "题目：" + "</font>" +
                "<font color=\"#565656\">" + question.getName() + "</font>"));
    }

    public void setGame(boolean b) {
        multiSelecView.setGame(b);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void showExplain() {
        if (multiSelecView.isGame()) {
            multiSelecView.show();
        }
        explainView.setVisibility(VISIBLE);
    }

    @Override
    public void showDoing() {
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

}
