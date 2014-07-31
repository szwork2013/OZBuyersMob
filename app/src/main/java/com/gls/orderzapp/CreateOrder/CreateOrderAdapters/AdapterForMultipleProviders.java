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
    TextView textGrandTotal, delivery_charge, deliveryTypeText;
    CreateOrderAddressDetails orderDeliveryAddress;
    String prefferedDeliveryDate;
    String[] keys;

    public AdapterForMultipleProviders(Context context, List<CreateOrderProductDetails> createOrderProductDetailsList, CreateOrderAddressDetails orderDeliveryAddress, String prefferedDeliveryDate) {
        this.context = context;
        this.createOrderProductDetailsList = createOrderProductDetailsList;
        this.orderDeliveryAddress = orderDeliveryAddress;
        this.prefferedDeliveryDate = prefferedDeliveryDate;

        keys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);
    }

    public void setMultipleProvidersList() {
        ArrayList<String> providers = new ArrayList<String>();
        double deliveryCharges = 0.0;

        for (int i = 0; i < createOrderProductDetailsList.size(); i++) {

            String branchid = createOrderProductDetailsList.get(i).getBranchid();
            String providerName = createOrderProductDetailsList.get(i).getProvidername();
            String providerArea = createOrderProductDetailsList.get(i).getLocation().getArea();
            String deliveryType = "";

            if (providers != null) {
                if (providers.contains(branchid)) {

                    list.add(createOrderProductDetailsList.get(i));


                } else {

                    if (i > 0) {
                        Log.d("delivery charge1", deliveryCharges + "");

                        new ConfirmOrderProductListAdapter(context, list).setProductList();

//                    OrderDetailsActivity.grandTotal.setText(String.format("%.2f", Cart.subTotal()+Cart.configurredProductPrice(createOrderProductDetailsList)));

                    }
                    //**********
//                    for(int j = 0; j < DisplayDeliveryChargesAndType.deliveryType.size(); j++){
//                        Log.d("branchid and provider name inside for but outside if", branchid + "   "+providerName+ "   "+providerArea);
//                        if(branchid.equals(DisplayDeliveryChargesAndType.deliveryType.get(j).split("_")[0])) {
//
//                            Log.d("branchid and provider name inside for but outside if", branchid + "   "+providerName+ "   "+providerArea);
//                            if (DisplayDeliveryChargesAndType.deliveryType.get(j).split("_")[1].equalsIgnoreCase("home")) {
//                                deliveryType = "Home Delivery";
//                            } else {
//                                deliveryType = "Pick-Up";
//                            }
//                            SellerDelivery sellerDelivery = new SellerDelivery();
//                            sellerDelivery.setBranchid(branchid);
//                            sellerDelivery.setDeliverytype(DisplayDeliveryChargesAndType.deliveryType.get(i).split("_")[1]);
//                            sellerDelivery.setDelivery_address(orderDeliveryAddress);
//                            if(DisplayDeliveryChargesAndType.deliveryType.get(j).split("_")[1].equalsIgnoreCase("pickup") && DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size() > 0) {
//                                for (int l = 0; l < DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size(); l++) {
//                                    if (branchid.equalsIgnoreCase(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getBranchid())) {
//                                        sellerDelivery.setPickup_address(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation());
//                                    }
//                                }
//                            }
//
//                            sellerDelivery.setPrefdeldtime(prefferedDeliveryDate);
//                            if(DisplayDeliveryChargesAndType.order_instruction.length > j && DisplayDeliveryChargesAndType.order_instruction[j] != null ) {
//                                {
//                                    if (!DisplayDeliveryChargesAndType.order_instruction[j].isEmpty()) {
//                                        sellerDelivery.setOrderinstructions(DisplayDeliveryChargesAndType.order_instruction[j]);
//                                    } else {
//                                        sellerDelivery.setOrderinstructions("");
//                                    }
//                                }
//                            }
//
//                            for(int k = 0; k < DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); k++) {
//                                if (branchid.equalsIgnoreCase(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getBranchid())) {
//                                    DeliveryChargeDetails deliveryChargeDetails = new DeliveryChargeDetails();
//                                    deliveryChargeDetails.setCharge(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getCharge());
//                                    deliveryChargeDetails.setDelivery(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isDelivery());
//                                    deliveryChargeDetails.setIsdeliverychargeinpercent(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isIsdeliverychargeinpercent());
//
//                                    sellerDelivery.setDeliverycharge(deliveryChargeDetails);
//
//                                }
//                            }
//                            OrderDetailsActivity.createOrderCartList.getSellerdelivery().add(sellerDelivery);
////                            OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(OrderDetailsActivity.deliveryTypes);
//                            adddeliveryTypes.setBranchid(branchid);
//                            adddeliveryTypes.setDeliverytype(DisplayDeliveryChargesAndType.deliveryType.get(j).split("_")[1]);
//                            OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(adddeliveryTypes);
//
//                        }
//                    }

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
                                SellerDelivery sellerDelivery = new SellerDelivery();
                                sellerDelivery.setBranchid(branchid);
                                sellerDelivery.setDeliverytype(Cart.hm.get(keys[j]).getDeliveryType().getDeliveryType());
                                sellerDelivery.setDelivery_address(orderDeliveryAddress);

                            if(Cart.hm.get(keys[j]).getDeliveryType().getDeliveryType().equalsIgnoreCase("pickup") && DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size() > 0) {
                                for (int l = 0; l < DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size(); l++) {
                                    if (branchid.equalsIgnoreCase(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getBranchid())) {
                                        sellerDelivery.setPickup_address(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation());
                                    }
                                }
                            }

                                sellerDelivery.setPrefdeldtime(prefferedDeliveryDate);
                                if(Cart.hm.get(keys[j]).getDeliveryType().getOrderinstructions() != null) {
                                    if (!Cart.hm.get(keys[j]).getDeliveryType().getOrderinstructions().isEmpty()) {

                                        sellerDelivery.setOrderinstructions(Cart.hm.get(keys[j]).getDeliveryType().getOrderinstructions());
                                    } else {
                                        sellerDelivery.setOrderinstructions("");
                                    }
                                }


                            for(int k = 0; k < DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); k++) {
                                if (branchid.equalsIgnoreCase(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getBranchid())) {
                                    DeliveryChargeDetails deliveryChargeDetails = new DeliveryChargeDetails();
                                    deliveryChargeDetails.setCharge(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getCharge());
                                    deliveryChargeDetails.setDelivery(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isDelivery());
                                    deliveryChargeDetails.setIsdeliverychargeinpercent(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).isIsdeliverychargeinpercent());

                                    sellerDelivery.setDeliverycharge(deliveryChargeDetails);

                                }
                            }
                                OrderDetailsActivity.createOrderCartList.getSellerdelivery().add(sellerDelivery);
//                            OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(OrderDetailsActivity.deliveryTypes);
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
                    deliveryTypeText.setText(deliveryType);

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
