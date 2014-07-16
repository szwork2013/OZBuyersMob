package com.gls.orderzapp.Provider.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 3/7/14.
 */
public class ProductConfiguration {
    String categoryid;
    String categoryname;
    List<ProductConfigurationDetails> configuration = new ArrayList<>();

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<ProductConfigurationDetails> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<ProductConfigurationDetails> configuration) {
        this.configuration = configuration;
    }
}
