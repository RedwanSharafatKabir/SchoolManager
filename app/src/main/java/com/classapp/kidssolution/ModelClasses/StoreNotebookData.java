package com.classapp.kidssolution.ModelClasses;

public class StoreNotebookData {
    String notebookTitle;
    String notebookDescription;

    public StoreNotebookData(String notebookTitle, String notebookDescription) {
        this.notebookTitle = notebookTitle;
        this.notebookDescription = notebookDescription;
    }

    public StoreNotebookData() {
    }

    public String getNotebookTitle() {
        return notebookTitle;
    }

    public void setNotebookTitle(String notebookTitle) {
        this.notebookTitle = notebookTitle;
    }

    public String getNotebookDescription() {
        return notebookDescription;
    }

    public void setNotebookDescription(String notebookDescription) {
        this.notebookDescription = notebookDescription;
    }
}
