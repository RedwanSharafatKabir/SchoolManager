package com.classapp.kidssolution.ModelClasses;

public class StoreGdClassesData {
    String classNameStringGd;
    String classIdStringGd;
    String classTeacherNameGd;

    public StoreGdClassesData() {
    }

    public StoreGdClassesData(String classNameStringGd, String classIdStringGd, String classTeacherNameGd) {
        this.classNameStringGd = classNameStringGd;
        this.classIdStringGd = classIdStringGd;
        this.classTeacherNameGd = classTeacherNameGd;
    }

    public String getClassNameStringGd() {
        return classNameStringGd;
    }

    public void setClassNameStringGd(String classNameStringGd) {
        this.classNameStringGd = classNameStringGd;
    }

    public String getClassIdStringGd() {
        return classIdStringGd;
    }

    public void setClassIdStringGd(String classIdStringGd) {
        this.classIdStringGd = classIdStringGd;
    }

    public String getClassTeacherNameGd() {
        return classTeacherNameGd;
    }

    public void setClassTeacherNameGd(String classTeacherNameGd) {
        this.classTeacherNameGd = classTeacherNameGd;
    }
}
