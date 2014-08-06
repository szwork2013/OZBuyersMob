package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 8/5/14.
 */
public class CheckDeliveryTimeSlotsProductIDs {
    String preferred_delivery_date;
List<String> productids=new ArrayList<>();

    public String getPreferred_delivery_date() {
        return preferred_delivery_date;
    }

    public void setPreferred_delivery_date(String preferred_delivery_date) {
        this.preferred_delivery_date = preferred_delivery_date;
    }

    public List<String> getProductids() {
        return productids;
    }

    public void setProductids(List<String> productids) {
        this.productids = productids;
    }
}
