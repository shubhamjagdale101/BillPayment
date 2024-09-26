package com.shubham.lightbill.lightbill_backend.model;

public class Consumption {
    private String month;
    private String year;
    private int units;
    private double amount;

    public Consumption(String month, int units, double amount) {
        this.month = month;
        this.units = units;
        this.amount = amount;
    }
}

