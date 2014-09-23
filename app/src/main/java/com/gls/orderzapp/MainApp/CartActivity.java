package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CartAdapter;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ResetStaticData;
import com.gls.orderzapp.Utility.ServerConnection;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by prajyot on 10/7/14.
 */
public class CartActivity extends Activity {

    public static LinearLayout llCartList;
    public static TextView area_text, grand_total;
    public static String date = "";
    static Context context;
    final int SIGN_IN = 0;
    public Button delivery_date;
    LinearLayout ll_products;
    LinearLayout ll_noproducts;
    Button place_an_order_button;
    Calendar c;
    int mYear, mMonth, mDay, yy, mm, dd;//, hh, min, cHH, cMin, cAm_Pm;
    DatePicker datePicker;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        date = "";
        context = CartActivity.this;
        findViewsById();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Cart.deleteFromCartIfQuantityIsZero();
        date = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            displayCart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findViewsById() {
        ll_products = (LinearLayout) findViewById(R.id.ll_products);
        ll_noproducts = (LinearLayout) findViewById(R.id.ll_noproducts);
        llCartList = (LinearLayout) findViewById(R.id.llCartList);
        place_an_order_button = (Button) findViewById(R.id.place_an_order_button);
        delivery_date = (Button) findViewById(R.id.btn_pick_date);
        area_text = (TextView) findViewById(R.id.area_text);
        grand_total = (TextView) findViewById(R.id.grand_total);
    }

    public void selectDeliveryDate(View view) {
        LayoutInflater li = LayoutInflater.from(CartActivity.this);
        View dialogView = li.inflate(R.layout.calendar_view_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
//        cHH = c.get(Calendar.HOUR_OF_DAY);
//        cMin = c.get(Calendar.MINUTE);
//        cAm_Pm = c.get(Calendar.AM_PM);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(dialogView);


        final Button select_date;
        // create alert dialog
        datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        select_date = (Button) dialogView.findViewById(R.id.select_date);

        if (!delivery_date.getText().toString().trim().isEmpty()) {
            String date = delivery_date.getText().toString().trim().split(" ")[0];
            int year = Integer.parseInt(date.split("-")[0]);
            int month = Integer.parseInt(date.split("-")[1]);
            int day = Integer.parseInt(date.split("-")[2]);
            datePicker.updateDate(year, month - 1, day);
        }
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yy = datePicker.getYear();
                mm = datePicker.getMonth();
                dd = datePicker.getDayOfMonth();
//                hh = timePicker.getCurrentHour();
//                min = timePicker.getCurrentMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, yy);
                calendar.set(Calendar.MONTH, mm);
                calendar.set(Calendar.DAY_OF_MONTH, dd);
//                calendar.set(Calendar.HOUR_OF_DAY, hh);
//                calendar.set(Calendar.MINUTE, min);
                Date selectedDate = calendar.getTime();
                Date TodaysDate = c.getTime();
                if (selectedDate.before(TodaysDate)) {
                    Toast.makeText(getApplicationContext(), "Please select a valid date and time", Toast.LENGTH_LONG).show();

                } else {

                    updateTime(yy, mm, dd);
                    displayCart();
//                    getProductIds();
//                    new GetDeliveryTimeSlotsAsync().execute();
                }
            }
        });

        alertDialog = alertDialogBuilder.create();

        alertDialog.setInverseBackgroundForced(true);

        // show it
        alertDialog.show();

    }

    // Used to convert 24hr format to 12hr format with
    public void updateTime(int yy, int mm, int dd) {

        date = new StringBuilder().append(yy).append('-').append(mm + 1).append('-').append(dd).toString();

        //dialog.cancel();
        delivery_date.setText(date);
        alertDialog.dismiss();
    }

    public void selectArea(View view) {
        Intent goToSelectAreaActivity = new Intent(CartActivity.this, AreaListActivity.class);
        startActivity(goToSelectAreaActivity);
    }

    public void displayCart() {
        llCartList.removeAllViews();
        if (Cart.getCount() > 0) {
            ll_noproducts.setVisibility(View.GONE);
            ll_products.setVisibility(View.VISIBLE);
            try {
//                Cart.deleteFromCartIfQuantityIsZero();
                new CartAdapter(context);
                grand_total.setText(Cart.subTotal() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            Cart.hm.clear();
            ll_noproducts.setVisibility(View.VISIBLE);
            ll_products.setVisibility(View.GONE);
        }
    }

    public void placeAnOrder(View view) {
        if (delivery_date.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please select your expected delivery date", Toast.LENGTH_LONG).show();
            return;
        }
        if (Cart.deleteFromCartIfQuantityIsZero() > 0) {
            if (Cart.checkForPrductConfigurarion() == true) {
                Intent product_config = new Intent(CartActivity.this, ProductConfigurationActivity.class);
                startActivity(product_config);
            } else {
                new CheckSessionAsync().execute();
            }
        } else {
            displayCart();
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
        public JSONObject jObj;
        String connectedOrNot, msg, code, resultOfCheckSession;
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
//                            finish();
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