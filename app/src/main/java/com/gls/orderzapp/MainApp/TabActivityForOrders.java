package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;

import org.json.JSONObject;

/**
 * Created by prajyot on 10/6/14.
 */
public class TabActivityForOrders extends TabActivity {
    TabHost tabHost;
    static boolean isload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_orders);
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
        if (isload == false) {
            isload = true;
            new CheckSessionAsync().execute();
        } else {
            isload = false;
            if (SignInActivity.islogedin == true) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                finish();
            }
        }
    }

    public String getSessionStatus() throws Exception {
        String resultOfCheckSession = "";

        resultOfCheckSession = ServerConnection.executeGet(getApplicationContext(), "/api/isloggedin");

        return resultOfCheckSession;
    }

    private class CheckSessionAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, msg, code, resultOfCheckSession;
        JSONObject jObj;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TabActivityForOrders.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultOfCheckSession = getSessionStatus();
                    if (!resultOfCheckSession.isEmpty()) {
                        jObj = new JSONObject(resultOfCheckSession);

                        if (jObj.has("success")) {
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
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
                    if (!resultOfCheckSession.isEmpty()) {
                        if (jObj.has("success")) {

                            tabHost = getTabHost();

                            TabHost.TabSpec currentOrdersSpec = tabHost.newTabSpec("Current");
                            currentOrdersSpec.setIndicator("Current", getResources().getDrawable(R.drawable.ic_launcher));
                            Intent currentIntent = new Intent(TabActivityForOrders.this, CurrentOrdersActivity.class);
                            currentOrdersSpec.setContent(currentIntent);
                            tabHost.addTab(currentOrdersSpec);

                            TabHost.TabSpec pastOrdersSpec = tabHost.newTabSpec("Past");
                            pastOrdersSpec.setIndicator("Past", getResources().getDrawable(R.drawable.ic_launcher));
                            Intent pastIntent = new Intent(TabActivityForOrders.this, PastOrdersActivity.class);
                            pastOrdersSpec.setContent(pastIntent);
                            tabHost.addTab(pastOrdersSpec);
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {
                                Intent goToSignin = new Intent(TabActivityForOrders.this, SignInActivity.class);
                                startActivity(goToSignin);
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
