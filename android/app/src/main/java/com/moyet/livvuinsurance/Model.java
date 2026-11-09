package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class Model {

    private String sNo;
    private String code;
    private String description;
    private String bill_date;
    private double price;
    private double total_price;
    private static final String TAG ="Model";

    public Model(String sNo, String code, String description, double price, double total_price,String bill_date) {
        this.sNo = sNo;
        this.code = code;
        this.description = description;
        this.price = price;
        this.total_price = total_price;
        this.bill_date = bill_date;

    }

    public String getsNo() {
        return sNo;
    }

    public String getBill_date() {
        return bill_date;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return total_price;
    }
}
