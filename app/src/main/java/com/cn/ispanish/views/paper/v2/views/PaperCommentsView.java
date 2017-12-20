package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.QuestionComment;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.InsideListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 2017/12/18.
 */

public class PaperCommentsView extends LinearLayout {

    @ViewInject(R.id.paperComments_hotList)
    private InsideListView hotListView;
    @ViewInject(R.id.paperComments_commentsList)
    private InsideListView allListView;

    private Context context;


    List<QuestionComment> hotList = new ArrayList<>();
    List<QuestionComment> allList = new ArrayList<>();
    QuestionComment hotConnent;

    private View view;
    private LayoutInflater inflater;

    private Question question;
    private int num;
    private QuestionCommentAdapter.CallbackForComment callbackForComment;

    public PaperCommentsView(Context context) {
        super(context);
        initView(context);
    }

    public PaperCommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperCommentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperCommentsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_comments, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void initQuestion(Question question, int num, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        this.question = question;
        this.num = num;
        this.callbackForComment = callbackForComment;
        initViewData();
    }

    private void initViewData() {


    }

    @Override
    public void setVisibility(int visibility) {
        if (callbackForComment == null) {
            super.setVisibility(GONE);
            return;
        }
        super.setVisibility(visibility);
        show();
    }

    public void show() {
        if (callbackForComment == null) {
            return;
        }
        downloadComments();
    }

    public void downloadComments() {

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("tid", question.getId());

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBackBankComment(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
//                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                setData(JsonHandle.getJSON(json, "data"));
                            }
                        }
                    }
                });
    }


    private void setData(JSONObject data) {
        if (data == null) {
            return;
        }

        JSONArray hotArray = JsonHandle.getArray(data, "hot");
        if (hotArray != null) {
            for (int i = 0; i < hotArray.length(); i++) {
                hotList.add(new QuestionComment(JsonHandle.getJSON(hotArray, i)));
            }

            QuestionCommentAdapter adapter = new QuestionCommentAdapter(context, hotList);
            adapter.setCallback(callbackForComment);
            hotListView.setAdapter(adapter);

        }

        JSONArray comArray = JsonHandle.getArray(data, "com");
        if (comArray != null) {
            for (int i = 0; i < comArray.length(); i++) {
                allList.add(new QuestionComment(JsonHandle.getJSON(comArray, i)));
            }

            QuestionCommentAdapter adapter = new QuestionCommentAdapter(context, allList);
            adapter.setCallback(callbackForComment);
            allListView.setAdapter(adapter);

        }

    }

}
