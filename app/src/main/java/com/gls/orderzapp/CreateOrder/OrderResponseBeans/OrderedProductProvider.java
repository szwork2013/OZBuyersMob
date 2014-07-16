package com.gls.orderzapp.CreateOrder.OrderResponseBeans;

/**
 * Created by prajyot on 6/5/14.
 */
public class OrderedProductProvider {
    String branchid;
    Address location;
    String providerid;
    String providerlogo;
    String providername;

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }

    public String getProviderlogo() {
        return providerlogo;
    }

    public void setProviderlogo(String providerlogo) {
        this.providerlogo = providerlogo;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }
}
