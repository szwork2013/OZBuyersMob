package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Adapter.AdapterForSelectaddressList;
import com.gls.orderzapp.AddressDetails.Bean.DeliveryAddressSuccessResponse;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by avinash on 2/7/14.
 */
public class SelectAddressListActivity extends Activity {
    ListView lst_address_list;
    DeliveryAddressSuccessResponse deliveryAddressSuccessResponse;
    Context context;
    public static boolean isAddNewaddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectaddressactivitylist_layout);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        context = SelectAddressListActivity.this;
        isAddNewaddress = false;
        findViewsById();
        new GetAddressListAsync().execute();

    }

    public void findViewsById() {
        lst_address_list = (ListView) findViewById(R.id.lst_address_list);
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

    public String getGetAddressList(String param) {
        String resultGedAddressList = "";
        try {
            resultGedAddressList = ServerConnection.executeGet(getApplicationContext(), "/api/mydeliveryaddresses/" + param);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultGedAddressList;
    }


    class GetAddressListAsync extends AsyncTask<String, Integer, String> {
        String resultGetAddressList, connectedOrNot, msg;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(SelectAddressListActivity.this, "", "Getting Address...");
//            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetAddressList = getGetAddressList(DeliveryPaymentActivity.user_id);
                    if (!resultGetAddressList.isEmpty()) {
                        Log.d("search result", new Gson().toJson(resultGetAddressList));
                        jObj = new JSONObject(resultGetAddressList);
                        if (jObj.has("success")) {
                            deliveryAddressSuccessResponse = new Gson().fromJson(resultGetAddressList, DeliveryAddressSuccessResponse.class);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
//                            code = jObjError.getString("code");
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
//                progressDialog.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (!resultGetAddressList.isEmpty()) {
                        if (jObj.has("success")) {
                            lst_address_list.setAdapter(new AdapterForSelectaddressList(context, deliveryAddressSuccessResponse.getSuccess().getDeliveryaddresses()));
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
