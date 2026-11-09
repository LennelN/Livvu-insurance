package com.moyet.livvuinsurance;

public class BillRequestDetails {

    private String merchant_name_data;
    private String merchant_code_data;
    private String merchant_phone_number_data;
    private String merchant_region_data;

    public BillRequestDetails(String merchant_name_data, String merchant_code_data,
                              String merchant_phone_number_data, String merchant_region_data) {
        this.merchant_name_data=merchant_name_data;
        this.merchant_code_data=merchant_code_data;
        this.merchant_phone_number_data=merchant_phone_number_data;
        this.merchant_region_data=merchant_region_data;

    }

    public String getMerchant_name_data() {
        return merchant_name_data;
    }

    public void setMerchant_name_data(String merchant_name_data) {
        this.merchant_name_data = merchant_name_data;
    }

    public String getMerchant_code_data() {
        return merchant_code_data;
    }

    public void setMerchant_code_data(String merchant_code_data) {
        this.merchant_code_data = merchant_code_data;
    }

    public String getMerchant_phone_number_data() {
        return merchant_phone_number_data;
    }

    public void setMerchant_phone_number_data(String merchant_phone_number_data) {
        this.merchant_phone_number_data = merchant_phone_number_data;
    }

    public String getMerchant_region_data() {
        return merchant_region_data;
    }

    public void setMerchant_region_data(String merchant_region_data) {
        this.merchant_region_data = merchant_region_data;
    }



    @Override
    public String toString() {
        return merchant_name_data;
    }

}
