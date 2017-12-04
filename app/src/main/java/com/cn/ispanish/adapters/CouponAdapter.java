package com.cn.ispanish.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.Coupon;

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
 * Created by Hua on 17/8/25.
 */
public class CouponAdapter extends BaseAdapter {

    Context context;
    List<Coupon> itemList;
    int click;

    public CouponAdapter(Context context, List<Coupon> itemList) {
        this.itemList = itemList;
        this.context = context;
        click = -1;
    }

    public Coupon getOnClickCoupon() {
        if (click < 0) {
            return null;
        }
        return itemList.get(click);
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
            view = View.inflate(context, R.layout.layout_coupon_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Coupon obj = itemList.get(i);
        setData(holder, obj);
        setRadio(holder.clickRadio, i);
        setOnClick(view, i);

        return view;
    }

    private void setRadio(RadioButton radio, int i) {
        if (i == click) {
            radio.setChecked(true);
        } else {
            radio.setChecked(false);
        }
    }

    private void setData(Holder holder, Coupon obj) {
        holder.typeText.setText(obj.getTypeText());
        holder.priceText.setText("￥" + obj.getPrice());
        holder.rangeStaText.setText("满" + obj.getRangeSta() + "可用");
        holder.timeEndText.setText("有效期：" + obj.getTimeEndText());
        holder.coupobBg.setBackgroundResource(obj.getCoupobBg());
    }

    public void setOnClick(View view, final int i) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click == i) {
                    click = -1;
                } else {
                    click = i;
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setClickItem(String id) {
        if (id == null) {
            return;
        }
        if(id.equals("null")){
            return;
        }
        if(id.equals("")){
            return;
        }
        for (int i = 0; i < itemList.size(); i++) {
            Coupon obj = itemList.get(i);
            if (obj.getId().equals(id)) {
                click = i;
                notifyDataSetChanged();
                return;
            }
        }
    }

    class Holder {

        TextView typeText;
        TextView priceText;
        TextView rangeStaText;
        TextView timeEndText;
        RadioButton clickRadio;
        LinearLayout coupobBg;

        Holder(View view) {
            typeText = (TextView) view.findViewById(R.id.coupob_typeText);
            priceText = (TextView) view.findViewById(R.id.coupob_priceText);
            rangeStaText = (TextView) view.findViewById(R.id.coupob_rangeStaText);
            timeEndText = (TextView) view.findViewById(R.id.coupob_timeEndText);
            clickRadio = (RadioButton) view.findViewById(R.id.coupob_clickRadio);
            coupobBg = (LinearLayout) view.findViewById(R.id.coupob_coupobBg);
        }

    }
}
