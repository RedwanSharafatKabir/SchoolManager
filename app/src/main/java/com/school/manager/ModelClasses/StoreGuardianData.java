package com.school.manager.ModelClasses;

public class StoreGuardianData {
    String email;
    String username;
    String phone;

    public StoreGuardianData(String email, String username, String phone) {
        this.email = email;
        this.username = username;
        this.phone = phone;
    }

    public StoreGuardianData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
