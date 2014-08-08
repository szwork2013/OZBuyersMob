package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

/**
 * Created by avi on 8/6/14.
 */
public class AvailableDeliveryTimingSlots {
    double from;
    double to;
    Boolean available;
    String branchid;

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
