package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRate {
    private String currency;
    private String code;
    private float rate; // Quy đổi 1 USD sang được bao nhiêu

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public CurrencyRate(String currency, String code, float rate) {
        this.currency = currency;
        this.code = code;
        this.rate = rate;
    }

    public CurrencyRate() {
    }

    @Override
    public String toString() {
        return "CurrencyRate [code=" + code + ", currency=" + currency + ", rate=" + rate + "]";
    }
}
