package com.cn.ispanish.views.paper.v1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.ChildViewPager;
import com.cn.ispanish.views.paper.PaperContentView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

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
public class PaperToReadingView extends PaperContentView {


    @ViewInject(R.id.paperReading_questionTypeText)
    private TextView questionTypeText;
    @ViewInject(R.id.paperReading_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperReading_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperReading_questionPic)
    private ImageView questionPic;
    @ViewInject(R.id.paperReading_questionViewPager)
    private ChildViewPager questionViewPager;

    private ReadingQuestion question;

    public PaperToReadingView(Context context, ReadingQuestion question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_reading, null);

        this.question = question;

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
        setPic(questionPic);

        questionViewPager.setAdapter(new PagerContentAdapter());
        questionViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void showExplain() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void setTitle(TextView titleView) {
        if (titleView == null) {
            return;
        }
        titleView.setText(question.getArticle());
//        titleView.setText("题目" + position + "(" + question.getQuestionType() + ") : " + question.getArticle());
    }

    @Override
    public void setPic(ImageView picView) {
        String picUrl = question.getImageUrl();
        if (picView == null) {
            return;
        }
        if (picUrl == null) {
            return;
        }
        if (picUrl.equals("")) {
            return;
        }
        if (picUrl.equals("null")) {
            return;
        }

        DownloadImageLoader.loadImage(picView, picUrl);
    }

    class PagerContentAdapter extends PagerAdapter {

        private Map<Integer, PaperContentView> viewMap;

        public PagerContentAdapter() {
            viewMap = new HashMap<>();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView(viewMap.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return question.getQuestionList().size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PaperContentView view;

            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(question.getQuestionList().get(position), position);
                viewMap.put(position, view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    private PaperContentView getContentView(Question question, int position) {
        position = position + 1;
        switch (question.getType()) {
            case Question.YueDu:
                return new ReadingToSingleView(context, question, position, onReadingQuestion);
            case Question.TianKong:
                return new ReadingToBlanksView(context, question, position, onReadingQuestion);
            default:
                return new ReadingToTranslationView(context, question, position, onReadingQuestion);
        }
    }

    OnQuestionListener onReadingQuestion = new OnQuestionListener() {
        @Override
        public void onQuestion(Question q, boolean b) {
            if (onQuestion != null) {
                onQuestion.onQuestion(question, b);
            }
            if (!b) {
                Question.saveOrUpdate(context, q);
            }
        }
    };

    @Override
    public void showDoing() {
    }
}



