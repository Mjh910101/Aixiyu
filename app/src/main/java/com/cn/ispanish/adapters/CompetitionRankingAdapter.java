package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.SharePaperActivity;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.Ranking;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;

import java.util.List;

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
 * Created by Hua on 17/6/12.
 */
public class CompetitionRankingAdapter extends BaseAdapter {

    Context context;
    List<Ranking> list;
    int goingType;
    String titleName;
    boolean isShare;

    public CompetitionRankingAdapter(Context context, String titleName, List<Ranking> list, int goingType, boolean isShare) {
        this.context = context;
        this.list = list;
        this.titleName = titleName;
        this.goingType = goingType;
        this.isShare = isShare;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.layout_competition_ranking_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Ranking ranking = list.get(i);
        setViewData(holder, ranking, i);

        return view;
    }

    public int getMysileIndex(Context context) {
        for (int i = 0; i < list.size(); i++) {
            Ranking obj = list.get(i);
            if (obj.isMe(context)) {
                return i;
            }
        }
        return 0;
    }

    private void setViewData(Holder holder, Ranking ranking, int i) {
        holder.medalIcon.setVisibility(View.VISIBLE);
        holder.medalText.setVisibility(View.INVISIBLE);
        switch (i) {
            case 0:
                holder.medalIcon.setImageResource(R.drawable.gold_medal_icon);
                break;
            case 1:
                holder.medalIcon.setImageResource(R.drawable.silver_medal_icon);
                break;
            case 2:
                holder.medalIcon.setImageResource(R.drawable.bronze_medal_icon);
                break;
            default:
                holder.medalIcon.setVisibility(View.INVISIBLE);
                holder.medalText.setVisibility(View.VISIBLE);
                holder.medalText.setText(String.valueOf(i + 1));
                break;
        }
        setUserIcon(holder.userIcon, ranking.getPortrait());
        holder.resultsText.setText(ranking.getAverage());
        holder.userNameText.setText(ranking.getName());
        setTime(holder.timeText, ranking.getUsetime());

        setShareButtonOnClick(holder.shareText, ranking);

        if(ranking.isLuck()){
            holder.lickIcon.setVisibility(View.VISIBLE);
        }else {
            holder.lickIcon.setVisibility(View.INVISIBLE);
        }

        switch (goingType) {
            case 2:
                holder.resultsText.setVisibility(View.VISIBLE);
                holder.shareText.setVisibility(View.GONE);
                break;
            default:
                setShareButton(holder.shareText, holder.resultsText, ranking);
                break;
        }


    }

    private void setShareButtonOnClick(TextView shareText, final Ranking ranking) {
        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putLong(SharePaperActivity.TIME_LONG, ranking.getUseTimeLong());
                b.putString(SharePaperActivity.TITLE_NAME, titleName);
                b.putString(SharePaperActivity.AVERAGER_NUM, ranking.getAverage());
                b.putString(SharePaperActivity.AVERAGER_TEXT, ranking.getAverageText());
                b.putString(SharePaperActivity.MATCH_ID, ranking.getId());
                PassagewayHandler.jumpActivity(context, SharePaperActivity.class, SharePaperActivity.RequestCode, b);
            }
        });
    }

    private void setShareButton(TextView shareText, TextView resultsText, Ranking ranking) {
        shareText.setVisibility(View.INVISIBLE);
        resultsText.setVisibility(View.INVISIBLE);
        if (ranking.isMe(context)) {
            if (isShare) {
                resultsText.setVisibility(View.VISIBLE);
            } else {
                shareText.setVisibility(View.VISIBLE);
                shareText.setText("分享可见");
                shareText.setClickable(true);
                shareText.setTextColor(ColorHandle.getColorForID(context, R.color.white));
                shareText.setBackgroundResource(R.drawable.blue_blue_rounded_5);
            }
        } else {
            shareText.setVisibility(View.VISIBLE);
            shareText.setText("等待公布");
            shareText.setClickable(false);
            shareText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
            shareText.setBackgroundResource(R.drawable.orange_whlit_rounded_5);

        }
    }

    private void setTime(TextView timeText, String usetime) {
        StringBuilder sb = new StringBuilder();
        try {
            int s = Integer.valueOf(usetime);
            int time_m = (int) (s / 60);
            int time_s = (int) (s % 60);

            sb.append(time_m);
            sb.append("分");
            if (time_s < 10) {
                sb.append("0");
            }
            sb.append(time_s);
            sb.append("秒");
        } catch (Exception e) {
            sb.append("");
        }
        timeText.setText(sb.toString());
    }

    private void setUserIcon(ImageView userIcon, String url) {
        userIcon.setImageResource(R.drawable.user_off_icon);
        if (url.equals("") || url.equals("null")) {
            return;
        }
        DownloadImageLoader.loadImage(userIcon, url, WinHandler.dipToPx(context, 25));
    }

    class Holder {

        ImageView lickIcon;
        ImageView medalIcon;
        ImageView userIcon;
        TextView medalText;
        TextView resultsText;
        TextView userNameText;
        TextView timeText;
        TextView shareText;

        Holder(View view) {
            lickIcon = (ImageView) view.findViewById(R.id.competitionRanking_lickIcon);
            medalIcon = (ImageView) view.findViewById(R.id.competitionRanking_medalIcon);
            userIcon = (ImageView) view.findViewById(R.id.competitionRanking_userIcon);
            medalText = (TextView) view.findViewById(R.id.competitionRanking_medalText);
            resultsText = (TextView) view.findViewById(R.id.competitionRanking_resultsText);
            userNameText = (TextView) view.findViewById(R.id.competitionRanking_userNameText);
            timeText = (TextView) view.findViewById(R.id.competitionRanking_timeText);
            shareText = (TextView) view.findViewById(R.id.competitionRanking_shareText);
        }

    }
}
