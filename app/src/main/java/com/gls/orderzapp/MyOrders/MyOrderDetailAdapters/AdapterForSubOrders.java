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
    LayoutInflater li;

    public AdapterForSubOrders(Context context, List<SubOrderDetails> subOrderDetailsList) {
        this.context = context;
        this.subOrderDetailsList = subOrderDetailsList;
    }

    public void setMultipleProvidersList() {
        for (int i = 0; i < subOrderDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mainLayout = (LinearLayout) li.inflate(R.layout.final_order_suborder_provider, null);

            TextView providerName = (TextView) mainLayout.findViewById(R.id.provider_name);
            TextView txt_final_order_total = (TextView) mainLayout.findViewById(R.id.txt_final_order_total);
            TextView subOrderNumber = (TextView) mainLayout.findViewById(R.id.suborder_no);

            ll = (LinearLayout) mainLayout.findViewById(R.id.ll);
            if (subOrderDetailsList.get(i).getProductprovider().getProvidername() != null) {
                providerName.setText(subOrderDetailsList.get(i).getProductprovider().getProvidername());
            }
            if (subOrderDetailsList.get(i).getSuborderid() != null) {
                subOrderNumber.setText(subOrderDetailsList.get(i).getSuborderid());
            }
            txt_final_order_total.setText(String.format("%.2f", Double.parseDouble(subOrderDetailsList.get(i).getSuborder_price().toString())));

            if (subOrderDetailsList.get(i).getProducts() != null) {
                new MyOrderProductListAdapter(context, subOrderDetailsList.get(i).getProducts()).setProductList();
            }
            DetailedOrderActivity.listProducts.addView(mainLayout);
        }
    }
}
