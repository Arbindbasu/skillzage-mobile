package com.abt.skillzage.model;

public class CourseModel {

    private int id , courseId;
    private String courseName , validFrom , validTo , imagePath , videoUrl , courseDescription , courseObjective , url1 , url2 , url3 , quizb4Course , quiza4Course , courseStatus , recommendedStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseObjective() {
        return courseObjective;
    }

    public void setCourseObjective(String courseObjective) {
        this.courseObjective = courseObjective;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getQuizb4Course() {
        return quizb4Course;
    }

    public void setQuizb4Course(String quizb4Course) {
        this.quizb4Course = quizb4Course;
    }

    public String getQuiza4Course() {
        return quiza4Course;
    }

    public void setQuiza4Course(String quiza4Course) {
        this.quiza4Course = quiza4Course;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getRecommendedStatus() {
        return recommendedStatus;
    }

    public void setRecommendedStatus(String recommendedStatus) {
        this.recommendedStatus = recommendedStatus;
    }

    @Override
    public String toString() {
        return "CourseModel{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", courseObjective='" + courseObjective + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", url3='" + url3 + '\'' +
                ", quizb4Course='" + quizb4Course + '\'' +
                ", quiza4Course='" + quiza4Course + '\'' +
                ", courseStatus='" + courseStatus + '\'' +
                ", recommendedStatus='" + recommendedStatus + '\'' +
                '}';
    }
}
