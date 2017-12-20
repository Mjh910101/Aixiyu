package com.cn.ispanish.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.LiveComment;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.QuestionComment;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.InsideListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

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
 * Created by Hua on 17/3/10.
 */
public class QuestionCommentAdapter extends BaseAdapter {

    Context context;
    List<QuestionComment> itemList;

    CallbackForComment callback;

    public QuestionCommentAdapter(Context context, List<QuestionComment> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.layout_live_comment_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        QuestionComment comment = itemList.get(i);
        setData(holder, comment);


        return view;
    }

    private void setData(Holder holder, QuestionComment comment) {
        setUserPic(holder.userPic, comment.getUserimg());
        holder.userName.setText(comment.getUsername());
        holder.uploadTime.setText(comment.getUploadTime());
        holder.conntntText.setText(comment.getConten());
        holder.goodText.setText(comment.getGoodnum());
        setConntntList(holder.conntntList, comment.getList());

        setOnCommentIcon(holder.commentIcon, comment);

        if (comment.isGood()) {
            holder.goodIcon.setImageResource(R.drawable.live_good_1);
        } else {
            holder.goodIcon.setImageResource(R.drawable.live_good_0);
        }

        setOnGood(holder.goodIcon, comment);

    }

    private void setOnGood(ImageView view, final QuestionComment comment) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment.isGood()) {
//                    uploadGoodData(UrlHandle.getCleanGood(context), comment);
                } else {
                    uploadGoodData(UrlHandle.getBankCommentGood(context), comment);
                }
            }
        });
    }

    private void uploadGoodData(String url, final QuestionComment comment) {
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("pid", comment.getId());

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                comment.setGood(!comment.isGood());
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void setOnCommentIcon(ImageView view, final QuestionComment comment) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.callback(comment);
                }
            }
        });
    }

    public void setCallback(CallbackForComment callback) {
        this.callback = callback;
    }

    public interface CallbackForComment {
        void callback(QuestionComment comment);
    }

    private void setConntntList(InsideListView listView, List<QuestionComment> list) {

        listView.setAdapter(new Adapter(list));
    }

    private void setUserPic(ImageView imageView, String images) {
        double w = (double) WinHandler.getWinWidth(context) / 9d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) w));

        DownloadImageLoader.loadImage(imageView, images, (int) (w / 2));
    }

    class Holder {

        ImageView userPic;
        ImageView commentIcon;
        ImageView goodIcon;
        TextView userName;
        TextView uploadTime;
        TextView conntntText;
        TextView goodText;
        InsideListView conntntList;

        Holder(View view) {
            userPic = (ImageView) view.findViewById(R.id.liveCommentItem_userPic);
            userName = (TextView) view.findViewById(R.id.liveCommentItem_userName);
            uploadTime = (TextView) view.findViewById(R.id.liveCommentItem_uploadTime);
            conntntText = (TextView) view.findViewById(R.id.liveCommentItem_conntntText);
            goodText = (TextView) view.findViewById(R.id.liveCommentItem_goodText);
            conntntList = (InsideListView) view.findViewById(R.id.liveCommentItem_conntntList);
            commentIcon = (ImageView) view.findViewById(R.id.liveCommentItem_commentIcon);
            goodIcon = (ImageView) view.findViewById(R.id.liveCommentItem_goodIcon);
        }

    }

    class Adapter extends BaseAdapter {

        int p = 15;

        List<QuestionComment> itemList;

        Adapter(List<QuestionComment> itemList) {
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int i) {
            return itemList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            QuestionComment obj = itemList.get(i);

            TextView textView = new TextView(context);

            textView.setText(obj.getUsername() + "：" + obj.getConten());
            textView.setPadding(p, p, p, p);
            textView.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));

            return textView;
        }
    }

}
