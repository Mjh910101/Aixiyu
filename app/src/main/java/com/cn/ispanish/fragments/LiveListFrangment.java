package com.cn.ispanish.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.LiveRoomActivity;
import com.cn.ispanish.activitys.MinClassContentActivity;
import com.cn.ispanish.activitys.OneByOneContentActivity;
import com.cn.ispanish.adapters.LiveIndexAdapter;
import com.cn.ispanish.adapters.MinClassAdapter;
import com.cn.ispanish.adapters.OneToOneAdapter;
import com.cn.ispanish.box.Banner;
import com.cn.ispanish.box.Live;
import com.cn.ispanish.box.MinClass;
import com.cn.ispanish.box.OneByOne;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.live.LiveTestActivity;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.FloatListView;
import com.cn.ispanish.views.banner.BannerView;
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
 * Created by Hua on 17/8/18.
 */
public class LiveListFrangment extends BaseFragment {

    private final static String HOT_TYPE = "1";
    private final static String BOOK_TYPE = "2";
    private final static String PLAY_TYPE = "3";
    private final static String ONE_TYPE = "4";
    private final static String SMALL_TYPE = "5";

    @ViewInject(R.id.live_listView)
    private FloatListView floatListView;
    @ViewInject(R.id.live_containerLayout)
    private LinearLayout container;
    @ViewInject(R.id.live_toolContainerLayout)
    private ViewGroup butsContainer;
    @ViewInject(R.id.live_hotIconText)
    private TextView hotIconText;
    @ViewInject(R.id.live_hotText)
    private TextView hotText;
    @ViewInject(R.id.live_bookIconText)
    private TextView bookIconText;
    @ViewInject(R.id.live_bookText)
    private TextView bookText;
    @ViewInject(R.id.live_playbackIconText)
    private TextView playbackIconText;
    @ViewInject(R.id.live_playbackText)
    private TextView playbackText;
    @ViewInject(R.id.live_oneToOneIconText)
    private TextView oneToOneIconText;
    @ViewInject(R.id.live_oneToOneText)
    private TextView oneToOneText;
    @ViewInject(R.id.live_smallIconText)
    private TextView smallIconText;
    @ViewInject(R.id.live_smallText)
    private TextView smallText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.live_bannerLayout)
    private RelativeLayout bannerLayout;

    private BannerView headBannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_live,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initView();

        downloadBanner();
        onToolButton(contactsLayout.findViewById(R.id.live_hotButton));

        return contactsLayout;
    }

    private void downloadBanner() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("type", "10");

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBackBanner(context), params,
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
                            JSONArray dataArray = JsonHandle.getArray(json, "data");
                            initBanner(dataArray);
                        }
                    }
                });
    }

    private void initBanner(JSONArray array) {
        if (array == null) {
            return;
        }
        List<Banner> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new Banner(JsonHandle.getJSON(array, i)));
        }

        headBannerView = new BannerView(context, list);
        bannerLayout.addView(headBannerView);
    }

    @OnClick({R.id.live_hotButton, R.id.live_bookButton, R.id.live_playbackButton, R.id.live_oneToOneButton, R.id.live_smallButton})
    public void onToolButton(View view) {
        initButton();
        switch (view.getId()) {
            case R.id.live_hotButton:
                onHotButton();
                break;
            case R.id.live_bookButton:
                onBookButton();
                break;
            case R.id.live_playbackButton:
                onPlayBackButton();
                break;
            case R.id.live_oneToOneButton:
                onOneButton();
                break;
            case R.id.live_smallButton:
                onSmallButton();
                break;
        }
    }

    private void onHotButton() {
        hotIconText.setBackgroundResource(R.drawable.orange_whlit_rounded_5);
        hotIconText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        hotText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dowmlaodData(HOT_TYPE);
    }

    private void onBookButton() {
        bookIconText.setBackgroundResource(R.drawable.orange_whlit_rounded_5);
        bookIconText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        bookText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dowmlaodData(BOOK_TYPE);
    }

    private void onPlayBackButton() {
        playbackIconText.setBackgroundResource(R.drawable.orange_whlit_rounded_5);
        playbackIconText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        playbackText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dowmlaodData(PLAY_TYPE);
    }

    private void onOneButton() {
        oneToOneIconText.setBackgroundResource(R.drawable.orange_whlit_rounded_5);
        oneToOneIconText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        oneToOneText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dowmlaodOneData();
    }

    private void onSmallButton() {
        smallIconText.setBackgroundResource(R.drawable.orange_whlit_rounded_5);
        smallIconText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        smallText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dowmlaodMinData();
    }

    private void initButton() {
        hotIconText.setBackgroundResource(R.drawable.gray_whilt_rounded_5);
        bookIconText.setBackgroundResource(R.drawable.gray_whilt_rounded_5);
        playbackIconText.setBackgroundResource(R.drawable.gray_whilt_rounded_5);
        oneToOneIconText.setBackgroundResource(R.drawable.gray_whilt_rounded_5);
        smallIconText.setBackgroundResource(R.drawable.gray_whilt_rounded_5);

        hotIconText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        bookIconText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        playbackIconText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        oneToOneIconText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        smallIconText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));

        hotText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        bookText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        playbackText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        oneToOneText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        smallText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));

        floatListView.setAdapter(null);
    }

    private void initView() {
        int height = WinHandler.getWinHeight(context);
        int butsContainerHeight = (int) getResources().getDimension(R.dimen.buts_container_height);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT
                        , height - butsContainerHeight - getStatusBarHeight());

        floatListView.setLayoutParams(params);
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void dowmlaodMinData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getMiniClassList(context),
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
                                setMinDataArray(JsonHandle.getArray(json, "data"));
                            }
                        }
                    }
                });
    }

    private void dowmlaodOneData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getOneByOne(context),
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
                                setOneDataArray(JsonHandle.getArray(json, "data"));
                            }
                        }
                    }
                });
    }

    private void dowmlaodData(String type) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("type", type);
