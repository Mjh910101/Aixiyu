package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.interfaces.OnQuestionListener;
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
 * Created by Hua on 17/7/31.
 */
public class PaperJudgmentView extends LinearLayout {

    @ViewInject(R.id.paperJudgment_flseIcon)
    private ImageView flseIcon;
    @ViewInject(R.id.paperJudgment_flseText)
    private TextView flseText;
    @ViewInject(R.id.paperJudgment_trueIcon)
    private ImageView trueIcon;
    @ViewInject(R.id.paperJudgment_trueText)
    private TextView trueText;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private Question question;
    private boolean isGame, isShow;
    private boolean onChooes;

    public OnQuestionListener onQuestion;

    public PaperJudgmentView(Context context) {
        super(context);
        initView(context);
    }

    public PaperJudgmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperJudgmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperJudgmentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_judgment, null);

        isGame = false;
        isShow = false;

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void setShow(boolean b) {
        isShow = b;
    }

    public void setGame(boolean b) {
        isGame = b;
    }

    public boolean isGame() {
        return isGame;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setOnQuestion(OnQuestionListener onQuestion) {
        this.onQuestion = onQuestion;
    }

    public void initQuestion(Question question) {
        this.question = question;
        initViewData();
    }

    private void initViewData() {
    }

    public void showExplain() {
        setShow(true);
        if (onChooes) {
            onTrue();
        } else {
            onFalse();
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

    private void setChooesView(boolean onChooes) {
        flseIcon.setImageResource(R.drawable.single_init_icon);
        trueIcon.setImageResource(R.drawable.single_init_icon);

        if (onChooes) {
            trueIcon.setImageResource(R.drawable.single_true_icon);
        } else {
            flseIcon.setImageResource(R.drawable.single_true_icon);
        }
    }

    @OnClick({R.id.paperJudgment_flseButton, R.id.paperJudgment_trueButton})
    public void onButton(View view) {
        if (isShow) {
            return;
        }
        boolean b = false;
        switch (view.getId()) {
            case R.id.paperJudgment_trueButton:
                b = question.isTrue(true);
                question.setDoingRight("1");
                onChooes = true;
                break;
            case R.id.paperJudgment_flseButton:
                b = question.isTrue(false);
                question.setDoingRight("0");
                onChooes = false;
                break;
        }
        setChooesView(onChooes);
        if (onQuestion != null) {
            onQuestion.onQuestion(question, b);
        }
    }

}
