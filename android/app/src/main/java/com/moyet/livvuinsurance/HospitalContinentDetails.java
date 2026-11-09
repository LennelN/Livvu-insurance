package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class HospitalContinentDetails {

    private String region;
    private String country;
    private String points_paid;
    private String points_cashed_out;
    private String balance;



    public HospitalContinentDetails(String region, String country, String points_paid, String points_cashed_out, String balance ) {
        this.region = region;
        this.country = country;
        this.points_paid = points_paid;
        this.points_cashed_out = points_cashed_out;
        this.balance = balance;
    }


    public String getRegion() { return region; }

    public String getCountry() { return country; }

    public String getPoints_paid() {
        return points_paid;
    }

    public String getPoints_cashed_out() {
        return points_cashed_out;
    }

    public String getBalance() {
        return balance;
    }


}
