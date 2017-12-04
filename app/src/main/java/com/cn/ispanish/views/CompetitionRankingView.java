package com.cn.ispanish.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.CompetitionRankingAdapter;
import com.cn.ispanish.box.Ranking;
import com.cn.ispanish.box.RankingTap;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.utils.L;

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
 * Created by Hua on 17/6/23.
 */
public class CompetitionRankingView extends LinearLayout {

    @ViewInject(R.id.competitionRanking_viewPage_contentItem)
    private ListView dataList;
    @ViewInject(R.id.competitionRanking_viewPage_sumText)
    private TextView sumText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private Context context;
    private RankingTap rankingTap;
    private View view;
    private LayoutInflater inflater;
    private int goingType;
    private String titleName;
    private boolean isShare = false;

    CompetitionRankingAdapter adapter;

    public CompetitionRankingView(Context context, RankingTap rankingTap) {
        super(context);

        this.context = context;
        this.rankingTap = rankingTap;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_comoetiton_ranking_content, null);

        ViewUtils.inject(this, view);

        addView(view);

        dowmloadData();
    }

    private void dowmloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sid", rankingTap.getId());
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getTestPaperRank(context), params,
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
                            JSONObject dataJson = JsonHandle.getJSON(json, "data");
                            if (dataJson != null) {
                                setDataView(dataJson);
                                setDataList(JsonHandle.getArray(dataJson, "conten"), JsonHandle.getInt(dataJson, "going"), JsonHandle.getInt(dataJson, "sharetype"));
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }

                });
    }

    private void setDataView(JSONObject json) {
        titleName = JsonHandle.getString(json, "name");
        String str = "参加人数：" + JsonHandle.getInt(json, "titnum");
//        String str = titleName + "参加人数：" + JsonHandle.getInt(json, "titnum");
        sumText.setText(str);
    }

    private void setDataList(JSONArray array, int goingType, int sharetype) {
        this.isShare = sharetype == 1;
        this.goingType = goingType;
        if (array == null) {
            return;
        }

        List<Ranking> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Ranking obj = new Ranking(JsonHandle.getJSON(array, i));
            obj.setId(rankingTap.getId());
            list.add(obj);
        }

        adapter = new CompetitionRankingAdapter(context, titleName, list, goingType, isShare);
        dataList.setAdapter(adapter);
        dataList.setSelection(adapter.getMysileIndex(context));
    }

}
