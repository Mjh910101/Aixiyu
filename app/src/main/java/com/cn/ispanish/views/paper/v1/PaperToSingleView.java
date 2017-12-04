package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.paper.PaperContentView;
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
 * Created by Hua on 17/5/24.
 */
public class PaperToSingleView extends PaperContentView {


    @ViewInject(R.id.paperSingle_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperSingle_questionTypeText)
    private TextView TypeText;
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


    public PaperToSingleView(Context context, Question question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_single, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);

        initViewData();
    }

    @Override
    public void initViewData() {
        setNum(questionNumText);
        setType(TypeText);
        setTitle(questionTitle);
        setExplain(questionExplainText);
        setPic(questionPic);
        setAnswerList(answerList);
    }
    @Override
    public void onStop() {

    }

    @Override
    public void showExplain() {
        questionExplainLayout.setVisibility(VISIBLE);
    }

    @Override
    public void showDoing() {
    }
}


