package com.moyet.livvuinsurance;

public class DoctorDeActiveReportDetails {

    private String dateJoined;
    private String doctorName;
    private String doctorPhone;
    private String doctorProfession;
    private String doctorRegNo;
    private String dateDeactivated;
    private String reasonDeactivated;

    public DoctorDeActiveReportDetails(
            String dateJoined,
            String doctorName,
            String doctorPhone,
            String doctorProfession,
            String doctorRegNo,
            String dateDeactivated,
            String reasonDeactivated
    ) {

        this.dateJoined = dateJoined;
        this.doctorName= doctorName;
        this.doctorPhone = doctorPhone;
        this.doctorProfession=doctorProfession;
        this.doctorRegNo=doctorRegNo;
        this.dateDeactivated=dateDeactivated;
        this.reasonDeactivated=reasonDeactivated;
    }

    public DoctorDeActiveReportDetails(){};

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorProfession() {
        return doctorProfession;
    }

    public void setDoctorProfession(String doctorProfession) {
        this.doctorProfession = doctorProfession;
    }

    public String getDoctorRegNo() {
        return doctorRegNo;
    }

    public void setDoctorRegNo(String doctorRegNo) {
        this.doctorRegNo = doctorRegNo;
    }

    public String getDateDeactivated() {
        return dateDeactivated;
    }

    public void setDateDeactivated(String dateDeactivated) {
        this.dateDeactivated = dateDeactivated;
    }

    public String getReasonDeactivated() {
        return reasonDeactivated;
    }

    public void setReasonDeactivated(String reasonDeactivated) {
        this.reasonDeactivated = reasonDeactivated;
    }

}
