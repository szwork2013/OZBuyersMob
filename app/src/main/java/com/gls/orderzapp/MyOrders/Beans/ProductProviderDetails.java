package com.gls.orderzapp.MyOrders.Beans;

/**
 * Created by prajyot on 5/5/14.
 */
public class ProductProviderDetails {
    String branchid;
    String providerid;
    String providerlogo;
    String providername;
    String branchname;
    ProviderLocation location;

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
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

    public ProviderLocation getLocation() {
        return location;
    }

    public void setLocation(ProviderLocation location) {
        this.location = location;
    }
}
