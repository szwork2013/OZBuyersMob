package com.gls.orderzapp.MyOrders.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 5/5/14.
 */
public class ProductDetails {
    String productid;
    String productname;
    double qty;
    double baseprice;
    double orderprice;
    String productlogo;
    String uom;
    List<ProductConfiguration> productconfiguration = new ArrayList<>();

    public double getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(double baseprice) {
        this.baseprice = baseprice;
    }

    public List<ProductConfiguration> getProductconfiguration() {
        return productconfiguration;
    }

    public void setProductconfiguration(List<ProductConfiguration> productconfiguration) {
        this.productconfiguration = productconfiguration;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(double orderprice) {
        this.orderprice = orderprice;
    }

    public String getProductlogo() {
        return productlogo;
    }

    public void setProductlogo(String productlogo) {
        this.productlogo = productlogo;
    }
}
