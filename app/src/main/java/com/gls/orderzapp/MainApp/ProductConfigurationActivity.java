package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gls.orderzapp.ProductConfiguration.Adapter.ProductConfigurationListAdapter;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 3/7/14.
 */
public class ProductConfigurationActivity extends Activity {
    public static ListView product_configuration_list;
    Context context;
    final int SIGN_IN = 0;
    Button add_configuration;
    public List<ProductDetails> cakeproductDetailes = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_configuration);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        findViewsById();
        setConfiguration();

        ProductConfigurationListAdapter productConfigurationListAdapter = new ProductConfigurationListAdapter(getApplicationContext(), cakeproductDetailes);
        product_configuration_list.setAdapter(productConfigurationListAdapter);
        setListViewHeightBasedOnChildren(product_configuration_list);

        add_configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("hm after configuration", new Gson().toJson(Cart.hm));
//                Cart.deleteConfigObject();
//                Log.d("hm after deletconfiguration", new Gson().toJson(Cart.hm));
                new CheckSessionAsync().execute();
            }
        });
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

    public void findViewsById() {
        product_configuration_list = (ListView) findViewById(R.id.product_configuration_list);
        add_configuration = (Button) findViewById(R.id.add_configuration);
    }

    void setConfiguration() {
        try {
            String[] keys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);
            final int keySize = keys.length;
            cakeproductDetailes.clear();
            for (int i = 0; i < keySize; i++) {
                if (Cart.hm.get(keys[i]).getProductconfiguration().getConfiguration().size() > 0) {

                    cakeproductDetailes.add(Cart.hm.get(keys[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSessionStatus() throws Exception {
        String resultOfCheckSession = "";
        try {
            resultOfCheckSession = ServerConnection.executeGet(getApplicationContext(), "/api/isloggedin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfCheckSession;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN:
                if (resultCode == RESULT_OK) {
                    Intent deliveryPayment = new Intent(ProductConfigurationActivity.this, DeliveryPaymentActivity.class);
                    startActivity(deliveryPayment);
                    finish();

                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;
        }
    }

    public class CheckSessionAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, msg, code, resultOfCheckSession;
        public JSONObject jObj;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProductConfigurationActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultOfCheckSession = getSessionStatus();
                    if (!resultOfCheckSession.isEmpty()) {
                        Log.d("check session", resultOfCheckSession);
                        jObj = new JSONObject(resultOfCheckSession);

                        if (jObj.has("success")) {
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
                            Log.d("Login success", "In doinbck");
                        } else {
                            Log.d("Login not success", "In doinbck");
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
                            Intent deliveryPayment = new Intent(ProductConfigurationActivity.this, DeliveryPaymentActivity.class);
                            startActivity(deliveryPayment);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {
                                Intent goToSignin = new Intent(ProductConfigurationActivity.this, SignInActivity.class);
                                startActivityForResult(goToSignin, SIGN_IN);
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


    public static void setListViewHeightBasedOnChildren(ListView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new LinearLayout.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = (totalHeight + (gridView.getDividerHeight() * listAdapter.getCount()));
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }


}
