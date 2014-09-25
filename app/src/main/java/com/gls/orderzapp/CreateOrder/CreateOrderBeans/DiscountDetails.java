package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

/**
 * Created by prajyot on 24/9/14.
 */
public class DiscountDetails {
    String code;
    double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
