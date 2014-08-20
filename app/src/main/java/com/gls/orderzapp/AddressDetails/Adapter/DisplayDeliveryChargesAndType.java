package com.gls.orderzapp.AddressDetails.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Bean.DeliveryTypeBean;
import com.gls.orderzapp.AddressDetails.Bean.ListOfPickupAddresses;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SuccessResponseForDeliveryChargesAndType;
import com.gls.orderzapp.MainApp.SelectPickUpAddressActivity;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 15/7/14.
 */
public class DisplayDeliveryChargesAndType {
    public static Context context;
    //    public static String[] delivery_mode;
    public static String[] order_instruction;
    //    public static String[] delivery_mode_branchid;
    public static List<String> deliveryType = new ArrayList<>();
    public static List<DeliveryTypeBean> deliverytypebean = new ArrayList<>();
    public static SuccessResponseForDeliveryChargesAndType listOfDeliveryCharges;
    public static ListOfPickupAddresses listOfPickupAddresses = new ListOfPickupAddresses();
    public static List<Button> listPickUpButtons = new ArrayList<>();
    public String[] providerid;
    int i = 0;
    public static List<ProductDetails> checkForDeliveryModeList;
    EditText tempEditText = null;
    String tag = "";

    public DisplayDeliveryChargesAndType(Context context, SuccessResponseForDeliveryChargesAndType listOfDeliveryCharges, List<ProductDetails> checkForDeliveryModeList) {
        this.context = context;
        this.listOfDeliveryCharges = listOfDeliveryCharges;
        this.checkForDeliveryModeList = checkForDeliveryModeList;
        order_instruction = new String[listOfDeliveryCharges.getSuccess().getDeliverycharge().size()];
        providerid = new String[listOfDeliveryCharges.getSuccess().getDeliverycharge().size()];
        providerid = new String[listOfDeliveryCharges.getSuccess().getDeliverycharge().size()];
        getDeliveryChargesView();
    }

    public static boolean deliveryTypeCheck() {
        boolean deliverycheck = false;
        if(checkForDeliveryModeList.size() == deliveryType.size()) {
            for (int l = 0; l < deliveryType.size(); l++) {
                Log.d("delivery ttype", new Gson().toJson(deliveryType));
                if (deliveryType.get(l) != null) {
                    if (deliverytypebean.get(l).getDeliveryType().equalsIgnoreCase("pickup")) {
                        Log.d("pickuppppppppppppppppppp", new Gson().toJson(deliveryType.get(l)));

                            if (deliverytypebean.get(l).getPickUpArea() == null) {
                                deliverycheck = false;
                                Toast.makeText(context, "Please select your pickup address", Toast.LENGTH_LONG).show();
                                break;
                            } else {
                                deliverycheck = true;
                            }

                    } else if (deliverytypebean.get(l).getDeliveryType().equalsIgnoreCase("home")) {
                        Log.d("homeeeeeeeeeeeeeeeeeeeeeeee", new Gson().toJson(deliveryType.get(l)));
                        deliverycheck = true;
                    }
                } else {
                    Toast.makeText(context, "Please select your delivery type", Toast.LENGTH_LONG).show();
                    deliverycheck = false;
                    break;
                }

            }
        } else {
            Toast.makeText(context, "Please select your delivery type", Toast.LENGTH_LONG).show();
            Log.d("3","3");
            deliverycheck = false;
        }
        return deliverycheck;
    }

    public static void displayAreaNameOnPickupButton(String tag) {
        for (int i = 0; i < listPickUpButtons.size(); i++) {
            if (tag.equals(listPickUpButtons.get(i).getTag() + "")) {
                listPickUpButtons.get(i).setText(AdapterForPickUpAddressList.pickupAddressFromList.getArea());
            }
        }

        for(int i = 0; i < deliverytypebean.size(); i++){
            if(tag.equals(deliverytypebean.get(i).getBranchid())){
                deliverytypebean.get(i).setPickUpArea(AdapterForPickUpAddressList.pickupAddressFromList.getArea());
            }
        }
    }

