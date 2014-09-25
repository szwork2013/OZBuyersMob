package com.gls.orderzapp.CreateOrder.CreateOrderAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderProductDetails;
import com.gls.orderzapp.ProductConfiguration.Adapter.PopUpForConfigurationCharges;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;

import java.util.List;

/**
 * Created by prajyot on 25/4/14.
 */

public class ConfirmOrderProductListAdapter {
    Context context;
    List<CreateOrderProductDetails> createOrderProductDetailsList;
    LinearLayout llProductList;

    public ConfirmOrderProductListAdapter(Context context, List<CreateOrderProductDetails> createOrderProductDetailsList) {

        this.context = context;
        this.createOrderProductDetailsList = createOrderProductDetailsList;

    }

    public void setProductList() {
        for (int i = 0; i < createOrderProductDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            llProductList = (LinearLayout) li.inflate(R.layout.order_details_list_item, null);

            TextView textProduct = (TextView) llProductList.findViewById(R.id.product_name);
            TextView textQuantity = (TextView) llProductList.findViewById(R.id.quantity);
            TextView textPrice = (TextView) llProductList.findViewById(R.id.price);
            TextView textSubTotal = (TextView) llProductList.findViewById(R.id.subtotal);

            LinearLayout ll_special_message = (LinearLayout) llProductList.findViewById(R.id.ll_special_message);
            TextView message = (TextView) llProductList.findViewById(R.id.message);
            View view_special_message = llProductList.findViewById(R.id.view_special_message);

            Typeface tfRobotoNormal = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
            textProduct.setTypeface(tfRobotoNormal);
            textQuantity.setTypeface(tfRobotoNormal);
            textPrice.setTypeface(tfRobotoNormal);
            textSubTotal.setTypeface(tfRobotoNormal);

            // ll_special_message.setVisibility(View.GONE);
            // view_special_message.setVisibility(View.GONE);
            message.setText(Cart.configurationPrice(createOrderProductDetailsList.get(i)) + "");
            if (createOrderProductDetailsList.get(i).getProductconfiguration().size() > 0) {
                ll_special_message.setVisibility(View.VISIBLE);
                view_special_message.setVisibility(View.VISIBLE);
//                message.setText(createOrderProductDetailsList.get(i).getMessageonproduct());
            } else {
                ll_special_message.setVisibility(View.GONE);
                view_special_message.setVisibility(View.GONE);
            }
            if (createOrderProductDetailsList.get(i).getProductname() != null) {
                textProduct.setText(createOrderProductDetailsList.get(i).getProductname());
            }
            if (createOrderProductDetailsList.get(i).getQty() != null && createOrderProductDetailsList.get(i).getUom() != null) {
                if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("kg") || createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("no") || createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("lb")) {
                    if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("kg") && Double.parseDouble(createOrderProductDetailsList.get(i).getQty()) < 1) {
                        textQuantity.setText(String.format("%.2f", Double.parseDouble(createOrderProductDetailsList.get(i).getQty()) * 1000) + " " + "gm");
                    } else {
                        if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("no") || createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("lb")) {
                            textQuantity.setText((Double.parseDouble(createOrderProductDetailsList.get(i).getQty())) + " " + createOrderProductDetailsList.get(i).getUom());
                        } else {
                            textQuantity.setText(String.format("%.2f", Double.parseDouble(createOrderProductDetailsList.get(i).getQty())) + " " + createOrderProductDetailsList.get(i).getUom());
                        }
                    }
                } else {
                    if (Double.parseDouble(createOrderProductDetailsList.get(i).getQty()) >= 1000) {
                        textQuantity.setText(String.format("%.2f", Double.parseDouble(createOrderProductDetailsList.get(i).getQty()) / 1000) + " " + "kg");
                    } else {
                        textQuantity.setText(String.format("%.2f", Double.parseDouble(createOrderProductDetailsList.get(i).getQty())) + " " + createOrderProductDetailsList.get(i).getUom());
                    }
                }
            }
            if (createOrderProductDetailsList.get(i).getOrderprice() != null && createOrderProductDetailsList.get(i).getQty() != null) {
                textPrice.setText(String.format("%.2f", (Double.parseDouble(createOrderProductDetailsList.get(i).getOrderprice()) / Double.parseDouble(createOrderProductDetailsList.get(i).getQty())) - (Cart.configurationPrice(createOrderProductDetailsList.get(i)) / Double.parseDouble(createOrderProductDetailsList.get(i).getQty()))));
            }


//                message.setText(createOrderProductDetailsList.get(i).getMessageonproduct());
            else {
                ll_special_message.setVisibility(View.GONE);
                view_special_message.setVisibility(View.GONE);
            }


            if (createOrderProductDetailsList.get(i).getUom() != null) {
                if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("Kg")) {
                    textSubTotal.setText(String.format("%.2f", (Double.parseDouble(createOrderProductDetailsList.get(i).getOrderprice())) - Cart.configurationPrice(createOrderProductDetailsList.get(i))));
                } else if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("lb")) {
                    textSubTotal.setText(String.format("%.2f", (Double.parseDouble(createOrderProductDetailsList.get(i).getOrderprice())) - Cart.configurationPrice(createOrderProductDetailsList.get(i))));
                } else if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("Gm")) {
                    textSubTotal.setText(String.format("%.2f", (Double.parseDouble(createOrderProductDetailsList.get(i).getOrderprice())) - Cart.configurationPrice(createOrderProductDetailsList.get(i))));
                } else if (createOrderProductDetailsList.get(i).getUom().equalsIgnoreCase("No")) {
                    textSubTotal.setText(String.format("%.2f", (Double.parseDouble(createOrderProductDetailsList.get(i).getOrderprice())) - Cart.configurationPrice(createOrderProductDetailsList.get(i))));
                }
            }

            ll_special_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("PopUp", "OrderDetails");
                    new PopUpForConfigurationCharges(context, v, Cart.configurationList((CreateOrderProductDetails) v.getTag()));
                }
            });

            ll_special_message.setTag(createOrderProductDetailsList.get(i));
            AdapterForMultipleProviders.ll.addView(llProductList);
        }
    }
}
