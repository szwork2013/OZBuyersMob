package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 8/6/14.
 */
public class DeliveryTimeSlots {
    String branchid;
    String expected_date;
    List<AvailableDeliveryTimingSlots> deliverytimingslots = new ArrayList<>();

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public List<AvailableDeliveryTimingSlots> getDeliverytimingslots() {
        return deliverytimingslots;
    }

    public void setDeliverytimingslots(List<AvailableDeliveryTimingSlots> deliverytimingslots) {
        this.deliverytimingslots = deliverytimingslots;
    }
}
