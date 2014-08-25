package com.gls.orderzapp.AddressDetails.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.AddressDetails.Bean.ListOfDeliveryAddress;
import com.gls.orderzapp.MainApp.DeliveryPaymentActivity;
import com.gls.orderzapp.MainApp.SelectAddressListActivity;
import com.gls.orderzapp.R;
import com.gls.orderzapp.SignUp.Location;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 2/7/14.
 */
public class AdapterForSelectaddressList extends BaseAdapter {
    public static ListOfDeliveryAddress deliveryAddressList;
    public static String deliveryaddressid = null;
    public SuccessResponseOfUser successResponseOfUserDeliveryAddresDetails;
    Context context;
    List<ListOfDeliveryAddress> deliveryaddresses = new ArrayList<>();

    public AdapterForSelectaddressList(Context context, List<ListOfDeliveryAddress> deliveryaddresses) {
        this.context = context;
        this.deliveryaddresses = deliveryaddresses;
    }

    @Override
    public int getCount() {
        return deliveryaddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return deliveryaddresses.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.selectaddress_list_item, null);
            }
            final TextView txt_select_address_list = (TextView) convertView.findViewById(R.id.txt_select_address_list);
            try {
                if (deliveryaddresses.get(position).getAddress() != null) {
                    txt_select_address_list.setText(deliveryaddresses.get(position).getAddress().getAddress1() + ", " +
                            deliveryaddresses.get(position).getAddress().getAddress2() + ", " +
                            deliveryaddresses.get(position).getAddress().getArea() + ", \n" +
                            deliveryaddresses.get(position).getAddress().getCity() + ". " +
                            deliveryaddresses.get(position).getAddress().getZipcode() + "\n" +
                            deliveryaddresses.get(position).getAddress().getState() + ", " +
                            deliveryaddresses.get(position).getAddress().getCountry() + ".");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        deliveryAddressList = new ListOfDeliveryAddress();
                        if (deliveryaddresses.get(position).getDeliveryaddressid() != null) {
                            deliveryaddressid = deliveryaddresses.get(position).getDeliveryaddressid();
                            Log.d("deliveryaddressid", deliveryaddressid);
                        }
                        Location location = new Location();
                        if (deliveryaddresses.get(position).getAddress() != null) {
                            location.setAddress1(deliveryaddresses.get(position).getAddress().getAddress1());
                            location.setAddress2(deliveryaddresses.get(position).getAddress().getAddress2());
                            location.setArea(deliveryaddresses.get(position).getAddress().getArea());
                            location.setCountry(deliveryaddresses.get(position).getAddress().getCountry());
                            location.setState(deliveryaddresses.get(position).getAddress().getState());
                            location.setZipcode(deliveryaddresses.get(position).getAddress().getZipcode());
                            location.setCity(deliveryaddresses.get(position).getAddress().getCity());
                            deliveryAddressList.setAddress(location);
                        }
                        if (deliveryAddressList.getAddress() != null) {
                            DeliveryPaymentActivity.shipping_address_textview.setText(deliveryAddressList.getAddress().getAddress1() + ", " +
                                    deliveryAddressList.getAddress().getAddress2() + ", " +
                                    deliveryAddressList.getAddress().getArea() + ", \n" +
                                    deliveryAddressList.getAddress().getCity() + ". " +
                                    deliveryAddressList.getAddress().getZipcode() + "\n" +
                                    deliveryAddressList.getAddress().getState() + ", " +
                                    deliveryAddressList.getAddress().getCountry() + ".");
                            SelectAddressListActivity.isAddNewaddress = true;
                            loadPreferencesUserDataForDeliveryAddress();
                            ((Activity) context).setResult(((Activity) context).RESULT_OK);
                            ((Activity) context).finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public String loadPreferencesUserDataForDeliveryAddress() throws Exception {
        String user = "";
        String DeliveryAddresDetails = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
            user = spLoad.getString("USER_DATA_DELIVERY_ADDRESS", null);
            successResponseOfUserDeliveryAddresDetails = new Gson().fromJson(user, SuccessResponseOfUser.class);
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setArea(deliveryAddressList.getAddress().getArea());
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setCity(deliveryAddressList.getAddress().getCity());
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setAddress1(deliveryAddressList.getAddress().getAddress1());
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setAddress2(deliveryAddressList.getAddress().getAddress2());
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setCountry(deliveryAddressList.getAddress().getCountry());
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setState(deliveryAddressList.getAddress().getState());
            successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().setZipcode(deliveryAddressList.getAddress().getZipcode());

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = sp.edit();
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            DeliveryAddresDetails = gson.toJson(successResponseOfUserDeliveryAddresDetails);

            edit.putString("USER_DATA_DELIVERY_ADDRESS", DeliveryAddresDetails);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
