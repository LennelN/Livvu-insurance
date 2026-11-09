package com.moyet.livvuinsurance;

public class HospitalCountryDetails {

    private String region;
    private String sector;
    private String name;
    private String code;
    private String points_paid;
    private String points_cashed_out;
    private String balance;
    private String phone;
    private String address;
    private String date;




    public HospitalCountryDetails(String region, String sector, String name, String code, String points_paid, String points_cashed_out, String balance,
                                  String phone, String address, String date) {
        this.region = region;
        this.sector = sector;
        this.name = name;
        this.code = code;
        this.points_paid = points_paid;
        this.points_cashed_out = points_cashed_out;
        this.balance = balance;
        this.phone = phone;
        this.address = address;
        this.date = date;
    }


    public String getRegion() { return region; }

    public String getSector() { return sector; }

    public String getName() { return name; }

    public String getPoints_paid() {
        return points_paid;
    }

    public String getPoints_cashed_out() {
        return points_cashed_out;
    }

    public String getBalance() {
        return balance;
    }

    public String getCode() { return code; }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

}
