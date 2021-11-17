package com.abt.skillzage.ui.chat.model;

public class ChatMsg {

    private String MSGTYPE , message , emailId ,topicId , userName , datetime ;

    public String getMSGTYPE() {
        return MSGTYPE;
    }

    public void setMSGTYPE(String MSGTYPE) {
        this.MSGTYPE = MSGTYPE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "ChatMsg{" +
                "MSGTYPE='" + MSGTYPE + '\'' +
                ", message='" + message + '\'' +
                ", emailId='" + emailId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", userName='" + userName + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
