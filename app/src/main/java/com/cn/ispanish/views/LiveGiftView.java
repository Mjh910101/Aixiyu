package com.cn.ispanish.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.play.PlayLiveGiftActivity;
import com.cn.ispanish.activitys.play.PlayOrderActivity;
import com.cn.ispanish.adapters.CommentAdapter;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.LiveGift;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.PlayHandler;
import com.cn.ispanish.handlers.WinHandler;
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
 * Created by Hua on 2017/10/23.
 */

public class LiveGiftView extends LinearLayout {

    private Context context;

    private View view;
    private LayoutInflater inflater;

    @ViewInject(R.id.liveGift_dataList)
    private GridView dataList;
    @ViewInject(R.id.liveGift_sumText)
    private TextView sumText;
    @ViewInject(R.id.liveGift_sendButton)
    private TextView sendButton;

    private LiveGift onGift;
    private int giftSum;

    public LiveGiftView(Context context) {
        super(context);
        initView(context);
    }

    public LiveGiftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LiveGiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LiveGiftView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_live_gift, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void showGift() {
        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBackGift(context),
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
                                setData(JsonHandle.getArray(json, "data"));
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }
                    }
                });
    }

    private void setData(JSONArray array) {
        if (array == null) {
            return;
        }

        List<LiveGift> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            LiveGift obj = new LiveGift(JsonHandle.getJSON(array, i));
            list.add(obj);
        }
        setData(list);
    }

    private void setData(List<LiveGift> list) {
        setVisibility(VISIBLE);
        dataList.setAdapter(new Adapter(list));
    }

    public void close() {
        setVisibility(INVISIBLE);
    }


    private void setGiftSum() {
        sumText.setText("数量：" + giftSum);
        sendButton.setClickable(true);
        sendButton.setBackgroundResource(R.drawable.orange_orange_rounded_5);
    }

    @OnClick(R.id.liveGift_sendButton)
    public void onSend(View view) {
        if (onGift == null) {
            return;
        }

        if (User.isLoginAndShowMessage(context)) {
            Bundle b = new Bundle();
            b.putString(PlayLiveGiftActivity.TOPUP_ID, onGift.getId());
            b.putString(PlayLiveGiftActivity.TOPUP_TITLE, onGift.getName());
            b.putString(PlayLiveGiftActivity.TOPUP_MONEY, onGift.getPrice());

            PassagewayHandler.jumpActivity(context, PlayLiveGiftActivity.class, PlayLiveGiftActivity.Result_Code, b);
        }

    }

    class Adapter extends BaseAdapter {
        List<LiveGift> itemList;

        Adapter(List<LiveGift> list) {
            itemList = list;
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
                view = View.inflate(context, R.layout.layout_live_gift_item, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            setData(holder, itemList.get(i));
            settOnClick(view, itemList.get(i));

            return view;
        }

        private void settOnClick(View view, final LiveGift gift) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onGift != null) {
                        if (onGift.equals(gift)) {
                            giftSum += 1;
                        } else {
                            onGift = gift;
                            giftSum = 1;
                        }
                    } else {
                        onGift = gift;
                        giftSum = 1;
                    }

                    setGiftSum();
                }
            });
        }

        private void setData(Holder holder, LiveGift obj) {
            holder.name.setText(obj.getName());
            holder.price.setText(obj.getPrice());

            double w = (double) WinHandler.getWinWidth(context) / 4d;
            double h = w;
            holder.imgView.setLayoutParams(new LinearLayout.LayoutParams((int) w, (int) h));

            DownloadImageLoader.loadImage(holder.imgView, obj.getImg());
        }
    }

    class Holder {

        ImageView imgView;
        TextView name;
        TextView price;

        Holder(View view) {
            imgView = (ImageView) view.findViewById(R.id.liveGift_imgView);
            name = (TextView) view.findViewById(R.id.liveGift_name);
            price = (TextView) view.findViewById(R.id.liveGift_price);
        }

    }
}
