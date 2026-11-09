package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class NoOfAdmissionContinentDetails {

    private String region;
    private String country;
    private String tariff_code;
    private String description;
    private String balance;



    public NoOfAdmissionContinentDetails(String region, String country, String tariff_code, String description,
                                         String balance ) {
        this.region = region;
        this.country = country;
        this.tariff_code = tariff_code;
        this.description = description;
        this.balance = balance;
    }


    public String getRegion() { return region; }

    public String getCountry() { return country; }

    public String getTariff_code() {
        return tariff_code;
    }

    public String getDescription() {
        return description;
    }

    public String getBalance() {
        return balance;
    }


}
