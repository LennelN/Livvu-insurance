package com.moyet.livvuinsurance;

public class TariffDetails {
    private String code,description,category,type;
    private double price;

    public TariffDetails(String code, String description, double price, String category, String type) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.category = category;
        this.type = type;
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


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return code;
    }

}
