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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 30/4/14.
 */
public class AdapterForMultipleProviders {
    Context context;
    String providerName = "", providerArea = "";
    public static LinearLayout ll;
    List<CreateOrderProductDetails> createOrderProductDetailsList;
    List<CreateOrderProductDetails> list = new ArrayList<>();
    LayoutInflater li;
    TextView textGrandTotal, delivery_charge;
    CreateOrderAddressDetails orderDeliveryAddress;
    String prefferedDeliveryDate;

    public AdapterForMultipleProviders(Context context, List<CreateOrderProductDetails> createOrderProductDetailsList, CreateOrderAddressDetails orderDeliveryAddress, String prefferedDeliveryDate) {
        this.context = context;
        this.createOrderProductDetailsList = createOrderProductDetailsList;
        this.orderDeliveryAddress = orderDeliveryAddress;
        this.prefferedDeliveryDate = prefferedDeliveryDate;
    }

    public void setMultipleProvidersList() {
        ArrayList<String> providers = new ArrayList<String>();
        double deliveryCharges = 0.0;
//        Log.d("createOrderProductDetailsList.size()",createOrderProductDetailsList.size()+"");
//        Log.d("DisplayDeliveryChargesAndType.delivery_mode_branchid",DisplayDeliveryChargesAndType.delivery_mode_branchid.length+"");
        for (int i = 0; i < createOrderProductDetailsList.size(); i++) {

            String branchid = createOrderProductDetailsList.get(i).getBranchid();
//            Log.d("create order branch id", branchid)
            providerName = createOrderProductDetailsList.get(i).getProvidername();
            providerArea = createOrderProductDetailsList.get(i).getLocation().getArea();
            String deliveryType = "";

//            for(int j = 0; j < createOrderProductDetailsList.get(i).getDeliverycharge().size(); j++) {
//                deliveryCharges = deliveryCharges + createOrderProductDetailsList.get(i).getDeliverycharge().get(j).getCharge();
//            }
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
                    for(int j=0;j<DisplayDeliveryChargesAndType.delivery_mode_branchid.length;j++){

                        if (branchid.equalsIgnoreCase(DisplayDeliveryChargesAndType.delivery_mode_branchid[j])) {
                            if (DisplayDeliveryChargesAndType.delivery_mode[i].equalsIgnoreCase("home")) {
                                deliveryType = "Home Delivery";
                            } else {
                                deliveryType = "Pick-Up";
                            }
                            Log.d("branch id", branchid);
                            Log.d("branch array", DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid());
                            OrderDetailsActivity.deliveryTypes = new DeliveryTypes();
                            OrderDetailsActivity.deliveryTypes.setBranchid(DisplayDeliveryChargesAndType.delivery_mode_branchid[j]);
                            OrderDetailsActivity.deliveryTypes.setDeliverytype(DisplayDeliveryChargesAndType.delivery_mode[j]);

                            SellerDelivery sellerDelivery = new SellerDelivery();
                            sellerDelivery.setBranchid(branchid);
                            sellerDelivery.setDeliverytype(DisplayDeliveryChargesAndType.delivery_mode[j]);

                            sellerDelivery.setDelivery_address(orderDeliveryAddress);

                            if(DisplayDeliveryChargesAndType.delivery_mode[j].equalsIgnoreCase("pickup") && DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size() > 0) {
                                for (int l = 0; l < DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().size(); l++) {
                                    if (branchid.equalsIgnoreCase(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getBranchid())) {
                                        sellerDelivery.setPickup_address(DisplayDeliveryChargesAndType.listOfPickupAddresses.getListPickUpAddress().get(l).getLocation());
                                    }
                                }
                            }

                            sellerDelivery.setPrefdeldtime(prefferedDeliveryDate);
                            sellerDelivery.setOrderinstructions("");
                            for(int k = 0; k < DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); k++) {
                                if (branchid.equalsIgnoreCase(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(k).getBranchid())) {
                                    DeliveryChargeDetails deliveryChargeDetails = new DeliveryChargeDetails();
                                    deliveryChargeDetails.setCharge(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge());
                                    deliveryChargeDetails.setDelivery(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery());
                                    deliveryChargeDetails.setIsdeliverychargeinpercent(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isIsdeliverychargeinpercent());

                                    sellerDelivery.setDeliverycharge(deliveryChargeDetails);

                                }
                            }
                            OrderDetailsActivity.createOrderCartList.getSellerdelivery().add(sellerDelivery);
                            OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(OrderDetailsActivity.deliveryTypes);
                        }
                    }
                    //***********

//                    Log.d("delivery type", deliveryType);
                    Log.d(createOrderProductDetailsList.get(i).getProductname(), DisplayDeliveryChargesAndType.delivery_mode[i]);
                    li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout llProductsForProvider = (LinearLayout) li.inflate(R.layout.product_for_provider, null);
                    ll = (LinearLayout) llProductsForProvider.findViewById(R.id.ll);
                    ((TextView) llProductsForProvider.findViewById(R.id.provider_name)).setText(providerName);
                    ((TextView) llProductsForProvider.findViewById(R.id.provider_area)).setText(providerArea);
                    ((TextView) llProductsForProvider.findViewById(R.id.delivery_type)).setText(deliveryType);

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
                Log.d("delivery charge2", deliveryCharges + "");
                new ConfirmOrderProductListAdapter(context, list).setProductList();

                delivery_charge.setText(deliveryCharges + "");
                textGrandTotal.setText((Cart.currentProviderSubTotal(list) + deliveryCharges) + "");
                OrderDetailsActivity.grandTotal.setText(String.format("%.2f", Cart.subTotal() + Cart.configurredProductPrice(createOrderProductDetailsList) + Cart.returnDeliveryCharges(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges)));
                deliveryCharges = 0.0;
            }
        }
    }
}
