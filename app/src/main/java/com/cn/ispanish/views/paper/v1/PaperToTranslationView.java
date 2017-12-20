package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.OnQuestionListener;
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
public class PaperToTranslationView extends PaperContentView {

    @ViewInject(R.id.paperTranslation_questionTypeLayout)
    private LinearLayout questionTypeLayout;
    @ViewInject(R.id.paperTranslation_questionTypeText)
    private TextView questionTypeText;
    @ViewInject(R.id.paperTranslation_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperTranslation_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperTranslation_questionExplainText)
    private TextView questionExplainText;
    @ViewInject(R.id.paperTranslation_questionExplainLayout)
    private LinearLayout questionExplainLayout;
    @ViewInject(R.id.paperTranslation_explainButton)
    private RelativeLayout explainButton;
    @ViewInject(R.id.paperTranslation_questionPic)
    private ImageView questionPic;

    public PaperToTranslationView(Context context, Question question, int position, OnQuestionListener onQuestion,QuestionCommentAdapter.CallbackForComment callbackForComment) {
        super(context, question, position, onQuestion,callbackForComment);

        view = inflater.inflate(R.layout.layout_paper_translation, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }

    @Override
    public void initViewData() {
        setNum(questionNumText);
        setType(questionTypeText);
        setTitle(questionTitle);
        setExplain(questionExplainText, explainButton);
        setPic(questionPic);
//        questionTitle.setText("题目" + position + "(" + question.getQuestionType() + ") : " + question.getName());
//        questionExplainText.setText(question.getExplain());
    }

    @Override
    public void onStop() {

    }

    @Override
    public void showExplain() {
        questionExplainLayout.setVisibility(VISIBLE);
    }

    @OnClick(R.id.paperTranslation_explainButton)
    public void onButton(View view) {
        explainButton.setVisibility(GONE);
        showExplain();
        if (onQuestion != null) {
            onQuestion.onQuestion(question, true);
        }
    }
    @Override
    public void showDoing() {
    }

}
