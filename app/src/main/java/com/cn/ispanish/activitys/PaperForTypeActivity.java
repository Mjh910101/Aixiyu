package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
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
public class PaperForTypeActivity extends BaseActivity {

    public static final String TYPE_KEY = "type";
    public static final String TIT_TYPE_KEY = "tit_type";
    public static final String TIT_THR_KEY = "tit_thr";
    public static final String PAPER_TITLE_KEY = "title";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.title_toolButton)
    private TextView toolButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paper_paperLayout)
    private ViewPager pagerLayout;
    @ViewInject(R.id.paper_collectionButton)
    private LinearLayout collectionButton;
    @ViewInject(R.id.paper_trueText)
    private TextView trueText;
    @ViewInject(R.id.paper_flaseText)
    private TextView flaseText;
    @ViewInject(R.id.paper_indexText)
    private TextView indexText;
    @ViewInject(R.id.paper_indexGridLayout)
    private LinearLayout indexGridLayout;
    @ViewInject(R.id.paper_indexGrid)
    private GridView indexGrid;
    @ViewInject(R.id.paper_indexGridBackgroud)
    private View indexGridBackgroud;

    private int nowPosition;
    private String type;
    private String tittype, thrId;
    private List<Question> questionLiat;

    private int page = 1;

    private PagerContentAdapter contentAdapter;

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
        if (!User.isLoginAndShowMessage(context)) {
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
//        if (indexGridLayout.getHeight() > WinHandler.dipToPx(context, 1)) {
//            return true;
//        }
//        return false;
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
//        indexGridLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WinHandler.pxToDip(context, 150)));
    }

    private void cloasIndexLayout() {
        indexGrid.setVisibility(View.GONE);
        indexGridBackgroud.setVisibility(View.GONE);
//        indexGridLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WinHandler.dipToPx(context, 1)));
    }

    private void initActivity() {
        collectionButton.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();

        if (b == null) {
            finish();
            return;
        }

        questionLiat = new ArrayList<>();
        titleText.setText(b.getString(PAPER_TITLE_KEY, ""));
//        toolButton.setVisibility(View.VISIBLE);
//        toolButton.setText("答案");
        type = b.getString(TYPE_KEY);
        tittype = b.getString(TIT_TYPE_KEY);
        thrId = b.getString(TIT_THR_KEY);

        setContentBoxListener();
        downloadData(type, thrId);
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

    private void downloadData(String type, String thrId) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("type", type);
        params.addBodyParameter("tittype", tittype);
        params.addBodyParameter("thr", thrId);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("page", String.valueOf(page));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getTypeToBank(context), params,
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
                            JSONArray array = JsonHandle.getArray(json, "data");
                            if (array != null) {
                                setDataPager(array);
                            } else {
                                MessageHandler.showToast(context, "里面没有题目哟~");
                            }
                            page += 1;
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    public void setDataPager(JSONArray array) {

        List<ReadingQuestion> readingQuestionList = new ArrayList<>();
        List<ListenQuestion> listenQuestionList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            Question q = new Question(JsonHandle.getJSON(array, i));
            switch (q.getType()) {
                case Question.TianKong:
                case Question.YueDu:
                case Question.WanXingTianKong:
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

//        if (!readingQuestionList.isEmpty()) {
//            questionLiat.addAll(readingQuestionList);
//        }
//
//        if (!listenQuestionList.isEmpty()) {
//            questionLiat.addAll(listenQuestionList);
//        }

//        Collections.sort(questionLiat);

        if (contentAdapter == null) {
            contentAdapter = new PagerContentAdapter();
            pagerLayout.setAdapter(contentAdapter);
            pagerLayout.setOffscreenPageLimit(1);
            setOnQuestion(1);
        } else {
            contentAdapter.notifyDataSetChanged();
        }

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
        return PaperContentView.getContentView(context, question, position, onQuestion,null);
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
                } else {
                    flaseSum += 1;
                    flaseText.setText(String.valueOf(flaseSum));
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
