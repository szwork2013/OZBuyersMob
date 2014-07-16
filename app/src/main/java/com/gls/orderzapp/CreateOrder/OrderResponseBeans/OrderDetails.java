package com.gls.orderzapp.CreateOrder.OrderResponseBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 6/5/14.
 */
public class OrderDetails {
    List<OrderedSubOrderDetails> suborder = new ArrayList<>();
    double total_order_price;
    Consumer consumer;
    String orderid;
    String createdate;
    PaymentMethod payment;
    String status;
    String preferred_delivery_date;

    public PaymentMethod getPayment() {
        return payment;
    }

    public void setPayment(PaymentMethod payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getPreferred_delivery_date() {
        return preferred_delivery_date;
    }

    public void setPreferred_delivery_date(String preferred_delivery_date) {
        this.preferred_delivery_date = preferred_delivery_date;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public List<OrderedSubOrderDetails> getSuborder() {
        return suborder;
    }

    public void setSuborder(List<OrderedSubOrderDetails> suborder) {
        this.suborder = suborder;
    }

    public double getTotal_order_price() {
        return total_order_price;
    }

    public void setTotal_order_price(double total_order_price) {
        this.total_order_price = total_order_price;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}