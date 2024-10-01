package com.shubham.lightbill.lightbill_backend.model;

import lombok.Getter;

@Getter
public class Consumption {
    private String month;
    private Boolean paid;
    private int units;
    private double amount;

    public Consumption(String month, int units, double amount, Boolean paid) {
        this.month = month;
        this.units = units;
        this.amount = amount;
        this.paid = paid;
    }
}
