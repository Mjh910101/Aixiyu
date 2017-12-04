package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.QuestionType;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.InsideListView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
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

import mlxy.utils.S;

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
public class PaperForErrorBankActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paperEroorBank_barChart)
    private BarChart mChart;
    @ViewInject(R.id.paperEroorBank_dataList)
    private InsideListView dataList;


    private int nowPosition;
    private List<Question> questionLiat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_bank);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }


    private void initActivity() {
        questionLiat = new ArrayList<>();
        titleText.setText("我的错题");

//        initErrorQuestionList();
        downloadErrorQuestionList();
    }

    private void downloadErrorQuestionList() {

        progress.setVisibility(View.VISIBLE);
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getType2ErrTit(context), params,
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
                            setDataList(JsonHandle.getArray(json, "data"));
                        }
                    }
                });
    }

    private void setDataList(JSONArray array) {
        if (array == null) {
            return;
        }

        List<QuestionType> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new QuestionType(JsonHandle.getJSON(array, i)));
        }

        initErrorQuestionType(list);

    }

    private void initErrorQuestionType(List<QuestionType> list) {
        initView();
        Map<String, List<QuestionType>> questionMap = new HashMap<>();
        List<String> typeList = new ArrayList<>();
        int x = 0;

        List<BarEntry> yVals = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (QuestionType q : list) {
            if (questionMap.containsKey(q.getType())) {
                questionMap.get(q.getType()).add(q);
            } else {
                List<QuestionType> l = new ArrayList<>();
                l.add(q);
                questionMap.put(q.getType(), l);
                typeList.add(q.getType());
            }
            yVals.add(new BarEntry(q.getCount(), x));
            titleList.add(q.getTit());

            x += 1;
        }

//        BarDataSet dataSet = new BarDataSet(yVals, "我的错题数");
        BarDataSet dataSet = new BarDataSet(yVals, "");
        dataSet.setColor(ColorHandle.getColorForID(context, R.color.red_text_c7));

        BarData data = new BarData(titleList, dataSet);
        data.setValueTextSize(10f);

        mChart.setData(data);
        mChart.animateY(1000);
        mChart.setDescription("");

        initQuestionTypeDataList(typeList, questionMap);
    }

    private void initQuestionTypeDataList(List<String> typeList, Map<String, List<QuestionType>> questionMap) {
        dataList.setFocusable(false);
        dataList.setAdapter(new TypeAdapter(typeList, questionMap));
    }

    private void initErrorQuestionList() {
        try {
            questionLiat = DBHandler.getDbUtils(context).findAll(Question.class);
            if (questionLiat != null) {
                initErrorQuestion(questionLiat);
                return;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        MessageHandler.showToast(context, "你还没有错题哟~啾咪！");
    }

    private void initErrorQuestion(List<Question> list) {
        initView();
        Map<String, List<Question>> questionMap = new HashMap<>();
        List<String> typeList = new ArrayList<>();
        for (Question q : list) {
            if (questionMap.containsKey(q.getQuestionType())) {
                questionMap.get(q.getQuestionType()).add(q);
            } else {
                List<Question> l = new ArrayList<>();
                l.add(q);
                questionMap.put(q.getQuestionType(), l);
                typeList.add(q.getQuestionType());
            }
        }

        List<BarEntry> yVals = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        int x = 0;
        for (String type : typeList) {
            List<Question> l = questionMap.get(type);

            yVals.add(new BarEntry(l.size(), x));
            titleList.add(l.get(0).getQuestionType());

            x += 1;
        }

//        BarDataSet dataSet = new BarDataSet(yVals, "我的错题数");
        BarDataSet dataSet = new BarDataSet(yVals, "");
        dataSet.setColor(ColorHandle.getColorForID(context, R.color.red_text_c7));

        BarData data = new BarData(titleList, dataSet);
        data.setValueTextSize(10f);

        mChart.setData(data);
        mChart.animateY(1000);
        mChart.setDescription("");

        initDataList(typeList, questionMap);
    }

    private void initDataList(List<String> typeList, Map<String, List<Question>> questionMap) {
//        dataList.setFocusable(false);
//        dataList.setAdapter(new TypeAdapter(typeList, questionMap));
    }

    private void initView() {
        mChart.setGridBackgroundColor(ColorHandle.getColorForID(context, R.color.red_bg));

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);

        YAxis yl = mChart.getAxisLeft();
        yl.setEnabled(false);

        YAxis yr = mChart.getAxisRight();
        yr.setEnabled(false);
    }

    class TypeAdapter extends BaseAdapter {

        List<String> typeList;
        Map<String, List<QuestionType>> questionMap;

        public TypeAdapter(List<String> typeList, Map<String, List<QuestionType>> questionMap) {
            this.typeList = typeList;
            this.questionMap = questionMap;
        }

        @Override
        public int getCount() {
            return typeList.size();
        }

        @Override
        public Object getItem(int i) {
            return typeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder;
            if (view == null) {
                view = View.inflate(context, R.layout.layout_question_type_item, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            setView(holder, questionMap.get(typeList.get(i)).get(0));
            setOnClick(view, typeList.get(i));

            return view;
        }

        private void setOnClick(View view, final String type) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putString(PaperForQuestionErrorInTypeActivity.TYPE_KEY, String.valueOf(type));
                    PassagewayHandler.jumpActivity(context, PaperForQuestionErrorInTypeActivity.class, b);
                }
            });
        }

        private void setView(Holder holder, QuestionType question) {
//            holder.typeIcon.setImageResource(question.getQuestionIcon());
            DownloadImageLoader.loadImage(holder.typeIcon, question.getImg());
            holder.typeText.setText(question.getTit());
        }

        class Holder {

            ImageView typeIcon;
            TextView typeText;

            Holder(View view) {
                typeIcon = (ImageView) view.findViewById(R.id.questionTypeItem_typeIcon);
                typeText = (TextView) view.findViewById(R.id.questionTypeItem_typeText);
            }

        }
    }

}
