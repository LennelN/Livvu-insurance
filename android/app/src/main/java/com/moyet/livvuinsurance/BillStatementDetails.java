package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class BillStatementDetails {

    private String code;
    private String transaction_date;
    private String no_of_points;
    private String no_of_points_paid;
    private String description;

    private static final String TAG ="BillStatementDetails";

    public BillStatementDetails(String transaction_date, String code, String description, String no_of_points, String no_of_points_paid ) {
        this.code = code;
        this.no_of_points = no_of_points;
        this.no_of_points_paid = no_of_points_paid;
        this.description = description;
        this.transaction_date = transaction_date;

    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getCode() {
        return code;
    }

    public String getNoOfPoints() {
        return no_of_points;
    }

    public String getNoOfPointsPaid() {
        return no_of_points_paid;
    }

    public String getDescription() {
        return description;
    }
}
