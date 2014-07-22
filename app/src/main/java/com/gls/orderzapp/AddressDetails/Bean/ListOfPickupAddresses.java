package com.gls.orderzapp.AddressDetails.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 22/7/14.
 */
public class ListOfPickupAddresses {
    List<StorePickUpAddress> listPickUpAddress = new ArrayList<>();

    public List<StorePickUpAddress> getListPickUpAddress() {
        return listPickUpAddress;
    }

    public void setListPickUpAddress(List<StorePickUpAddress> listPickUpAddress) {
        this.listPickUpAddress = listPickUpAddress;
    }
}
