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
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.dialog.GridDialog;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.FeelBackHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
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
public class CollectionPaperQuestionActivity extends BaseActivity {

    public static final String KID_KEY = "kid";
    public static final String PAPER_TITLE_KEY = "title";
    public static final String COLL_KEY = "coll";

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
    @ViewInject(R.id.paper_indexGridLayout)
    private LinearLayout indexGridLayout;
    @ViewInject(R.id.paper_indexGrid)
    private GridView indexGrid;
    @ViewInject(R.id.paper_indexGridBackgroud)
    private View indexGridBackgroud;

    private boolean isCollection;
    private int nowPosition;
    private String kid;
    private List<Question> questionLiat;
    private List<PaperContentView> questionViewLiat;

    private PaperInfoView pPaperInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        ViewUtils.inject(this);

        setFlagSecure(true);

        initActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllAudio();
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

    @OnClick(R.id.paper_collectionButton)
    public void onCollection(View view) {
        Question question = questionLiat.get(nowPosition);
        if (question != null) {
            if (question.iscoll()) {
                collectionQuestion(question, 0);
            } else {
                collectionQuestion(question, 1);
            }
        }
//        if (isCollection) {
//            collection(2);
//        } else {
//            collection(1);
//        }
    }

    @OnClick(R.id.paper_feelBackButton)
    public void onFeelBack(View view) {
        if (!User.isLoginAndShowMessage(context) || nowPosition >= questionLiat.size()) {
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

    private void showIndexDialog() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= questionLiat.size(); i++) {
            list.add(String.valueOf(i));
        }
        final GridDialog dialog = new GridDialog(context);
        dialog.setList(list);
        dialog.setTitleGone();
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                pagerLayout.setCurrentItem(i);
            }
        });
    }

    private void initActivity() {

        Bundle b = getIntent().getExtras();

        if (b == null) {
            finish();
            return;
        }

        pPaperInfoView = new PaperInfoView(context);

        questionLiat = new ArrayList<>();
        questionViewLiat = new ArrayList<>();

        titleText.setText(b.getString(PAPER_TITLE_KEY, ""));
        kid = b.getString(KID_KEY);
        isCollection = b.getBoolean(COLL_KEY);

        setContentBoxListener();
        setCollectionView();
        downloadData(kid);
    }

    private void setContentBoxListener() {
        pagerLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position < questionLiat.size()) {
                    nowPosition = position;
                    setOnQuestion(position + 1);
                }

                stopAudio(position);

                Question question = questionLiat.get(nowPosition);

                showQuestionColl(question);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void showQuestionColl(Question question) {
        if (question != null && question.iscoll()) {
            collectionIcon.setImageResource(R.drawable.star_red_icon);
            collectionText.setText("已收藏");
            collectionText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        } else {
            collectionIcon.setImageResource(R.drawable.star_gray_icon);
            collectionText.setText("收藏");
            collectionText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        }
    }

    private void collectionQuestion(final Question question, int isColl) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("kid", kid);
        params.addBodyParameter("tid", question.getId());
        params.addBodyParameter("iscoll", String.valueOf(isColl));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getTitColl(context), params,
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

                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        MessageHandler.showException(context, json);

                        int code = JsonHandle.getInt(json, "code");
                        if (code == 1) {
                            question.setIscoll(!question.iscoll());
                            showQuestionColl(question);
                        }
                    }
                });
    }

    private void stopAudio(int position) {
        for (int i = 0; i < questionLiat.size(); i++) {
            try {
                if (position != i) {
                    questionViewLiat.get(i).onStop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopAllAudio() {
        for (int i = 0; i < questionLiat.size(); i++) {
            try {
                questionViewLiat.get(i).onStop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setOnQuestion(int position) {
        indexText.setText(position + "/" + questionLiat.size());
    }

    private void collection(int set) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("kid", kid);
        params.addBodyParameter("set", String.valueOf(set));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getCollbank(context), params,
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

                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        MessageHandler.showException(context, json);

                        int code = JsonHandle.getInt(json, "error");
                        if (code == 1031 || code == 1052) {
                            isCollection = !isCollection;
                            setCollectionView();
                        }
                    }
                });
    }

    private void setCollectionView() {
        if (isCollection) {
            collectionIcon.setImageResource(R.drawable.star_red_icon);
            collectionText.setText("已收藏");
            collectionText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        } else {
            collectionIcon.setImageResource(R.drawable.star_gray_icon);
            collectionText.setText("收藏");
            collectionText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        }

    }

    private void downloadData(String kid) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("kid", kid);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBank2CollTit(context), params,
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
                                if (array != null) {
                                    setDataPager(array);
                                }
                            }
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

//        if (!readingQuestionList.isEmpty()) {
//            questionLiat.addAll(readingQuestionList);
//        }

//        if (!listenQuestionList.isEmpty()) {
//            questionLiat.addAll(listenQuestionList);
//        }

//        Collections.sort(questionLiat);

        pPaperInfoView.setAllQuestion(questionLiat);
        pagerLayout.setAdapter(new PagerContentAdapter());
        pagerLayout.setOffscreenPageLimit(1);
        setOnQuestion(1);
        showQuestionColl(questionLiat.get(0));
    }

    class PagerContentAdapter extends PagerAdapter {


        private Map<Integer, PaperContentView> viewMap;

        public PagerContentAdapter() {
            viewMap = new HashMap<>();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                if (position >= questionLiat.size()) {
                    container.removeView(pPaperInfoView);
                } else {
                    container.removeView(viewMap.get(position));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return questionLiat.size() + 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (position >= questionLiat.size()) {
                container.addView(pPaperInfoView);
                return pPaperInfoView;
            }

            PaperContentView view;
            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(questionLiat.get(position), position);
                questionViewLiat.add(view);
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
        return PaperContentView.getContentView(context, question, position, onQuestion);
    }

    int tureSum = 0;
    int flaseSum = 0;
    OnQuestionListener onQuestion = new OnQuestionListener() {
        @Override
        public void onQuestion(Question q, boolean b) {

            if (b) {
                if (!q.isDoing()) {
                    tureSum += 1;
                    trueText.setText(String.valueOf(tureSum));
                }
                pPaperInfoView.addTrueQuestion(q);
            } else {
                if (!q.isDoing()) {
                    flaseSum += 1;
                    flaseText.setText(String.valueOf(flaseSum));
                }
                saveErrorQuestion(q);
                pPaperInfoView.addFlaseQuestion(q);
            }

            q.setRight(b);

        }
    };

    private void saveErrorQuestion(Question q) {
        try {
            if (q instanceof Question) {
                Question.saveOrUpdate(context, q);
            }
        } catch (Exception e) {

        }
    }
}
