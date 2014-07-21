package com.gls.orderzapp.Cart.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 21/7/14.
 */
public class AreaList {
    String message;
    List<String> area = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}
