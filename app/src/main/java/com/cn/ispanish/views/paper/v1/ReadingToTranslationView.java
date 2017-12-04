package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperInputView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

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
public class ReadingToTranslationView extends PaperContentView {

    @ViewInject(R.id.paperTranslation_questionTypeLayout)
    private LinearLayout questionTypeLayout;
    @ViewInject(R.id.paperTranslation_questionTypeText)
    private TextView questionTypeText;
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
    @ViewInject(R.id.paperTranslation_answerList)
    private InsideListView answerList;
    @ViewInject(R.id.paperTranslation_inputView)
    private PaperInputView inputView;
    @ViewInject(R.id.paperTranslation_scrollView)
    private ScrollView scrollView;

    public ReadingToTranslationView(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_translation, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }

    @Override
    public void initViewData() {
        inputView.initQuestion(question);
        questionTypeLayout.setVisibility(GONE);
        setTitle(questionTitle);
        setExplain(questionExplainText);
        setPic(questionPic);
        setAnswerList(answerList);
        scrollView.setBackgroundResource(R.color.white);
//        questionTitle.setText("题目" + position + "(" + question.getQuestionType() + ") : " + question.getName());
//        questionExplainText.setText(question.getExplain());
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
    public void setTitle(TextView titleView) {
        if (titleView == null) {
            return;
        }
        titleView.setText(
                Html.fromHtml("<font color=\"#3685D6\">" + "题目：" + "</font>" +
                        "<font color=\"#565656\">" + question.getName() + "</font>"));
//        titleView.setText("题目" + position + ": " + question.getName());
    }

    @Override
    public void setExplain(TextView explainView) {
        if (explainView == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (!question.getRightText().equals("")) {
            sb.append("答案：\n" + question.getRightText() + "\n");
        }
        if (!question.getExplain().equals("")) {
            sb.append("考点：\n" + question.getExplain());
        }
        explainView.setText(sb.toString());
    }

    @Override
    public void onStop() {

    }

    @Override
    public void setAnswerList(InsideListView answerList) {
        List<String> datalist = question.getAnswerList();

        if (answerList == null || datalist == null || datalist.isEmpty()) {
            showExplainButton();
            return;
        }

        if (datalist.size() == 1) {
            String str = datalist.get(0);
            if (str == null || str.equals("") || str.equals("null")) {
                showExplainButton();
                return;
            }
        }

        setExplain(questionExplainText);

        answerAdapter = new AnswerAdapter();
        answerList.setAdapter(answerAdapter);
    }

    private void showExplainButton() {
        explainButton.setVisibility(VISIBLE);
        answerList.setVisibility(GONE);
        setOnlyExplain(questionExplainText);

        if (question.getType() == Question.TianKong) {
            inputView.setVisibility(VISIBLE);
        }
    }

    public void setOnlyExplain(TextView explainView) {
        if (explainView == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (!question.getRightText().equals("")) {
            sb.append("答案：\n" + question.getRightText() + "\n");
        }
        if (!question.getExplain().equals("")) {
            sb.append("考点：\n" + question.getExplain());
        }
        explainView.setText(sb.toString());
    }

    @Override
    public void showDoing() {
    }
}
