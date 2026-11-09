package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class ClientStatementDetails {

    private String transaction_type;
    private String date_paid;
    private String phone_number;
    private String merchant_phone_number;
    private String no_of_points_paid;
    private String no_of_points_credit;

    private static final String TAG ="ClientStatementDetails";

    public ClientStatementDetails(String transaction_type,String date_paid, String phone_number, String merchant_phone_number,  String no_of_points_paid,String no_of_points_credit) {
        this.transaction_type = transaction_type;
        this.date_paid = date_paid;
        this.no_of_points_paid = no_of_points_paid;
        this.phone_number = phone_number;
        this.merchant_phone_number = merchant_phone_number;
        this.no_of_points_credit = no_of_points_credit;

    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public String getDate_paid() {
        return date_paid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getMerchant_phone_number() {
        return merchant_phone_number;
    }

    public String getNoOfPointsPaid() {
        return no_of_points_paid;
    }

    public String getNo_of_points_credit() {
        return no_of_points_credit;
    }


}
