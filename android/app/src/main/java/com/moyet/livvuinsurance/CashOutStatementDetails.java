package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class CashOutStatementDetails {

    private String transaction_date;
    private String no_of_points;
    private String no_of_days;
    private String cashed_out_transaction_no;


    public CashOutStatementDetails(String transaction_date, String no_of_points, String no_of_days,String cashed_out_transaction_no ) {
        this.no_of_points = no_of_points;
        this.no_of_days = no_of_days;
        this.transaction_date = transaction_date;
        this.cashed_out_transaction_no = cashed_out_transaction_no;

    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getNo_of_days() {
        return no_of_days;
    }

    public String getNoOfPoints() {
        return no_of_points;
    }

    public String getCashed_out_transaction_no() {
        return cashed_out_transaction_no;
    }


}
