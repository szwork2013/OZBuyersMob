package com.gls.orderzapp.Help;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 18/9/14.
 */
public class PhoneAndEmailSupport {
    List<String> phone = new ArrayList<>();
    String email;

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
