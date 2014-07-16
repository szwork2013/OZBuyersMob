package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderAddressDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by avinash on 10/4/14.
 */
public class DeliveryAddressActivity extends Activity {
    static boolean isload = false, date_selected = false;
    int yy, mm, dd, mYear, mMonth, mDay;
    String date;
    DatePicker datePicker;
    TextView textBillingAddress, textShippingAddress;
    EditText editBillingAddress1, editBillingAddress2, editBillingArea, editBillingCity, editBillingZipcode, editBillingCountry,
            editShippingAddress1, editShippingAddress2, editShippingArea, editShippingCity, editShippingZipcode, editShippingCountry,
            edittext_shipping_state, edittext_billing_state;
    Button btn_pick_date;
    RadioGroup shippingDetailsGroup;
    RadioButton sameAddress, differentAddress, pickUp;
    LinearLayout llShippingAddress;
    SuccessResponseOfUser successResponseOfUser;
    String deliveryType;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliveryaddress_layout);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        findViewsById();
        deliveryType = "same";
        shippingDetailsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.other_than_billing) {
                    llShippingAddress.setVisibility(View.VISIBLE);
                    deliveryType = "other";
                } else if (checkedId == R.id.pick_up) {
                    llShippingAddress.setVisibility(View.GONE);
                    deliveryType = "pick";
                } else {
                    llShippingAddress.setVisibility(View.GONE);
                    deliveryType = "same";
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isload == false) {
                isload = true;
                new CheckSessionAsync().execute();
            } else {
                isload = false;
                if (SignInActivity.islogedin == true) {
                    setBillingAddress();
                    if (date_selected == true) {
                        btn_pick_date.setText(date);
                    }

                } else {
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void proceedToPay(View view) {
        try {
            if (date_selected == true) {
                if (deliveryType.equals("same")) {
                    sameShippingAddress();
                } else if (deliveryType.equals("other")) {
                    differentShippingAddress();
                } else if (deliveryType.equals("pick")) {
                    pickUp();
                }
//                date_selected = false;
            } else {
                Toast.makeText(getApplicationContext(), "Please Select Valid Date", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pickDate(View view) {

        LayoutInflater li = LayoutInflater.from(DeliveryAddressActivity.this);
        View dialogView = li.inflate(R.layout.calendar_view_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryAddressActivity.this);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(dialogView);

        // create alert dialog
        datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

        alertDialogBuilder
                .setMessage("Select Expected Delivery Date!")
                .setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        yy = datePicker.getYear();
                        mm = datePicker.getMonth();
                        dd = datePicker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, yy);
                        calendar.set(Calendar.MONTH, mm);
                        calendar.set(Calendar.DAY_OF_MONTH, dd);

                        Date selectedDate = calendar.getTime();
                        Date TodaysDate = c.getTime();
                        Log.d("selected date", new Gson().toJson(selectedDate));
                        Log.d("current date", new Gson().toJson(TodaysDate));
                        if (selectedDate.before(TodaysDate)) {
                            Toast.makeText(getApplicationContext(), "Please select a valid date", Toast.LENGTH_LONG).show();
                        } else {
                            date = yy + "-" + (mm + 1) + "-" + dd;
                            dialog.cancel();
                            btn_pick_date.setText(date);
                            date_selected = true;
                        }
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setInverseBackgroundForced(true);
        // show it
        alertDialog.show();

    }

    public void sameShippingAddress() throws Exception {

        if (editBillingAddress1.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingAddress2.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingArea.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingCity.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingZipcode.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_billing_state.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing state", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingCountry.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing country", Toast.LENGTH_LONG).show();
            return;
        }
        CreateOrderAddressDetails createOrderBillingAddressDetails = new CreateOrderAddressDetails();
        CreateOrderAddressDetails createOrderShippingAddressDetails = new CreateOrderAddressDetails();
        createOrderBillingAddressDetails.setDate(date);
        createOrderBillingAddressDetails.setAddress1(editBillingAddress1.getText().toString().trim());
        createOrderBillingAddressDetails.setAddress2(editBillingAddress2.getText().toString().trim());
        createOrderBillingAddressDetails.setArea(editBillingArea.getText().toString().trim());
        createOrderBillingAddressDetails.setCity(editBillingCity.getText().toString().trim());
        createOrderBillingAddressDetails.setZipcode(editBillingZipcode.getText().toString().trim());
        createOrderBillingAddressDetails.setState(edittext_billing_state.getText().toString().trim());
        createOrderBillingAddressDetails.setCountry(editBillingCountry.getText().toString().trim());
        createOrderShippingAddressDetails.setAddress1(editBillingAddress1.getText().toString().trim());
        createOrderShippingAddressDetails.setAddress2(editBillingAddress2.getText().toString().trim());
        createOrderShippingAddressDetails.setArea(editBillingArea.getText().toString().trim());
        createOrderShippingAddressDetails.setCity(editBillingCity.getText().toString().trim());
        createOrderShippingAddressDetails.setZipcode(editBillingZipcode.getText().toString().trim());
        createOrderShippingAddressDetails.setState(edittext_billing_state.getText().toString().trim());
        createOrderShippingAddressDetails.setCountry(editBillingCountry.getText().toString().trim());


        Intent goToOrderDetailsActivity = new Intent(DeliveryAddressActivity.this, OrderDetailsActivity.class);
        goToOrderDetailsActivity.putExtra("BILLING_ADDRESS", new Gson().toJson(createOrderBillingAddressDetails));
        goToOrderDetailsActivity.putExtra("SHIPPING_ADDRESS", new Gson().toJson(createOrderShippingAddressDetails));
        goToOrderDetailsActivity.putExtra("DELIVERY_TYPE", deliveryType);
        startActivity(goToOrderDetailsActivity);
    }

    public void pickUp() throws Exception {

        if (editBillingAddress1.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingAddress2.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingArea.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingCity.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingZipcode.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_billing_state.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing state", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingCountry.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing country", Toast.LENGTH_LONG).show();
            return;
        }

        CreateOrderAddressDetails createOrderBillingAddressDetails = new CreateOrderAddressDetails();
        CreateOrderAddressDetails createOrderShippingAddressDetails = new CreateOrderAddressDetails();
        createOrderBillingAddressDetails.setDate(date);
        createOrderBillingAddressDetails.setAddress1(editBillingAddress1.getText().toString().trim());
        createOrderBillingAddressDetails.setAddress2(editBillingAddress2.getText().toString().trim());
        createOrderBillingAddressDetails.setArea(editBillingArea.getText().toString().trim());
        createOrderBillingAddressDetails.setCity(editBillingCity.getText().toString().trim());
        createOrderBillingAddressDetails.setZipcode(editBillingZipcode.getText().toString().trim());
        createOrderBillingAddressDetails.setState(edittext_billing_state.getText().toString().trim());
        createOrderBillingAddressDetails.setCountry(editBillingCountry.getText().toString().trim());

        createOrderShippingAddressDetails.setAddress1("");
        createOrderShippingAddressDetails.setAddress2("");
        createOrderShippingAddressDetails.setArea("");
        createOrderShippingAddressDetails.setCity("");
        createOrderShippingAddressDetails.setZipcode("");
        createOrderShippingAddressDetails.setState("");
        createOrderShippingAddressDetails.setCountry("");

        Intent goToOrderDetailsActivity = new Intent(DeliveryAddressActivity.this, OrderDetailsActivity.class);
        goToOrderDetailsActivity.putExtra("BILLING_ADDRESS", new Gson().toJson(createOrderBillingAddressDetails));
        goToOrderDetailsActivity.putExtra("SHIPPING_ADDRESS", new Gson().toJson(createOrderShippingAddressDetails));
        goToOrderDetailsActivity.putExtra("DELIVERY_TYPE", deliveryType);
        startActivity(goToOrderDetailsActivity);
    }

    public void differentShippingAddress() throws Exception {
        if (editBillingAddress1.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingAddress2.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing address2", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingArea.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing area", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingCity.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing city", Toast.LENGTH_LONG).show();
            return;
        }
        if (editBillingZipcode.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter Billing zipcode", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_billing_state.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing state", Toast.LENGTH_LONG).show();
            return;
        }

        if (editBillingCountry.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Billing country", Toast.LENGTH_LONG).show();
            return;
        }

        if (editShippingAddress1.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery address1", Toast.LENGTH_LONG).show();
            return;
        }
        if (editShippingAddress2.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery address2", Toast.LENGTH_LONG).show();
            return;
        }
        if (editShippingArea.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery area", Toast.LENGTH_LONG).show();
            return;
        }
        if (editShippingCity.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery city", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_shipping_state.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery state", Toast.LENGTH_LONG).show();
            return;
        }
        if (editShippingCountry.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery country", Toast.LENGTH_LONG).show();
            return;
        }
        if (editShippingZipcode.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter Delivery zipcode", Toast.LENGTH_LONG).show();
            return;
        }
        CreateOrderAddressDetails createOrderBillingAddressDetails = new CreateOrderAddressDetails();
        CreateOrderAddressDetails createOrderShippingAddressDetails = new CreateOrderAddressDetails();
        createOrderBillingAddressDetails.setDate(date);
        createOrderBillingAddressDetails.setAddress1(editBillingAddress1.getText().toString().trim());
        createOrderBillingAddressDetails.setAddress2(editBillingAddress2.getText().toString().trim());
        createOrderBillingAddressDetails.setArea(editBillingArea.getText().toString().trim());
        createOrderBillingAddressDetails.setCity(editBillingCity.getText().toString().trim());
        createOrderBillingAddressDetails.setZipcode(editBillingZipcode.getText().toString().trim());
        createOrderBillingAddressDetails.setState(edittext_billing_state.getText().toString().trim());
        createOrderBillingAddressDetails.setCountry(editBillingCountry.getText().toString().trim());

        createOrderShippingAddressDetails.setAddress1(editShippingAddress1.getText().toString().trim());
        createOrderShippingAddressDetails.setAddress2(editShippingAddress2.getText().toString().trim());
        createOrderShippingAddressDetails.setArea(editShippingArea.getText().toString().trim());
        createOrderShippingAddressDetails.setCity(editShippingCity.getText().toString().trim());
        createOrderShippingAddressDetails.setZipcode(editShippingZipcode.getText().toString().trim());
        createOrderShippingAddressDetails.setState(edittext_shipping_state.getText().toString().trim());
        createOrderShippingAddressDetails.setCountry(editShippingCountry.getText().toString().trim());

        Intent goToOrderDetailsActivity = new Intent(DeliveryAddressActivity.this, OrderDetailsActivity.class);
        goToOrderDetailsActivity.putExtra("BILLING_ADDRESS", new Gson().toJson(createOrderBillingAddressDetails));
        goToOrderDetailsActivity.putExtra("SHIPPING_ADDRESS", new Gson().toJson(createOrderShippingAddressDetails));
        goToOrderDetailsActivity.putExtra("DELIVERY_TYPE", deliveryType);
        startActivity(goToOrderDetailsActivity);
    }

    public void findViewsById() {
        btn_pick_date = (Button) findViewById(R.id.btn_pick_date);
        textBillingAddress = (TextView) findViewById(R.id.text_billing_address);
        textShippingAddress = (TextView) findViewById(R.id.text_shipping_address);
        editBillingAddress1 = (EditText) findViewById(R.id.edittext_billing_address1);
        editBillingAddress2 = (EditText) findViewById(R.id.edittext_billing_address2);
        editBillingArea = (EditText) findViewById(R.id.edittext_billing_area);
        editBillingCity = (EditText) findViewById(R.id.edittext_billing_city);
        editBillingZipcode = (EditText) findViewById(R.id.edittext_billing_zipcode);
        editBillingCountry = (EditText) findViewById(R.id.edittext_billing_country);
        editShippingAddress1 = (EditText) findViewById(R.id.edittext_shipping_address1);
        editShippingAddress2 = (EditText) findViewById(R.id.edittext_shipping_address2);
        editShippingArea = (EditText) findViewById(R.id.edittext_shipping_area);
        editShippingCity = (EditText) findViewById(R.id.edittext_shipping_city);
        editShippingZipcode = (EditText) findViewById(R.id.edittext_shipping_zipcode);
        editShippingCountry = (EditText) findViewById(R.id.edittext_shipping_country);
        shippingDetailsGroup = (RadioGroup) findViewById(R.id.shippingaddress);
        sameAddress = (RadioButton) findViewById(R.id.same_as_billing);
        differentAddress = (RadioButton) findViewById(R.id.other_than_billing);
        pickUp = (RadioButton) findViewById(R.id.pick_up);
        llShippingAddress = (LinearLayout) findViewById(R.id.llShippingAddress);
        edittext_shipping_state = (EditText) findViewById(R.id.edittext_shipping_state);
        edittext_billing_state = (EditText) findViewById(R.id.edittext_billing_state);

        Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        textBillingAddress.setTypeface(tf);
        textShippingAddress.setTypeface(tf);
        editBillingAddress1.setTypeface(tf);
        editBillingAddress2.setTypeface(tf);
        editBillingArea.setTypeface(tf);
        editBillingCity.setTypeface(tf);
        editBillingZipcode.setTypeface(tf);
        editBillingCountry.setTypeface(tf);
        editShippingAddress1.setTypeface(tf);
        editShippingAddress2.setTypeface(tf);
        editShippingArea.setTypeface(tf);
        editShippingCity.setTypeface(tf);
        editShippingZipcode.setTypeface(tf);
        editShippingCountry.setTypeface(tf);
        sameAddress.setTypeface(tf);
        differentAddress.setTypeface(tf);
        pickUp.setTypeface(tf);
        edittext_shipping_state.setTypeface(tf);
        edittext_billing_state.setTypeface(tf);

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

    public String loadPreferences() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            user = spLoad.getString("USER_DATA", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setBillingAddress() throws NullPointerException, Exception {

        successResponseOfUser = new Gson().fromJson(loadPreferences(), SuccessResponseOfUser.class);
        if (successResponseOfUser.getSuccess().getUser().getLocation() != null) {
            editBillingAddress1.setText(successResponseOfUser.getSuccess().getUser().getLocation().getAddress1());
            editBillingAddress2.setText(successResponseOfUser.getSuccess().getUser().getLocation().getAddress2());
            editBillingArea.setText(successResponseOfUser.getSuccess().getUser().getLocation().getArea());
            editBillingCity.setText(successResponseOfUser.getSuccess().getUser().getLocation().getCity());
            editBillingZipcode.setText(successResponseOfUser.getSuccess().getUser().getLocation().getZipcode());
            edittext_billing_state.setText(successResponseOfUser.getSuccess().getUser().getLocation().getState());
            editBillingCountry.setText(successResponseOfUser.getSuccess().getUser().getLocation().getCountry());
        }
    }

    public class CheckSessionAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, msg, code, resultOfCheckSession;
        public JSONObject jObj;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DeliveryAddressActivity.this, "", "Enter Order Delivery Address...");
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
                            Log.d("Login success", "sussecccc");

                            setBillingAddress();
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {
                                Intent goToSignin = new Intent(DeliveryAddressActivity.this, SignInActivity.class);
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