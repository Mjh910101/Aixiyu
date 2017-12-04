package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.CommentAdapter;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.PaperTitle;
import com.cn.ispanish.box.question.PaperType;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.fragments.NewOldPaperFragment;
import com.cn.ispanish.fragments.PaperFragment;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
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
 * Created by Hua on 17/5/22.
 */
public class ClassificationQusetionGridV2Activity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.classificationQusetion_dataGrid)
    private GridView dataGrid;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paperTypeList_typeLayout)
    private RelativeLayout typeLayout;
    @ViewInject(R.id.paperTypeList_typeList)
    private ListView typeList;


    private String typeId = "", thrId = "";
    private PaperTitle type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_qusetion_grid_v2);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.title_titleText)
    public void onTitle(View view) {
        typeLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.paperTypeList_closeTypeLayoutButton)
    public void onCloseTypeLayoutButton(View view) {
        typeLayout.setVisibility(View.GONE);
    }

    private List<String> getTypeData() {
        List<String> data = new ArrayList<>();
        for (PaperTitle obj : type.getTypeList()) {
            data.add(obj.getName());
        }
        return data;
    }

    private void initActivity() {
        titleText.setText("专项练习");

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        type = (PaperTitle) b.getSerializable(NewOldPaperFragment.TYPE_OBJ_KEY);
        if (type.isHaveChild()) {
            typeId = type.getId();
            titleText.setText(type.getName() + " ﹀");
            typeList.setAdapter(new ArrayAdapter<String>(this, R.layout.view_paper_title_item, getTypeData()));
            typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    thrId = type.getTypeList().get(i).getId();
                    titleText.setText(type.getTypeList().get(i).getName() + " ﹀");
                    dataGrid.setAdapter(null);
                    dowmloadData(typeId, thrId);
                    typeLayout.setVisibility(View.GONE);
                }
            });
            titleText.setClickable(true);
            dowmloadData(typeId, "");
        } else {
            typeId = type.getId();
            titleText.setText(type.getName());
            dowmloadData(typeId, "");
            titleText.setClickable(false);
        }

    }

    private void dowmloadData(String type, String thr) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("lan", type);
        params.addBodyParameter("thr", thr);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBackBankType(context), params,
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
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                JSONArray array = JsonHandle.getArray(json, "data");
                                initDataForArray(array);
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }

                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataForArray(JSONArray array) {
        if (array == null) {
            return;
        }

        List<PaperType> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new PaperType(JsonHandle.getJSON(array, i)));
        }

        dataGrid.setAdapter(new Adapter(list));

    }

    class Adapter extends BaseAdapter {

        List<PaperType> itemList;

        Adapter(List<PaperType> list) {
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
                view = View.inflate(context, R.layout.layout_paper_classification_type_item, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            PaperType obj = itemList.get(i);

            setData(holder, obj);
            setOnClick(view, obj);

            return view;
        }

        private void setOnClick(View view, final PaperType obj) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putString(PaperForTypeActivity.TYPE_KEY, obj.getId());
                    b.putString(PaperForTypeActivity.TIT_TYPE_KEY, type.getId());
                    b.putString(PaperForTypeActivity.TIT_THR_KEY, thrId);
                    b.putString(PaperForTypeActivity.PAPER_TITLE_KEY, obj.getTit());
                    PassagewayHandler.jumpActivity(context, PaperForTypeActivity.class, b);
                }
            });
        }

        private void setData(Holder holder, PaperType obj) {
            holder.name.setText(obj.getTit());

            DownloadImageLoader.loadImage(holder.pic, obj.getImg());
        }

    }

    class Holder {

        ImageView pic;
        TextView name;

        Holder(View view) {
            pic = (ImageView) view.findViewById(R.id.classificationQusetionItem_pic);
            name = (TextView) view.findViewById(R.id.classificationQusetionItem_name);
        }

    }
}
