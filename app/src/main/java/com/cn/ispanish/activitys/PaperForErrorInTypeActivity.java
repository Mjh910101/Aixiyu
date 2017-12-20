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
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.dialog.InputDialog;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.handlers.FeelBackHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForString;
import com.cn.ispanish.interfaces.OnErrorQuestionListener;
import com.cn.ispanish.views.paper.PaperContentView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
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
public class PaperForErrorInTypeActivity extends BaseActivity {

    public final static String TYPE_KEY = "type";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
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
    @ViewInject(R.id.paper_indexGrid)
    private GridView indexGrid;
    @ViewInject(R.id.paper_indexGridBackgroud)
    private View indexGridBackgroud;

    private int nowPosition;
    private List<Question> questionLiat;
    private String type;
    private List<PaperContentView> questionViewLiat;

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

        questionLiat = new ArrayList<>();
        questionViewLiat = new ArrayList<>();

        titleText.setText("我的错题");

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        initErrorQuestionList(b.getString(TYPE_KEY));
        setContentBoxListener();
    }

    private void setContentBoxListener() {
        pagerLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                nowPosition = position;
                setOnQuestion(position + 1);

                for (int i = 0; i < questionLiat.size(); i++) {
                    try {
                        if (position != i) {
                            questionViewLiat.get(i).onStop();
                        }
                    } catch (Exception e) {

                    }
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

    private void initErrorQuestionList(String type) {
        List<Question> liat;
        try {
            liat = DBHandler.getDbUtils(context).findAll(Selector.from(Question.class).where("type_text", "=", type));
            if (liat != null) {
                setDataPager(liat);
                return;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        MessageHandler.showToast(context, "你还没有错题哟~啾咪！");
    }


    public void setDataPager(List<Question> list) {

        List<ReadingQuestion> readingQuestionList = new ArrayList<>();
        List<ListenQuestion> listenQuestionList = new ArrayList<>();

        for (Question q : list) {
            switch (q.getType()) {
                case Question.YueDu:
                case Question.TianKong:
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

        pagerContentAdapter = new PagerContentAdapter();

        pagerLayout.setAdapter(pagerContentAdapter);
        pagerLayout.setOffscreenPageLimit(1);
        setOnQuestion(1);
    }

    PagerContentAdapter pagerContentAdapter;

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

            Question q = questionLiat.get(position);
            if (viewMap.containsKey(position)) {
                view = viewMap.get(position);
            } else {
                view = getContentView(q, position);
                viewMap.put(position, view);
                questionViewLiat.add(view);
            }
            container.addView(view);
            return view;
        }

        public void deleteItem(Question q) {
//            questionLiat.remove(q);
//            viewMap.remove(q.getId());
//            notifyDataSetChanged();
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
    OnErrorQuestionListener onQuestion = new OnErrorQuestionListener() {
        @Override
        public void onQuestion(Question q, boolean b) {
            if (b) {
                tureSum += 1;
                trueText.setText(String.valueOf(tureSum));
                showDeleteDialog(q);
            } else {
                flaseSum += 1;
                flaseText.setText(String.valueOf(flaseSum));
//                saveErrorQuestion(q);
            }
        }
    };

    private void showDeleteDialog(final Question q) {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setTitel("提示");
        dialog.setMessage("恭喜你答对啦\n是否将ta移出错题本");
        dialog.setCommitStyle("留着吧");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {

            }
        });
        dialog.setCancelStyle("移除");
        dialog.setCancelListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                Question.delete(context, q);
                if (pagerContentAdapter != null) {
                    pagerContentAdapter.deleteItem(q);
                }
            }
        });
    }

    private void saveErrorQuestion(Question q) {
        Question.saveOrUpdate(context, q);
    }

}
