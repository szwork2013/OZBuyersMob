package com.gls.orderzapp.Cart.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 18/7/14.
 */
public class CitiesList {
    String message;
    List<String> city = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }
}
