package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CartAdapter;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;

import org.json.JSONObject;

/**
 * Created by prajyot on 10/7/14.
 */
public class CartActivity extends Activity {

    public static LinearLayout llCartList;
    LinearLayout ll_products;
    LinearLayout ll_noproducts;
    static Context context;
    Button place_an_order_button;
    public static TextView area_text, grand_total;
    final int SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        context = CartActivity.this;
        findViewsById();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            displayCart();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void findViewsById() {
        ll_products=(LinearLayout) findViewById(R.id.ll_products);
        ll_noproducts=(LinearLayout) findViewById(R.id.ll_noproducts);
        llCartList = (LinearLayout) findViewById(R.id.llCartList);
        place_an_order_button = (Button) findViewById(R.id.place_an_order_button);
        area_text = (TextView) findViewById(R.id.area_text);
        grand_total = (TextView) findViewById(R.id.grand_total);
    }

    public void selectArea(View view){
        Intent goToSelectAreaActivity = new Intent(CartActivity.this, CityAreaDetailsActivity.class);
        startActivity(goToSelectAreaActivity);
    }

    public void displayCart() {
        llCartList.removeAllViews();
        if (Cart.getCount() > 0) {
            ll_noproducts.setVisibility(View.GONE);
            ll_products.setVisibility(View.VISIBLE);
            try {
//                area_text.setText(loadPreferences());
                new CartAdapter(context);
                grand_total.setText(Cart.subTotal() + "");
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
ll_noproducts.setVisibility(View.VISIBLE);
            ll_products.setVisibility(View.GONE);
        }
    }

    public void placeAnOrder(View view) {
        if (Cart.checkForPrductConfigurarion() == true) {
            Intent product_config = new Intent(CartActivity.this, ProductConfigurationActivity.class);
            startActivity(product_config);
        } else if (Cart.deleteFromCartIfQuantityIsZero() > 0) {
            if (Cart.deleteFromCartIfQuantityIsZero() > 0) {
                new CheckSessionAsync().execute();
//                Intent deliveryActivity = new Intent(CartActivity.this, DeliveryPaymentActivity.class);
//                startActivity(deliveryActivity);
            } else {
                displayCart();
            }
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
                    Intent deliveryPayment = new Intent(CartActivity.this, DeliveryPaymentActivity.class);
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
            progressDialog = ProgressDialog.show(CartActivity.this, "", "");
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
                            Intent deliveryActivity = new Intent(CartActivity.this, DeliveryPaymentActivity.class);
                            startActivity(deliveryActivity);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {
                                Intent goToSignin = new Intent(CartActivity.this, SignInActivity.class);
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
}