package com.gls.orderzapp.MyOrders.Beans;

import java.util.List;

/**
 * Created by prajyot on 5/5/14.
 */
public class ProductProviderDetails {
    String branchid;
    String providerid;
    String providerlogo;
    String providername;
    String providerbrandname;
    String branchname;
    ProviderLocation location;
    List<String>contact_supports;

    public List<String> getContact_supports() {
        return contact_supports;
    }

    public void setContact_supports(List<String> contact_supports) {
        this.contact_supports = contact_supports;
    }

    public String getProviderbrandname() {
        return providerbrandname;
    }

    public void setProviderbrandname(String providerbrandname) {
        this.providerbrandname = providerbrandname;
    }

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
