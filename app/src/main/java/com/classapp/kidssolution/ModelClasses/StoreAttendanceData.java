package com.classapp.kidssolution.ModelClasses;

public class StoreAttendanceData {
    String present;
    String username;

    public StoreAttendanceData(String present, String username) {
        this.present = present;
        this.username = username;
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
}
