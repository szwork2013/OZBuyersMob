package com.gls.orderzapp.MyOrders.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 5/5/14.
 */
public class OrderDetails {
    List<SubOrderDetails> suborder = new ArrayList<>();
    String orderid;
    String total_order_price;
    String createdate;
    String status;
    String order_placeddate;
    PaymentMethod payment;
    String preferred_delivery_date;

    public String getPreferred_delivery_date() {
        return preferred_delivery_date;
    }

    public void setPreferred_delivery_date(String preferred_delivery_date) {
        this.preferred_delivery_date = preferred_delivery_date;
    }

    public PaymentMethod getPayment() {
        return payment;
    }

    public void setPayment(PaymentMethod payment) {
        this.payment = payment;
    }

    public List<SubOrderDetails> getSuborder() {
        return suborder;
    }

    public void setSuborder(List<SubOrderDetails> suborder) {
        this.suborder = suborder;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTotal_order_price() {
        return total_order_price;
    }

    public void setTotal_order_price(String total_order_price) {
        this.total_order_price = total_order_price;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_placeddate() {
        return order_placeddate;
    }

    public void setOrder_placeddate(String order_placeddate) {
        this.order_placeddate = order_placeddate;
    }
}
