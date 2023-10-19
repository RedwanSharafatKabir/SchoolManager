package com.kids.schoolmanager.ModelClasses;

public class StoreClassesData {
    String classNameString;
    String classIdString;
    String classTeacherName;

    public StoreClassesData() {
    }

    public StoreClassesData(String classNameString, String classIdString, String classTeacherName) {
        this.classNameString = classNameString;
        this.classIdString = classIdString;
        this.classTeacherName = classTeacherName;
    }

    public String getClassNameString() {
        return classNameString;
    }

    public void setClassNameString(String classNameString) {
        this.classNameString = classNameString;
    }

    public String getClassIdString() {
        return classIdString;
    }

    public void setClassIdString(String classIdString) {
        this.classIdString = classIdString;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
    }
}
