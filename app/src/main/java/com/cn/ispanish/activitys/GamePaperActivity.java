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
import com.cn.ispanish.box.Ranking;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.dialog.InputDialog;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.handlers.FeelBackHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForString;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.cn.ispanish.views.paper.v2.layouts.PaperToClozeLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToJudgmentLayout;
import com.cn.ispanish.views.paper.v2.layouts.ChildInRadioLayout;
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
public class GamePaperActivity extends BaseActivity {

    public final static String ID_KEY = "id";
    public final static String TIME_KEY = "time";
    public final static String TITLE_KEY = "title";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.title_toolButton)
    private TextView toolButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.gamePaper_paperLayout)
    private ViewPager pagerLayout;
    @ViewInject(R.id.gamePaper_indexText)
    private TextView indexText;
    @ViewInject(R.id.gamePaper_timeText)
    private TextView timeText;

    private int nowPosition;
    private int time;
    private String id, title;
    private boolean isUpload = false;

    private PagerContentAdapter contentAdapter;

    private List<Question> questionLiat;
    private List<PaperContentView> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_paper);

        ViewUtils.inject(this);

        setFlagSecure(true);

        initActivity();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        close();
    }

    @OnClick(R.id.title_toolButton)
    public void onUpload(View view) {
        if (isDoingAll()) {
            showUploadDialog();
        } else {
            showUnDoingUploadDialog();
        }
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
        toolButton.setText("提交");
        toolButton.setVisibility(View.VISIBLE);

        Bundle b = getIntent().getExtras();

        if (b == null) {
            return;
        }

        time = b.getInt(TIME_KEY);
        id = b.getString(ID_KEY);
        title = b.getString(TITLE_KEY);

//        titleText.setText("这里共有差不多十个字");
        titleText.setText(title);

        questionLiat = new ArrayList<>();

        setTime(time * 60);

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

    private void uploadQuestuon() {
        cancelTimer();

        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("fid", id);
        params.addBodyParameter("usetime", String.valueOf(time * 60 - allTime));
        params.addBodyParameter("conten", getPaperConten());

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getTakeranking(context), params,
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

                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
//                            isUpload = true;
//                            showResultDialog(json);
//                            toolButton.setClickable(false);
//                            jumpResultActivity(json);
                            jumpSharePaperActivity(json);
                        }

                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void jumpSharePaperActivity(JSONObject json) {

        String score = JsonHandle.getString(json, "data");
        long t = time * 60 - allTime;

        Bundle b = new Bundle();
        b.putLong(SharePaperActivity.TIME_LONG, t);
        b.putString(SharePaperActivity.TITLE_NAME, title);
        b.putString(SharePaperActivity.AVERAGER_NUM, score);
        b.putString(SharePaperActivity.AVERAGER_TEXT, Ranking.getAverageText(score));
        b.putString(SharePaperActivity.MATCH_ID, id);
        PassagewayHandler.jumpActivity(context, SharePaperActivity.class, SharePaperActivity.RequestCode, b);
        finish();
    }

    private void jumpResultActivity(JSONObject json) {
        int score = JsonHandle.getInt(json, "data");
        long t = time * 60 - allTime;

        Bundle b = new Bundle();
        b.putInt(GameResultActivity.SCORE, score);
        b.putLong(GameResultActivity.TIME, t);
        PassagewayHandler.jumpActivity(context, GameResultActivity.class, b);
        finish();

    }

    private void showResultDialog(JSONObject json) {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("提交成功!");
        dialog.setMessage("您的得分是" + JsonHandle.getInt(json, "data") + "\n用时" + getTime(time * 60 - allTime) + "");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCommitStyle("查看答案");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {

            }
        });
        showExplain();

    }

    private void showExplain() {
        for (PaperContentView contentView : viewList) {
            contentView.showExplain();
        }
        contentAdapter.notifyDataSetChanged();
    }

    private String getPaperConten() {

        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();

        for (Question q : questionLiat) {
            JsonHandle.put(array, q.getRightJson());
        }

        JsonHandle.put(json, "callback", array);

        try {
            Log.e("", URLEncoder.encode(json.toString(), "utf-8"));
//            return  URLEncoder.encode(json.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("", json.toString());
        try {
            String newStr = new String(json.toString().getBytes(), "utf-8");
            Log.e("", newStr);
            return newStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("jid", id);
        params.addBodyParameter("isNew", "1");

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getMatchTotitle(context), params,
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
                                    startTimeRun();
                                    setDataPager(array);
                                }
                            } else {
                                MessageHandler.showToast(context, JsonHandle.getString(json, "error"));
                            }
                        }

                        progress.setVisibility(View.GONE);
                    }
                });
    }

    long allTime;
    Timer timer;

    private void startTimeRun() {
        allTime = time * 60;
//        allTime = 100;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 0, 1000);
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            allTime -= 1;
            if (allTime <= 0) {
                timeText.setText("时间到！");
                showTimeOutDialog();
                cancelTimer();
            } else {
                setTime(allTime);
            }
        }
    };

    private void setTime(long s) {
        timeText.setText("时间 " + getTime(s));
    }

    private String getTime(long s) {
        StringBuilder sb = new StringBuilder();
//        sb.append("时间 ");

        int time_m = (int) (s / 60);
        int time_s = (int) (s % 60);

        sb.append(time_m);
        sb.append(":");
        if (time_s < 10) {
            sb.append("0");
        }
        sb.append(time_s);

        return sb.toString();
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

//        for (int i = 0; i < array.length(); i++) {
//            Question q = new Question(JsonHandle.getJSON(array, i));
//            questionLiat.add(q);
//        }

        contentAdapter = new PagerContentAdapter();

        pagerLayout.setAdapter(contentAdapter);
        pagerLayout.setOffscreenPageLimit(1);
        setOnQuestion(1);
    }

    private PaperContentView getContentView(Question question, int position) {
        position = position + 1;
        switch (question.getType()) {
            case Question.WanXingTianKong:
                PaperToClozeLayout clozeLayout = new PaperToClozeLayout(context, (ReadingQuestion) question, position, onQuestion);
                clozeLayout.setGame(true);
                return clozeLayout;
            case Question.YueDu:
                PaperToReadingLayout readingLayout = new PaperToReadingLayout(context, (ReadingQuestion) question, position, onQuestion);
                readingLayout.setGame(true);
                return readingLayout;
            case Question.TingLi:
            case Question.TingLi_DATI:
            case Question.TingLi_XUANZHE:
            case Question.TingLi_PANDUAN:
                PaperToListenLayout listenLayout = new PaperToListenLayout(context, (ListenQuestion) question, position, onQuestion);
                listenLayout.setGame(true);
                return listenLayout;
            default:
                PaperToRadioLayout radioLayout = new PaperToRadioLayout(context, question, position, onQuestion);
                radioLayout.setGame(true);
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


    private void showTimeOutDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("比赛时间到了哟~");
        dialog.setMessage("提交后将无法再次参加本次比赛!");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCommitStyle("提交");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                uploadQuestuon();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    private void close() {
        if (isUpload) {
            finish();
        } else {
            showCloseDialog();
        }
    }

    private void showCloseDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("提示");
        dialog.setMessage("退出后将无法再次参加比赛了哟~");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCommitStyle("退出");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                finish();
            }
        });
        dialog.setCancelStyle("取消");
        dialog.setCancelListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {

            }
        });
    }

    private void showUploadDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("提交后将无法再次参加本次比赛");
        dialog.setMessage("是否提交！");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCommitStyle("提交");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                uploadQuestuon();
            }
        });
        dialog.setCancelStyle("取消");
        dialog.setCancelListener(null);
    }

    private void showUnDoingUploadDialog() {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("您还有未完成的题目哟~\n" +
                "提交后将无法再次参加本次比赛");
        dialog.setMessage("是否提交！");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCommitStyle("交卷");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                uploadQuestuon();
            }
        });
        dialog.setCancelStyle("取消");
        dialog.setCancelListener(null);
    }

    public boolean isDoingAll() {
        for (Question q : questionLiat) {
            if (!q.isDoing()) {
                return false;
            }
        }
        return true;
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
            return questionLiat.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PaperContentView view;

            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
                if (isUpload) {
                    view.showExplain();
                }
            } else {
                view = getContentView(questionLiat.get(position), position);
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


    }

}
