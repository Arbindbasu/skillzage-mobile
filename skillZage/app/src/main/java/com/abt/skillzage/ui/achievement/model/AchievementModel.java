package com.abt.skillzage.ui.achievement.model;

public class AchievementModel {

    private String certificationTitle , certificateDescription , certificationType , dateOfCompletion , uploadCertificate;
    private int certificationScore , id;

    public String getCertificationTitle() {
        return certificationTitle;
    }

    public void setCertificationTitle(String certificationTitle) {
        this.certificationTitle = certificationTitle;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public String getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(String certificationType) {
        this.certificationType = certificationType;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public String getUploadCertificate() {
        return uploadCertificate;
    }

    public void setUploadCertificate(String uploadCertificate) {
        this.uploadCertificate = uploadCertificate;
    }

    public int getCertificationScore() {
        return certificationScore;
    }

    public void setCertificationScore(int certificationScore) {
        this.certificationScore = certificationScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
