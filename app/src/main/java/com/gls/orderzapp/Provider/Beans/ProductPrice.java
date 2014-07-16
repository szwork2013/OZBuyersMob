package com.gls.orderzapp.Provider.Beans;

/**
 * Created by avinash on 11/4/14.
 */
public class ProductPrice {
    String currency;
    double value;
    String uom;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
