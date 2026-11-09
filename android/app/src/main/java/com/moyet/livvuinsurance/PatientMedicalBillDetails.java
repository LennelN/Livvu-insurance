package com.moyet.livvuinsurance;

class PatientMedicalBillDetails {
    private String transactionDate,billNo,no_of_points,merchant_phone_number,merchant_name,bill_paid_indicator;
    private double bill_total_amount;

    public PatientMedicalBillDetails(String billNo, String merchant_name, double bill_total_amount,
                                     String transactionDate, String bill_paid_indicator, String no_of_points,
                                     String merchant_phone_number) {
        this.billNo = billNo;
        this.merchant_name = merchant_name;
        this.bill_total_amount = bill_total_amount;
        this.transactionDate = transactionDate;
        this.bill_paid_indicator = bill_paid_indicator;
        this.no_of_points=no_of_points;
        this.merchant_phone_number = merchant_phone_number;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public double getBill_total_amount() {
        return bill_total_amount;
    }

    public void setBill_total_amount(double bill_total_amount) {
        this.bill_total_amount = bill_total_amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getBill_paid_indicator() {
        return bill_paid_indicator;
    }

    public void setBill_paid_indicator(String bill_paid_indicator) {
        this.bill_paid_indicator = bill_paid_indicator;
    }

    public String getNo_of_points() {
        return no_of_points;
    }

    public void setNo_of_points(String no_of_points) {
        this.no_of_points = no_of_points;
    }

    public String getMerchant_phone_number() {
        return merchant_phone_number;
    }

    public void setMerchant_phone_number(String merchant_phone_number) {
        this.merchant_phone_number = merchant_phone_number;
    }

}
