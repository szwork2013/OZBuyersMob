package com.gls.orderzapp.Cart.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 18/7/14.
 */
public class StatesList {
    String message;
    List<String> states = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }
}
