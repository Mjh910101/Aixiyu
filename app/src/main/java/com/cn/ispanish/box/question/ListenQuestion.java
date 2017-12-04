package com.cn.ispanish.box.question;

import android.util.Log;

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
 * Created by Hua on 17/7/17.
 */
public class ListenQuestion extends Question {

    public List<Question> questionList;

    private String id;
    private String imageUrl, videourl;
    private int type;

    public ListenQuestion(Question question) {
        super(question.getJson());

        id = question.getId();
        videourl = question.getVideourl();
        type = question.getType();
        imageUrl = question.getImg();

        questionList = new ArrayList<>();

        questionList.add(question);

    }

    public void addQuestion(Question q) {
        if (equals(q)) {
            questionList.add(q);
        }
    }

    public boolean equals(Question q) {
        if (type != q.getType()) {
            return false;
        }
        String str = q.getVideourl();
        return isTrue(str, videourl);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    @Override
    public String getVideourl() {
        Log.e("", "mp3 : " + videourl);
        return videourl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getQuestionType() {
        return questionList.get(0).getQuestionType();
    }
}
