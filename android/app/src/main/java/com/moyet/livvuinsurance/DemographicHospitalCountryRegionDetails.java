package com.moyet.livvuinsurance;


public class DemographicHospitalCountryRegionDetails {

    private String region;
    private String category;
    private String no_of_category;


    public DemographicHospitalCountryRegionDetails(String region, String category, String no_of_category ) {
        this.region = region;
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



}
