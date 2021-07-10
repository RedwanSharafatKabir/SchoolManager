package com.classapp.kidssolution.ModelClasses;

public class StoreNoticeData {

    String noticeTitle;
    String noticeDescription;
    String noticeDate;

    public StoreNoticeData(String noticeTitle, String noticeDescription, String noticeDate) {
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
        this.noticeDate = noticeDate;
    }

    public StoreNoticeData() {
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeDescription() {
        return noticeDescription;
    }

    public void setNoticeDescription(String noticeDescription) {
        this.noticeDescription = noticeDescription;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }
}
