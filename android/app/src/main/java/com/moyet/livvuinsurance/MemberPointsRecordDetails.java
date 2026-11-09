package com.moyet.livvuinsurance;

class MemberPointsRecordDetails {

    private String date_of_purchase;
    private String transaction_type;
    private String transaction_no;
    private double purchase_amount;
    private String no_of_points;



    private static final String TAG ="MemberPointsDetails";

    public MemberPointsRecordDetails(String date_of_purchase, String transaction_type, String transaction_no,
                                     double purchase_amount,
                                           String no_of_points){


        this.date_of_purchase = date_of_purchase;
        this.transaction_type = transaction_type;
        this.transaction_no = transaction_no;
        this.purchase_amount = purchase_amount;
        this.no_of_points = no_of_points;

    }

    public String getDate_of_purchase() {
        return date_of_purchase;
    }

    public String getNo_of_points() {
        return no_of_points;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public double getPurchase_amount(){return purchase_amount;}

}
