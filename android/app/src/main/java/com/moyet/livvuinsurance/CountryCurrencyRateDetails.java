package com.moyet.livvuinsurance;


public class CountryCurrencyRateDetails {

    private String currencyName;
    private String country;
    private String currencyRate;
    private String currencyCode;


    public CountryCurrencyRateDetails(String currencyName, String currencyRate, String currencyCode, String country ) {
        this.currencyName = currencyName;
        this.country = country;
        this.currencyRate = currencyRate;
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

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }



}