//        params.addBodyParameter("channel", type);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getLiveIndex(context), params,
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
                                setDataArray(JsonHandle.getArray(json, "data"));
                            }
                        }
                    }
                });
    }

    private void setDataArray(JSONArray array) {
        if (array == null) {
            return;
        }

        List<Live> liveList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Live obj = new Live(JsonHandle.getJSON(array, i));
            liveList.add(obj);
        }

        setDataArray(liveList);
    }

    private void setOneDataArray(JSONArray array) {
        if (array == null) {
            return;
        }

        List<OneByOne> oneList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            OneByOne obj = new OneByOne(JsonHandle.getJSON(array, i));
            oneList.add(obj);
        }

        setOneDataArray(oneList);

    }

    private void setMinDataArray(JSONArray array) {
        if (array == null) {
            return;
        }

        List<MinClass> minList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            MinClass obj = new MinClass(JsonHandle.getJSON(array, i));
            minList.add(obj);
        }

        setMinDataArray(minList);

    }

    private void setMinDataArray(List<MinClass> oneList) {
        final MinClassAdapter adapter = new MinClassAdapter(context, oneList);
        floatListView.setAdapter(adapter);
        floatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MinClass obj = (MinClass) adapter.getItem(i);
                Bundle b = new Bundle();
                b.putSerializable(MinClassContentActivity.OBJ_KEY, obj);
                PassagewayHandler.jumpActivity(context, MinClassContentActivity.class, b);
            }
        });
        initListPosition();
    }

    private void setOneDataArray(List<OneByOne> oneList) {
        final OneToOneAdapter adapter = new OneToOneAdapter(context, oneList);
        floatListView.setAdapter(adapter);
        floatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OneByOne obj = (OneByOne) adapter.getItem(i);
                Bundle b = new Bundle();
                b.putSerializable(OneByOneContentActivity.OBJ_KEY, obj);
                PassagewayHandler.jumpActivity(context, OneByOneContentActivity.class, b);
            }
        });
        initListPosition();
    }

    private void setDataArray(List<Live> liveList) {
        final LiveIndexAdapter adapter = new LiveIndexAdapter(context, liveList);
        floatListView.setAdapter(adapter);
        floatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(User.isLoginAndShowMessage(context)){
                    Live live = (Live) adapter.getItem(i);
                    Bundle b = new Bundle();
                    b.putSerializable(LiveRoomActivity.LIVE_KEY, live);
                    PassagewayHandler.jumpActivity(context, LiveRoomActivity.class, b);
                }
            }
        });

        initListPosition();
    }

    private void initListPosition(){
//        int butsContainerHeight = (int) getResources().getDimension(R.dimen.buts_container_height);
//        container.scrollTo(0, butsContainerHeight + getStatusBarHeight()+headBannerView.getHeight());
//
//        int pos = floatListView.getCount() - 1;
//        if (android.os.Build.VERSION.SDK_INT >= 8) {
//            floatListView.smoothScrollToPosition(pos);
//        } else {
//            floatListView.setSelection(pos);
//        }
//
//        container.scrollTo(0, 0);
//        floatListView.smoothScrollToPosition(0);
//        adapter.notifyDataSetChanged();

        container.scrollTo(0, 0);
    }

}
