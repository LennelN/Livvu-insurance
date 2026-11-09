package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class MerchantCompanyStatusDetails {


    private String merchant_name;
    private String merchant_code;
    private String merchant_description;
    private String merchant_address;
    private String merchant_date_joined;
    private String merchant_country;
    private String merchant_region;
    private String group_category;
    private String type_of_registration;
    private String status_flag;
    private String no_of_points;
    private String bonus_points;
    private String type;


    private static final String TAG ="MerchantCompDetails";

    public MerchantCompanyStatusDetails(String merchant_name, String merchant_code, String merchant_description,
            String merchant_address, String merchant_date_joined, String merchant_country, String merchant_region,
            String group_category, String type_of_registration, String status_flag, String no_of_points, String bonus_points,
                                        String type)
    {
        this.merchant_name = merchant_name;
        this.merchant_code = merchant_code;
        this.merchant_description = merchant_description;
        this.merchant_address = merchant_address;
        this.merchant_date_joined = merchant_date_joined;
        this.merchant_country = merchant_country;
        this.merchant_region = merchant_region;
        this.group_category = group_category;
        this.type_of_registration = type_of_registration;
        this.status_flag = status_flag;
        this.no_of_points = no_of_points;
        this.bonus_points = bonus_points;
        this.type = type;

    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public String getMerchant_description() {
        return merchant_description;
    }

    public String getMerchant_address() {
        return merchant_address;
    }

    public String getMerchant_date_joined() {
        return merchant_date_joined;
    }

    public String getMerchant_country() {
        return merchant_country;
    }

    public String getMerchant_region() {
        return merchant_region;
    }

    public String getGroup_category() {
        return group_category;
    }

    public String getType_of_registration() {
        return type_of_registration;
    }

    public String getStatus_flag() {
        return status_flag;
    }

    public String getNo_of_points() {
        return no_of_points;
    }

    public String getBonus_points() {
        return bonus_points;
    }

    public String getType() {
        return type;
    }

}