    public void getDeliveryChargesView() {
        for (i = 0; i < checkForDeliveryModeList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout lldelievry_charge_type_details = (LinearLayout) li.inflate(R.layout.ll_deliver_charge_type_list, null);

            TextView txt_sellername = (TextView) lldelievry_charge_type_details.findViewById(R.id.txt_sellername);
            TextView txt_delivery_charges = (TextView) lldelievry_charge_type_details.findViewById(R.id.txt_delivery_charges);
            final EditText edt_orderInstruction = (EditText) lldelievry_charge_type_details.findViewById(R.id.edt_orderInstruction);
            final Button btn_selct_pickup_address = (Button) lldelievry_charge_type_details.findViewById(R.id.btn_selct_pickup_address);
            final LinearLayout ll_delivery_charges = (LinearLayout) lldelievry_charge_type_details.findViewById(R.id.ll_delivery_charges);
            final RadioGroup delivery_type_group = (RadioGroup) lldelievry_charge_type_details.findViewById(R.id.delivery_type_group);
            final RadioButton pick_up = (RadioButton) lldelievry_charge_type_details.findViewById(R.id.pick_up);
            final RadioButton home_delivery = (RadioButton) lldelievry_charge_type_details.findViewById(R.id.home_delivery);

            listPickUpButtons.add(btn_selct_pickup_address);

            if (checkForDeliveryModeList.get(i).getProviderName() != null) {
                txt_sellername.setText(checkForDeliveryModeList.get(i).getProviderName());
                providerid[i] = checkForDeliveryModeList.get(i).getProviderid();
            }

            for (int j = 0; j < listOfDeliveryCharges.getSuccess().getDeliverycharge().size(); j++) {
                if (listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid().equals(checkForDeliveryModeList.get(i).getBranchid())) {
                    if (listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery() == true) {
                        home_delivery.setVisibility(View.VISIBLE);
                        if(listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).isIsdeliverychargeinpercent() == true){
                            txt_delivery_charges.setText(String.format("%.2f", (Cart.deliveryCharges(listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid()) * listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge())/100));
                        }else {
                            txt_delivery_charges.setText(String.format("%.2f", listOfDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge()));
                        }
                    } else {
                        txt_delivery_charges.setText("0.0");
                        home_delivery.setVisibility(View.GONE);
                        ll_delivery_charges.setVisibility(View.GONE);
                    }
                }
            }

            delivery_type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.pick_up:
                            Cart.saveDeliveryTypeInfoInCart(checkForDeliveryModeList.get(group.getId() - 200).getBranchid(), "pickup");
                            btn_selct_pickup_address.setVisibility(View.VISIBLE);
                            ll_delivery_charges.setVisibility(View.INVISIBLE);
                            for (int k = 0; k < deliveryType.size(); k++) {
                                if (deliveryType.get(k).split("_")[0].equals(checkForDeliveryModeList.get(group.getId() - 200).getBranchid())) {
                                    deliveryType.remove(k);
                                }
                            }
                            DeliveryTypeBean deliveryTypeBean = new DeliveryTypeBean();
                            deliveryTypeBean.setBranchid(checkForDeliveryModeList.get(group.getId() - 200).getBranchid());
                            deliveryTypeBean.setDeliveryType("pickup");
                            deliverytypebean.add(deliveryTypeBean);
                            deliveryType.add(checkForDeliveryModeList.get(group.getId() - 200).getBranchid() + "_" + "pickup");
                            break;

                        case R.id.home_delivery:
                            ll_delivery_charges.setVisibility(View.VISIBLE);
                            Cart.saveDeliveryTypeInfoInCart(checkForDeliveryModeList.get(group.getId() - 200).getBranchid(), "home");
                            btn_selct_pickup_address.setVisibility(View.INVISIBLE);
                            for (int k = 0; k < deliveryType.size(); k++) {
                                if (deliveryType.get(k).split("_")[0].equals(checkForDeliveryModeList.get(group.getId() - 200).getBranchid())) {
                                    deliveryType.remove(k);
                                }
                            }
                            DeliveryTypeBean deliveryTypeBeanDelivery = new DeliveryTypeBean();
                            deliveryTypeBeanDelivery.setBranchid(checkForDeliveryModeList.get(group.getId() - 200).getBranchid());
                            deliveryTypeBeanDelivery.setDeliveryType("home");
                            deliverytypebean.add(deliveryTypeBeanDelivery);
                            deliveryType.add(checkForDeliveryModeList.get(group.getId() - 200).getBranchid() + "_" + "home");
                            break;
                    }
                }
            });

            btn_selct_pickup_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToSelectPickUpAddressActivity = new Intent(context, SelectPickUpAddressActivity.class);
                    goToSelectPickUpAddressActivity.putExtra("providerid", providerid[btn_selct_pickup_address.getId() - 100]);
                    goToSelectPickUpAddressActivity.putExtra("branchid", checkForDeliveryModeList.get(view.getId() - 100).getBranchid());
                    goToSelectPickUpAddressActivity.putExtra("pickupbuttontag", view.getTag() + "");
                    ((Activity) context).startActivityForResult(goToSelectPickUpAddressActivity, 2);
                }
            });

            final TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Cart.saveOrderInstructions(tag, tempEditText.getText().toString().trim());
//                    order_instruction[edt_orderInstruction.getId()-100]=tempEditText.getText().toString().trim();
//                    Cart.addMessageOnCake(mKeys[position], cakeList.get(position), tempEditText.getText().toString().trim());
                }
            };

            edt_orderInstruction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (tempEditText != null) {
                        tempEditText.removeTextChangedListener(textWatcher);
                    }
                    tag = view.getTag() + "";
                    tempEditText = ((EditText) view);

                    tempEditText.addTextChangedListener(textWatcher);
                }
            });

            edt_orderInstruction.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (tempEditText != null) {
                        tempEditText.removeTextChangedListener(textWatcher);
                    }
                    tag = view.getTag() + "";
                    tempEditText = ((EditText) view);

                    tempEditText.addTextChangedListener(textWatcher);
                    return false;
                }
            });


            //*****************************
            btn_selct_pickup_address.setTag(checkForDeliveryModeList.get(i).getBranchid());
//            edt_orderInstruction.setId(100+i);
            edt_orderInstruction.setTag(checkForDeliveryModeList.get(i).getBranchid());
            btn_selct_pickup_address.setId(100 + i);
            delivery_type_group.setId(200 + i);
            DeliveryChargesAndTypeAdapter.llDeliveryChargeAndType.addView(lldelievry_charge_type_details);
        }
    }
}

