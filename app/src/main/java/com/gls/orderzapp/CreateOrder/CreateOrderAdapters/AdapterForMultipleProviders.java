package com.gls.orderzapp.CreateOrder.CreateOrderAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.AddressDetails.Adapter.DeliveryChargesAndTypeAdapter;
import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderAddressDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderProductDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.DeliveryChargeDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.DeliveryTypes;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SellerDelivery;
import com.gls.orderzapp.MainApp.OrderDetailsActivity;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 30/4/14.
 */
public class AdapterForMultipleProviders {
    Context context;

    public static LinearLayout ll;
    public  DeliveryTypes adddeliveryTypes=new DeliveryTypes();
    List<CreateOrderProductDetails> createOrderProductDetailsList;
    List<CreateOrderProductDetails> list = new ArrayList<>();
    List<String> branchIds = new ArrayList<>();
    LayoutInflater li;
    String pickupaddress="";
    TextView textGrandTotal, delivery_charge, deliveryTypeText,deliveryDateText,deliveryTimeText,deliveryAddressText;
    CreateOrderAddressDetails orderDeliveryAddress;
    String[] keys;

    public AdapterForMultipleProviders(Context context, List<CreateOrderProductDetails> createOrderProductDetailsList, CreateOrderAddressDetails orderDeliveryAddress) {
        this.context = context;
        this.createOrderProductDetailsList = createOrderProductDetailsList;
        this.orderDeliveryAddress = orderDeliveryAddress;

        keys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);

    }

    public void setMultipleProvidersList() {
        ArrayList<String> providers = new ArrayList<String>();
        double deliveryCharges = 0.0;

        for (int i = 0; i < createOrderProductDetailsList.size(); i++) {

            String branchid = createOrderProductDetailsList.get(i).getBranchid();
            String providerName = createOrderProductDetailsList.get(i).getProvidername();
            String providerArea = createOrderProductDetailsList.get(i).getLocation().getArea();
            String deliveryType = "",deliveryDate="",deliveryTime="";
            int getFromHrs=0,getToHrs=0;
            String getFromMin="",getToMin="";

            if (providers != null) {
                if (providers.contains(branchid)) {

                    list.add(createOrderProductDetailsList.get(i));
                } else {

                    if (i > 0) {
                        Log.d("delivery charge1", deliveryCharges + "");

                        new ConfirmOrderProductListAdapter(context, list).setProductList();

                    }

                    for(int j = 0; j < Cart.hm.size(); j++){
                        if(branchIds.contains(Cart.hm.get(keys[j]).getBranchid())){

                        }else {
//                            branchIds.add(Cart.hm.get(keys[j]).getBranchid());
                            if (branchid.equals(Cart.hm.get(keys[j]).getBranchid())) {

                                if (Cart.hm.get(keys[j]).getDeliveryType().getDeliveryType().equalsIgnoreCase("home")) {
                                    deliveryType = "Home Delivery";
                                } else {
                                    deliveryType = "Pick-Up";
                                }
                                Log.d("del type", deliveryType);
                                deliveryDate=Cart.hm.get(keys[j]).getPrefereddeliverydate().toString();
                                Log.d("del datentime",deliveryDate);
                                //*******************


                                if(Cart.hm.get(keys[j]).getTimeslot()!=null){
                                    DecimalFormat formatter = new DecimalFormat("00");
                                    getFromHrs=(int)Cart.hm.get(keys[j]).getTimeslot().getFrom();
                                    getFromMin=formatter.format(Math.round(((Cart.hm.get(keys[j]).getTimeslot().getFrom()-getFromHrs)*60)*100)/100);
                                    getToHrs=(int)Cart.hm.get(keys[j]).getTimeslot().getTo();
                                    getToMin=formatter.format(Math.round(((Cart.hm.get(keys[j]).getTimeslot().getTo()-getToHrs)*60)*100)/100);

                                    if(getFromHrs<12)
                                    {
                                        deliveryTime=getFromHrs+":"+getFromMin+" AM";
                                    }else if(getFromHrs==12)
                                    {
                                        deliveryTime=getFromHrs+":"+getFromMin+" PM";
                                    }
                                    else if(getFromHrs>12)
                                    {
                                        deliveryTime=(getFromHrs-12)+":"+getFromMin+" PM";
                                    }

                                    if(getToHrs<12)
                                    {
                                        deliveryTime=deliveryTime.concat(" to "+getToHrs+":"+getToMin+" AM");
                                    }else if(getToHrs==12)
                                    {
                                        deliveryTime=deliveryTime.concat(" to "+getToHrs+":"+getToMin+" PM");
                                    }
                                    else if(getToHrs>12)
                                    {
                                        deliveryTime=deliveryTime.concat(" to "+(getToHrs-12)+":"+getToMin+" PM");
                                    }
//
//                                    if(Cart.hm.get(keys[j]).getTimeslot().getFrom()<12)
//                                        {
//                                            deliveryTime=String.format("%.2f",Cart.hm.get(keys[j]).getTimeslot().getFrom())+" AM";
//                                        }else if(Cart.hm.get(keys[j]).getTimeslot().getFrom()==12)
//                                        {
//                                            deliveryTime=String.format("%.2f",Cart.hm.get(keys[j]).getTimeslot().getFrom())+" PM";
//                                        }
//                                        else if(Cart.hm.get(keys[j]).getTimeslot().getFrom()>12)
//                                        {
//                                            deliveryTime=String.format("%.2f",(Cart.hm.get(keys[j]).getTimeslot().getFrom()-12))+" PM";
//                                        }
//
//                                        if(Cart.hm.get(keys[j]).getTimeslot().getTo()<12)
//                                        {
//                                            deliveryTime=deliveryTime.concat(" to "+String.format("%.2f",Cart.hm.get(keys[j]).getTimeslot().getTo())+" AM");
//                                        }else if(Cart.hm.get(keys[j]).getTimeslot().getTo()==12)
//                                        {
//                                            deliveryTime=deliveryTime.concat(" to "+String.format("%.2f",Cart.hm.get(keys[j]).getTimeslot().getTo())+" PM");
//                                        }
//                                        else if(Cart.hm.get(keys[j]).getTimeslot().getTo()>12)
//                                        {
//                                            deliveryTime=deliveryTime.concat(" to "+String.format("%.2f",(Cart.hm.get(keys[j]).getTimeslot().getTo()-12))+" PM");
//                                        }

                                }
                                //********************
                                SellerDelivery sellerDelivery = new SellerDelivery();
                                sellerDelivery.setBranchid(branchid);
                                sellerDelivery.setPrefdeldtime(deliveryDate);
                                sellerDelivery.setPrefdeltimeslot(Cart.hm.get(keys[j]).getTimeslot());
                                sellerDelivery.setDeliverytype(Cart.hm.get(keys[j]).getDeliveryType().getDeliveryType());
                                sellerDelivery.setDelivery_address(orderDeliveryAddress);

                            if(Cart.hm.get(keys[j]).getDeliveryType().getDeliveryType().equalsIgnoreCase("pickup") && DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size() > 0) {
                                for (int l = 0; l < DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size(); l++) {
                                    if (branchid.equalsIgnoreCase(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getBranchid())) {
                                        sellerDelivery.setPickup_address(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation());
                                        pickupaddress=DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getAddress1()
                                                +","+DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getAddress2()
                                                +"\n"+DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getArea()
                                                +","+DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getCity()
                                                +"\n"+DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getState()
                                                +","+DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getZipcode()
                                                +" ("+DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation().getCountry()+")";
                                    }
                                }
                            }

                                sellerDelivery.setPrefdeldtime(Cart.hm.get(keys[j]).getPrefereddeliverydate());
                                sellerDelivery.setPrefdeltimeslot(Cart.hm.get(keys[j]).getTimeslot());

                                if(Cart.hm.get(keys[j]).getDeliveryType().getOrderinstructions() != null) {
                                    if (!Cart.hm.get(keys[j]).getDeliveryType().getOrderinstructions().isEmpty()) {

                                        sellerDelivery.setOrderinstructions(Cart.hm.get(keys[j]).getDeliveryType().getOrderinstructions());
                                    } else {
                                        sellerDelivery.setOrderinstructions("");
                                    }
                                }

//                            if(i > 0) {
                                for (int k = 0; k < DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); k++) {
                                    if (branchid.equalsIgnoreCase(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getBranchid())) {
                                        DeliveryChargeDetails deliveryChargeDetails = new DeliveryChargeDetails();
                                        if (DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isIsdeliverychargeinpercent() == true) {
                                            deliveryChargeDetails.setCharge((Cart.currentProviderSubTotal(list) * DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getCharge()) / 100);
                                        } else {
                                            deliveryChargeDetails.setCharge(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getCharge());
                                        }
                                        deliveryChargeDetails.setDelivery(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isDelivery());
                                        deliveryChargeDetails.setIsdeliverychargeinpercent(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isIsdeliverychargeinpercent());

                                        sellerDelivery.setDeliverycharge(deliveryChargeDetails);

                                    }
                                }
//                            }
                                OrderDetailsActivity.createOrderCartList.getSellerdelivery().add(sellerDelivery);
                                adddeliveryTypes.setBranchid(branchid);
                                adddeliveryTypes.setDeliverytype(Cart.hm.get(keys[j]).getDeliveryType().getDeliveryType());
                                OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(adddeliveryTypes);
                            }
                        }
                    }

                    li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout llProductsForProvider = (LinearLayout) li.inflate(R.layout.product_for_provider, null);
                    ll = (LinearLayout) llProductsForProvider.findViewById(R.id.ll);
                    ((TextView) llProductsForProvider.findViewById(R.id.provider_name)).setText(providerName);
                    ((TextView) llProductsForProvider.findViewById(R.id.provider_area)).setText(providerArea);
                    deliveryTypeText = (TextView) llProductsForProvider.findViewById(R.id.delivery_type);
                    deliveryDateText = (TextView) llProductsForProvider.findViewById(R.id.delivery_date_order_details);
                    deliveryTimeText = (TextView) llProductsForProvider.findViewById(R.id.delivery_time_slot);
                    deliveryAddressText = (TextView) llProductsForProvider.findViewById(R.id.delivery_address);

                    deliveryTypeText.setText(deliveryType);
                    deliveryTimeText.setText("Between "+deliveryTime);
                    deliveryDateText.setText(deliveryDate);
                    if(deliveryType.equalsIgnoreCase("Pick-Up"))
                    {
                        deliveryAddressText.setText(pickupaddress);
                    }
                    else {
                        deliveryAddressText.setText(orderDeliveryAddress.getAddress1() + "," + orderDeliveryAddress.getAddress2()
                                + "\n" + orderDeliveryAddress.getArea() + "," + orderDeliveryAddress.getCity()
                                + "\n" + orderDeliveryAddress.getState() + "," + orderDeliveryAddress.getZipcode() + "(" + orderDeliveryAddress.getCountry() + ")");
                    }

                    if (i > 0) {
                        delivery_charge.setText(deliveryCharges + "");
                        textGrandTotal.setText((Cart.currentProviderSubTotal(list) + deliveryCharges) + "");
                        deliveryCharges = 0.0;
                    }

                    for (int j = 0; j < DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); j++) {

                        if (branchid.equalsIgnoreCase(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid())) {
                            OrderDetailsActivity.deliveryChargeDetails = new DeliveryChargeDetails();
                            OrderDetailsActivity.deliveryChargeDetails.setBranchid(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid());
                            OrderDetailsActivity.deliveryChargeDetails.setDelivery(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery());
                            OrderDetailsActivity.deliveryChargeDetails.setIsdeliverychargeinpercent(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isIsdeliverychargeinpercent());
                            OrderDetailsActivity.deliveryChargeDetails.setCoverage(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCoverage());
                            if (deliveryType.equalsIgnoreCase("Home Delivery")) {
                                OrderDetailsActivity.deliveryChargeDetails.setCharge((DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge()));
                            }
                            if (deliveryType.equalsIgnoreCase("Home Delivery")) {
                                if (DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery() == true) {

                                    deliveryCharges = deliveryCharges + DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge();
                                }
                            } else {
                                deliveryCharges = 0.0;
                            }

                            OrderDetailsActivity.createOrderCartList.getDeliverycharges().add(OrderDetailsActivity.deliveryChargeDetails);
                        }
                    }

                    textGrandTotal = ((TextView) llProductsForProvider.findViewById(R.id.grandtoatal));
                    delivery_charge = (TextView) llProductsForProvider.findViewById(R.id.delivery_charge);
                    list.clear();
                    list.add(createOrderProductDetailsList.get(i));
                    providers.add(branchid);

                    OrderDetailsActivity.llProductsList.addView(llProductsForProvider);
                }
            }

            if (i == createOrderProductDetailsList.size() - 1) {
                new ConfirmOrderProductListAdapter(context, list).setProductList();
                delivery_charge.setText(deliveryCharges + "");
                textGrandTotal.setText((Cart.currentProviderSubTotal(list) + deliveryCharges) + "");
                OrderDetailsActivity.grandTotal.setText(String.format("%.2f", Cart.subTotal() + Cart.configurredProductPrice(createOrderProductDetailsList) + Cart.returnDeliveryCharges(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges)));
                deliveryCharges = 0.0;
            }
        }
    }
}
