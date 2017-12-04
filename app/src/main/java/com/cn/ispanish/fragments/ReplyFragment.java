package com.cn.ispanish.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.CommentAdapter;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
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
 * Created by Hua on 17/4/18.
 */
public class ReplyFragment extends BaseFragment {

    @ViewInject(R.id.videoPlayContent_reply_dataList)
    private ListView dataList;
    @ViewInject(R.id.videoPlayContent_reply_replyInput)
    private EditText replyInput;

    private List<Comment> commentList;

    ProgressBar progress;
    String courseId = "";

    InputMethodManager imm;

    public ReplyFragment() {

    }

    public ReplyFragment(String courseId, ProgressBar progress) {
        this.progress = progress;
        this.courseId = courseId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_video_paly_content_reply,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        downloadComments(courseId);

        return contactsLayout;
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (progress.getVisibility() == View.GONE ) {
                            downloadComments(courseId);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

            }
        });
    }

    public void closeInput() {
        if (imm == null) {
            imm = (InputMethodManager) replyInput.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
        }

        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(
                    replyInput.getApplicationWindowToken(), 0);
        }
    }

    private void downloadComments(String id) {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", id);
        params.addBodyParameter("start", "3");

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getVideoComment(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        if (progress != null) {
                            progress.setVisibility(View.GONE);
                        }
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            initCommenr(JsonHandle.getArray(json, "comment"));
                        }
                        if (progress != null) {
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initCommenr(JSONArray array) {
        if (array == null) {
            return;
        }

        commentList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            commentList.add(new Comment(JsonHandle.getJSON(array, i)));
        }

        initCommenr(commentList);

    }

    private void initCommenr(List<Comment> commentList) {
        dataList.setFocusable(false);
        dataList.setAdapter(new CommentAdapter(context, commentList));
    }

    @OnClick(R.id.videoPlayContent_reply_commitButton)
    public void onCommit(View view) {
        String reply = TextHandler.getText(replyInput);
        if (reply.equals("")) {
            return;
        }
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("content", reply);
        params.addBodyParameter("cid", courseId);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getVideoReply(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        if (progress != null) {
                            progress.setVisibility(View.GONE);
                        }
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        if (progress != null) {
                            progress.setVisibility(View.GONE);
                        }

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            downloadComments(courseId);
                            closeInput();
                        }

                    }
                });

    }


}
