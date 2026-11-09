package com.moyet.livvuinsurance;

public class DoctorActiveReportDetails {

    private String dateJoined;
    private String doctorName;
    private String doctorPhone;
    private String doctorProfession;
    private String doctorRegNo;

    public DoctorActiveReportDetails(
            String dateJoined,
            String doctorName,
            String doctorPhone,
            String doctorProfession,
            String doctorRegNo
    ) {

        this.dateJoined = dateJoined;
        this.doctorName= doctorName;
        this.doctorPhone = doctorPhone;
        this.doctorProfession=doctorProfession;
        this.doctorRegNo=doctorRegNo;
    }

    public DoctorActiveReportDetails(){};

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

}
