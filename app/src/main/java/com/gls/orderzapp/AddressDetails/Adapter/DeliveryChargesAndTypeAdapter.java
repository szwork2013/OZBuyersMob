package com.gls.orderzapp.AddressDetails.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Beans.BranchIdsForGettingDeliveryCharges;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SuccessResponseForDeliveryChargesAndType;
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
 * Created by avinash on 11/7/14.
 */
public class DeliveryChargesAndTypeAdapter {
    public static LinearLayout llDeliveryChargeAndType;
    public static BranchIdsForGettingDeliveryCharges branchIdsForGettingDeliveryCharges;
    public static SuccessResponseForDeliveryChargesAndType successResponseForDeliveryCharges;
    public static SuccessResponseOfUser successResponseOfUserDeliveryAddresDetails;
    Context context;
    ProductDetails[] checkForDeliveryModeValues;
    List<ProductDetails> checkForDeliveryModeList;
    List<ProductDetails> SortedProviderList = new ArrayList<>();
    String branchId = "";
    ArrayList<String> branchIdforDelivery = new ArrayList<>();

    public DeliveryChargesAndTypeAdapter(Context context) {
        this.context = context;
        checkForDeliveryModeValues = Cart.hm.values().toArray(new ProductDetails[Cart.hm.size()]);
        checkForDeliveryModeList = new ArrayList<>(Arrays.asList(checkForDeliveryModeValues));
        Collections.sort(checkForDeliveryModeList, new DeliveryModeComparator());
        successResponseForDeliveryCharges = null;
        branchIdsForGettingDeliveryCharges = null;
        successResponseForDeliveryCharges = new SuccessResponseForDeliveryChargesAndType();
        branchIdsForGettingDeliveryCharges = new BranchIdsForGettingDeliveryCharges();
        getDeliveryCharges();

    }

    public String loadPreferencesUserDataForDeliveryAddress() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
            user = spLoad.getString("USER_DATA_DELIVERY_ADDRESS", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setBranchId(String getUserData) {
        try {
            successResponseOfUserDeliveryAddresDetails = null;
            successResponseOfUserDeliveryAddresDetails = new Gson().fromJson(getUserData, SuccessResponseOfUser.class);
            branchIdsForGettingDeliveryCharges.setBranchids(branchIdforDelivery);
            if (successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getArea() != null
                    && successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCity() != null) {
                branchIdsForGettingDeliveryCharges.setArea(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getArea());
                branchIdsForGettingDeliveryCharges.setCity(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCity());
            }

            new GetDeliveryChargesAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDeliveryCharges() {

        for (int i = 0; i < Cart.hm.size(); i++) {

            branchId = checkForDeliveryModeList.get(i).getBranchid();

            if (!branchIdforDelivery.contains(branchId)) {
                branchIdforDelivery.add(branchId);
                SortedProviderList.add(checkForDeliveryModeList.get(i));
            }
        }
        try {
            setBranchId(loadPreferencesUserDataForDeliveryAddress());
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
            Log.d("jsonToSendOverServerBranchIds", jsonToSendOverServer);
            resultOfDeliveryCharges = ServerConnection.executePost1(jsonToSendOverServer, "/api/deliverycharge");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfDeliveryCharges;
    }

    private class DeliveryModeComparator implements Comparator<ProductDetails> {
        @Override
        public int compare(ProductDetails o1, ProductDetails o2) {
            return o1.getBranchid().compareTo(o2.getBranchid());
        }
    }

    public class GetDeliveryChargesAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String connectedOrNot, resultOfGetDeliveryCharges, msg, code;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
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
//            progressDialog.dismiss();
            try {
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultOfGetDeliveryCharges.isEmpty()) {
                        if (jObj.has("success")) {
                            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            llDeliveryChargeAndType = (LinearLayout) li.inflate(R.layout.delivery_charge_type, null);
//                            new DisplayDeliveryChargesAndType(context, successResponseForDeliveryCharges, checkForDeliveryModeList);
//                            DisplayDeliveryChargesAndType.deliveryType.clear();
                            new DisplayDeliveryChargesAndType(context, successResponseForDeliveryCharges, SortedProviderList);
                            DeliveryPaymentActivity.ll_deliver_charge_type.removeAllViews();
                            DeliveryPaymentActivity.ll_deliver_charge_type.addView(llDeliveryChargeAndType);
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
