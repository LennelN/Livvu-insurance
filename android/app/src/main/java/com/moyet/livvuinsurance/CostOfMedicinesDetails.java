package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class CostOfMedicinesDetails {


    private String code;
    private String description;
    private String average_cost;
    private String total_amount;
    private String count;


    public CostOfMedicinesDetails(String code, String description, String average_cost, String total_amount, String count ) {

        this.code = code;
        this.description = description;
        this.total_amount = total_amount;
        this.count = count;
        this.average_cost = average_cost;

    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone_no() {
        return average_cost;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getCount() {
        return count;
    }
}
