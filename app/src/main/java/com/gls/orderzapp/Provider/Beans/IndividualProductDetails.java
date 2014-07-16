package com.gls.orderzapp.Provider.Beans;

/**
 * Created by prajyot on 15/4/14.
 */
public class IndividualProductDetails {
    String message;
    ProductDetails proudctcatalog;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductDetails getProudctcatalog() {
        return proudctcatalog;
    }

    public void setProudctcatalog(ProductDetails proudctcatalog) {
        this.proudctcatalog = proudctcatalog;
    }
}
