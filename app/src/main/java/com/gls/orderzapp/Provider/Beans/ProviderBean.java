package com.gls.orderzapp.Provider.Beans;

/**
 * Created by prajyot on 21/4/14.
 */
public class ProviderBean {
    String providerid;
    String providername;
    String providerlogo;
    PaymentMode paymentmode;

    public PaymentMode getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(PaymentMode paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getProviderlogo() {
        return providerlogo;
    }

    public void setProviderlogo(String providerlogo) {
        this.providerlogo = providerlogo;
    }
}
