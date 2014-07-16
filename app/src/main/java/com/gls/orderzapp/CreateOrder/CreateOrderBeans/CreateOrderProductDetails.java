package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import com.gls.orderzapp.SignUp.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 24/4/14.
 */
public class CreateOrderProductDetails {
    String preferred_delivery_date;
    String productid;
    String qty;
    String branchid;
    String uom;
    String orderprice;
    String productname;
    String cartCount;
    String providername;
    List<ProductConfiguration> productconfiguration = new ArrayList<>();
    Location location;


    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    public List<ProductConfiguration> getProductconfiguration() {
        return productconfiguration;
    }

    public void setProductconfiguration(List<ProductConfiguration> productconfiguration) {
        this.productconfiguration = productconfiguration;
    }


    public String getPreferred_delivery_date() {
        return preferred_delivery_date;
    }

    public void setPreferred_delivery_date(String preferred_delivery_date) {
        this.preferred_delivery_date = preferred_delivery_date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(String orderprice) {
        this.orderprice = orderprice;
    }
}