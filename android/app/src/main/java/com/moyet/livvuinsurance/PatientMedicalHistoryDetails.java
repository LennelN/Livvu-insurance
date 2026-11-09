package com.moyet.livvuinsurance;

/**
 * Created by FMoyo on 08-Oct-2017.
 */

public class PatientMedicalHistoryDetails {

    private String transaction_date;
    private String merchant_phone_number;
    private String merchant_name;
    private String body_temperature;
    private String blood_pressure;
    private String body_pulse;
    private String body_allergies;
    private String blood_type;
    private String other_details;



    public PatientMedicalHistoryDetails(String transaction_date, String merchant_phone_number, String merchant_name
            ,String body_temperature, String blood_pressure,
                                        String body_pulse, String body_allergies, String blood_type,
                                        String other_details) {
        this.transaction_date = transaction_date;
        this.merchant_phone_number = merchant_phone_number;
        this.merchant_name = merchant_name;
        this.body_temperature = body_temperature;
        this.blood_pressure = blood_pressure;
        this.body_pulse = body_pulse;
        this.body_allergies =body_allergies;
        this.blood_type = blood_type;
        this.other_details=other_details;

    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getMerchant_phone_number() {
        return merchant_phone_number;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getBody_temperature() {
        return body_temperature;
    }



    public String getBody_pulse() {
        return body_pulse;
    }

    public String getBody_allergies() {
        return body_allergies;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public String getOther_details() {
        return other_details;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }


}
