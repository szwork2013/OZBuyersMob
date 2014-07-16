package com.gls.orderzapp.Provider.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 4/4/14.
 */
public class ProviderDetails {
    List<ProductDetails> productcatalog = new ArrayList<>();
    BranchInfo branch;
    ProviderBean provider;
    boolean loadmoreproduct;

    public boolean isLoadmoreproduct() {
        return loadmoreproduct;
    }

    public void setLoadmoreproduct(boolean loadmoreproduct) {
        this.loadmoreproduct = loadmoreproduct;
    }

    public ProviderBean getProvider() {
        return provider;
    }

    public void setProvider(ProviderBean provider) {
        this.provider = provider;
    }

    public List<ProductDetails> getProducts() {
        return productcatalog;
    }

    public void setProducts(List<ProductDetails> products) {
        this.productcatalog = products;
    }

    public BranchInfo getBranch() {
        return branch;
    }

    public void setBranch(BranchInfo branch) {
        this.branch = branch;
    }

}
