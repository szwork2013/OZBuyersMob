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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Adapter.DeliveryChargesAndTypeAdapter;
import com.gls.orderzapp.R;
import com.gls.orderzapp.SignUp.Location;
import com.gls.orderzapp.User.SettingsUserData;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.User.UserDetails;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by prajyot on 20/6/14.
 */
public class ChangeAddressActivity extends Activity {
    TextView text_address;
    Context context;
    String userID,user;
    public static EditText edittext_address1, edittext_address2, edittext_area, edittext_city, edittext_state, edittext_country, edittext_zipcode;
    Button save_address;
    public static boolean isAddressChanged = false;
    Bundle bundle;
    UserDetails userDetails;
    SettingsUserData settingsUserData;
    SuccessResponseOfUser successResponseForUserID;
//    SuccessResponseOfUser successResponseOfUser;
    DeliveryPaymentActivity deliveryPaymentActivity;
    GsonBuilder gsonBuild = new GsonBuilder();
    Gson gson = gsonBuild.disableHtmlEscaping().create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        setContentView(R.layout.change_address_activity);
        context=ChangeAddressActivity.this;
        findViewsById();
         bundle=getIntent().getExtras();
        deliveryPaymentActivity=new DeliveryPaymentActivity();

        isAddressChanged = false;
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
        save_address = (Button) findViewById(R.id.save_address);
        edittext_address1 = (EditText) findViewById(R.id.edittext_address1);
        edittext_address2 = (EditText) findViewById(R.id.edittext_address2);
        edittext_area = (EditText) findViewById(R.id.edittext_area);
        edittext_city = (EditText) findViewById(R.id.edittext_city);
        edittext_state = (EditText) findViewById(R.id.edittext_state);
        edittext_country = (EditText) findViewById(R.id.edittext_country);
        edittext_zipcode = (EditText) findViewById(R.id.edittext_zipcode);
        text_address = (TextView) findViewById(R.id.text_address);
    }

    public void saveAddress(View view) throws Exception {


        if (edittext_address1.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 1", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_address2.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 2", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_city.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your city", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_area.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your area", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_zipcode.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter your Pincode", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_country.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your Country", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_state.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your State", Toast.LENGTH_LONG).show();
            return;
        }
        if(bundle.getString("User_Address").equals("DeliveryAddress")) {
            DeliveryPaymentActivity.shipping_address_textview.setText(edittext_address1.getText().toString() + ", " +
                    edittext_address2.getText().toString() + ", " +
                    edittext_area.getText().toString() + ", \n" +
                    edittext_city.getText().toString() + ". " +
                    edittext_zipcode.getText().toString() + "\n" +
                    edittext_state.getText().toString() + ", " +
                    edittext_country.getText().toString() + ".");
            isAddressChanged = true;
            loadPreferencesUserDataForDeliveryAddress();
            DeliveryPaymentActivity.shipping_address_textview.setText(edittext_address1.getText().toString() + ", " +
                    edittext_address2.getText().toString() + ", " +
                    edittext_area.getText().toString() + ", \n" +
                    edittext_city.getText().toString() + ". " +
                    edittext_zipcode.getText().toString() + "\n" +
                    edittext_state.getText().toString() + ", " +
                    edittext_country.getText().toString() + ".");
            ((Activity) context).setResult(((Activity) context).RESULT_OK);
            finish();

        }
    }


    public String loadPreferencesUserDataForDeliveryAddress() throws Exception {
        String user = "";
        String DeliveryAddresDetails="";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
            user = spLoad.getString("USER_DATA_DELIVERY_ADDRESS", null);
            successResponseForUserID= new Gson().fromJson(user, SuccessResponseOfUser.class);
            successResponseForUserID.getSuccess().getUser().getLocation().setArea( edittext_area.getText().toString());
            successResponseForUserID.getSuccess().getUser().getLocation().setCity( edittext_city.getText().toString());
            successResponseForUserID.getSuccess().getUser().getLocation().setAddress1(edittext_address1.getText().toString());
            successResponseForUserID.getSuccess().getUser().getLocation().setAddress2(edittext_address2.getText().toString());
            successResponseForUserID.getSuccess().getUser().getLocation().setCountry( edittext_country.getText().toString());
            successResponseForUserID.getSuccess().getUser().getLocation().setState(edittext_state.getText().toString());
            successResponseForUserID.getSuccess().getUser().getLocation().setZipcode(edittext_zipcode.getText().toString() );

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = sp.edit();
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            DeliveryAddresDetails = gson.toJson(successResponseForUserID);
            edit.putString("USER_DATA_DELIVERY_ADDRESS", DeliveryAddresDetails);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
//    //****************
//
//
//
//
//
//
//
//
//    public void savePostSettingsData() throws Exception {
////        userDetails = new UserDetails();
//        successResponseForUserID= new Gson().fromJson(loadPreferencesUserID(), SuccessResponseOfUser.class);
//        successResponseForUserID.getSuccess().getUser().getLocation().setArea(edittext_area.getText().toString().trim());
//        successResponseForUserID.getSuccess().getUser().getLocation().setCity(edittext_city.getText().toString().trim());
//        successResponseForUserID.getSuccess().getUser().getLocation().setAddress1(edittext_address1.getText().toString().trim());
//        successResponseForUserID.getSuccess().getUser().getLocation().setAddress2(edittext_address2.getText().toString().trim());
//        successResponseForUserID.getSuccess().getUser().getLocation().setCountry(edittext_country.getText().toString().trim());
//        successResponseForUserID.getSuccess().getUser().getLocation().setState(edittext_state.getText().toString().trim());
//        successResponseForUserID.getSuccess().getUser().getLocation().setZipcode(edittext_zipcode.getText().toString().trim());
////        settingsUserData = new SettingsUserData();
////        Location location = new Location();
////        location.setAddress1(edittext_address1.getText().toString().trim());
////        location.setAddress2(edittext_address2.getText().toString().trim());
////        location.setCity(edittext_city.getText().toString().trim());
////        location.setArea(edittext_area.getText().toString().trim());
////        location.setCountry(edittext_country.getText().toString().trim());
////        location.setState(edittext_state.getText().toString().trim());
////        location.setZipcode(edittext_zipcode.getText().toString().trim());
////        userDetails.setLocation(location);
////        settingsUserData.setUserdata(userDetails);
//    }
//    public String postSettingsData() {
//        String resultOfSaveSettings = "";
//        String jsonPostSettingsData;
//        try {
//            jsonPostSettingsData = gson.toJson(successResponseForUserID);
//            setUserID(loadPreferencesUserID());
//            resultOfSaveSettings = ServerConnection.executePut(jsonPostSettingsData, "/api/user/" + userID);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultOfSaveSettings;
//    }
//
//    public void setUserID(String getUserData) {
//        try {
//            successResponseForUserID = new Gson().fromJson(getUserData, SuccessResponseOfUser.class);
//            userID=successResponseForUserID.getSuccess().getUser().getUserid();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String loadPreferencesUserID() throws Exception {
//        try {
//            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
//            user = spLoad.getString("USER_DATA", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
//    }
//
//    public class SaveSettingsAsync extends AsyncTask<String, Integer, String> {
//        String connectedOrNot, resultOfSaveSettings, msg, code;
//        JSONObject jObj, jObjError, jObjSuccess;
//        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
////            progressDialog = ProgressDialog.show(ChangeAddressActivity.this, "", "");
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
//                    connectedOrNot = "success";
//                    savePostSettingsData();
//                    resultOfSaveSettings = postSettingsData();
//                    if (!resultOfSaveSettings.isEmpty()) {
//                        Log.d("result", resultOfSaveSettings);
//                        jObj = new JSONObject(resultOfSaveSettings);
//                        if (jObj.has("success")) {
//                            jObjSuccess = jObj.getJSONObject("success");
//                            msg = jObjSuccess.getString("message");
//                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//                            SharedPreferences.Editor edit = sp.edit();
//                            edit.putString("USER_DATA_DELIVERY_ADDRESS", gson.toJson(successResponseForUserID));
//                            edit.commit();
//                        } else {
//                            jObjError = jObj.getJSONObject("error");
//                            msg = jObjError.getString("message");
//                            code = jObjError.getString("code");
//                        }
//                    }
//                } else {
//                    connectedOrNot = "error";
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return connectedOrNot;
//        }
//
//        @Override
//        protected void onPostExecute(String connectedOrNot) {
//            try {
//                if (connectedOrNot.equals("success")) {
//                    if (!resultOfSaveSettings.isEmpty()) {
//                        if (jObj.has("success")) {
//
//                            DeliveryPaymentActivity.billing_address_textview.setText(edittext_address1.getText().toString() + ", " +
//                                    edittext_address2.getText().toString() + ", " +
//                                    edittext_area.getText().toString() + ", \n" +
//                                    edittext_city.getText().toString() + ". " +
//                                    edittext_zipcode.getText().toString() + "\n" +
//                                    edittext_state.getText().toString() + ", " +
//                                    edittext_country.getText().toString() + ".");
//                            DeliveryPaymentActivity.shipping_address_textview.setText(edittext_address1.getText().toString() + ", " +
//                                    edittext_address2.getText().toString() + ", " +
//                                    edittext_area.getText().toString() + ", \n" +
//                                    edittext_city.getText().toString() + ". " +
//                                    edittext_zipcode.getText().toString() + "\n" +
//                                    edittext_state.getText().toString() + ", " +
//                                    edittext_country.getText().toString() + ".");
//                            ((Activity) context).setResult(((Activity) context).RESULT_OK);
//                               finish();
//                        } else {
//                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Server is not responding please try again later", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
