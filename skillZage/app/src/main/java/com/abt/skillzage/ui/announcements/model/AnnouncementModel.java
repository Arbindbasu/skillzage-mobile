package com.abt.skillzage.ui.announcements.model;

public class AnnouncementModel {

    private String announcementtitle , description , date ,id ,event_reg_date;

    public String getAnnouncementtitle() {
        return announcementtitle;
    }

    public void setAnnouncementtitle(String announcementtitle) {
        this.announcementtitle = announcementtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_reg_date() {
        return event_reg_date;
    }

    public void setEvent_reg_date(String event_reg_date) {
        this.event_reg_date = event_reg_date;
    }


    @Override
    public String toString() {
        return "AnnouncementModel{" +
                "announcementtitle='" + announcementtitle + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", id='" + id + '\'' +
                ", event_reg_date='" + event_reg_date + '\'' +
                '}';
    }
}
