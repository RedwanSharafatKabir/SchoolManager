package com.classapp.kidssolution.ModelClasses;

public class StoreChatData {
    String message;
    String receiver;
    String sender;
    String imageUrl;

    public StoreChatData(String message, String receiver, String sender, String imageUrl) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.imageUrl = imageUrl;
    }

    public StoreChatData() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
