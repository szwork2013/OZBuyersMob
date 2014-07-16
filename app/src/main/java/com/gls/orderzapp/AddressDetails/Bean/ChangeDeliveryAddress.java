package com.gls.orderzapp.AddressDetails.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 2/7/14.
 */
public class ChangeDeliveryAddress {
    String message;
    List<ListOfDeliveryAddress> deliveryaddresses = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ListOfDeliveryAddress> getDeliveryaddresses() {
        return deliveryaddresses;
    }

    public void setDeliveryaddresses(List<ListOfDeliveryAddress> deliveryaddresses) {
        this.deliveryaddresses = deliveryaddresses;
    }
}
