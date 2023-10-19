package com.kids.schoolmanager.ModelClasses;

public class StoreNotebookData {
    String notebookTitle;
    String notebookDescription;
    String notebookDate;

    public StoreNotebookData(String notebookTitle, String notebookDescription, String notebookDate) {
        this.notebookTitle = notebookTitle;
        this.notebookDescription = notebookDescription;
        this.notebookDate = notebookDate;
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

    public String getNotebookDate() {
        return notebookDate;
    }

    public void setNotebookDate(String notebookDate) {
        this.notebookDate = notebookDate;
    }
}
