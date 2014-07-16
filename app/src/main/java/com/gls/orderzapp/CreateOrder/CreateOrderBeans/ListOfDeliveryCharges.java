package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 14/7/14.
 */
public class ListOfDeliveryCharges {
    String message;
    List<DeliveryChargesDetails> deliverycharge = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DeliveryChargesDetails> getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(List<DeliveryChargesDetails> deliverycharge) {
        this.deliverycharge = deliverycharge;
    }
}
