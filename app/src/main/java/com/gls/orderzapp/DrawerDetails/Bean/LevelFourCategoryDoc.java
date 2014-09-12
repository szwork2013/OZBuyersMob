package com.gls.orderzapp.DrawerDetails.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 9/10/14.
 */
public class LevelFourCategoryDoc {
    String categoryid;
    String categoryname;
    List<LevelFourCategoryProvider> provider = new ArrayList<>();

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

    public List<LevelFourCategoryProvider> getProvider() {
        return provider;
    }

    public void setProvider(List<LevelFourCategoryProvider> provider) {
        this.provider = provider;
    }
}
