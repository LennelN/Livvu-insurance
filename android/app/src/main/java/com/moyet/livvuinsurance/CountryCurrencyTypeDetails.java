package com.moyet.livvuinsurance;


public class CountryCurrencyTypeDetails {

    private String currencyName;
    private String country;
    private String currencySymbol;
    private String currencyCode;


    public CountryCurrencyTypeDetails(String currencyName, String currencySymbol, String currencyCode, String country ) {
        this.currencyName = currencyName;
        this.country = country;
        this.currencySymbol = currencySymbol;
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    

}
