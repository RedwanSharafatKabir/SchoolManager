package com.classapp.kidssolution.ModelClasses;

public class StoreGdClassesData {
    String classNameStringGd;
    String classIdStringGd;
    String classTeacherNameGd;
    String guardianPhone;
    String guardianName;

    public StoreGdClassesData() {
    }

    public StoreGdClassesData(String classNameStringGd, String classIdStringGd, String classTeacherNameGd, String guardianPhone, String guardianName) {
        this.classNameStringGd = classNameStringGd;
        this.classIdStringGd = classIdStringGd;
        this.classTeacherNameGd = classTeacherNameGd;
        this.guardianPhone = guardianPhone;
        this.guardianName = guardianName;
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

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
}
