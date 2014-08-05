package com.gls.orderzapp.MyOrders.MyOrderDetailAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.DetailedOrderActivity;
import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.R;


import java.util.List;

/**
 * Created by prajyot on 7/5/14.
 */
public class AdapterForSubOrders {
    Context context;
    public static LinearLayout ll;
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

            ll = (LinearLayout) mainLayout.findViewById(R.id.ll);
            if(subOrderDetailsList.get(i).getProductprovider() != null) {
                if (subOrderDetailsList.get(i).getProductprovider().getProvidername() != null) {
                    providerName.setText(subOrderDetailsList.get(i).getProductprovider().getProvidername());
                }
                if(subOrderDetailsList.get(i).getProductprovider().getLocation().getArea() != null){
                    provider_area.setText(subOrderDetailsList.get(i).getProductprovider().getLocation().getArea());
                }
            }
            if (subOrderDetailsList.get(i).getSuborderid() != null) {
                subOrderNumber.setText(subOrderDetailsList.get(i).getSuborderid());
            }
            delivery_charge.setText(String.format("%.2f", subOrderDetailsList.get(i).getDeliverycharge()));
            txt_final_order_total.setText(String.format("%.2f", Double.parseDouble(subOrderDetailsList.get(i).getSuborder_price().toString())));

            if (subOrderDetailsList.get(i).getProducts() != null) {
                new MyOrderProductListAdapter(context, subOrderDetailsList.get(i).getProducts()).setProductList();
            }

            if(subOrderDetailsList.get(i).getDeliverytype() != null){
                if(subOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home")) {
                    delivery_type.setText("Home Delivery");
                }else{
                    delivery_type.setText("Pick-up");
                }
            }
            DetailedOrderActivity.listProducts.addView(mainLayout);
        }
    }
}
