package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class DemographicDetails {

    private String category;
    private String no_of_category;

    public DemographicDetails(String category, String no_of_category ) {
        this.category = category;
        this.no_of_category = no_of_category;
    }

    public String getCategory() {
        return category;
    }

    public String getNo_of_category() {
        return no_of_category;
    }


}
