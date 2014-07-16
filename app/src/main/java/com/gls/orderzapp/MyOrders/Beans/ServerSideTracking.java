package com.gls.orderzapp.MyOrders.Beans;

/**
 * Created by prajyot on 11/6/14.
 */
public class ServerSideTracking {
    String status;
    String datetime;
    String updatedby;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }
}
