package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

/**
 * Created by prajyot on 14/7/14.
 */
public class DeliveryChargeDetails {
    boolean delivery;
    boolean isdeliverychargeinpercent;
    String branchid;
    double charge;
    AreaCoverage coverage;

    public boolean isIsdeliverychargeinpercent() {
        return isdeliverychargeinpercent;
    }

    public void setIsdeliverychargeinpercent(boolean isdeliverychargeinpercent) {
        this.isdeliverychargeinpercent = isdeliverychargeinpercent;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public AreaCoverage getCoverage() {
        return coverage;
    }

    public void setCoverage(AreaCoverage coverage) {
        this.coverage = coverage;
    }
}
