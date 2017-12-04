package com.cn.ispanish.box.question;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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
@Table(name = "tbl_question")
public class Question implements Comparable {

    public final static int XuanZhe = 1;
    public final static int PanDuan = 2;
    public final static int YueDu = 3;
    public final static int TingLi = 4;
    public final static int WanXingTianKong = 5;
    public final static int XieZuo = 6;
    public final static int XiYiHan = 7;
    public final static int HanYiXi = 8;
    public final static int RenWen = 9;
    public final static int KouYU = 10;
    public final static int TingLiTingXie = 11;
    public final static int TingLi_DATI = 12;
    public final static int TingLi_PANDUAN = 13;
    public final static int TingLi_XUANZHE = 14;
    public final static int TingXIE_WENZHANG = 15;
    public final static int TingLi_XIEZUO = 16;
    public final static int XuanZhe_IMAGE = 17;
    public final static int TianKong = 18;
    public final static int TingLi_LuYin = 19;

    @Id(column = "kid")
    private String id;

    @Column(column = "name")
    private String name;

    @Column(column = "explain")
    private String explain;

    @Column(column = "img")
    private String img;

    @Column(column = "videourl")
    private String videourl;

    @Column(column = "article")
    private String article;

    @Column(column = "type_text")
    private String typeText;

    @Column(column = "question_type")
    private int blockType;

    @Column(column = "type")
    private int type;

    @Column(column = "answer")
    private String answer;

    @Column(column = "right")
    private String right;

    @Column(column = "tittype")
    private int tittype;

    @Transient
    private boolean iscoll;

    @Transient
    private List<String> answerList;
    @Transient
    private List<String> imageList;
    @Transient
    private List<String> rightList;
    @Transient
    private JSONObject dataJson;
    @Transient
    public boolean isRight;
    @Transient
    public boolean isDoing;
    @Transient
    public String doingRight;

    public Question() {
    }

    public Question(JSONObject json) {

        if (json == null) {
            return;
        }
        dataJson = json;

        id = JsonHandle.getString(json, "id");
        name = JsonHandle.getString(json, "name");
        explain = JsonHandle.getString(json, "explain");
//        img = JsonHandle.getString(json, "img");
        videourl = JsonHandle.getString(json, "videourl");
        article = JsonHandle.getString(json, "article");
        type = JsonHandle.getInt(json, "type");
        tittype = JsonHandle.getInt(json, "tittype");
        iscoll = JsonHandle.getInt(json, "iscoll") == 1;

        this.blockType = 1;

        answerList = new ArrayList<>();
        rightList = new ArrayList<>();
        imageList = new ArrayList<>();

        initAnswerList(JsonHandle.getArray(json, "answer"));
        initRightList(JsonHandle.getArray(json, "right"));
        initImageList(JsonHandle.getArray(json, "img"));

    }

    private void initImageList(JSONArray array) {
        img = JsonHandle.jsonToString(array);
        initList(imageList, array);
    }

    private void initRightList(JSONArray array) {
        right = JsonHandle.jsonToString(array);
        initList(rightList, array);
    }

    private void initAnswerList(JSONArray array) {
        answer = JsonHandle.jsonToString(array);
        initList(answerList, array);
    }

