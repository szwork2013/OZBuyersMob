package com.gls.orderzapp.Cart.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.CartActivity;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prajyot on 10/7/14.
 */
public class CartAdapter {
    Context context;
    String[] mKeys;
    ProductDetails[] mValues;
    public static LinearLayout llCartListItemView, llProductList;
    List<ProductDetails> productList;
    List<String> branchids = new ArrayList<>();
    List<ProductDetails> listProducts = new ArrayList<>();
    String providerName = "";
    TextView sub_total;

    public CartAdapter(Context context) {
        this.context = context;

        mKeys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);
        mValues = Cart.hm.values().toArray(new ProductDetails[Cart.hm.size()]);
        productList = new ArrayList<>(Arrays.asList(mValues));

        Collections.sort(productList, new CustomComparator());
    }

    public void getCartView() {
        for (int i = 0; i < Cart.hm.size(); i++) {
            String branchid = productList.get(i).getBranchid();
            Log.d("branchid", branchid + "");
            providerName = productList.get(i).getProviderName();

            if (branchids.contains(branchid)) {

                listProducts.add(productList.get(i));

            } else {
                if (i > 0) {

                    new ProductListAdapter(context, listProducts).getProductView();
                    sub_total.setText(Cart.providerSubTotalInCart(listProducts)+"");

                }

                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                llCartListItemView = (LinearLayout) li.inflate(R.layout.cart_list_items, null);
                llProductList = (LinearLayout) llCartListItemView.findViewById(R.id.llProductList);
                TextView textProviderName = (TextView) llCartListItemView.findViewById(R.id.textProviderName);
                TextView delivery_type = (TextView) llCartListItemView.findViewById(R.id.delivery_type);
                sub_total = (TextView) llCartListItemView.findViewById(R.id.sub_total);

                branchids.add(branchid);
                listProducts.clear();
                listProducts.add(productList.get(i));

                if (productList.get(i).getProviderName() != null) {

                    textProviderName.setText(productList.get(i).getProviderName());

                }

                if (productList.get(i).getDelivery() != null) {
                    if (productList.get(i).getDelivery().getIsprovidehomedelivery() != null) {
                        if (productList.get(i).getDelivery().getIsprovidehomedelivery() == true) {
                            delivery_type.setText("Home delivery available");
                        } else {
                            delivery_type.setText("Only pick up available");
                        }
                    }
                }

                CartActivity.llCartList.addView(llCartListItemView);
            }

            if (i == productList.size() - 1) {
                new ProductListAdapter(context, listProducts).getProductView();
                sub_total.setText(Cart.providerSubTotalInCart(listProducts)+"");
            }

        }
    }

    public class CustomComparator implements Comparator<ProductDetails> {
        @Override
        public int compare(ProductDetails o1, ProductDetails o2) {
            return o1.getBranchid().compareTo(o2.getBranchid());
        }
    }
}