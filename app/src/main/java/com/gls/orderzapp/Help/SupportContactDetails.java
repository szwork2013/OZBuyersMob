package com.gls.orderzapp.Help;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 12/9/14.
 */
public class SupportContactDetails {
    String message;
    List<String> oz_conatactsupport = new ArrayList<>();

    public List<String> getOz_conatactsupport() {
        return oz_conatactsupport;
    }

    public void setOz_conatactsupport(List<String> oz_conatactsupport) {
        this.oz_conatactsupport = oz_conatactsupport;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
