package com.kids.schoolmanager.ModelClasses;

public class StoreAttendanceData {
    String present;
    String username;
    String finalDay;
    String fixedDate;
    Boolean ifChecked;
    String userPhone;

    public StoreAttendanceData(String present, String username, String finalDay, String fixedDate, Boolean ifChecked, String userPhone) {
        this.present = present;
        this.username = username;
        this.finalDay = finalDay;
        this.fixedDate = fixedDate;
        this.ifChecked = ifChecked;
        this.userPhone = userPhone;
    }

    public StoreAttendanceData() {
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFinalDay() {
        return finalDay;
    }

    public void setFinalDay(String finalDay) {
        this.finalDay = finalDay;
    }

    public String getFixedDate() {
        return fixedDate;
    }

    public void setFixedDate(String fixedDate) {
        this.fixedDate = fixedDate;
    }

    public Boolean getIfChecked() {
        return ifChecked;
    }

    public void setIfChecked(Boolean ifChecked) {
        this.ifChecked = ifChecked;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
