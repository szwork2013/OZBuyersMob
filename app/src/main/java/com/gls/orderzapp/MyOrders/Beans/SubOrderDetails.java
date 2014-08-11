package com.gls.orderzapp.MyOrders.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 5/5/14.
 */
public class SubOrderDetails {
    String status;
    String suborderid;
    String suborder_price;
    String deliverytype;
    String prefdeldtime;
    double deliverycharge;
    ProviderLocation delivery_address;
    ProviderLocation billing_address;
    ProviderLocation pickup_address;
    DeliveryTimeSlot prefdeltimeslot;
    ProductProviderDetails productprovider;
    List<ProductDetails> products = new ArrayList<>();
    List<ServerSideTracking> tracking = new ArrayList<>();

    public DeliveryTimeSlot getPrefdeltimeslot() {
        return prefdeltimeslot;
    }

    public void setPrefdeltimeslot(DeliveryTimeSlot prefdeltimeslot) {
        this.prefdeltimeslot = prefdeltimeslot;
    }

    public String getPrefdeldtime() {
        return prefdeldtime;
    }

    public void setPrefdeldtime(String prefdeldtime) {
        this.prefdeldtime = prefdeldtime;
    }

    public double getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(double deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public ProviderLocation getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(ProviderLocation pickup_address) {
        this.pickup_address = pickup_address;
    }

    public List<ServerSideTracking> getTracking() {
        return tracking;
    }

    public void setTracking(List<ServerSideTracking> tracking) {
        this.tracking = tracking;
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

    public String getSuborder_price() {
        return suborder_price;
    }

    public void setSuborder_price(String suborder_price) {
        this.suborder_price = suborder_price;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public ProviderLocation getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(ProviderLocation delivery_address) {
        this.delivery_address = delivery_address;
    }

    public ProviderLocation getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(ProviderLocation billing_address) {
        this.billing_address = billing_address;
    }

    public ProductProviderDetails getProductprovider() {
        return productprovider;
    }

    public void setProductprovider(ProductProviderDetails productprovider) {
        this.productprovider = productprovider;
    }

    public List<ProductDetails> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetails> products) {
        this.products = products;
    }
}
