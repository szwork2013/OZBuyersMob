package com.gls.orderzapp.CreateOrder.OrderResponseBeans;

/**
 * Created by prajyot on 8/7/14.
 */
public class ProductConfiguration {
    String prod_configtype;
    String prod_configname;

    ProductConfigurationPrice prod_configprice;

    public String getProd_configtype() {
        return prod_configtype;
    }

    public void setProd_configtype(String prod_configtype) {
        this.prod_configtype = prod_configtype;
    }

    public String getProd_configname() {
        return prod_configname;
    }

    public void setProd_configname(String prod_configname) {
        this.prod_configname = prod_configname;
    }

    public ProductConfigurationPrice getProd_configprice() {
        return prod_configprice;
    }

    public void setProd_configprice(ProductConfigurationPrice prod_configprice) {
        this.prod_configprice = prod_configprice;
    }
}
