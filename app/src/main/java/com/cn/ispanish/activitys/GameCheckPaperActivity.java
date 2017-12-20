package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.handlers.FeelBackHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.layouts.PaperToClozeLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToListenLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToRadioLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToReadingLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
 * Created by Hua on 17/6/22.
 */
public class GameCheckPaperActivity extends BaseActivity {

    public final static String ID_KEY = "id";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.gamePaper_paperLayout)
    private ViewPager pagerLayout;
    @ViewInject(R.id.gamePaper_indexText)
    private TextView indexText;
    @ViewInject(R.id.gamePaper_timeText)
    private TextView timeText;

    private int nowPosition;
    private String id;

    private PagerContentAdapter contentAdapter;

    private List<Question> questionLiat;
    private List<PaperContentView> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_paper);

        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.gamePaper_feelBackButton)
    public void onFeelBack(View view) {
        try {
            final Question question = questionLiat.get(nowPosition);
            FeelBackHandler.showFeelBackDialog(context, question);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initActivity() {
        titleText.setText("有奖竞猜");

        Bundle b = getIntent().getExtras();

        if (b == null) {
            return;
        }

        id = b.getString(ID_KEY);

        questionLiat = new ArrayList<>();
        setTime();

        setContentBoxListener();
        downloadData(id);

    }

    private void setContentBoxListener() {
        pagerLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                nowPosition = position;
                setOnQuestion(position + 1);
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

    private void showExplain() {
        for (PaperContentView contentView : viewList) {
            contentView.showExplain();
        }
        contentAdapter.notifyDataSetChanged();
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("fid", id);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getSeluserTest(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");

                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                JSONArray array = JsonHandle.getArray(json, "data");
                                setDataPager(array);
//                                showExplain();
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "error"));
                            }

                        }

                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void setTime() {
        timeText.setText("");
    }

    public void setDataPager(JSONArray array) {
        if (array == null) {
            return;
        }

        if (array.length() < 1) {
            return;
        }

        JSONArray recordArray = JsonHandle.getArray(JsonHandle.getJSON(array, 0), "record");
        initDataPager(recordArray);
    }

    public void initDataPager(JSONArray array) {

        List<ReadingQuestion> readingQuestionList = new ArrayList<>();
        List<ListenQuestion> listenQuestionList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = JsonHandle.getJSON(array, i);
            Question q = new Question(JsonHandle.getJSON(json, "prototype"));
            q.setDoingRight(JsonHandle.getString(json, "answer"));
            switch (q.getType()) {
                case Question.YueDu:
                case Question.WanXingTianKong:
                case Question.TianKong:
                    if (readingQuestionList.isEmpty()) {
                        ReadingQuestion readingQuestion = new ReadingQuestion(q);
                        readingQuestionList.add(readingQuestion);
                        questionLiat.add(readingQuestion);
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
                            questionLiat.add(readingQuestion);
                        }
                    }
                    break;
                case Question.TingLi:
                case Question.TingLi_DATI:
                case Question.TingLi_XUANZHE:
                case Question.TingLi_PANDUAN:
                    if (listenQuestionList.isEmpty()) {
                        ListenQuestion listenQuestion = new ListenQuestion(q);
                        listenQuestionList.add(listenQuestion);
                        questionLiat.add(listenQuestion);
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
                            questionLiat.add(listenQuestion);
                        }
                    }
                    break;
                default:
                    questionLiat.add(q);
                    break;
            }
        }

        contentAdapter = new PagerContentAdapter();

        pagerLayout.setAdapter(contentAdapter);
        pagerLayout.setOffscreenPageLimit(1);
        setOnQuestion(1);
    }

    private PaperContentView getContentView(Question question, int position) {
        position = position + 1;
        switch (question.getType()) {
            case Question.WanXingTianKong:
                PaperToClozeLayout clozeLayout = new PaperToClozeLayout(context, (ReadingQuestion) question, position, onQuestion, null);
                return clozeLayout;
            case Question.YueDu:
                PaperToReadingLayout readingLayout = new PaperToReadingLayout(context, (ReadingQuestion) question, position, onQuestion, null);
                return readingLayout;
            case Question.TingLi:
            case Question.TingLi_DATI:
            case Question.TingLi_XUANZHE:
            case Question.TingLi_PANDUAN:
                PaperToListenLayout listenLayout = new PaperToListenLayout(context, (ListenQuestion) question, position, onQuestion, null);
                return listenLayout;
            default:
                PaperToRadioLayout radioLayout = new PaperToRadioLayout(context, question, position, onQuestion, null);
                radioLayout.showRight();
                return radioLayout;
        }
    }

    Map<String, Question> questionMap = new HashMap<>();

    OnQuestionListener onQuestion = new OnQuestionListener() {
        @Override
        public void onQuestion(Question q, boolean b) {
            q.setRight(b);
            if (!questionMap.containsKey(q.getId())) {
                questionMap.put(q.getId(), q);
            }
        }
    };

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
            return questionLiat.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PaperContentView view;
            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(questionLiat.get(position), position);
                viewMap.put(position, view);
                viewList.add(view);
            }
            container.addView(view);

            view.showExplain();

            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }


    }

}
