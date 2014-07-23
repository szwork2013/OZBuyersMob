package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import com.gls.orderzapp.SignUp.Location;

/**
 * Created by prajyot on 22/7/14.
 */
public class SellerDelivery {
    String branchid;
    String deliverytype;
    String prefdeldtime;
    DeliveryChargeDetails deliverycharge;
    String orderinstructions;
    Location pickup_address;
    CreateOrderAddressDetails delivery_address;

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public String getPrefdeldtime() {
        return prefdeldtime;
    }

    public void setPrefdeldtime(String prefdeldtime) {
        this.prefdeldtime = prefdeldtime;
    }

    public DeliveryChargeDetails getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(DeliveryChargeDetails deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public String getOrderinstructions() {
        return orderinstructions;
    }

    public void setOrderinstructions(String orderinstructions) {
        this.orderinstructions = orderinstructions;
    }

    public Location getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(Location pickup_address) {
        this.pickup_address = pickup_address;
    }

    public CreateOrderAddressDetails getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(CreateOrderAddressDetails delivery_address) {
        this.delivery_address = delivery_address;
    }
}
