package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class PatientClientStatusDetails {

    private String name;
    private String dob;
    private String idno;
    private String gender;
    private String date_joined;
    private String member_active;
    private String country_name;
    private String group_category;
    private String no_of_points;
    private String bonus_points;
    private String type;


    private static final String TAG ="PatientClientDetails";

    public PatientClientStatusDetails(String name, String dob, String idno, String gender,
                                      String date_joined, String member_active, String country_name,
                                      String group_category, String no_of_points, String bonus_points,
                                      String type) {
        this.name = name;
        this.dob = dob;
        this.idno = idno;
        this.gender = gender;
        this.date_joined = date_joined;
        this.member_active =member_active;
        this.country_name = country_name;
        this.group_category=group_category;
        this.no_of_points = no_of_points;
        this.bonus_points = bonus_points;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getIdno() {
        return idno;
    }

    public String getNoOfPoints() {
        return no_of_points;
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

    public String getGroup_category() {
        return group_category;
    }

    public String getGender() {
        return gender;
    }

    public String getBonus_points() {
        return bonus_points;
    }

    public String getType() {
        return type;
    }

}
