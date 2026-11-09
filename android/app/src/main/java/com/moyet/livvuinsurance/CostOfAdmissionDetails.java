package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class CostOfAdmissionDetails {


    private String code;
    private String description;
    private String phone_no;
    private String total_amount;
    private String count;


    public CostOfAdmissionDetails(String code, String description, String phone_no, String total_amount, String count ) {

        this.code = code;
        this.description = description;
        this.total_amount = total_amount;
        this.count = count;
        this.phone_no = phone_no;

    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getCount() {
        return count;
    }
}
