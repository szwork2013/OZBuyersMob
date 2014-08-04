package com.gls.orderzapp.MyOrders.MyOrderDetailAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.MyOrders.Beans.ProductConfiguration;
import com.gls.orderzapp.MyOrders.Beans.ProductDetails;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 7/5/14.
 */
public class MyOrderProductListAdapter {
    Context context;
    List<ProductDetails> productDetailsList;
    LinearLayout llProductList;

    public MyOrderProductListAdapter(Context context, List<ProductDetails> productDetailsList) {
        this.context = context;
        this.productDetailsList = productDetailsList;
    }

    public void setProductList() {
        for (int i = 0; i < productDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            llProductList = (LinearLayout) li.inflate(R.layout.final_order_list_item, null);
            TextView product_name = (TextView) llProductList.findViewById(R.id.product_name);
            TextView quantity = (TextView) llProductList.findViewById(R.id.quantity);
            TextView price = (TextView) llProductList.findViewById(R.id.price);
            TextView subtotal = (TextView) llProductList.findViewById(R.id.subtotal);
            TextView special_message = (TextView) llProductList.findViewById(R.id.special_message);
            TextView message = (TextView) llProductList.findViewById(R.id.message);
            LinearLayout ll_special_message = (LinearLayout) llProductList.findViewById(R.id.ll_special_message);

            if(productDetailsList.get(i).getProductconfiguration().size() > 0){
                ll_special_message.setVisibility(View.VISIBLE);
                message.setText(configurationPrice(productDetailsList.get(i).getProductconfiguration())+"");
            }else{
                ll_special_message.setVisibility(View.GONE);
            }

            if (productDetailsList.get(i).getProductname() != null) {
                product_name.setText(productDetailsList.get(i).getProductname());
            }
            quantity.setText(productDetailsList.get(i).getQty() + " " + productDetailsList.get(i).getUom());
            price.setText(String.format("%.2f", productDetailsList.get(i).getOrderprice()));
            subtotal.setText(String.format("%.2f", productDetailsList.get(i).getOrderprice()));

            AdapterForSubOrders.ll.addView(llProductList);
        }
    }

    public double configurationPrice(List<ProductConfiguration> list) {
        double configurationPrice = 0.0;
        try {
            for (int i = 0; i < list.size(); i++) {
                configurationPrice = configurationPrice + list.get(i).getProd_configprice().getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurationPrice;
    }
}
