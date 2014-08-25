package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Adapter.AdapterForPickUpAddressList;
import com.gls.orderzapp.AddressDetails.Bean.PickUpAddressResponse;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by avinash on 18/7/14.
 */
public class SelectPickUpAddressActivity extends Activity {
    Context context;
    ListView lst_address_list;
    PickUpAddressResponse pickUpAddressSuccessResponse;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectaddressactivitylist_layout);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        context = SelectPickUpAddressActivity.this;
        bundle = getIntent().getExtras();
        findViewsById();

        new GetPickUpAddressListAsync().execute();
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

    public String getGetPickUpAddressList(String param) {
        String resultGedAddressList = "";
        try {
            resultGedAddressList = ServerConnection.executeGet(getApplicationContext(), "/api/pickupaddress/" + param);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultGedAddressList;
    }


    class GetPickUpAddressListAsync extends AsyncTask<String, Integer, String> {
        String resultGetPickUpAddressList, connectedOrNot, msg;
        String provider_pos;
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
                    Log.d("Provider id in bundle", bundle.getString("providerid"));
                    resultGetPickUpAddressList = getGetPickUpAddressList(bundle.getString("providerid"));
                    if (!resultGetPickUpAddressList.isEmpty()) {
                        Log.d("search result", new Gson().toJson(resultGetPickUpAddressList));
                        jObj = new JSONObject(resultGetPickUpAddressList);
                        if (jObj.has("success")) {
                            pickUpAddressSuccessResponse = new Gson().fromJson(resultGetPickUpAddressList, PickUpAddressResponse.class);
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
                    if (!resultGetPickUpAddressList.isEmpty()) {
                        if (jObj.has("success")) {
                            lst_address_list.setAdapter(new AdapterForPickUpAddressList(context, pickUpAddressSuccessResponse.getSuccess().getAddresses(), bundle.getString("providerid"), bundle.getString("branchid"), bundle.getString("pickupbuttontag")));
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
