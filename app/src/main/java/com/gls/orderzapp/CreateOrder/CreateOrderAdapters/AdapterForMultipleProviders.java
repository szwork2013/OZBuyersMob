package com.gls.orderzapp.CreateOrder.CreateOrderAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.AddressDetails.Adapter.DeliveryChargesAndTypeAdapter;
import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderProductDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.DeliveryChargeDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.DeliveryTypes;
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
    boolean isViewAllreadySet = false;
    public static int count = 0;
    TextView textGrandTotal, delivery_charge;

    public AdapterForMultipleProviders(Context context, List<CreateOrderProductDetails> createOrderProductDetailsList) {
        this.context = context;
        this.createOrderProductDetailsList = createOrderProductDetailsList;
    }

    public void setMultipleProvidersList() {
        ArrayList<String> providers = new ArrayList<String>();
        double deliveryCharges = 0.0;
        Log.d("createOrderProductDetailsList.size()",createOrderProductDetailsList.size()+"");
        Log.d("DisplayDeliveryChargesAndType.delivery_mode_branchid",DisplayDeliveryChargesAndType.delivery_mode_branchid.length+"");
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
                            if (DisplayDeliveryChargesAndType.delivery_mode[j].equalsIgnoreCase("home")) {
                                deliveryType = "Home Delivery";
                            } else {
                                deliveryType = "Pick-Up";
                            }
                            OrderDetailsActivity.deliveryTypes = new DeliveryTypes();
                            OrderDetailsActivity.deliveryTypes.setBranchid(DisplayDeliveryChargesAndType.delivery_mode_branchid[j]);
                            OrderDetailsActivity.deliveryTypes.setDeliverytype(DisplayDeliveryChargesAndType.delivery_mode[j]);

                            OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(OrderDetailsActivity.deliveryTypes);
                        }
//                    if (branchid.equalsIgnoreCase(DisplayDeliveryChargesAndType.delivery_mode_branchid[i])) {
//                        if (DisplayDeliveryChargesAndType.delivery_mode[i].equalsIgnoreCase("home")) {
//                            deliveryType = "Home Delivery";
//                        } else {
//                            deliveryType = "Pick-Up";
//                        }
//                        OrderDetailsActivity.deliveryTypes = new DeliveryTypes();
//                        OrderDetailsActivity.deliveryTypes.setBranchid(DisplayDeliveryChargesAndType.delivery_mode_branchid[i]);
//                        OrderDetailsActivity.deliveryTypes.setDeliverytype(DisplayDeliveryChargesAndType.delivery_mode[i]);
//
//                        OrderDetailsActivity.createOrderCartList.getDeliverytypes().add(OrderDetailsActivity.deliveryTypes);
//                    }
                    }
                    //***********

                    Log.d("delivery type", deliveryType);
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
Log.d("list 1",DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size()+"");
                    Log.d("list 2", DisplayDeliveryChargesAndType.delivery_mode.length+"");

                    for (int j = 0; j < DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); j++) {

                        if (branchid.equalsIgnoreCase(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid())) {
                            OrderDetailsActivity.deliveryChargeDetails = new DeliveryChargeDetails();
                            OrderDetailsActivity.deliveryChargeDetails.setBranchid(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid());
                            OrderDetailsActivity.deliveryChargeDetails.setDelivery(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery());
                            OrderDetailsActivity.deliveryChargeDetails.setIsdeliverychargeinpercent(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isIsdeliverychargeinpercent());
                            OrderDetailsActivity.deliveryChargeDetails.setCoverage(DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCoverage());
//                            if (DisplayDeliveryChargesAndType.delivery_mode[i].equalsIgnoreCase("home")) {
                            if (deliveryType.equalsIgnoreCase("Home Delivery")) {
                                OrderDetailsActivity.deliveryChargeDetails.setCharge((DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getCharge()));
                            }
//                            if (DisplayDeliveryChargesAndType.delivery_mode[i].equalsIgnoreCase("home")) {
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
//                count++;
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
