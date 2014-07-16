package com.gls.orderzapp.Cart.Beans;

import com.gls.orderzapp.CreateOrder.CreateOrderBeans.AreaCoverage;

/**
 * Created by prajyot on 30/6/14.
 */
public class DeliveryChargeDetails {

    boolean delivery;
    double charge;
    boolean isdeliverychargeinpercent;
    AreaCoverage coverage = new AreaCoverage();

    public AreaCoverage getCoverage() {
        return coverage;
    }

    public void setCoverage(AreaCoverage coverage) {
        this.coverage = coverage;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public boolean isdeliverychargeinpercent() {
        return isdeliverychargeinpercent;
    }

    public void setIsdeliverychargeinpercent(boolean isdeliverychargeinpercent) {
        this.isdeliverychargeinpercent = isdeliverychargeinpercent;
    }
}
