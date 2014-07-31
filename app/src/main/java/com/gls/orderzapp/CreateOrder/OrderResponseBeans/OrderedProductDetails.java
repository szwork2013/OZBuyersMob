package com.gls.orderzapp.CreateOrder.OrderResponseBeans;





import java.util.ArrayList;
import java.util.List;


/**
 * Created by prajyot on 6/5/14.
 */
public class OrderedProductDetails {
    String productid;
    String productname;
    String productcode;
    String productlogo;
    List<ProductConfiguration> productconfiguration = new ArrayList<>();
    double qty;
    double orderprice;
    String uom;

    public List<ProductConfiguration> getProductconfiguration() {
        return productconfiguration;
    }

    public void setProductconfiguration(List<ProductConfiguration> productconfiguration) {
        this.productconfiguration = productconfiguration;
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

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProductlogo() {
        return productlogo;
    }

    public void setProductlogo(String productlogo) {
        this.productlogo = productlogo;
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
}
