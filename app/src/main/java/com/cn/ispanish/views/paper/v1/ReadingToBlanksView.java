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
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperInputView;
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
 * Created by Hua on 17/5/24.
 */
public class ReadingToBlanksView extends PaperContentView {


    @ViewInject(R.id.paperSingle_questionTypeLayout)
    private LinearLayout questionTypeLayout;
    @ViewInject(R.id.paperSingle_questionTypeText)
    private TextView questionTypeText;
    @ViewInject(R.id.paperSingle_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperSingle_questionExplainText)
    private TextView questionExplainText;
    @ViewInject(R.id.paperSingle_questionExplainLayout)
    private LinearLayout questionExplainLayout;
    @ViewInject(R.id.paperSingle_answerList)
    private InsideListView answerList;
    @ViewInject(R.id.paperSingle_questionPic)
    private ImageView questionPic;
    @ViewInject(R.id.paperSingle_explainButton)
    private RelativeLayout explainButton;
    @ViewInject(R.id.paperSingle_inputView)
    private PaperInputView inputView;
    @ViewInject(R.id.paperSingle_scrollVIew)
    private ScrollView scrollView;


    public ReadingToBlanksView(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion,null);

        view = inflater.inflate(R.layout.layout_paper_single, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);

        initViewData();
    }

    @Override
    public void initViewData() {

        questionTypeLayout.setVisibility(GONE);
        setTitle(questionTitle);
        setOnlyExplain(questionExplainText);
        setPic(questionPic);

        scrollView.setBackgroundResource(R.color.white);
        inputView.initQuestion(question);
        inputView.setVisibility(VISIBLE);
        inputView.setOnCheckListener(new CallbackForBoolean() {
            @Override
            public void callback(boolean b) {
                showExplain();
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, true);
                }
            }
        });
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
    public void showExplain() {
        questionExplainLayout.setVisibility(VISIBLE);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void setTitle(TextView titleView) {
        if (titleView == null) {
            return;
        }
        titleView.setText(
                Html.fromHtml("<font color=\"#3685D6\">" + "题目：" + "</font>" +
                        "<font color=\"#565656\">" + question.getName() + "</font>"));
    }


    @OnClick(R.id.paperSingle_explainButton)
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


