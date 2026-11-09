package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class BillClientDetails {

    private String name;
    private String code;
    private String bill_date;
    private String total_bill_amount;
    private String bill_amount;
    private String bill_number;
    private String phone_number;
    private String phone_country;


    public BillClientDetails(String bill_date, String name, String code,String total_bill_amount,
                             String bill_amount,String bill_number,
                             String phone_number, String phone_country) {
        this.name = name;
        this.code = code;
        this.total_bill_amount = total_bill_amount;
        this.bill_amount = bill_amount;
        this.bill_number = bill_number;
        this.bill_date = bill_date;
        this.phone_number=phone_number;
        this.phone_country=phone_country;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getTotal_bill_amount() {
        return total_bill_amount;
    }

    public void setTotal_bill_amount(String total_bill_amount) {
        this.total_bill_amount = total_bill_amount;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_country() {
        return phone_country;
    }

    public void setPhone_country(String phone_country) {
        this.phone_country = phone_country;
    }


}
