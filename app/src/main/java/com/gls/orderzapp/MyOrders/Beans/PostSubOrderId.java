package com.gls.orderzapp.MyOrders.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 8/14/14.
 */
public class PostSubOrderId {
    List<String> suborderids = new ArrayList<>();

    public List<String> getSuborderids() {
        return suborderids;
    }

    public void setSuborderids(List<String> suborderids) {
        this.suborderids = suborderids;
    }
}
