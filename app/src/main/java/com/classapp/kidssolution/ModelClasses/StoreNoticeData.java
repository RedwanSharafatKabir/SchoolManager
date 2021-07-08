package com.classapp.kidssolution.ModelClasses;

public class StoreNoticeData {

    String noticeTitle;
    String noticeDescription;

    public StoreNoticeData(String noticeTitle, String noticeDescription) {
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
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
}
