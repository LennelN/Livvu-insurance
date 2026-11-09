package com.moyet.livvuinsurance;


public class DemographicPharmacyContinentDetails {

    private String region;
    private String country;
    private String no_of_pharmacies;


    public DemographicPharmacyContinentDetails(String region, String country, String no_of_pharmacies ) {
        this.region = region;
        this.country = country;
        this.no_of_pharmacies = no_of_pharmacies;
    }

    public String getCountry() {
        return country;
    }

    public String getNo_of_pharmacies() {
        return no_of_pharmacies;
    }

    public String getRegion() { return region; }



}
