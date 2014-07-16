package com.gls.orderzapp.Provider.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 4/4/14.
 */
public class ProviderList {
    String message;
    List<ProviderDetails> provider = new ArrayList<>();
    boolean more;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProviderDetails> getProvider() {
        return provider;
    }

    public void setProvider(List<ProviderDetails> provider) {
        this.provider = provider;
    }
}
