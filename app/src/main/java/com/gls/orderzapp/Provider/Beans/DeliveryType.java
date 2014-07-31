package com.gls.orderzapp.Provider.Beans;

/**
 * Created by prajyot on 14/7/14.
 */
public class DeliveryType {
    String branchId;
    String deliveryType;
    String orderinstructions;

    public String getOrderinstructions() {
        return orderinstructions;
    }

    public void setOrderinstructions(String orderinstructions) {
        this.orderinstructions = orderinstructions;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