    private void initList(List<String> list, JSONArray array) {
        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {
            list.add(JsonHandle.getString(array, i));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExplain() {
        if (explain == null) {
            return "";
        }
        if (explain.equals("null")) {
            return "";
        }
        return explain;
    }

    public String getImg() {
        if (img == null) {
            return "";
        }
        if (img.equals("")) {
            return "";
        }
        if (img.equals("null")) {
            return "";
        }
        if (img.contains("http://") || img.contains("https://")) {
            return img;
        }
        if (img.length() > 1 && img.substring(0, 1).equals(".")) {
            return "http://www.ispanish.cn" + img.substring(1, img.length());
        }
        return "http://www.ispanish.cn/Public/Uploads/" + img;

    }

    public int getTittype() {
        return tittype;
    }

    public void setTittype(int tittype) {
        this.tittype = tittype;
    }

    public String getVideourl() {
        return videourl;
    }

    public String getArticle() {
        return article;
    }

    public int getType() {
        return type;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public List<String> getRightList() {
        return rightList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public boolean isTrue(boolean answer) {
        if (rightList.isEmpty() || rightList.size() > 1) {
            return false;
        }

        boolean right = rightList.get(0).equals("1");

        return right == answer;
    }

    public boolean isTrue(String answer) {
        for (int i = 0; i < rightList.size(); i++) {
            if (isTrue(rightList.get(i), answer)) {
                return true;
            }
        }
        return false;
    }

    public int getTrueIndex() {
        for (int i = 0; i < rightList.size(); i++) {
            if (isTrue(rightList.get(i), getRightText())) {
                return i;
            }
        }
        return -1;
    }

    public boolean isTrue(String right, String answer) {

        right = right.trim().replace(" ", "").replace("\n", "").replace("\r", "");
        answer = answer.trim().replace(" ", "").replace("\n", "").replace("\r", "");

        char[] rightCharArray = right.toCharArray();
        char[] answerCharArray = answer.toCharArray();

        if (rightCharArray.length != answerCharArray.length) {
            return false;
        }

        for (int i = 0; i < rightCharArray.length; i++) {
            if (rightCharArray[i] != answerCharArray[i]) {
                return false;
            }
        }
        return true;
    }

    public String getQuestionType() {
        switch (type) {
            case XuanZhe:
            case XuanZhe_IMAGE:
                return "选择题";
            case PanDuan:
                return "判断题";
            case YueDu:
                return "阅读理解";
            case TingLi:
            case TingLi_LuYin:
            case TingLi_DATI:
            case TingLi_PANDUAN:
            case TingLi_XUANZHE:
                return "听力题";
            case TingXIE_WENZHANG:
                return "听写文章";
            case TingLi_XIEZUO:
                return "听力写作";
            case WanXingTianKong:
                return "完形填空";
            case XieZuo:
                return "写作题";
            case XiYiHan:
                return "西译汉";
            case HanYiXi:
                return "汉译西";
            case RenWen:
                return "人文知识";
            case KouYU:
                return "口语题";
            case TingLiTingXie:
                return "听力听写";
            case TianKong:
                return "填空题";
        }
        return "        ";
    }

    public int getQuestionIcon() {
        switch (type) {
            case XuanZhe:
            case XuanZhe_IMAGE:
                return R.drawable.xuanzhe_icon;
            case PanDuan:
                return R.drawable.panduan_icon;
            case YueDu:
                return R.drawable.yuedu_icon;
            case TingLi:
            case TingLi_LuYin:
            case TingLi_DATI:
            case TingLi_PANDUAN:
            case TingLi_XUANZHE:
            case TingXIE_WENZHANG:
            case TingLi_XIEZUO:
                return R.drawable.tingli_icon;
            case WanXingTianKong:
                return R.drawable.wanxing_icon;
            case XieZuo:
                return R.drawable.xiezuo_icon;
            case XiYiHan:
                return R.drawable.xiyihan_icon;
            case HanYiXi:
                return R.drawable.hanyixi_icon;
            case RenWen:
                return R.drawable.renwen_icon;
            case KouYU:
                return R.drawable.kouyu_icon;
            case TingLiTingXie:
                return R.drawable.tingxie_icon;
            case TianKong:
                return R.drawable.tiankong_icon;

        }
        return R.mipmap.ic_launcher;
    }

    public String getRightText() {
        if (!rightList.isEmpty()) {
            return rightList.get(0);
        }
        return "";
    }

    public JSONObject getJson() {
        return dataJson;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
        this.answerList = new ArrayList<>();
        initList(answerList, JsonHandle.getArray(answer));
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
        this.rightList = new ArrayList<>();
        initList(rightList, JsonHandle.getArray(right));
    }

    public JSONObject getDataJson() {
        return dataJson;
    }

    public void setDataJson(JSONObject dataJson) {
        this.dataJson = dataJson;
    }

    public static boolean saveOrUpdate(Context context, Question q) {
        if (q == null) {
            return false;
        }
//        try {
//            DBHandler.getDbUtils(context).saveOrUpdate(q);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
        uploadErrorQuestion(context, q);
        return true;
    }

    private static void uploadErrorQuestion(final Context context, Question q) {
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("tid", q.getId());
        params.addBodyParameter("isclean", "1");

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getSaveErrorTit(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                    }
                });
    }

    public static void deleteErrorQuestion(final Context context, Question q) {
        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("tid", q.getId());
        params.addBodyParameter("isclean", "0");

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getSaveErrorTit(context), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                    }
                });
    }

    public static boolean delete(Context context, Question q) {
        if (q == null) {
            return false;
        }
        try {
            DBHandler.getDbUtils(context).deleteById(Question.class, q.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isDoing() {
        return isDoing;
    }

    public boolean isRight() {
        return isRight;
    }

    public int getIsRight() {
        if (isRight) {
            return 1;
        }
        return 0;
    }

    public void setRight(boolean right) {
        isRight = right;
        isDoing = true;
    }

    public JSONObject getRightJson() {
        JSONObject json = new JSONObject();
        JsonHandle.put(json, "id", getId());
        JsonHandle.put(json, "isRight", getIsRight());
        JsonHandle.put(json, "answer", getDoingRight());
        return json;
    }

    public String getTypeText() {
        return getQuestionType();
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    @Override
    public int compareTo(Object obj) {
        Question p = (Question) obj;
        return this.getType() - p.getType();
    }

    public String getDoingRight() {
        if (doingRight == null) {
            return "";
        }
        return doingRight;
    }

    public void setDoingRight(String doingRight) {
        this.doingRight = doingRight;
    }

    public boolean iscoll() {
        return iscoll;
    }

    public void setIscoll(boolean iscoll) {
        this.iscoll = iscoll;
    }
}
