package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperAudioView;
import com.cn.ispanish.views.paper.v2.views.PaperExplainView;
import com.cn.ispanish.views.paper.v2.views.PaperInputView;
import com.cn.ispanish.views.paper.v2.views.PaperJudgmentView;
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
 * Created by Hua on 17/7/31.
 */
public class PaperToJudgmentLayout extends PaperContentView {

    @ViewInject(R.id.paperJudgemt_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperJudgemt_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperJudgemt_judgmentView)
    private PaperJudgmentView judgmentView;
    @ViewInject(R.id.paperJudgemt_explainView)
    private PaperExplainView explainView;

    public PaperToJudgmentLayout(Context context, Question question, int position, OnQuestionListener onQuestion, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        super(context, question, position, onQuestion, callbackForComment);

        view = inflater.inflate(R.layout.layout_paper_to_judgment, null);

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
        judgmentView.initQuestion(question);
        judgmentView.setOnQuestion(new OnQuestionListener() {
            @Override
            public void onQuestion(Question question, boolean b) {
                if (!judgmentView.isGame()) {
                    showExplain();
                }
                if (onQuestion != null) {
                    onQuestion.onQuestion(question, b);
                }
            }
        });
    }

    public void setGame(boolean b) {
        judgmentView.setGame(b);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void showExplain() {
        judgmentView.showExplain();
        explainView.setVisibility(VISIBLE);
    }

    @Override
    public void showDoing() {
    }
}
