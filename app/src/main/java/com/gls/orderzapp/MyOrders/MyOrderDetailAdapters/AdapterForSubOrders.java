package com.gls.orderzapp.MyOrders.MyOrderDetailAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.DetailedMyOrderActivity;
import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by prajyot on 7/5/14.
 */
public class AdapterForSubOrders {
    public static LinearLayout ll;
    Context context;
    LinearLayout mainLayout;
    List<SubOrderDetails> subOrderDetailsList;

    public AdapterForSubOrders(Context context, List<SubOrderDetails> subOrderDetailsList) {
        this.context = context;
        this.subOrderDetailsList = subOrderDetailsList;
    }

    public void setMultipleProvidersList() {
        for (int i = 0; i < subOrderDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mainLayout = (LinearLayout) li.inflate(R.layout.final_order_suborder_provider, null);

            TextView providerName = (TextView) mainLayout.findViewById(R.id.provider_name);
            TextView provider_area = (TextView) mainLayout.findViewById(R.id.provider_area);
            TextView txt_final_order_total = (TextView) mainLayout.findViewById(R.id.txt_final_order_total);
            TextView subOrderNumber = (TextView) mainLayout.findViewById(R.id.suborder_no);
            TextView delivery_type = (TextView) mainLayout.findViewById(R.id.delivery_type);
            TextView delivery_charge = (TextView) mainLayout.findViewById(R.id.delivery_charge);
            TextView delivery_date_my_order = (TextView) mainLayout.findViewById(R.id.delivery_date_final_order);
            TextView delivery_time_slot_my_order = (TextView) mainLayout.findViewById(R.id.delivery_time_slot_final_order);
            TextView delivery_address_my_order = (TextView) mainLayout.findViewById(R.id.delivery_address_final_order);
            TextView cont_no_final_order = (TextView) mainLayout.findViewById(R.id.cont_no_final_order);

            ll = (LinearLayout) mainLayout.findViewById(R.id.ll);
            if (subOrderDetailsList.get(i).getProductprovider() != null) {
                if (subOrderDetailsList.get(i).getProductprovider().getProviderbrandname() != null) {
                    providerName.setText(subOrderDetailsList.get(i).getProductprovider().getProviderbrandname());
                }
                if (subOrderDetailsList.get(i).getProductprovider().getLocation().getArea() != null) {
                    provider_area.setText(subOrderDetailsList.get(i).getProductprovider().getLocation().getArea());
                }
            }

            if (subOrderDetailsList.get(i).getPrefdeldtime() != null) {
                try {
                    final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                    final DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");
                    outputFormat.setTimeZone(tz);
                    Date order_date = inputFormat.parse(subOrderDetailsList.get(i).getPrefdeldtime());
                    delivery_date_my_order.setText(outputFormat.format(order_date));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (subOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home")) {
                if (subOrderDetailsList.get(i).getDelivery_address() != null) {
                    delivery_address_my_order.setText(subOrderDetailsList.get(i).getDelivery_address().getAddress1() + "," + subOrderDetailsList.get(i).getDelivery_address().getAddress2()
                            + "\n" + subOrderDetailsList.get(i).getDelivery_address().getArea() + "," + subOrderDetailsList.get(i).getDelivery_address().getCity()
                            + "\n" + subOrderDetailsList.get(i).getDelivery_address().getState() + "," + subOrderDetailsList.get(i).getDelivery_address().getZipcode()
                            + "," + subOrderDetailsList.get(i).getDelivery_address().getCountry());
                }
            } else {
                if (subOrderDetailsList.get(i).getPickup_address() != null) {
                    delivery_address_my_order.setText(subOrderDetailsList.get(i).getPickup_address().getAddress1() + "," + subOrderDetailsList.get(i).getPickup_address().getAddress2()
                            + "\n" + subOrderDetailsList.get(i).getPickup_address().getArea() + "," + subOrderDetailsList.get(i).getPickup_address().getCity()
                            + "\n" + subOrderDetailsList.get(i).getPickup_address().getState() + "," + subOrderDetailsList.get(i).getPickup_address().getZipcode()
                            + "," + subOrderDetailsList.get(i).getPickup_address().getCountry());
                }
            }
            if (subOrderDetailsList.get(i).getProductprovider().getContact_supports() != null) {
                String cont = "";
                for (int cont_int = 0; cont_int < subOrderDetailsList.get(i).getProductprovider().getContact_supports().size(); cont_int++) {
                    cont = cont.concat(subOrderDetailsList.get(i).getProductprovider().getContact_supports().get(i) + ",");
                }
                cont_no_final_order.setText(cont);
            }

            if (subOrderDetailsList.get(i).getPrefdeltimeslot() != null) {
                String deliveryTime = "";
                if (subOrderDetailsList.get(i).getPrefdeltimeslot().getFrom() < 12) {

                    deliveryTime = String.format("%.2f", subOrderDetailsList.get(i).getPrefdeltimeslot().getFrom()) + " AM";

                } else if (subOrderDetailsList.get(i).getPrefdeltimeslot().getFrom() == 12) {

                    deliveryTime = String.format("%.2f", subOrderDetailsList.get(i).getPrefdeltimeslot().getFrom()) + " PM";

                } else if (subOrderDetailsList.get(i).getPrefdeltimeslot().getFrom() > 12) {

                    deliveryTime = String.format("%.2f", (subOrderDetailsList.get(i).getPrefdeltimeslot().getFrom() - 12)) + " PM";

                }

                if (subOrderDetailsList.get(i).getPrefdeltimeslot().getTo() < 12) {

                    deliveryTime = deliveryTime.concat(" to " + String.format("%.2f", subOrderDetailsList.get(i).getPrefdeltimeslot().getTo()) + " AM");

                } else if (subOrderDetailsList.get(i).getPrefdeltimeslot().getTo() == 12) {

                    deliveryTime = deliveryTime.concat(" to " + String.format("%.2f", subOrderDetailsList.get(i).getPrefdeltimeslot().getTo()) + " PM");

                } else if (subOrderDetailsList.get(i).getPrefdeltimeslot().getTo() > 12) {

                    deliveryTime = deliveryTime.concat(" to " + String.format("%.2f", (subOrderDetailsList.get(i).getPrefdeltimeslot().getTo() - 12)) + " PM");

                }
                delivery_time_slot_my_order.setText("Between " + deliveryTime);
            }

            if (subOrderDetailsList.get(i).getSuborderid() != null) {
                subOrderNumber.setText(subOrderDetailsList.get(i).getSuborderid());
            }
            delivery_charge.setText(String.format("%.2f", subOrderDetailsList.get(i).getDeliverycharge()));
            txt_final_order_total.setText(String.format("%.2f", Double.parseDouble(subOrderDetailsList.get(i).getSuborder_price().toString())));

            if (subOrderDetailsList.get(i).getProducts() != null) {
                new MyOrderProductListAdapter(context, subOrderDetailsList.get(i).getProducts()).setProductList();
            }

            if (subOrderDetailsList.get(i).getDeliverytype() != null) {
                if (subOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home")) {
                    delivery_type.setText("Home Delivery");
                } else {
                    delivery_type.setText("Pick-up");
                }
            }
            DetailedMyOrderActivity.listProducts.addView(mainLayout);
        }
    }
}
