package com.cn.ispanish.views.paper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.PaperForShowExplainActivity;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
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
 * Created by Hua on 17/5/9.
 */
public class PaperInfoView extends LinearLayout {

    @ViewInject(R.id.paperInfo_nilText)
    private TextView nilText;
    @ViewInject(R.id.paperInfo_trueText)
    private TextView trueText;
    @ViewInject(R.id.paperInfo_flaseText)
    private TextView flaseText;
    @ViewInject(R.id.paperInfo_errorButton)
    private TextView errorButton;
    @ViewInject(R.id.paperInfo_allButton)
    private TextView allButton;

    public Context context;
    public View view;
    public LayoutInflater inflater;

    private int sum;
    private List<Question> allList;
    private List<Question> trueList;
    private List<Question> flaseList;

    public PaperInfoView(Context context) {
        super(context);
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_info, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);

        trueList = new ArrayList<>();
        flaseList = new ArrayList<>();

        addView(view);
    }

    public void setAllQuestion(List<Question> allList) {
        this.allList = allList;
        this.sum = allList.size();
        setText(nilText, allList.size());
    }

    public void addTrueQuestion(Question q) {
        trueList.add(q);
        initText();
    }

    public void addFlaseQuestion(Question q) {
        flaseList.add(q);
        initText();
    }

    private void initText() {
        setText(nilText, sum - trueList.size() - flaseList.size());
        setText(trueText, trueList.size());
        setText(flaseText, flaseList.size());
    }

    private void setText(TextView text, int i) {
        text.setText(String.valueOf(i));
    }

    @OnClick(R.id.paperInfo_allButton)
    public void onAllButton(View view) {
        PaperForShowExplainActivity.questionLiat = allList;
        Bundle b = new Bundle();
        b.putString(PaperForShowExplainActivity.PAPER_TITLE_KEY, "全部解析");
        PassagewayHandler.jumpActivity(context, PaperForShowExplainActivity.class, b);
    }

    @OnClick(R.id.paperInfo_errorButton)
    public void onErrorButton(View view) {
        PaperForShowExplainActivity.questionLiat = flaseList;
        Bundle b = new Bundle();
        b.putString(PaperForShowExplainActivity.PAPER_TITLE_KEY, "错题解析");
        PassagewayHandler.jumpActivity(context, PaperForShowExplainActivity.class, b);
    }

}
