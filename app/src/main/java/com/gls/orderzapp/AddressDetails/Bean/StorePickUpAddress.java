package com.gls.orderzapp.AddressDetails.Bean;

import com.gls.orderzapp.SignUp.Location;

/**
 * Created by prajyot on 22/7/14.
 */
public class StorePickUpAddress {
    String branchid;
    Location location;

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
