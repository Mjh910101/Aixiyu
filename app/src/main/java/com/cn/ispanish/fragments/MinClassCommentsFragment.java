package com.cn.ispanish.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.CommentAdapter;
import com.cn.ispanish.adapters.LiveCommentAdapter;
import com.cn.ispanish.box.LiveComment;
import com.cn.ispanish.box.MinClass;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.ColorHandle;
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
public class MinClassCommentsFragment extends BaseFragment {

    private MinClass minClass;

    InputMethodManager imm;

    @ViewInject(R.id.one2OneComments_dataList)
    private ListView dataList;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private EditText inputView;
    private View sendButton;

    private List<LiveComment> list;

    LiveComment commentObj;

    public MinClassCommentsFragment(MinClass minClass, EditText inputView, View sendButton) {
        this.minClass = minClass;
        this.inputView = inputView;
        this.sendButton = sendButton;
        list = new ArrayList<>();
        setSendListener();
    }

    private void setSendListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCommtent();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_one_by_one_comments,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        dowmloadData(minClass.getId());

        return contactsLayout;
    }

    private void dowmloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("lid", id);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getLiveConget(context), params,
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

        JSONArray array = JsonHandle.getArray(data, "conten");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(new LiveComment(JsonHandle.getJSON(array, i)));
            }

            LiveCommentAdapter adapter = new LiveCommentAdapter(context, list);
            adapter.setCallback(new LiveCommentAdapter.CallbackForComment() {
                @Override
                public void callback(LiveComment comment) {
                    commentObj = comment;
                    inputView.setText("");
                    inputView.setHint("回复" + comment.getUsername());
                }
            });
            dataList.setAdapter(adapter);

        }

    }

    private void uploadCommtent() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("lid", minClass.getId());
        params.addBodyParameter("conten", TextHandler.getText(inputView));
        params.addBodyParameter("fid", getFid());

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getLiveCongetPut(context), params,
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
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                closeInput();
                                list.removeAll(list);
                                dowmloadData(minClass.getId());
                            }
                        }
                    }
                });
    }

    public void closeInput() {

        inputView.setText("");
        inputView.setHint("");

        if (imm == null) {
            imm = (InputMethodManager) inputView.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
        }
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(
                    inputView.getApplicationWindowToken(), 0);
        }
    }


    public String getFid() {
        if (commentObj != null) {
            return commentObj.getId();
        }
        return "";
    }
}
