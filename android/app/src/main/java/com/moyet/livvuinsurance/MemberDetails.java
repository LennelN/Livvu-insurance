package com.moyet.livvuinsurance;

import androidx.annotation.NonNull;

public class MemberDetails {
    private String phone;
    private String mdetails;

    public MemberDetails(String phone, String mdetails) {
        this.phone = phone; this.mdetails = mdetails;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMdetails() {
        return mdetails;
    }

    public void setMdetails(String mdetails){this.mdetails = mdetails;}

    @NonNull
    @Override
    public String toString() {
        return mdetails;
    }
}
