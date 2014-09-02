package com.gls.orderzapp.Cart.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 8/20/14.
 */
public class ZipCodeList {
    String message;
    List<String> zipcode = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getZipcode() {
        return zipcode;
    }

    public void setZipcode(List<String> zipcode) {
        this.zipcode = zipcode;
    }
}
