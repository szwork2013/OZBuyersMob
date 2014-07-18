package com.gls.orderzapp.CreateOrder.OrderResponseAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.OrderedProductDetails;
import com.gls.orderzapp.CreateOrder.OrderResponseBeans.ProductConfiguration;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 6/5/14.
 */
public class FinalOrderProductListAdapter {

    Context context;
    List<OrderedProductDetails> orderedProductDetailsList;
    LinearLayout llProductList;

    public FinalOrderProductListAdapter(Context context, List<OrderedProductDetails> orderedProductDetailsList) {
        this.context = context;
        this.orderedProductDetailsList = orderedProductDetailsList;
    }

    public void setProductList() {
        for (int i = 0; i < orderedProductDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            llProductList = (LinearLayout) li.inflate(R.layout.final_order_list_item, null);
            TextView product_name = (TextView) llProductList.findViewById(R.id.product_name);
            TextView quantity = (TextView) llProductList.findViewById(R.id.quantity);
            TextView price = (TextView) llProductList.findViewById(R.id.price);
            TextView subtotal = (TextView) llProductList.findViewById(R.id.subtotal);

            if (orderedProductDetailsList.get(i).getProductname() != null) {
                product_name.setText(orderedProductDetailsList.get(i).getProductname());
            }

            quantity.setText(String.format("%.2f", orderedProductDetailsList.get(i).getQty()) + " " + orderedProductDetailsList.get(i).getUom());
            price.setText(String.format("%.2f", (orderedProductDetailsList.get(i).getOrderprice() / orderedProductDetailsList.get(i).getQty()) - (configurationPrice(orderedProductDetailsList.get(i).getProductconfiguration()) / orderedProductDetailsList.get(i).getQty())));
            LinearLayout ll_special_message = (LinearLayout) llProductList.findViewById(R.id.ll_special_message);
            TextView special_message = (TextView) llProductList.findViewById(R.id.special_message);
            TextView message = (TextView) llProductList.findViewById(R.id.message);

            if (orderedProductDetailsList.get(i).getProductconfiguration().size() > 0) {
                ll_special_message.setVisibility(View.VISIBLE);
                message.setText(configurationPrice(orderedProductDetailsList.get(i).getProductconfiguration()) + "");
            } else {
                ll_special_message.setVisibility(View.GONE);
            }
            subtotal.setText(String.format("%.2f", orderedProductDetailsList.get(i).getOrderprice() - configurationPrice(orderedProductDetailsList.get(i).getProductconfiguration())));
            AdapterForFinalOrderMultipleProviders.ll.addView(llProductList);
        }
    }

    public double configurationPrice(List<ProductConfiguration> list) {
        double configurationPrice = 0.0;
        try {
            for (int i = 0; i < list.size(); i++) {
                configurationPrice = configurationPrice + Double.parseDouble(list.get(i).getProd_configprice().getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurationPrice;
    }
}