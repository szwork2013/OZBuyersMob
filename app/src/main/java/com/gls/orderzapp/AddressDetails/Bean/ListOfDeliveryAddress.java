package com.gls.orderzapp.AddressDetails.Bean;

import com.gls.orderzapp.SignUp.Location;

/**
 * Created by avinash on 2/7/14.
 */
public class ListOfDeliveryAddress {
    String deliveryaddressid;
    Location address;

    public String getDeliveryaddressid() {
        return deliveryaddressid;
    }

    public void setDeliveryaddressid(String deliveryaddressid) {
        this.deliveryaddressid = deliveryaddressid;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }
}
