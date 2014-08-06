package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

/**
 * Created by avi on 8/6/14.
 */
public class AvailableDeliveryTimingSlots {
    String from;
    String to;
    Boolean available;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
