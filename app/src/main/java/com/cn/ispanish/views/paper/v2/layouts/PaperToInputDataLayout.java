package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperExplainView;
import com.cn.ispanish.views.paper.v2.views.PaperInputView;
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
public class PaperToInputDataLayout extends PaperContentView {

    @ViewInject(R.id.paperinputData_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperinputData_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperinputData_inputView)
    private PaperInputView inputView;
    @ViewInject(R.id.paperinputData_explainView)
    private PaperExplainView explainView;

    public PaperToInputDataLayout(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_input_data, null);

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
        inputView.initQuestion(question);
        inputView.setVisibility(VISIBLE);
        inputView.setOnCheckListener(new CallbackForBoolean() {
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
        inputView.showDoing();
    }
}
