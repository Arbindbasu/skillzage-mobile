package com.abt.skillzage.ui.course_detail.model;

public class CourseSessionModel implements Comparable<CourseSessionModel>{

    private String show_collapse_status = "";

    private String session_video_url , url_3 , subscription_required , image_path , course_description , video_url , session_name ,
            session_description , session_status , course_name , session_logo ,course_objective , session_url , valid_to , course_status ,
            recommended_status , valid_from , quiza_4_course ,url_1 , url_2 , quiz;

    private int courses_management_id , session_number , session_id , course_id ;

    public String getSession_video_url() {
        return session_video_url;
    }

    public void setSession_video_url(String session_video_url) {
        this.session_video_url = session_video_url;
    }

    public String getUrl_3() {
        return url_3;
    }

    public void setUrl_3(String url_3) {
        this.url_3 = url_3;
    }

    public String getSubscription_required() {
        return subscription_required;
    }

    public void setSubscription_required(String subscription_required) {
        this.subscription_required = subscription_required;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getSession_description() {
        return session_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public String getSession_status() {
        return session_status;
    }

    public void setSession_status(String session_status) {
        this.session_status = session_status;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getSession_logo() {
        return session_logo;
    }

    public void setSession_logo(String session_logo) {
        this.session_logo = session_logo;
    }

    public String getCourse_objective() {
        return course_objective;
    }

    public void setCourse_objective(String course_objective) {
        this.course_objective = course_objective;
    }

    public String getSession_url() {
        return session_url;
    }

    public void setSession_url(String session_url) {
        this.session_url = session_url;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getCourse_status() {
        return course_status;
    }

    public void setCourse_status(String course_status) {
        this.course_status = course_status;
    }

    public String getRecommended_status() {
        return recommended_status;
    }

    public void setRecommended_status(String recommended_status) {
        this.recommended_status = recommended_status;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getQuiza_4_course() {
        return quiza_4_course;
    }

    public void setQuiza_4_course(String quiza_4_course) {
        this.quiza_4_course = quiza_4_course;
    }

    public String getUrl_1() {
        return url_1;
    }

    public void setUrl_1(String url_1) {
        this.url_1 = url_1;
    }

    public String getUrl_2() {
        return url_2;
    }

    public void setUrl_2(String url_2) {
        this.url_2 = url_2;
    }

    public int getCourses_management_id() {
        return courses_management_id;
    }

    public void setCourses_management_id(int courses_management_id) {
        this.courses_management_id = courses_management_id;
    }

    public int getSession_number() {
        return session_number;
    }

    public void setSession_number(int session_number) {
        this.session_number = session_number;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getShow_collapse_status() {
        return show_collapse_status;
    }

    public void setShow_collapse_status(String show_collapse_status) {
        this.show_collapse_status = show_collapse_status;
    }

    @Override
    public int compareTo(CourseSessionModel o) {
        return  this.session_id - o.session_id;
    }
}
