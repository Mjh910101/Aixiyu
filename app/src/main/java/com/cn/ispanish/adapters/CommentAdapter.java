package com.cn.ispanish.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.download.DownloadImageLoader;
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
 * Created by Hua on 17/3/10.
 */
public class CommentAdapter extends BaseAdapter {

    Context context;
    List<Comment> itemList;

    public CommentAdapter(Context context, List<Comment> itemList) {
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
            view = View.inflate(context, R.layout.layout_comment_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Comment comment = itemList.get(i);
        setData(holder, comment);


        return view;
    }

    private void setData(Holder holder, Comment comment) {
        setUserPic(holder.userPic, comment.getPortrait());
        holder.userName.setText(comment.getName());
        holder.uploadTime.setText(comment.getUploadTime());
        holder.conntntText.setText(comment.getContent());

    }

    private void setUserPic(ImageView imageView, String images) {
        double w = (double) WinHandler.getWinWidth(context) / 9d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) w));

        DownloadImageLoader.loadImage(imageView, images, (int) (w / 2));
    }

    class Holder {

        ImageView userPic;
        TextView userName;
        TextView uploadTime;
        TextView conntntText;

        Holder(View view) {
            userPic = (ImageView) view.findViewById(R.id.commentItem_userPic);
            userName = (TextView) view.findViewById(R.id.commentItem_userName);
            uploadTime = (TextView) view.findViewById(R.id.commentItem_uploadTime);
            conntntText = (TextView) view.findViewById(R.id.commentItem_conntntText);
        }

    }

}
