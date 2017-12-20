package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.ColorHandle;
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
public class PaperToJudgeInGameView extends PaperContentView {

    @ViewInject(R.id.paperJudge_questionTypeText)
    private TextView questionTypeText;
    @ViewInject(R.id.paperJudge_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperJudge_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperJudge_questionExplainText)
    private TextView questionExplainText;
    @ViewInject(R.id.paperJudge_questionExplainLayout)
    private LinearLayout questionExplainLayout;
    @ViewInject(R.id.paperJudge_flseIcon)
    private ImageView flseIcon;
    @ViewInject(R.id.paperJudge_flseText)
    private TextView flseText;
    @ViewInject(R.id.paperJudge_trueIcon)
    private ImageView trueIcon;
    @ViewInject(R.id.paperJudge_trueText)
    private TextView trueText;
    @ViewInject(R.id.paperJudge_questionPic)
    private ImageView questionPic;

    private boolean onChooes;

    public PaperToJudgeInGameView(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion,null);

        view = inflater.inflate(R.layout.layout_paper_judge, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        this.isShow = false;
        initViewData();
    }

    @Override
    public void initViewData() {
        setNum(questionNumText);
        setType(questionTypeText);
        setTitle(questionTitle);
        setExplain(questionExplainText);
        setPic(questionPic);
//        questionTitle.setText("题目" + position + "(" + question.getQuestionType() + ") : " + question.getName());
//        questionExplainText.setText( question.getExplain());
    }

    @Override
    public void showExplain() {
        isShow = true;
        questionExplainLayout.setVisibility(VISIBLE);
        if (onChooes) {
            onTrue();
        } else {
            onFalse();
        }
    }

    @Override
    public void onStop() {

    }

    @OnClick({R.id.paperJudge_flseButton, R.id.paperJudge_trueButton})
    public void onButton(View view) {
        if (isShow) {
            return;
        }
        boolean b = false;
        switch (view.getId()) {
            case R.id.paperJudge_trueButton:
                b = question.isTrue(true);
                onChooes = true;
                break;
            case R.id.paperJudge_flseButton:
                b = question.isTrue(false);
                onChooes = false;
                break;
        }
        if (onQuestion != null) {
            onQuestion.onQuestion(question, b);
        }
        setChooesView(onChooes);
    }

    private void setChooesView(boolean onChooes) {
        flseIcon.setImageResource(R.drawable.single_init_icon);
        trueIcon.setImageResource(R.drawable.single_init_icon);

        if (onChooes) {
            trueIcon.setImageResource(R.drawable.single_true_icon);
        } else {
            flseIcon.setImageResource(R.drawable.single_true_icon);
        }
    }

    private void onFalse() {
        if (question.isTrue(false)) {
            flseIcon.setImageResource(R.drawable.single_true_icon);
            flseText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
        } else {
            flseIcon.setImageResource(R.drawable.single_flse_icon);
            flseText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));

            trueIcon.setImageResource(R.drawable.single_true_icon);
            trueText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
        }
    }

    private void onTrue() {
        if (question.isTrue(true)) {
            trueIcon.setImageResource(R.drawable.single_true_icon);
            trueText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
        } else {
            trueIcon.setImageResource(R.drawable.single_flse_icon);
            trueText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));

            flseIcon.setImageResource(R.drawable.single_true_icon);
            flseText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
        }
    }

    @Override
    public void showDoing() {
    }

}
