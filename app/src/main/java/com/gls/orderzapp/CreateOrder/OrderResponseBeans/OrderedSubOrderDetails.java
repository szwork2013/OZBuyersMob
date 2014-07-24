package com.gls.orderzapp.CreateOrder.OrderResponseBeans;

import java.util.List;

/**
 * Created by prajyot on 6/5/14.
 */
public class OrderedSubOrderDetails {
    String status;
    String suborderid;
    OrderedProductProvider productprovider;
    List<OrderedProductDetails> products;
    Address billing_address;
    Address delivery_address;
    Address pickup_address;
    double suborder_price;
    double deliverycharge;
    String deliverytype;


    public Address getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(Address pickup_address) {
        this.pickup_address = pickup_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuborderid() {
        return suborderid;
    }

    public void setSuborderid(String suborderid) {
        this.suborderid = suborderid;
    }

    public OrderedProductProvider getProductprovider() {
        return productprovider;
    }

    public void setProductprovider(OrderedProductProvider productprovider) {
        this.productprovider = productprovider;
    }

    public List<OrderedProductDetails> getProducts() {
        return products;
    }

    public void setProducts(List<OrderedProductDetails> products) {
        this.products = products;
    }

    public Address getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(Address billing_address) {
        this.billing_address = billing_address;
    }

    public Address getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(Address delivery_address) {
        this.delivery_address = delivery_address;
    }

    public double getSuborder_price() {
        return suborder_price;
    }

    public void setSuborder_price(double suborder_price) {
        this.suborder_price = suborder_price;
    }

    public double getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(double deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }
}
