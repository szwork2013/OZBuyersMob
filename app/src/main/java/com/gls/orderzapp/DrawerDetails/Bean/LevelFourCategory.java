package com.gls.orderzapp.DrawerDetails.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 9/10/14.
 */
public class LevelFourCategory {
    String message;
    List<LevelFourCategoryDoc> doc = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LevelFourCategoryDoc> getDoc() {
        return doc;
    }

    public void setDoc(List<LevelFourCategoryDoc> doc) {
        this.doc = doc;
    }
}
