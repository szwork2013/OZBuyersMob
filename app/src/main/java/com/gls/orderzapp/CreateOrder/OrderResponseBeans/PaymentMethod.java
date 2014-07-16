package com.gls.orderzapp.CreateOrder.OrderResponseBeans;

/**
 * Created by prajyot on 6/5/14.
 */
public class PaymentMethod {
    String mode;
    String paymentid;


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }
}
