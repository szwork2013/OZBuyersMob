package com.gls.orderzapp.Provider.Beans;

import com.gls.orderzapp.SignUp.Location;

/**
 * Created by prajyot on 6/5/14.
 */
public class BranchInfo {
    String branchid;
    String branchname;
    Location location;
    String note;
    DeliveryMode delivery;

    public DeliveryMode getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryMode delivery) {
        this.delivery = delivery;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
