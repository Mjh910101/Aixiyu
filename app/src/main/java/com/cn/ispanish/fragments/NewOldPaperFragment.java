package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.ChannelActivity;
import com.cn.ispanish.activitys.ClassificationQusetionGridActivity;
import com.cn.ispanish.activitys.ClassificationQusetionGridV2Activity;
import com.cn.ispanish.activitys.CollectionPaperActivity;
import com.cn.ispanish.activitys.CompetitionRankingActivity;
import com.cn.ispanish.activitys.GameContentActivity;
import com.cn.ispanish.activitys.MyselfPaperForVipListActivity;
import com.cn.ispanish.activitys.MyselfRankingActivity;
import com.cn.ispanish.activitys.PaperForErrorBankActivity;
import com.cn.ispanish.activitys.PaperForVipListActivity;
import com.cn.ispanish.activitys.PaperGridActivity;
import com.cn.ispanish.activitys.ShareActivity;
import com.cn.ispanish.activitys.VipBlocktoListActivity;
import com.cn.ispanish.activitys.WebContentActivity;
import com.cn.ispanish.adapters.CouponAdapter;
import com.cn.ispanish.box.Banner;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.PaperTitle;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.HorizontalListView;
import com.cn.ispanish.views.banner.BannerView;
import com.cn.ispanish.webview.MyWebViewClient;
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
 * Created by Hua on 17/5/8.
 */
public class NewOldPaperFragment extends BaseFragment {

