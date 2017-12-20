package com.cn.ispanish.box.question;

import com.cn.ispanish.handlers.JsonHandle;
import com.lidroid.xutils.db.annotation.Transient;

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
 * Created by Hua on 2017/12/7.
 */

public class LineQuestion extends Question {

    @Transient
    private List<Option> answerList;
    @Transient
    private List<Option> rightList;

    public LineQuestion(JSONObject json) {
        super(json);

        answerList = new ArrayList<>();
        rightList = new ArrayList<>();

        initList(answerList, JsonHandle.getArray(json, "answer"));
        initList(rightList, JsonHandle.getArray(json, "right"));
    }

    private void initList(List<Option> list, JSONArray array) {

        if (array == null) {
            return;
        }

        for (int i = 0; i < array.length(); i++) {
            list.add(new Option(JsonHandle.getJSON(array, i)));
        }

    }

    public List<Option> getAnswerOptionList() {
        return answerList;
    }

    public void setAnswerOptionList(List<Option> answerList) {
        this.answerList = answerList;
    }

    public List<Option> getRightOptionList() {
        return rightList;
    }

    public void setRightList(List<Option> rightOptionList) {
        this.rightList = rightList;
    }

    public class Option {

        int id;
        String text;

        Option(JSONObject json) {
            id = JsonHandle.getInt(json, "id");
            text = JsonHandle.getString(json, "conten");
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }

}
