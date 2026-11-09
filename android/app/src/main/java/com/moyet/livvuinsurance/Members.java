package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class Members {

    private String sPhoneNo;
    private String sCountry;

    public Members(String sPhoneNo, String sCountry) {
        this.sPhoneNo = sPhoneNo;
        this.sCountry = sCountry;
    }

    public String getsPhoneNo() {
        return sPhoneNo;
    }
    public String getsCountry() {
        return sCountry;
    }

    public void setsCountry(String sCountry) {
        this.sCountry = sCountry;
    }

}
