package com.cn.ispanish.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cn.ispanish.R;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.views.VideoTitleView;

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
public class VideoTitleAdapter extends BaseAdapter {

    Context context;
    List<VideoInfo> itemList;
    String courseId;

    public VideoTitleAdapter(Context context, List<VideoInfo> itemList, String courseId) {
        this.context = context;
        this.itemList = itemList;
        this.courseId = courseId;
    }

    @Override
    public int getCount() {
        if (itemList.size() > 7) {
            return 7;
        }
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
        return new VideoTitleView(context, itemList.get(i), courseId);
    }
}
