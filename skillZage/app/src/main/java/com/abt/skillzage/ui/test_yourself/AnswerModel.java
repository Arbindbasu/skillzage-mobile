package com.abt.skillzage.ui.test_yourself;

public class AnswerModel {

    private int ansid , questionsId ,score;
    private String answer_option;
    private boolean isCorrect;

    public int getAnsid() {
        return ansid;
    }

    public void setAnsid(int ansid) {
        this.ansid = ansid;
    }

    public int getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(int questionsId) {
        this.questionsId = questionsId;
    }

    public String getAnswer_option() {
        return answer_option;
    }

    public void setAnswer_option(String answer_option) {
        this.answer_option = answer_option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "AnswerModel{" +
                "ansid=" + ansid +
                ", questionsId=" + questionsId +
                ", score=" + score +
                ", answer_option='" + answer_option + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
