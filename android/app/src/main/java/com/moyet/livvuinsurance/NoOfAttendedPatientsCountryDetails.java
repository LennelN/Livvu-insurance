package com.moyet.livvuinsurance;


public class NoOfAttendedPatientsCountryDetails {

    private String description;
    private String no_of_category;


    public NoOfAttendedPatientsCountryDetails( String description, String no_of_category ) {
        this.description = description;
        this.no_of_category = no_of_category;
    }

    public String getDescription() {
        return description;
    }

    public String getNo_of_category() {
        return no_of_category;
    }

   }
