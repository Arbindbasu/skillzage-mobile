package com.abt.skillzage.ui.test_yourself;

import java.util.ArrayList;
import java.util.List;

public class QuestionModel {

    private int id , score , questionSetId;
    private String question , videoUrl , imageUrl;
    private boolean isTrueFalse , isMultiple , isOrderBy , isDiscussion , isImageType , isVideoType;
    private List<AnswerModel> lisOfAns = new ArrayList<>();
    private List<QuestionModel> subquestionset = new ArrayList<>();

    public List<QuestionModel> getSubquestionset() {
        return subquestionset;
    }

    public void setSubquestionset(List<QuestionModel> subquestionset) {
        this.subquestionset = subquestionset;
    }

    public List<AnswerModel> getLisOfAns() {
        return lisOfAns;
    }

    public void setLisOfAns(List<AnswerModel> lisOfAns) {
        this.lisOfAns = lisOfAns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(int questionSetId) {
        this.questionSetId = questionSetId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTrueFalse() {
        return isTrueFalse;
    }

    public void setTrueFalse(boolean trueFalse) {
        isTrueFalse = trueFalse;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public boolean isOrderBy() {
        return isOrderBy;
    }

    public void setOrderBy(boolean orderBy) {
        isOrderBy = orderBy;
    }

    public boolean isDiscussion() {
        return isDiscussion;
    }

    public void setDiscussion(boolean discussion) {
        isDiscussion = discussion;
    }

    public boolean isImageType() {
        return isImageType;
    }

    public void setImageType(boolean imageType) {
        isImageType = imageType;
    }

    public boolean isVideoType() {
        return isVideoType;
    }

    public void setVideoType(boolean videoType) {
        isVideoType = videoType;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "id=" + id +
                ", score=" + score +
                ", questionSetId=" + questionSetId +
                ", question='" + question + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isTrueFalse=" + isTrueFalse +
                ", isMultiple=" + isMultiple +
                ", isOrderBy=" + isOrderBy +
                ", isDiscussion=" + isDiscussion +
                ", lisOfAns=" + lisOfAns +
                '}';
    }
}
