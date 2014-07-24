package com.gls.orderzapp.AddressDetails.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.AddressDetails.Bean.ListOfPickupAddresses;
import com.gls.orderzapp.AddressDetails.Bean.StorePickUpAddress;
import com.gls.orderzapp.R;
import com.gls.orderzapp.SignUp.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 18/7/14.
 */
public class AdapterForPickUpAddressList extends BaseAdapter {
    Context context;
    List<Location> addresses = new ArrayList<>();
    public static Location pickupAddressFromList;
    public static Boolean isPickUpAddressSelected=false;
    String provider_id;
//    public static String pickuparea = null;
    GsonBuilder gBuild = new GsonBuilder();
    Gson gson = gBuild.disableHtmlEscaping().create();
    String branch_id;
    String tag;
    public AdapterForPickUpAddressList(Context context, List<Location> addresses,String provider_id, String branch_id, String tag) {
        this.context = context;
        this.addresses = addresses;
        this.provider_id=provider_id;
        isPickUpAddressSelected=false;
        this.branch_id = branch_id;
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        try {
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.selectaddress_list_item, null);
            }
            final TextView txt_select_address_list = (TextView) convertView.findViewById(R.id.txt_select_address_list);
            try {
                if (addresses.get(position) != null) {
                    txt_select_address_list.setText(addresses.get(position).getAddress1() + ", " +
                            addresses.get(position).getAddress2() + ", " +
                            addresses.get(position).getArea() + ", \n" +
                            addresses.get(position).getCity() + ". " +
                            addresses.get(position).getZipcode() + "\n" +
                            addresses.get(position).getState() + ", " +
                            addresses.get(position).getCountry() + ".");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        pickupAddressFromList = new Location();
//                        if (pickupAddressFromList.getArea() != null) {
//                            pickuparea = pickupAddressFromList.getArea();
//                        }
                        if (addresses.get(position).getArea() != null) {
                            pickupAddressFromList.setAddress1(addresses.get(position).getAddress1());
                            pickupAddressFromList.setAddress2(addresses.get(position).getAddress2());
                            pickupAddressFromList.setArea(addresses.get(position).getArea());
                            pickupAddressFromList.setCountry(addresses.get(position).getCountry());
                            pickupAddressFromList.setState(addresses.get(position).getState());
                            pickupAddressFromList.setZipcode(addresses.get(position).getZipcode());
                            pickupAddressFromList.setCity(addresses.get(position).getCity());
                       Log.d("You selected area", addresses.get(position).getArea());

                            StorePickUpAddress storePickUpAddress = new StorePickUpAddress();
                            storePickUpAddress.setBranchid(branch_id);
                            storePickUpAddress.setLocation(pickupAddressFromList);
                            isPickUpAddressSelected=true;


                            DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().add(storePickUpAddress);

                            Log.d("pick up addresss", new Gson().toJson(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress()));

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("pickupaddress",gson.toJson(pickupAddressFromList));
                            returnIntent.putExtra("providerid_forpickupaddress",provider_id);

                            ((Activity) context).setResult(((Activity) context).RESULT_OK, returnIntent);
                            ((Activity) context).finish();
                            DisplayDeliveryChargesAndType.displayAreaNameOnPickupButton(tag);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });




        }catch (Exception e) {
                e.printStackTrace();
            }
        return convertView;
    }
}
