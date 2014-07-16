package com.gls.orderzapp.CreateOrder.OrderResponseBeans;

/**
 * Created by prajyot on 6/5/14.
 */
public class Order {
    String message;
    OrderDetails order;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }
}
