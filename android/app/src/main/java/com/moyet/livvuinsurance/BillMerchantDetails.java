package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class BillMerchantDetails {

    private String phone_no;
    private String bill_date;
    private String no_of_points;
    private String bill_number;
    private String no_of_days;

    private static final String TAG ="BillMerchantDetails";

    public BillMerchantDetails(String bill_date, String phone_no, String no_of_points, String bill_number, String no_of_days ) {
        this.phone_no = phone_no;
        this.no_of_points = no_of_points;
        this.bill_number = bill_number;
        this.bill_date = bill_date;
        this.no_of_days=no_of_days;

    }


    public String getBill_date() {
        return bill_date;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getNoOfPoints() {
        return no_of_points;
    }

    public String getBillNumber() {
        return bill_number;
    }

    public String getNo_of_days() {
        return no_of_days;
    }

}
