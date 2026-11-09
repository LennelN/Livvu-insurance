package com.moyet.livvuinsurance;


public class DemographicContinentDetails {

    private String region;
    private String country;
    private String category;
    private String no_of_category;


    public DemographicContinentDetails(String region, String country, String category, String no_of_category ) {
        this.region = region;
        this.country = country;
        this.category = category;
        this.no_of_category = no_of_category;
    }

    public String getCategory() {
        return category;
    }

    public String getNo_of_category() {
        return no_of_category;
    }

    public String getRegion() { return region; }

    public String getCountry() { return country; }


}
