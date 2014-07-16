package com.gls.orderzapp.Provider.Beans;

/**
 * Created by avinash on 11/7/14.
 */
public class DeliveryMode {
    Boolean isprovidehomedelivery;
    Boolean isprovidepickup;

    public Boolean getIsprovidehomedelivery() {
        return isprovidehomedelivery;
    }

    public void setIsprovidehomedelivery(Boolean isprovidehomedelivery) {
        this.isprovidehomedelivery = isprovidehomedelivery;
    }

    public Boolean getIsprovidepickup() {
        return isprovidepickup;
    }

    public void setIsprovidepickup(Boolean isprovidepickup) {
        this.isprovidepickup = isprovidepickup;
    }
}
