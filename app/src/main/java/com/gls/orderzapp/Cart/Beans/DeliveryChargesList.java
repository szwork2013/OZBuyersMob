package com.gls.orderzapp.Cart.Beans;

import java.util.List;

/**
 * Created by prajyot on 30/6/14.
 */
public class DeliveryChargesList {
    List<DeliveryChargeDetails> deliverycharge;
    String message;

    public List<DeliveryChargeDetails> getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(List<DeliveryChargeDetails> deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
