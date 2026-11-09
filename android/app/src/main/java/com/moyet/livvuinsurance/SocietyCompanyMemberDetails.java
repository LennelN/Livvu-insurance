package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class SocietyCompanyMemberDetails {


    private String phone_no;
    private String name;
    private String joining_date;
    private String due_date;
    private String no_of_points;

    private static final String TAG ="SocComMemberDetails";

    public SocietyCompanyMemberDetails( String phone_no, String name,String joining_date, String due_date,String no_of_points){

        this.phone_no = phone_no;
        this.no_of_points = no_of_points;
        this.joining_date = joining_date;
        this.due_date = due_date;
        this.name = name;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public String getNo_of_points() {
        return no_of_points;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getName() {
        return name;
    }

    public String getDue_date(){return due_date;}

}
