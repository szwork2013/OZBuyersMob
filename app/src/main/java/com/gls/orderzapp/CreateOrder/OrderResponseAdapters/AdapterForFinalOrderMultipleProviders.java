package com.gls.orderzapp.CreateOrder.OrderResponseAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.OrderedSubOrderDetails;
import com.gls.orderzapp.MainApp.FinalOrderActivity;
import com.gls.orderzapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by prajyot on 6/5/14.
 */
public class AdapterForFinalOrderMultipleProviders {
    public static LinearLayout ll;
    Context context;
    LinearLayout mainLayout;
    List<OrderedSubOrderDetails> orderedSubOrderDetailsList;

    public AdapterForFinalOrderMultipleProviders(Context context, List<OrderedSubOrderDetails> orderedSubOrderDetailsList) {
        this.context = context;
        this.orderedSubOrderDetailsList = orderedSubOrderDetailsList;
    }

    public void setMultipleProvidersList() {
        for (int i = 0; i < orderedSubOrderDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mainLayout = (LinearLayout) li.inflate(R.layout.final_order_suborder_provider, null);

            TextView providerName = (TextView) mainLayout.findViewById(R.id.provider_name);
            TextView providerArea = (TextView) mainLayout.findViewById(R.id.provider_area);
            TextView subOrderNumber = (TextView) mainLayout.findViewById(R.id.suborder_no);
            TextView txt_final_order_total = (TextView) mainLayout.findViewById(R.id.txt_final_order_total);
            TextView delivery_type = (TextView) mainLayout.findViewById(R.id.delivery_type);
            TextView delivery_charge = (TextView) mainLayout.findViewById(R.id.delivery_charge);
            TextView delivery_date_final_order = (TextView) mainLayout.findViewById(R.id.delivery_date_final_order);
            TextView delivery_time_slot_final_order = (TextView) mainLayout.findViewById(R.id.delivery_time_slot_final_order);
            TextView delivery_address_final_order = (TextView) mainLayout.findViewById(R.id.delivery_address_final_order);
            LinearLayout ll_cont_no_final_order = (LinearLayout) mainLayout.findViewById(R.id.ll_cont_no);

            ll_cont_no_final_order.setVisibility(View.GONE);
            Log.d("View GONE", "GONE");

            ll = (LinearLayout) mainLayout.findViewById(R.id.ll);

            delivery_charge.setText(orderedSubOrderDetailsList.get(i).getDeliverycharge() + "");
            if (orderedSubOrderDetailsList.get(i).getDeliverytype() != null) {
                if (orderedSubOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home")) {
                    delivery_type.setText("Home Delivery");
                } else {
                    delivery_type.setText("Pick-Up");
                }
            }
            if (orderedSubOrderDetailsList.get(i).getPrefdeltimeslot() != null) {
                String deliveryTime = "", getFromMin = "", getToMin = "";
                int getFromHrs = 0, getToHrs = 0;
                DecimalFormat formatter = new DecimalFormat("00");
                getFromHrs = (int) orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom();
                getFromMin = formatter.format(Math.round((int) ((orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom() - getFromHrs) * 60) * 100) / 100);
                getToHrs = (int) orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo();
                getToMin = formatter.format(Math.round((int) ((orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo() - getToHrs) * 60) * 100) / 100);

                if (getFromHrs < 12) {
                    deliveryTime = getFromHrs + ":" + getFromMin + " AM";
                } else if (getFromHrs == 12) {
                    deliveryTime = getFromHrs + ":" + getFromMin + " PM";
                } else if (getFromHrs > 12) {
                    deliveryTime = (getFromHrs - 12) + ":" + getFromMin + " PM";
                }

                if (getToHrs < 12) {
                    deliveryTime = deliveryTime.concat(" - " + getToHrs + ":" + getToMin + " AM");
                } else if (getToHrs == 12) {
                    deliveryTime = deliveryTime.concat(" - " + getToHrs + ":" + getToMin + " PM");
                } else if (getToHrs > 12) {
                    deliveryTime = deliveryTime.concat(" - " + (getToHrs - 12) + ":" + getToMin + " PM");
                }

//                if(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom()<12)
//                {
//                    deliveryTime=String.format("%.2f",orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom())+" AM";
//                }else if(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom()==12)
//                {
//                    deliveryTime=String.format("%.2f",orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom())+" PM";
//                }
//                else if(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom()>12)
//                {
//                    deliveryTime=String.format("%.2f",(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getFrom()-12))+" PM";
//                }
//
//                if(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo()<12)
//                {
//                    deliveryTime=deliveryTime.concat(" to "+String.format("%.2f",orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo())+" AM");
//                }else if(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo()==12)
//                {
//                    deliveryTime=deliveryTime.concat(" to "+String.format("%.2f",orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo())+" PM");
//                }
//                else if(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo()>12)
//                {
//                    deliveryTime=deliveryTime.concat(" to "+String.format("%.2f",(orderedSubOrderDetailsList.get(i).getPrefdeltimeslot().getTo()-12))+" PM");
//                }

                delivery_time_slot_final_order.setText("Between " + deliveryTime);
            }
            if (orderedSubOrderDetailsList.get(i).getProductprovider().getProviderbrandname() != null) {
                providerName.setText(orderedSubOrderDetailsList.get(i).getProductprovider().getProviderbrandname());
            }
            if (orderedSubOrderDetailsList.get(i).getPrefdeldtime() != null) {
                try {

                    final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                    final DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
                    outputFormat.setTimeZone(tz);
                    Date order_date = inputFormat.parse(orderedSubOrderDetailsList.get(i).getPrefdeldtime());
                    delivery_date_final_order.setText(outputFormat.format(order_date));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (orderedSubOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home")) {
                if (orderedSubOrderDetailsList.get(i).getDelivery_address() != null) {
                    delivery_address_final_order.setText(orderedSubOrderDetailsList.get(i).getDelivery_address().getAddress1() + ", " + orderedSubOrderDetailsList.get(i).getDelivery_address().getAddress2()
                            + "\n" + orderedSubOrderDetailsList.get(i).getDelivery_address().getArea() + ", " + orderedSubOrderDetailsList.get(i).getDelivery_address().getCity()
                            + "\n" + orderedSubOrderDetailsList.get(i).getDelivery_address().getState() + ", " + orderedSubOrderDetailsList.get(i).getDelivery_address().getZipcode()
                            + ", " + orderedSubOrderDetailsList.get(i).getDelivery_address().getCountry());
                }
            } else {
                if (orderedSubOrderDetailsList.get(i).getPickup_address() != null) {
                    delivery_address_final_order.setText(orderedSubOrderDetailsList.get(i).getPickup_address().getAddress1() + ", " + orderedSubOrderDetailsList.get(i).getPickup_address().getAddress2()
                            + "\n" + orderedSubOrderDetailsList.get(i).getPickup_address().getArea() + ", " + orderedSubOrderDetailsList.get(i).getPickup_address().getCity()
                            + "\n" + orderedSubOrderDetailsList.get(i).getPickup_address().getState() + ", " + orderedSubOrderDetailsList.get(i).getPickup_address().getZipcode()
                            + ", " + orderedSubOrderDetailsList.get(i).getPickup_address().getCountry());
                }
            }

            if (orderedSubOrderDetailsList.get(i).getProductprovider().getLocation().getArea() != null) {
                providerArea.setText(orderedSubOrderDetailsList.get(i).getProductprovider().getLocation().getArea());
            }
            if (orderedSubOrderDetailsList.get(i).getSuborderid() != null) {
                subOrderNumber.setText(orderedSubOrderDetailsList.get(i).getSuborderid());
            }
            txt_final_order_total.setText(String.format("%.2f", orderedSubOrderDetailsList.get(i).getSuborder_price()));
            if (orderedSubOrderDetailsList.get(i).getProducts() != null) {
                new FinalOrderProductListAdapter(context, orderedSubOrderDetailsList.get(i).getProducts()).setProductList();
                FinalOrderActivity.listProducts.addView(mainLayout);
            }
        }
    }
}