    public final static String TYPE_KEY = "type";
    public final static String TYPE_OBJ_KEY = "type_obj";

    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.mainPaper_orderPaperBg)
    private ImageView orderPaperBg;
    @ViewInject(R.id.mainPaper_contestPaperBg)
    private ImageView contestPaperBg;
    @ViewInject(R.id.paper_titleLayout)
    private RecyclerView titleLayout;
    @ViewInject(R.id.paper_titleTypeLayout)
    private RecyclerView titleTypeLayout;
    @ViewInject(R.id.mainPaper_orderPaperButton)
    private TextView orderPaperButton;

    PaperTitle nowType;
    public static PaperTitle nowTitle = null;

    public static String getLan() {
        if (nowTitle == null) {
            return "";
        }

        return nowTitle.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_new_old_paper,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initActivity();
        downloadTitleData();

        return contactsLayout;
    }

    private void initActivity() {
        setAnimation();
    }

    private void setPaperBg(int paperIcon) {
        orderPaperBg.setImageResource(paperIcon);
//        contestPaperBg.setImageResource(paperIcon);
    }

    private void startAnimation(final View view) {
        view.setVisibility(View.VISIBLE);

        Animation alphaAnim = AnimationUtils.loadAnimation(context, R.anim.paper_altha);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnim);
    }

    @OnClick(R.id.paper_setChannelButton)
    public void onChannelButton(View view) {
        PassagewayHandler.jumpActivity(context, ChannelActivity.class, ChannelActivity.CHANNELRESULT);

    }

    @OnClick(R.id.mainPaper_orderPaperButton)
    public void onOrderPaper(View view) {
        if (!onClickType()) {
            return;
        }
//        PassagewayHandler.jumpActivity(context, PaperActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(TYPE_OBJ_KEY, nowType);
        PassagewayHandler.jumpActivity(context, PaperGridActivity.class, b);

    }

    @OnClick(R.id.mainPaper_orderSprintButton)
    public void onOrderSprint(View view) {
        if (!onClickType()) {
            return;
        }
        Bundle b = new Bundle();
        b.putString(TYPE_KEY, nowType.getId());
        PassagewayHandler.jumpActivity(context, PaperForVipListActivity.class, b);
    }

    @OnClick(R.id.mainPaper_vipButton)
    public void onVipButton(View view) {
        if (!onClickType()) {
            return;
        }
        Bundle b = new Bundle();
        b.putString(TYPE_KEY, nowType.getId());
        PassagewayHandler.jumpActivity(context, VipBlocktoListActivity.class, b);
    }

    @OnClick(R.id.mainPaper_classificationQusetionButton)
    public void onSprintButton(View view) {
        if (!onClickType()) {
            return;
        }
        Bundle b = new Bundle();
        b.putSerializable(TYPE_OBJ_KEY, nowType);
        PassagewayHandler.jumpActivity(context, ClassificationQusetionGridV2Activity.class, b);
    }

    private boolean onClickType() {
        if (nowType == null) {
            MessageHandler.showToast(context, "请先选择题库类型");
            return false;
        }
        return true;
    }

    @OnClick(R.id.mainPaper_collectionButton)
    public void onCollectionButton(View view) {
        PassagewayHandler.jumpActivity(context, CollectionPaperActivity.class);
    }

    @OnClick(R.id.mainPaper_myDataButton)
    public void onMyDataButton(View view) {
        if (User.isLoginAndShowMessage(context)) {
            PassagewayHandler.jumpActivity(context, MyselfPaperForVipListActivity.class);
        }
    }

    @OnClick(R.id.mainPaper_paimingButton)
    public void onPaiMingButton(View view) {
        PassagewayHandler.jumpActivity(context, CompetitionRankingActivity.class);
    }

    @OnClick(R.id.mainPaper_gameButton)
    public void onGameButton(View view) {
        PassagewayHandler.jumpActivity(context, GameContentActivity.class);
    }

    @OnClick({R.id.mainPaper_eroorButton, R.id.mainPaper_myEroorButton})
    public void onErrorButton(View view) {
        PassagewayHandler.jumpActivity(context, PaperForErrorBankActivity.class);
//        PassagewayHandler.jumpActivity(context, PaperForErrorActivity.class);
    }

    @OnClick(R.id.mainPaper_shareButton)
    public void onShareButton(View view) {
        Bundle b = new Bundle();
        b.putString(ShareActivity.URL_KEY, "http://www.ispanish.cn/index.php/Home/Api/imgpage");
        PassagewayHandler.jumpActivity(context, ShareActivity.class, b);
    }

    @OnClick(R.id.mainPaper_rankingButton)
    public void onRankingButton(View view) {
        if (User.isLoginAndShowMessage(context)) {
            PassagewayHandler.jumpActivity(context, MyselfRankingActivity.class);
        }
    }

    @OnClick(R.id.mainPaper_sprintButton)
    public void onSprint(View view) {
        Bundle b = new Bundle();
        b.putString(MyWebViewClient.KEY, "http://www.ispanish.cn/index.php/Home/Api/matchpage.html");
        b.putString(MyWebViewClient.TITLE, "参赛奖品");
        PassagewayHandler.jumpActivity(context, WebContentActivity.class, b);
    }

    private void setAnimation() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.paper_rotate);
        contestPaperBg.startAnimation(animation);
    }

    private void downloadTitleData() {
        progress.setVisibility(View.VISIBLE);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBackLanguage(context),
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
                                setTitleData(JsonHandle.getArray(json, "data"));
                            }
                        }
                    }
                });
    }

    private void setTitleData(JSONArray array) {
        if (array == null) {
            return;
        }

        List<PaperTitle> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new PaperTitle(JsonHandle.getJSON(array, i)));
        }

        titleLayout.setHasFixedSize(true);
        titleLayout.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        titleLayout.setAdapter(new PaperTitleAdpter(list));
        setPaperTitle(list.get(0));

    }

    private void setPaperTitle(PaperTitle obj) {
        nowTitle = obj;
        List<PaperTitle> typeList = obj.getTypeList();
        titleTypeLayout.setHasFixedSize(true);
        titleTypeLayout.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        titleTypeLayout.setAdapter(new PaperTypeAdpter(typeList));

        if (!typeList.isEmpty()) {
            nowType = typeList.get(0);
            orderPaperButton.setText("顺序练习" + "\n" + "共" + typeList.get(0).getCount() + "套");
        } else {
            nowType = null;
        }
    }

    class PaperTitleAdpter extends RecyclerView.Adapter {

        List<PaperTitle> itemList;
        int nowPosition = 0;

        PaperTitleAdpter(List<PaperTitle> list) {
            this.itemList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_paper_title_item, parent, false);
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            RecyclerHolder mHolder = (RecyclerHolder) holder;
            final PaperTitle obj = itemList.get(position);
            mHolder.textView.setText(obj.getName());

            if (nowPosition == position) {
                mHolder.textView.setTextColor(ColorHandle.getColorForID(context, R.color.white));
            } else {
                mHolder.textView.setTextColor(ColorHandle.getColorForID(context, R.color.white_f));
            }

            mHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nowPosition = position;
                    setPaperTitle(obj);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder {
            TextView textView;

            private RecyclerHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.paperTitle_textView);
            }
        }
    }

    class PaperTypeAdpter extends RecyclerView.Adapter {

        List<PaperTitle> itemList;
        int onClickPosition = 0;

        PaperTypeAdpter(List<PaperTitle> list) {
            this.itemList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_paper_type_item, parent, false);
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            RecyclerHolder mHolder = (RecyclerHolder) holder;
            final PaperTitle obj = itemList.get(position);

            mHolder.textView.setText(obj.getName());

            if (onClickPosition == position) {
                mHolder.textView.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
                mHolder.textView.setBackgroundResource(R.drawable.orange_whlit_rounded_25px);
            } else {
                mHolder.textView.setTextColor(ColorHandle.getColorForID(context, R.color.stack));
                mHolder.textView.setBackgroundResource(R.drawable.gray_whlit_rounded_25px);
            }

            mHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nowType = obj;
                    orderPaperButton.setText("顺序练习" + "\n" + "共" + obj.getCount() + "套");
                    onClickPosition = position;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder {
            TextView textView;

            private RecyclerHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.paperType_textView);
            }
        }
    }

}
