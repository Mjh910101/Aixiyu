package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.question.LineQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.LineLayout;
import com.cn.ispanish.views.paper.v2.views.PaperExplainView;
import com.cn.ispanish.views.paper.v2.views.PaperLineView;
import com.cn.ispanish.views.paper.v2.views.PaperTitleView;
import com.cn.ispanish.views.paper.v2.views.PaperTypeView;
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
 * Created by Hua on 2017/12/6.
 */

public class PaperToLineLayout extends PaperContentView {

    @ViewInject(R.id.paperLine_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperLine_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperLine_lineView)
    private PaperLineView lineView;
    @ViewInject(R.id.paperLine_explainView)
    private PaperExplainView explainView;

    LineQuestion lineQuestion;


    public PaperToLineLayout(Context context, Question question, int position, OnQuestionListener onQuestion, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        super(context, question, position, onQuestion, callbackForComment);

        view = inflater.inflate(R.layout.layout_paper_line, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        this.lineQuestion = (LineQuestion) question;

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }


    @Override
    public void initViewData() {
        typeView.initQuestion(lineQuestion.getQuestionType(), position);
        titleView.initQuestion(lineQuestion, position);
        explainView.initQuestion(question, position, callbackForComment);

        lineView.initQuestion(lineQuestion);
        lineView.show();

        lineView.setOnCheckListener(new CallbackForBoolean() {
            @Override
            public void callback(boolean b) {
                showExplain();
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, b);
                }
            }
        });
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
