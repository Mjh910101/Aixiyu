package com.cn.ispanish.box.question;

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
 * Created by Hua on 17/6/1.
 */
public class ReadingQuestion extends Question {

    public List<Question> questionList;

    private String id;
    private String article;
    private String imageUrl;
    private int type;

    public ReadingQuestion(Question question) {
        super(question.getJson());

        id = question.getId();
        article = question.getArticle();
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
        String str = q.getArticle();
        return isTrue(str, article);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    @Override
    public String getArticle() {
        return article;
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

    @Override
    public void setRight(boolean right) {
        if (!isDoing) {
            isRight = right;
            isDoing = true;
        } else {
            if (!right) {
                isRight = right;
            }
        }

    }
}
