package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 8/6/14.
 */
public class SuccesMessageForDeliveryTimingSlots {
    String message;
    List<DeliveryTimeSlots> doc = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DeliveryTimeSlots> getDoc() {
        return doc;
    }

    public void setDoc(List<DeliveryTimeSlots> doc) {
        this.doc = doc;
    }
}
