package com.gls.orderzapp.Provider.Beans;

/**
 * Created by prajyot on 14/4/14.
 */
public class ProductLogo {
    String bucket;
    String key;
    String image;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
