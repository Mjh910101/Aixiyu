package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.GameCheckPaperActivity;
import com.cn.ispanish.activitys.GamePaperActivity;
import com.cn.ispanish.activitys.SharePaperActivity;
import com.cn.ispanish.box.PushInfo;
import com.cn.ispanish.box.Ranking;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;

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
 * Created by Hua on 17/6/24.
 */
public class RankingAdapter extends BaseAdapter {

    Context context;
    List<Ranking> itemList;

    public RankingAdapter(Context context, List<Ranking> itemList) {
        this.context = context;
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
        Holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.layout_ranking_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        setData(holder, itemList.get(i));
        setShareButtonOnClick(holder.shareText, itemList.get(i));
        setOnClick(view, itemList.get(i));

        return view;
    }

    private void setShareButtonOnClick(TextView shareText, final Ranking ranking) {
        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putLong(SharePaperActivity.TIME_LONG, ranking.getUseTimeLong());
                b.putString(SharePaperActivity.TITLE_NAME, ranking.getName());
                b.putString(SharePaperActivity.AVERAGER_NUM, ranking.getAverage());
                b.putString(SharePaperActivity.AVERAGER_TEXT, ranking.getAverageText());
                b.putString(SharePaperActivity.MATCH_ID, ranking.getId());
                PassagewayHandler.jumpActivity(context, SharePaperActivity.class, SharePaperActivity.RequestCode, b);

            }
        });
    }

    private void setOnClick(View view, final Ranking ranking) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ranking.isEnd()) {
                    Bundle b = new Bundle();
                    b.putString(GameCheckPaperActivity.ID_KEY, ranking.getId());
                    PassagewayHandler.jumpActivity(context, GameCheckPaperActivity.class, b);
                } else {
                    MessageHandler.showToast(context, "该比赛还没结束~");
                }
            }
        });
    }

    private void setData(Holder holder, Ranking obj) {

        holder.uploadText.setText(obj.getPutime());
        holder.timeText.setText("耗时：" + obj.getTime());
        holder.titleText.setText(obj.getName());
        holder.ordersText.setText(String.valueOf(obj.getOrders()));

        setInfo(holder.infoText, obj);

        if (obj.isShare() || obj.isEnd()) {
            holder.averageText.setText("成绩：" + obj.getAverage());
            holder.shareText.setVisibility(View.GONE);
            holder.infoText.setVisibility(View.VISIBLE);
            holder.uploadText.setVisibility(View.VISIBLE);
        } else {
            holder.averageText.setText("成绩：？");
            holder.shareText.setVisibility(View.VISIBLE);
            holder.infoText.setVisibility(View.INVISIBLE);
            holder.uploadText.setVisibility(View.INVISIBLE);
        }
    }

    private void setInfo(TextView infoText, Ranking obj) {
        infoText.setVisibility(View.INVISIBLE);
        if (!obj.getAverageText().equals("")) {
            infoText.setText(obj.getAverageText());
            infoText.setVisibility(View.VISIBLE);
        }

    }

    class Holder {

        TextView averageText;
        TextView titleText;
        TextView timeText;
        TextView infoText;
        TextView uploadText;
        TextView ordersText;
        TextView shareText;

        Holder(View view) {
            averageText = (TextView) view.findViewById(R.id.rankingItem_averageText);
            titleText = (TextView) view.findViewById(R.id.rankingItem_titleText);
            timeText = (TextView) view.findViewById(R.id.rankingItem_timeText);
            infoText = (TextView) view.findViewById(R.id.rankingItem_infoText);
            uploadText = (TextView) view.findViewById(R.id.rankingItem_uploadText);
            ordersText = (TextView) view.findViewById(R.id.rankingItem_ordersText);
            shareText = (TextView) view.findViewById(R.id.rankingItem_shareText);
        }

    }
}
