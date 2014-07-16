package com.gls.orderzapp.SignIn;

/**
 * Created by prajyot on 23/4/14.
 */
public class ForgotPasswordPostData {
    String mobileno;
    String otp;

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
