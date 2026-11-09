package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class PaymentDetails {

    private String transaction_date;
    private String no_of_points_paid;
    private String phone_number;
    private String bill_number;

    public PaymentDetails(String transaction_date,  String phone_number, String bill_number, String no_of_points_paid ) {
        this.no_of_points_paid = no_of_points_paid;
        this.phone_number = phone_number;
        this.transaction_date = transaction_date;
        this.bill_number = bill_number;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getNoOfPointsPaid() {
        return no_of_points_paid;
    }

    public String getBill_number() {
        return bill_number;
    }


}
