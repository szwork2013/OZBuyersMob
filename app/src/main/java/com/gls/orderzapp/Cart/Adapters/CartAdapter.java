package com.gls.orderzapp.Cart.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.Cart.Beans.BranchIdsForGettingDeliveryCharges;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SuccessResponseForDeliveryChargesAndType;
import com.gls.orderzapp.MainApp.CartActivity;
import com.gls.orderzapp.MainApp.DeliveryPaymentActivity;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

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
    String providerName = "", branchId = "";;
    TextView sub_total;
    List<ProductDetails> SortedProviderList = new ArrayList<>();
    BranchIdsForGettingDeliveryCharges branchIdsForGettingDeliveryCharges;
    SuccessResponseForDeliveryChargesAndType successResponseForDeliveryCharges;
    ArrayList<String> branchIdforDelivery = new ArrayList<>();

    public CartAdapter(Context context) {
        this.context = context;

        mKeys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);
        mValues = Cart.hm.values().toArray(new ProductDetails[Cart.hm.size()]);
        productList = new ArrayList<>(Arrays.asList(mValues));

        Collections.sort(productList, new CustomComparator());

        successResponseForDeliveryCharges = new SuccessResponseForDeliveryChargesAndType();
        branchIdsForGettingDeliveryCharges = new BranchIdsForGettingDeliveryCharges();

        dataForGetDeliveryCharges();
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

                if(successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size() > 0) {
                    for (int j = 0; j < successResponseForDeliveryCharges.getSuccess().getDeliverycharge().size(); j++) {
                        if(successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid() != null) {
                            if (successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).getBranchid().equalsIgnoreCase(branchid)) {
                                if (successResponseForDeliveryCharges.getSuccess().getDeliverycharge().get(j).isDelivery() == true) {
                                    delivery_type.setText("Delivery available");
                                } else {
                                    delivery_type.setText("Delivery NOT available");
                                }
                            }
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

    public String loadCityPreference(){
        String userCity = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
        userCity = spLoad.getString("USER_CITY", "Pune");
        return userCity;
    }

    public String loadAreaPreference(){
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
        userArea = spLoad.getString("USER_AREA", "Shivaji nagar");
        return userArea;
    }

    public class CustomComparator implements Comparator<ProductDetails> {
        @Override
        public int compare(ProductDetails o1, ProductDetails o2) {
            return o1.getBranchid().compareTo(o2.getBranchid());
        }
    }

    public void setBranchId() {
        try {

            branchIdsForGettingDeliveryCharges.setBranchids(branchIdforDelivery);
                CartActivity.area_text.setText(loadAreaPreference());
                branchIdsForGettingDeliveryCharges.setArea(loadAreaPreference());
                branchIdsForGettingDeliveryCharges.setCity(loadCityPreference());

                new GetDeliveryChargesAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeliverCharges() {
        String resultOfDeliveryCharges = "";
        String jsonToSendOverServer = "";
        try {

            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(branchIdsForGettingDeliveryCharges);
            Log.d("jsonToSendOverServer", jsonToSendOverServer);
            resultOfDeliveryCharges = ServerConnection.executePost1(jsonToSendOverServer, "/api/deliverycharge");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfDeliveryCharges;
    }

    public void dataForGetDeliveryCharges() {

        for (int i = 0; i < Cart.hm.size(); i++) {

            branchId = productList.get(i).getBranchid();

            if (!branchIdforDelivery.contains(branchId)) {
                branchIdforDelivery.add(branchId);
                SortedProviderList.add(productList.get(i));
            }
        }
        try {
            setBranchId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class GetDeliveryChargesAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String connectedOrNot, resultOfGetDeliveryCharges, msg, code;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            context = (CartActivity)context;
            progressDialog = ProgressDialog.show(context, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(context).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultOfGetDeliveryCharges = getDeliverCharges();
                    if (!resultOfGetDeliveryCharges.isEmpty()) {
                        Log.d("resultOfGetDeliveryCharges", resultOfGetDeliveryCharges);
                        jObj = new JSONObject(resultOfGetDeliveryCharges);
                        if (jObj.has("success")) {
                            successResponseForDeliveryCharges = new Gson().fromJson(resultOfGetDeliveryCharges, SuccessResponseForDeliveryChargesAndType.class);
                            Log.d("Successresponse for delivery charges", resultOfGetDeliveryCharges);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                            code = jObjError.getString("code");

                        }
                    }
                } else {
                    connectedOrNot = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            progressDialog.dismiss();
            try {
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultOfGetDeliveryCharges.isEmpty()) {
                        if (jObj.has("success")) {
                            getCartView();
                        } else {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Server is not responding please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}