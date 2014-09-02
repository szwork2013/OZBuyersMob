package com.gls.orderzapp.CountryCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 1/9/14.
 */
public class CountryCodeDetails {
    String message ;
    List<CountryCode> countrycode = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CountryCode> getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(List<CountryCode> countrycode) {
        this.countrycode = countrycode;
    }
}
