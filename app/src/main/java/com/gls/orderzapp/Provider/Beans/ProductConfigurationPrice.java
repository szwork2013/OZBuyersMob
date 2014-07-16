package com.gls.orderzapp.Provider.Beans;

/**
 * Created by prajyot on 3/7/14.
 */
public class ProductConfigurationPrice {
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
}
