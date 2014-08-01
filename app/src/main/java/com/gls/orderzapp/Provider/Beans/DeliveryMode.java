package com.gls.orderzapp.Provider.Beans;

/**
 * Created by avinash on 11/7/14.
 */
public class DeliveryMode {
    boolean isprovidehomedelivery;
    boolean isprovidepickup;
    boolean isdeliverychargeinpercent;

    public boolean isIsdeliverychargeinpercent() {
        return isdeliverychargeinpercent;
    }

    public void setIsdeliverychargeinpercent(boolean isdeliverychargeinpercent) {
        this.isdeliverychargeinpercent = isdeliverychargeinpercent;
    }

    public boolean getIsprovidehomedelivery() {
        return isprovidehomedelivery;
    }

    public void setIsprovidehomedelivery(Boolean isprovidehomedelivery) {
        this.isprovidehomedelivery = isprovidehomedelivery;
    }

    public boolean getIsprovidepickup() {
        return isprovidepickup;
    }

    public void setIsprovidepickup(Boolean isprovidepickup) {
        this.isprovidepickup = isprovidepickup;
    }
}
