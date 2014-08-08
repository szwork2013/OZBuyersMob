package com.gls.orderzapp.CreateOrder.CreateOrderBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 24/4/14.
 */
public class CreateOrderCartList {
    List<CreateOrderProductDetails> cart = new ArrayList<>();
    List<DeliveryChargeDetails> deliverycharges = new ArrayList<>();
    List<DeliveryTypes> deliverytypes = new ArrayList<>();
    String totalorderprice;
    CreateOrderAddressDetails billing_address;
    CreateOrderAddressDetails delivery_address;
    String deliverytype;
    String preferred_delivery_date;
    List<String> orderinstructions = new ArrayList<>();
    String paymentmode;
    List<SellerDelivery> sellerdelivery = new ArrayList<>();

    public List<SellerDelivery> getSellerdelivery() {
        return sellerdelivery;
    }

    public void setSellerdelivery(List<SellerDelivery> sellerdelivery) {
        this.sellerdelivery = sellerdelivery;
    }

    public List<DeliveryTypes> getDeliverytypes() {
        return deliverytypes;
    }

    public void setDeliverytypes(List<DeliveryTypes> deliverytypes) {
        this.deliverytypes = deliverytypes;
    }

    public List<DeliveryChargeDetails> getDeliverycharges() {
        return deliverycharges;
    }

    public void setDeliverycharges(List<DeliveryChargeDetails> deliverycharges) {
        this.deliverycharges = deliverycharges;
    }

    public List<String> getOrderinstructions() {
        return orderinstructions;
    }

    public void setOrderinstructions(List<String> orderinstructions) {
        this.orderinstructions = orderinstructions;
    }


    public String getPreferred_delivery_date() {
        return preferred_delivery_date;
    }

    public void setPreferred_delivery_date(String preferred_delivery_date) {
        this.preferred_delivery_date = preferred_delivery_date;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public List<CreateOrderProductDetails> getCart() {
        return cart;
    }

    public void setCart(List<CreateOrderProductDetails> cart) {
        this.cart = cart;
    }

    public String getTotalorderprice() {
        return totalorderprice;
    }

    public void setTotalorderprice(String totalorderprice) {
        this.totalorderprice = totalorderprice;
    }

    public CreateOrderAddressDetails getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(CreateOrderAddressDetails billing_address) {
        this.billing_address = billing_address;
    }

    public CreateOrderAddressDetails getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(CreateOrderAddressDetails delivery_address) {
        this.delivery_address = delivery_address;
    }
}
