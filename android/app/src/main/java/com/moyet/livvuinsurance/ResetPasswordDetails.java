package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class ResetPasswordDetails {

    private String name;
    private String dob_licence_no_email;
    private String idno_merchant_code;
    private String gender_branch_description;
    private String date_joined;
    private String member_active;
    private String country_name;
    private String group_category_user_role;
    private String phone_number;
    private String request_date;
    private String request_completed;
    private String request_processed;
    private String category;


    private static final String TAG ="ResetPasswordDetails";

    public ResetPasswordDetails(String name, String dob_licence_no_email, String idno_merchant_code, String gender_branch_description,
                                 String date_joined, String member_active, String country_name,
                                 String group_category_user_role, String phone_number, String request_date,
                                 String request_completed, String category,String request_processed) {
        this.name = name;
        this.dob_licence_no_email = dob_licence_no_email;
        this.idno_merchant_code = idno_merchant_code;
        this.gender_branch_description = gender_branch_description;
        this.date_joined = date_joined;
        this.member_active =member_active;
        this.country_name = country_name;
        this.group_category_user_role=group_category_user_role;
        this.phone_number = phone_number;
        this.request_date = request_date;
        this.request_completed = request_completed;
        this.request_processed = request_processed;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDob_licence_no_email() {
        return dob_licence_no_email;
    }

    public String getIdno_merchant_code() {
        return idno_merchant_code;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public String getMember_active() {
        return member_active;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getGroup_category_user_role() {
        return group_category_user_role;
    }

    public String getGender_branch_description() {
        return gender_branch_description;
    }

    public String getRequest_date() {
        return request_date;
    }

    public String getRequest_completed() {
        return request_completed;
    }

    public String getRequest_processed() {
        return request_processed;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getCategory() {
        return category;
    }

    }
