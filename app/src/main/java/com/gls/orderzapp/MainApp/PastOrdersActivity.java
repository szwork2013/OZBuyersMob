package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gls.orderzapp.MyOrders.Beans.SuccessResponseForMyOrders;
import com.gls.orderzapp.MyOrders.MyOrdersListAdapters.MainOrderListAdapter;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by prajyot on 3/6/14.
 */
public class PastOrdersActivity extends MyOrdersListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetMyPastOrdersAsync().execute();
    }

    class GetMyPastOrdersAsync extends AsyncTask<String, Integer, String> {
        String resultOfMyOrders, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PastOrdersActivity.this, "", "Retrieving Orders");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultOfMyOrders = getMyOrders("past");
                    if (!resultOfMyOrders.isEmpty()) {
                        jObj = new JSONObject(resultOfMyOrders);
                        if (jObj.has("success")) {
                            successResponseForMyOrders = new Gson().fromJson(resultOfMyOrders, SuccessResponseForMyOrders.class);
                            msg = successResponseForMyOrders.getSuccess().getMessage();
                            actualList.clear();
                            serverTrackingStatus.clear();

                            for (int i = 0; i < successResponseForMyOrders.getSuccess().getOrders().size(); i++) {
                                actualList.add(new ArrayList<String>());
                                serverTrackingStatus.add(new ArrayList<ArrayList<String>>());
                                for (int j = 0; j < successResponseForMyOrders.getSuccess().getOrders().get(i).getSuborder().size(); j++) {
                                    actualList.get(i).add(successResponseForMyOrders.getSuccess().getOrders().get(i).getSuborder().get(j).getStatus());
                                    serverTrackingStatus.get(i).add(new ArrayList<String>());
                                    for (int k = 0; k < successResponseForMyOrders.getSuccess().getOrders().get(i).getSuborder().get(j).getTracking().size(); k++) {
                                        serverTrackingStatus.get(i).get(j).add(successResponseForMyOrders.getSuccess().getOrders().get(i).getSuborder().get(j).getTracking().get(k).getStatus());
                                    }
                                }
                            }
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
            try {
                progressDialog.dismiss();
                if (connectedOrNot.equals("success")) {
//                llMyOrderList.removeAllViews();
                    if (!resultOfMyOrders.isEmpty()) {
                        if (jObj.has("success")) {
                            orderExists();
                            Intent mainOrderStatus = new Intent();
                            mainOrderStatus.putExtra("MainOrderStatus", "PastOrder");
                            mainOrderList.setAdapter(new MainOrderListAdapter(context, successResponseForMyOrders.getSuccess().getOrders()));
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("SODR001")) {
                                noOrder(msg);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server is not responding please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
