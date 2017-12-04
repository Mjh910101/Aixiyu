package com.cn.ispanish.views.paper.v2.layouts;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.ChildViewPager;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.views.PaperAudioView;
import com.cn.ispanish.views.paper.v2.views.PaperTitleView;
import com.cn.ispanish.views.paper.v2.views.PaperTypeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Hua on 17/7/31.
 */
public class PaperToListenLayout extends PaperContentView {

    @ViewInject(R.id.paperListen_typeView)
    private PaperTypeView typeView;
    @ViewInject(R.id.paperListen_titleView)
    private PaperTitleView titleView;
    @ViewInject(R.id.paperListen_audioView)
    private PaperAudioView audioView;
    @ViewInject(R.id.paperListen_questionViewPager)
    private ChildViewPager questionViewPager;

    private ListenQuestion listenQuestion;

    private boolean isGame = false;

    private List<PaperContentView> viewList;
    private PagerContentAdapter contentAdapter;

    public PaperToListenLayout(Context context, ListenQuestion question, int position, OnQuestionListener onQuestion) {
        super(context, question, position, onQuestion);

        view = inflater.inflate(R.layout.layout_paper_listen_v2, null);
        listenQuestion = question;

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);
        initViewData();
    }

    @Override
    public void initViewData() {
        typeView.initQuestion(listenQuestion.getQuestionType(), position);
        titleView.initQuestion(listenQuestion, position);
        titleView.goneTitleLayout();

        audioView.initQuestion(listenQuestion);

        contentAdapter = new PagerContentAdapter();
        questionViewPager.setAdapter(contentAdapter);
        questionViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void onStop() {
        audioView.stopSound();
    }

    @Override
    public void showExplain() {
        for (PaperContentView contentView : viewList) {
            contentView.showExplain();
        }
        contentAdapter.notifyDataSetChanged();
    }

    public void setGame(boolean b) {
        isGame = b;
    }

    @Override
    public void showDoing() {
        for (PaperContentView view : viewList) {
            view.showDoing();
        }
    }
    class PagerContentAdapter extends PagerAdapter {

        private Map<Integer, PaperContentView> viewMap;

        public PagerContentAdapter() {
            viewMap = new HashMap<>();
            viewList = new ArrayList<>();
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
            return listenQuestion.getQuestionList().size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PaperContentView view;

            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(listenQuestion.getQuestionList().get(position), position);
                viewMap.put(position, view);
                viewList.add(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        private PaperContentView getContentView(Question question, int position) {
            position = position + 1;
            ChildInRadioLayout layout = new ChildInRadioLayout(context, question, position, onReadingQuestion);
            layout.setGame(isGame);
            layout.setInChild();
            return layout;
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

    }
}
