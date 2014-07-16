package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Adapter.AdapterForSelectaddressList;
import com.gls.orderzapp.AddressDetails.Adapter.AddressPopUpMenu;
import com.gls.orderzapp.AddressDetails.Adapter.DeliveryChargesAndTypeAdapter;
import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderAddressDetails;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by prajyot on 24/6/14.
 */
public class DeliveryPaymentActivity extends Activity {
    Context context;
    public static TextView shipping_address_textview, billing_address_textview;
    RadioGroup payment_mode_group;
    RadioButton cash_on_delivery, credit_card;
    public static LinearLayout ll_deliver_charge_type;
    Button delivery_date, proceed_to_pay;
    SuccessResponseOfUser successResponseOfUserDeliveryAddresDetails, successResponseOfUserBillingAddress;

    ImageView popup_image;
    public static String payment_mode = "", user_id = "";
    final int SIGN_IN = 0;
    final int CHANGE_ADDRESS = 1;
    Calendar c;
    int mYear, mMonth, mDay, yy, mm, dd, hh, min, cHH, cMin, cAm_Pm;
    DatePicker datePicker;
    TimePicker timePicker;
    String date = "";
    boolean date_selected = false;
    AlertDialog alertDialog;
    //for payment mode
    ProductDetails[] checkForPaymentModeValues;
    List<ProductDetails> checkForPaymentModeList;
    //for delivery mode
//    ProductDetails[] checkForDeliveryModeValues;
//    List<ProductDetails> checkForDeliveryModeList;
//     HashMap<String,ProductDetails> listOfSellerDeliveryMode = new HashMap<>();
//    ArrayList<String> providerNamesforDelivery = new ArrayList<>();

    String providerName = "";

