package com.gls.orderzapp.Cart.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 17/7/14.
 */
public class CountryList {
    String message;
    List<String> country = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }
}
