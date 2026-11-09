package com.moyet.livvuinsurance;


public class NoOfAdmissionsCountryDetails {

    private String code;
    private String description;
    private String no_of_category;


    public NoOfAdmissionsCountryDetails(String code, String description, String no_of_category ) {
        this.code = code;
        this.description = description;
        this.no_of_category = no_of_category;
    }

    public String getDescription() {
        return description;
    }

    public String getNo_of_category() {
        return no_of_category;
    }

    public String getCode() { return code; }



}
