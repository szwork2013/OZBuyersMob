package com.gls.orderzapp.CreateOrder.OrderResponseAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.OrderedSubOrderDetails;
import com.gls.orderzapp.MainApp.FinalOrderActivity;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 6/5/14.
 */
public class AdapterForFinalOrderMultipleProviders {
    Context context;
    public static LinearLayout ll;
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

            ll = (LinearLayout) mainLayout.findViewById(R.id.ll);

            delivery_charge.setText(orderedSubOrderDetailsList.get(i).getDeliverycharge() + "");
            if (orderedSubOrderDetailsList.get(i).getDeliverytype() != null) {
                if(orderedSubOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home")) {
                    delivery_type.setText("Home Delivery");
                }
                else
                {  delivery_type.setText("Pick-Up");}
            }
            if (orderedSubOrderDetailsList.get(i).getProductprovider().getProviderbrandname() != null) {
                providerName.setText(orderedSubOrderDetailsList.get(i).getProductprovider().getProviderbrandname());
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
