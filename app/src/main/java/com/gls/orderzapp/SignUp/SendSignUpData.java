package com.gls.orderzapp.SignUp;

/**
 * Created by prajyot on 1/4/14.
 */
public class SendSignUpData {

    String mobileno;
    String password;
    String username;
    String email;
    String usertype;
    String firstname;
    Location location = new Location();
    String gcmregistrationid;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getGcmregistrationid() {
        return gcmregistrationid;
    }

    public void setGcmregistrationid(String gcmregistrationid) {
        this.gcmregistrationid = gcmregistrationid;
    }


    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
