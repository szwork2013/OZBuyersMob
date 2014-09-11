package com.gls.orderzapp.Provider.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 10/9/14.
 */
public class SelectCityDetails {
    String message;
   List<String> city = new ArrayList<>();


    public List<String> getCity() {
        return city;
    }

    public void setCity(List<String> city) {
        this.city = city;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
