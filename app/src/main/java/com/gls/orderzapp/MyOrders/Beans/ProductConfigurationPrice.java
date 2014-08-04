package com.gls.orderzapp.MyOrders.Beans;

/**
 * Created by prajyot on 4/8/14.
 */
public class ProductConfigurationPrice {
    String uom;
    double value;

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