    Boolean cashOnDelivery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        setContentView(R.layout.delivery_payment_activity);
        context = DeliveryPaymentActivity.this;
        findViewsById();
        new CheckSessionAsync().execute();
        checkPaymentmode();
        selectPaymentMode();


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
    }

    public void checkPaymentmode() {
        checkForPaymentModeValues = Cart.hm.values().toArray(new ProductDetails[Cart.hm.size()]);
        checkForPaymentModeList = new ArrayList<>(Arrays.asList(checkForPaymentModeValues));
        Collections.sort(checkForPaymentModeList, new PaymentModeComparator());

        for (int i = 0; i < Cart.hm.size(); i++) {
            if (checkForPaymentModeList.get(i).getProviderName() != null) {
                if (checkForPaymentModeList.get(i).getPaymentmode().getCod() == false) {
                    cashOnDelivery = false;
                }
            }
        }

    }

    public void selectDeliveryType() {

//ll_deliver_charge_type.removeAllViews();
        new DeliveryChargesAndTypeAdapter(context);

    }

    public void selectPaymentMode() {
        if (cashOnDelivery == true) {
            cash_on_delivery.setVisibility(View.VISIBLE);
        } else {
            credit_card.setVisibility(View.VISIBLE);
        }
        payment_mode_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cash_on_delivery:
                        payment_mode = "Cash on Delivery";
                        break;

                    case R.id.credit_card:
                        payment_mode = "Payment by Card";
                        break;
                }
            }
        });
    }

    public void findViewsById() {
        ll_deliver_charge_type = (LinearLayout) findViewById(R.id.ll_deliver_charge_type);
        shipping_address_textview = (TextView) findViewById(R.id.shipping_address_textview);
        popup_image = (ImageView) findViewById(R.id.popup_image);
        delivery_date = (Button) findViewById(R.id.delivery_date);
        payment_mode_group = (RadioGroup) findViewById(R.id.payment_mode_group);
        cash_on_delivery = (RadioButton) findViewById(R.id.cash_on_delivery);
        credit_card = (RadioButton) findViewById(R.id.credit_card);
        billing_address_textview = (TextView) findViewById(R.id.billing_address_textview);
        proceed_to_pay = (Button) findViewById(R.id.proceed_to_pay);
    }

    public void proceedToPay(View view) {
        try {
            if (payment_mode.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please select a payment mode", Toast.LENGTH_LONG).show();
                return;
            }
            if (date.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please select your expected delivery date", Toast.LENGTH_LONG).show();
                return;
            }
            if (DisplayDeliveryChargesAndType.deliveryTypeCheck() == false) {
                Toast.makeText(context, "Please select a Delivery Type", Toast.LENGTH_LONG).show();
                DisplayDeliveryChargesAndType.deliveryTypeCheck();
                return;
            }
            setDataForBillingAndDeliveryAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DeliveryChargesAndTypeAdapter.delivery_type = "";
        payment_mode = "";
    }

    public void selectDeliveryDate(View view) {
        LayoutInflater li = LayoutInflater.from(DeliveryPaymentActivity.this);
        View dialogView = li.inflate(R.layout.calendar_view_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryPaymentActivity.this);

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        cHH = c.get(Calendar.HOUR_OF_DAY);
        cMin = c.get(Calendar.MINUTE);
        cAm_Pm = c.get(Calendar.AM_PM);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(dialogView);


        final Button select_date;
        // create alert dialog
        datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
        select_date = (Button) dialogView.findViewById(R.id.select_date);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yy = datePicker.getYear();
                mm = datePicker.getMonth();
                dd = datePicker.getDayOfMonth();
                hh = timePicker.getCurrentHour();
                min = timePicker.getCurrentMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, yy);
                calendar.set(Calendar.MONTH, mm);
                calendar.set(Calendar.DAY_OF_MONTH, dd);
                calendar.set(Calendar.HOUR_OF_DAY, hh);
                calendar.set(Calendar.MINUTE, min);

                Date selectedDate = calendar.getTime();
                Date TodaysDate = c.getTime();
                Log.d("selected date", new Gson().toJson(selectedDate));
                Log.d("current date", new Gson().toJson(TodaysDate));
                if (selectedDate.before(TodaysDate)) {
                    Toast.makeText(getApplicationContext(), "Please select a valid date", Toast.LENGTH_LONG).show();

                } else {

                    updateTime(yy, mm, dd, hh, min);
                }
            }
        });

        alertDialog = alertDialogBuilder.create();

        alertDialog.setInverseBackgroundForced(true);

        // show it
        alertDialog.show();

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    public void updateTime(int yy, int mm, int dd, int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);


        date = new StringBuilder().append(yy).append('-').append(mm + 1).append('-').append(dd).append("  ").append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        //dialog.cancel();
        delivery_date.setText(date);

        date_selected = true;
        alertDialog.dismiss();

    }


    public void showPopUp(View view) {
        new AddressPopUpMenu(DeliveryPaymentActivity.this, view).handlePopupEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN:
                if (resultCode == RESULT_OK) {
                    setDeliveryAddress(data.getStringExtra("USER"));
                    setBillingAddress(data.getStringExtra("USER"));
                    selectDeliveryType();
                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;

            case CHANGE_ADDRESS:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("RESULT");
                }
                break;
        }

    }

    public String loadPreferencesUserDataForDeliveryAddress() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            user = spLoad.getString("USER_DATA_DELIVERY_ADDRESS", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    public String loadPreferencesUserDataForBillingAddress() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            user = spLoad.getString("USER_DATA", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setDeliveryAddress(String getUserData) {
        try {
            successResponseOfUserDeliveryAddresDetails = new Gson().fromJson(getUserData, SuccessResponseOfUser.class);
            if (successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getUserid() != null) {
                user_id = successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getUserid();
            }
            if (successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation() != null) {
                shipping_address_textview.setText(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getAddress1() + ", " +
                        successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getAddress2() + ", " +
                        successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getArea() + ", \n" +
                        successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCity() + ". " +
                        successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getZipcode() + "\n" +
                        successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getState() + ", " +
                        successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCountry() + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBillingAddress(String getUserData) {
        try {
            successResponseOfUserBillingAddress = new Gson().fromJson(getUserData, SuccessResponseOfUser.class);
            if (successResponseOfUserBillingAddress.getSuccess().getUser().getLocation() != null) {
                billing_address_textview.setText(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getAddress1() + ", " +
                        successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getAddress2() + ", " +
                        successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getArea() + ", \n" +
                        successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getCity() + ". " +
                        successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getZipcode() + "\n" +
                        successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getState() + ", " +
                        successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getCountry() + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String myAddressList(){
//        String resultMyAddressList = "";
//        try{
//            if(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getUserid()!=null) {
//                resultMyAddressList = ServerConnection.executeGet(getApplicationContext(), "/api/mydeliveryaddresses/" + successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getUserid());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return resultMyAddressList;
//    }

    public void setDataForBillingAndDeliveryAddress() throws Exception {

        CreateOrderAddressDetails createOrderBillingAddressDetails = new CreateOrderAddressDetails();
        CreateOrderAddressDetails createOrderDeliveryAddressDetails = new CreateOrderAddressDetails();

        createOrderBillingAddressDetails.setDate(date);
        if (successResponseOfUserBillingAddress.getSuccess().getUser().getLocation() != null) {
            createOrderBillingAddressDetails.setAddress1(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getAddress1());
            createOrderBillingAddressDetails.setAddress2(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getAddress2());
            createOrderBillingAddressDetails.setArea(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getArea());
            createOrderBillingAddressDetails.setCity(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getCity());
            createOrderBillingAddressDetails.setZipcode(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getZipcode());
            createOrderBillingAddressDetails.setState(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getState());
            createOrderBillingAddressDetails.setCountry(successResponseOfUserBillingAddress.getSuccess().getUser().getLocation().getCountry());
        }
        if (DeliveryChargesAndTypeAdapter.delivery_type.equalsIgnoreCase("Home Delivery")) {
            if (ChangeAddressActivity.isAddressChanged == true) {
                ChangeAddressActivity.isAddressChanged = false;
                createOrderDeliveryAddressDetails.setAddress1(ChangeAddressActivity.edittext_address1.getText().toString().trim());
                createOrderDeliveryAddressDetails.setAddress2(ChangeAddressActivity.edittext_address2.getText().toString().trim());
                createOrderDeliveryAddressDetails.setArea(ChangeAddressActivity.edittext_area.getText().toString().trim());
                createOrderDeliveryAddressDetails.setCity(ChangeAddressActivity.edittext_city.getText().toString().trim());
                createOrderDeliveryAddressDetails.setZipcode(ChangeAddressActivity.edittext_zipcode.getText().toString().trim());
                createOrderDeliveryAddressDetails.setState(ChangeAddressActivity.edittext_state.getText().toString().trim());
                createOrderDeliveryAddressDetails.setCountry(ChangeAddressActivity.edittext_country.getText().toString().trim());
            } else if (SelectAddressListActivity.isAddNewaddress == true) {
                SelectAddressListActivity.isAddNewaddress = false;
                createOrderDeliveryAddressDetails.setDeliveryaddressid(AdapterForSelectaddressList.deliveryaddressid);
                createOrderDeliveryAddressDetails.setAddress1(AdapterForSelectaddressList.deliveryAddressList.getAddress().getAddress1());
                createOrderDeliveryAddressDetails.setAddress2(AdapterForSelectaddressList.deliveryAddressList.getAddress().getAddress2());
                createOrderDeliveryAddressDetails.setArea(AdapterForSelectaddressList.deliveryAddressList.getAddress().getArea());
                createOrderDeliveryAddressDetails.setCity(AdapterForSelectaddressList.deliveryAddressList.getAddress().getCity());
                createOrderDeliveryAddressDetails.setZipcode(AdapterForSelectaddressList.deliveryAddressList.getAddress().getZipcode());
                createOrderDeliveryAddressDetails.setState(AdapterForSelectaddressList.deliveryAddressList.getAddress().getState());
                createOrderDeliveryAddressDetails.setCountry(AdapterForSelectaddressList.deliveryAddressList.getAddress().getCountry());
            } else {
                if (successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation() != null) {
                    createOrderDeliveryAddressDetails.setAddress1(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getAddress1());
                    createOrderDeliveryAddressDetails.setAddress2(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getAddress2());
                    createOrderDeliveryAddressDetails.setArea(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getArea());
                    createOrderDeliveryAddressDetails.setCity(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCity());
                    createOrderDeliveryAddressDetails.setZipcode(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getZipcode());
                    createOrderDeliveryAddressDetails.setState(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getState());
                    createOrderDeliveryAddressDetails.setCountry(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCountry());
                }
            }
        } else {
            createOrderDeliveryAddressDetails.setAddress1(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getAddress1());
            createOrderDeliveryAddressDetails.setAddress2(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getAddress2());
            createOrderDeliveryAddressDetails.setArea(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getArea());
            createOrderDeliveryAddressDetails.setCity(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCity());
            createOrderDeliveryAddressDetails.setZipcode(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getZipcode());
            createOrderDeliveryAddressDetails.setState(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getState());
            createOrderDeliveryAddressDetails.setCountry(successResponseOfUserDeliveryAddresDetails.getSuccess().getUser().getLocation().getCountry());
        }

        Intent goToOrderDetailsActivity = new Intent(DeliveryPaymentActivity.this, OrderDetailsActivity.class);
        goToOrderDetailsActivity.putExtra("BILLING_ADDRESS", new Gson().toJson(createOrderBillingAddressDetails));
        goToOrderDetailsActivity.putExtra("DELIVERY_ADDRESS", new Gson().toJson(createOrderDeliveryAddressDetails));
        startActivity(goToOrderDetailsActivity);
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

    public class CheckSessionAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, msg, code, resultOfCheckSession;
        public JSONObject jObj;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DeliveryPaymentActivity.this, "", "");
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
                            Log.d("success", loadPreferencesUserDataForBillingAddress());
                            setDeliveryAddress(loadPreferencesUserDataForDeliveryAddress());
                            setBillingAddress(loadPreferencesUserDataForBillingAddress());
                            selectDeliveryType();

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {
                                Intent goToSignin = new Intent(DeliveryPaymentActivity.this, SignInActivity.class);
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


    private class PaymentModeComparator implements Comparator<ProductDetails> {
        @Override
        public int compare(ProductDetails o1, ProductDetails o2) {
            return o1.getProviderName().compareTo(o2.getProviderName());
        }
    }

}
