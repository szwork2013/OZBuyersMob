package com.gls.orderzapp.Cart.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 6/5/14.
 */
public class BranchIdsForGettingDeliveryCharges {
    String area;
    String city;
    List<String> branchids = new ArrayList<>();

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getBranchids() {
        return branchids;
    }

    public void setBranchids(List<String> branchids) {
        this.branchids = branchids;
    }
}
