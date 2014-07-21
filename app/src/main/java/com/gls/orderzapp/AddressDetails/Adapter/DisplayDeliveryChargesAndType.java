package com.gls.orderzapp.AddressDetails.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SuccessResponseForDeliveryChargesAndType;
import com.gls.orderzapp.MainApp.SelectPickUpAddressActivity;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 15/7/14.
 */
public class DisplayDeliveryChargesAndType {
    public static Context context;
    public static String[] delivery_mode;
    public static String pickuparea=null;
    int i=0;
    public static String[] delivery_mode_branchid;
    public String[] providerid;
    public static SuccessResponseForDeliveryChargesAndType listOfDeliveryCharges;
    List<ProductDetails> checkForDeliveryModeList;

    public DisplayDeliveryChargesAndType(Context context, SuccessResponseForDeliveryChargesAndType listOfDeliveryCharges, List<ProductDetails> checkForDeliveryModeList) {
        this.context = context;
        this.listOfDeliveryCharges = listOfDeliveryCharges;
        this.checkForDeliveryModeList = checkForDeliveryModeList;
        getDeliveryChargesView();
        delivery_mode = new String[listOfDeliveryCharges.getSuccess().getDeliverycharge().size()];
        delivery_mode_branchid = new String[listOfDeliveryCharges.getSuccess().getDeliverycharge().size()];
        providerid = new String[listOfDeliveryCharges.getSuccess().getDeliverycharge().size()];

    }

    public void getDeliveryChargesView() {
        for (i=0; i < checkForDeliveryModeList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout lldelievry_charge_type_details = (LinearLayout) li.inflate(R.layout.ll_deliver_charge_type_list, null);

            TextView txt_sellername = (TextView) lldelievry_charge_type_details.findViewById(R.id.txt_sellername);
            TextView txt_delivery_charges = (TextView) lldelievry_charge_type_details.findViewById(R.id.txt_delivery_charges);
           final Button btn_selct_pickup_address=(Button) lldelievry_charge_type_details.findViewById(R.id.btn_selct_pickup_address);
           final RadioGroup delivery_type_group = (RadioGroup) lldelievry_charge_type_details.findViewById(R.id.delivery_type_group);
            final RadioButton pick_up = (RadioButton) lldelievry_charge_type_details.findViewById(R.id.pick_up);
            final RadioButton home_delivery = (RadioButton) lldelievry_charge_type_details.findViewById(R.id.home_delivery);


            if (checkForDeliveryModeList.get(i).getProviderName() != null) {
                txt_sellername.setText(checkForDeliveryModeList.get(i).getProviderName());
                Log.d("provider id",checkForDeliveryModeList.get(i).getProviderid());
//                providerid[i] = checkForDeliveryModeList.get(i).getProviderid();
            }

            for (int j = 0; j < listOfDeliveryCharges.getSuccess().getDeliverycharge().size(); j++) {
                if (listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid().equals(checkForDeliveryModeList.get(i).getBranchid())) {
                    if (listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery() == true) {
                        home_delivery.setVisibility(View.VISIBLE);
                        txt_delivery_charges.setText(String.format("%.2f", listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge()));
                    } else {
                        txt_delivery_charges.setText("0.0");
                        home_delivery.setVisibility(View.GONE);
                    }
                }
            }

            delivery_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.pick_up:
                            Log.d("Pickup pos", group.getId() - 100 + "");
                            delivery_mode[group.getId() - 100] = "pickup";
                            btn_selct_pickup_address.setVisibility(View.VISIBLE);
                            delivery_mode_branchid[group.getId() - 100] = listOfDeliveryCharges.getSuccess().getDeliverycharge().get(group.getId() - 100).getBranchid();
                            break;

                        case R.id.home_delivery:
                            delivery_mode[group.getId() - 100] = "home";
                            btn_selct_pickup_address.setVisibility(View.GONE);
                            delivery_mode_branchid[group.getId() - 100] = listOfDeliveryCharges.getSuccess().getDeliverycharge().get(group.getId() - 100).getBranchid();
                            Log.d("hom pos", group.getId() - 100 + "");
                            break;
                    }
                }
            });
            btn_selct_pickup_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToSelectPickUpAddressActivity = new Intent(context, SelectPickUpAddressActivity.class);
//                    goToSelectPickUpAddressActivity.putExtra("providerid",providerid[btn_selct_pickup_address.getId()-100]);
                    ((Activity) context).startActivityForResult(goToSelectPickUpAddressActivity,1);
                }
            });
            btn_selct_pickup_address.setId(100+i);
            delivery_type_group.setId(100 + i);
            DeliveryChargesAndTypeAdapter.llDeliveryChargeAndType.addView(lldelievry_charge_type_details);
        }
    }

    public static boolean deliveryTypeCheck() {
        for (int l = 0; l < listOfDeliveryCharges.getSuccess().getDeliverycharge().size(); l++)
            if (delivery_mode[l] != null) {
                if (delivery_mode[l].isEmpty()) {
                    return false;
                }
            } else {
                return false;
            }
        return true;
    }
}

