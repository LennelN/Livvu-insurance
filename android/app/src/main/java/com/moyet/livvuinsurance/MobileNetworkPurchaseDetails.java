package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class MobileNetworkPurchaseDetails {

    private String transaction_date;
    private String phone_no;
    private double purchase_amount;
    private String purchase_done_by;
    private String no_of_days;
    private String transaction_no;

    private static final String TAG ="MobPurchasesDetails";

    public MobileNetworkPurchaseDetails(String transaction_date, String phone_no, double purchase_amount,
                                        String purchase_done_by, String no_of_days, String transaction_no){

        this.phone_no = phone_no;
        this.no_of_days = no_of_days;
        this.transaction_date = transaction_date;
        this.purchase_done_by = purchase_done_by;
        this.purchase_amount = purchase_amount;
        this.transaction_no = transaction_no;

    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getNo_of_days() {
        return no_of_days;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getPurchase_done_by() {
        return purchase_done_by;
    }

    public double getPurchase_amount() {return purchase_amount;}

    public String getTransaction_no() {
        return transaction_no;
    }


}
