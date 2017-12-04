package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.PaperIndexAdapter;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dialog.InputDialog;
import com.cn.ispanish.handlers.FeelBackHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForString;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.PaperInfoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

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
 * Created by Hua on 17/5/9.
 */
public class PaperForShowExplainActivity extends BaseActivity {

    public static final String PAPER_TITLE_KEY = "title";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paper_paperLayout)
    private ViewPager pagerLayout;
    @ViewInject(R.id.paper_indexText)
    private TextView indexText;
    @ViewInject(R.id.paper_collectionIcon)
    private ImageView collectionIcon;
    @ViewInject(R.id.paper_collectionText)
    private TextView collectionText;
    @ViewInject(R.id.paper_trueText)
    private TextView trueText;
    @ViewInject(R.id.paper_flaseText)
    private TextView flaseText;
    @ViewInject(R.id.paper_collectionButton)
    private LinearLayout collectionButton;
    @ViewInject(R.id.paper_indexGrid)
    private GridView indexGrid;
    @ViewInject(R.id.paper_indexGridBackgroud)
    private View indexGridBackgroud;

    private int nowPosition;
    public static List<Question> questionLiat;

    private PaperInfoView pPaperInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.title_toolButton)
    public void onTool(View view) {
        PaperContentView childView = (PaperContentView) pagerLayout.getChildAt(pagerLayout.getCurrentItem());
        childView.showExplain();
    }

    @OnClick(R.id.paper_feelBackButton)
    public void onFeelBack(View view) {
        if (!User.isLoginAndShowMessage(context) || nowPosition < questionLiat.size()) {
            return;
        }
        try {
            final Question question = questionLiat.get(nowPosition);
            FeelBackHandler.showFeelBackDialog(context, question);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.paper_indexLayout})
    public void onIndex(View view) {
        if (isShowIndexLaout()) {
            cloasIndexLayout();
        } else {
            showIndexLayout();
        }
    }

    @OnClick({R.id.paper_indexGridBackgroud})
    public void onIndexBackgroud(View view) {
        cloasIndexLayout();
    }

    private boolean isShowIndexLaout() {
        return indexGrid.getVisibility() == View.VISIBLE;
    }

    PaperIndexAdapter indexAdapter;

    private void showIndexLayout() {
        indexGrid.setVisibility(View.VISIBLE);
        indexGridBackgroud.setVisibility(View.VISIBLE);
        if (indexAdapter == null) {
            indexAdapter = new PaperIndexAdapter(context, questionLiat);
            indexGrid.setAdapter(indexAdapter);
            indexGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    pagerLayout.setCurrentItem(i);
                    cloasIndexLayout();
                }
            });
        } else {
            indexAdapter.notifyDataSetChanged();
        }
    }

    private void cloasIndexLayout() {
        indexGrid.setVisibility(View.GONE);
        indexGridBackgroud.setVisibility(View.GONE);
    }

    private void initActivity() {

        pPaperInfoView = new PaperInfoView(context);
        titleText.setText("错题解析");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            titleText.setText(b.getString(PAPER_TITLE_KEY));
        }

        collectionButton.setVisibility(View.GONE);

        setDataPager();
        setContentBoxListener();
    }

    private void setContentBoxListener() {
        pagerLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < questionLiat.size()) {
                    nowPosition = position;
                    setOnQuestion(position + 1);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void setOnQuestion(int position) {
        indexText.setText(position + "/" + questionLiat.size());
    }

    public void setDataPager() {

        List<ReadingQuestion> readingQuestionList = new ArrayList<>();
        List<ListenQuestion> listenQuestionList = new ArrayList<>();

        List<Question> questionList = new ArrayList<>();

        for (Question q : questionLiat) {
            switch (q.getType()) {
                case Question.YueDu:
                case Question.TianKong:
                case Question.WanXingTianKong:
                    if (readingQuestionList.isEmpty()) {
                        ReadingQuestion readingQuestion = new ReadingQuestion(q);
                        readingQuestionList.add(readingQuestion);
                        questionList.add(readingQuestion);
                    } else {
                        boolean isHave = false;
                        for (ReadingQuestion readingQuestion : readingQuestionList) {
                            if (readingQuestion.equals(q)) {
                                readingQuestion.addQuestion(q);
                                isHave = true;
                            }
                        }
                        if (!isHave) {
                            ReadingQuestion readingQuestion = new ReadingQuestion(q);
                            readingQuestionList.add(readingQuestion);
                            questionList.add(readingQuestion);
                        }
                    }
                    break;
                case Question.TingLi_DATI:
                case Question.TingLi_XUANZHE:
                case Question.TingLi_PANDUAN:
                    if (listenQuestionList.isEmpty()) {
                        ListenQuestion listenQuestion = new ListenQuestion(q);
                        listenQuestionList.add(listenQuestion);
                        questionList.add(listenQuestion);
                    } else {
                        boolean isHave = false;
                        for (ListenQuestion listenQuestion : listenQuestionList) {
                            if (listenQuestion.equals(q)) {
                                listenQuestion.addQuestion(q);
                                isHave = true;
                            }
                        }
                        if (!isHave) {
                            ListenQuestion listenQuestion = new ListenQuestion(q);
                            listenQuestionList.add(listenQuestion);
                            questionList.add(listenQuestion);
                        }
                    }
                    break;
                default:
                    questionList.add(q);
                    break;
            }
        }

//        if (!readingQuestionList.isEmpty()) {
//            questionList.addAll(readingQuestionList);
//        }
//
//        if (!listenQuestionList.isEmpty()) {
//            questionList.addAll(listenQuestionList);
//        }

//        Collections.sort(questionList);

        setDataPager(questionLiat);
//        setDataPager(questionList);
    }

    public void setDataPager(List<Question> liat) {
        pPaperInfoView.setAllQuestion(liat);
        pagerLayout.setAdapter(new PagerContentAdapter(liat));
        pagerLayout.setOffscreenPageLimit(1);
        setOnQuestion(1);
    }

    class PagerContentAdapter extends PagerAdapter {

        private List<Question> itemList;
        private Map<Integer, PaperContentView> viewMap;

        public PagerContentAdapter(List<Question> itemList) {
            viewMap = new HashMap<>();
            this.itemList = itemList;
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
            return itemList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PaperContentView view;
            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(itemList.get(position), position);
                viewMap.put(position, view);
            }

            view.showExplain();
            view.showDoing();

            container.addView(view);
            return view;
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    private PaperContentView getContentView(Question question, int position) {
        return PaperContentView.getContentView(context, question, position, onQuestion);
    }

    int tureSum = 0;
    int flaseSum = 0;
    OnQuestionListener onQuestion = new OnQuestionListener() {
        @Override
        public void onQuestion(Question q, boolean b) {
            if (!q.isDoing()) {
                if (b) {
                    tureSum += 1;
                    trueText.setText(String.valueOf(tureSum));
                    pPaperInfoView.addTrueQuestion(q);
                } else {
                    flaseSum += 1;
                    flaseText.setText(String.valueOf(flaseSum));
                    pPaperInfoView.addFlaseQuestion(q);
                }
            }
            if (!b) {
                saveErrorQuestion(q);
            }
            q.setRight(b);
        }
    };

    private void saveErrorQuestion(Question q) {
        Question.saveOrUpdate(context, q);
    }
}
