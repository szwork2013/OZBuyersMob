package com.gls.orderzapp.AddressDetails.Bean;

import com.gls.orderzapp.SignUp.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 18/7/14.
 */
public class SelectPickUpAddressBean {
    String message;
   List<Location> addresses = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Location> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Location> addresses) {
        this.addresses = addresses;
    }
}
