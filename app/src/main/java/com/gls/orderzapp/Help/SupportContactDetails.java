package com.gls.orderzapp.Help;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 12/9/14.
 */
public class SupportContactDetails {
    String message;
    PhoneAndEmailSupport oz_contactsupport;

    public PhoneAndEmailSupport getOz_contactsupport() {
        return oz_contactsupport;
    }

    public void setOz_contactsupport(PhoneAndEmailSupport oz_contactsupport) {
        this.oz_contactsupport = oz_contactsupport;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
