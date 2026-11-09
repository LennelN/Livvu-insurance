package com.example.livvumobileapp;

class PatientMedicalReportDetails {
    private String code,description,transactionDate,doctor,frequency,no_of_points;
    private double tariff_amount,tariff_total_amount;

    public PatientMedicalReportDetails(String code, String description, double tariff_amount,double tariff_total_amount,
                                       String transactionDate, String doctor, String frequency, String no_of_points) {
        this.code = code;
        this.description = description;
        this.tariff_amount = tariff_amount;
        this.tariff_total_amount = tariff_total_amount;
        this.transactionDate = transactionDate;
        this.doctor = doctor;
        this.frequency = frequency;
        this.no_of_points=no_of_points;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getTariff_amount() {
        return tariff_amount;
    }

    public void setTariff_amount(double tariff_amount) {
        this.tariff_amount = tariff_amount;
    }

    public double getTariff_total_amount() {
        return tariff_total_amount;
    }

    public void setTariff_total_amount(double tariff_total_amount) {
        this.tariff_total_amount = tariff_total_amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNo_of_points() {
        return no_of_points;
    }

    public void setNo_of_points(String no_of_points) {
        this.no_of_points = no_of_points;
    }


}
