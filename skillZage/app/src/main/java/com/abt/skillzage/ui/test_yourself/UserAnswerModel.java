package com.abt.skillzage.ui.test_yourself;

public class UserAnswerModel {

    private int questionid ;
    private String question , user_answer , correct_ans_id , correct_ans;

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public String getCorrect_ans_id() {
        return correct_ans_id;
    }

    public void setCorrect_ans_id(String correct_ans_id) {
        this.correct_ans_id = correct_ans_id;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }
}
