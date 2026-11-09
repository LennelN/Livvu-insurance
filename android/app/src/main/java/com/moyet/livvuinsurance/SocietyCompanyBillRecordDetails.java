package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class SocietyCompanyBillRecordDetails {

    private String phone_no;
    private String bill_date;
    private String bill_no;
    private String merchant_name;
    private double bill_amount;
    private String no_of_points;
    private String bill_paid;



    private static final String TAG ="SocComBillRecDetails";

    public SocietyCompanyBillRecordDetails(String phone_no, String bill_date, String bill_no, String merchant_name, double bill_amount,
                                            String no_of_points,String bill_paid){

        this.phone_no = phone_no;
        this.bill_date = bill_date;
        this.bill_no = bill_no;
        this.no_of_points = no_of_points;
        this.bill_amount = bill_amount;
        this.bill_paid = bill_paid;
        this.merchant_name = merchant_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getBill_date() {
        return bill_date;
    }

    public String getNo_of_points() {
        return no_of_points;
    }

    public String getBill_no() {
        return bill_no;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getBill_paid(){return bill_paid;}

    public double getBill_amount(){return bill_amount;}

}
