package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gls.orderzapp.R;
import com.gls.orderzapp.SignUp.SendSignUpData;
import com.gls.orderzapp.SignUp.SignUpDataInUserObject;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.gls.orderzapp.Utility.UtilityClassForLanguagePreferance;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;


public class SignUpActivity extends ActionBarActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    String regid = "";
    Context context;
    EditText mobileNoEditText, passwordEditText, usernameEditText, address1EditText, address2EditText, cityEditText, areaEditText,
            pincodeEditText, countryCodeEditText, countryEditText, stateEditText, emailEditText;
    Button signUpButton;
    //    String SENDER_ID = "926441694335";
    String SENDER_ID = "1088135189222";
    SignUpDataInUserObject signUpData;
    SendSignUpData sendSignUpData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        context = SignUpActivity.this;
        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        setContentView(R.layout.sign_up);

        findViewsById();

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

        mobileNoEditText = (EditText) findViewById(R.id.editTextMobileNUmber);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        usernameEditText = (EditText) findViewById(R.id.editTextUserName);
        address1EditText = (EditText) findViewById(R.id.editTextAddress1);
        address2EditText = (EditText) findViewById(R.id.editTextAddress2);
        cityEditText = (EditText) findViewById(R.id.editTextCity);
        areaEditText = (EditText) findViewById(R.id.editTextArea);
        pincodeEditText = (EditText) findViewById(R.id.editTextPincode);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        signUpButton = (Button) findViewById(R.id.buttonSignUp);
        countryCodeEditText = (EditText) findViewById(R.id.editTextCountryCode);
        countryEditText = (EditText) findViewById(R.id.editTextCountry);
        stateEditText = (EditText) findViewById(R.id.editTextState);

        UtilityClassForLanguagePreferance.applyTypeface(UtilityClassForLanguagePreferance.getParentView(passwordEditText), UtilityClassForLanguagePreferance.getTypeFace(context));
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
                return true;
            } else {
                Toast.makeText(context, "Device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void storeRegistrationId(Context context, String regId) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("REG_ID", regId);
        edit.commit();

    }

    public String getRegistrationId() {

        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
        String regId = spLoad.getString("REG_ID", "");
        return regId;

    }

    public void signUp(View view) {
        if (mobileNoEditText.getText().toString().trim().length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter a correct mobile number", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordEditText.getText().toString().trim().length() < 3 || passwordEditText.getText().toString().trim().length() > 10) {
            Toast.makeText(getApplicationContext(), "Please enter a password between 3 - 10 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (emailEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a Email Address", Toast.LENGTH_LONG).show();
            return;
        }
        if (usernameEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        }
        if (address1EditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 1", Toast.LENGTH_LONG).show();
            return;
        }
        if (address2EditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 2", Toast.LENGTH_LONG).show();
            return;
        }
        if (cityEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your city", Toast.LENGTH_LONG).show();
            return;
        }
        if (areaEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your area", Toast.LENGTH_LONG).show();
            return;
        }
        if (pincodeEditText.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter your Pincode", Toast.LENGTH_LONG).show();
            return;
        }
        if (countryEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your Country", Toast.LENGTH_LONG).show();
            return;
        }
        if (stateEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your State", Toast.LENGTH_LONG).show();
            return;
        }
        if (checkPlayServices()) {

            new RegisterToGcmInBackground(context).execute();

        }
    }

    public String postSignUp() {
        String signUpResultJson = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(signUpData);
            signUpResultJson = ServerConnection.executePost1(jsonToSendOverServer, "/api/user/signup");
        } catch (Exception e) {

            e.printStackTrace();
        }
        return signUpResultJson;
    }

    public void setPostSignupParameters() {
        signUpData = new SignUpDataInUserObject();
        sendSignUpData = new SendSignUpData();
        sendSignUpData.setUsertype("individual");
//        sendSignUpData.setMobileno(mobileNoEditText.getText().toString().trim());
        sendSignUpData.setMobileno(countryCodeEditText.getText().toString().trim() + mobileNoEditText.getText().toString().trim());
        sendSignUpData.setPassword(passwordEditText.getText().toString().trim());
        sendSignUpData.setUsername(usernameEditText.getText().toString().trim());
        sendSignUpData.setGcmregistrationid(regid);
        sendSignUpData.setEmail(emailEditText.getText().toString().trim());
        sendSignUpData.getLocation().setAddress1(address1EditText.getText().toString().trim());
        sendSignUpData.getLocation().setAddress2(address2EditText.getText().toString().trim());
        sendSignUpData.getLocation().setCity(cityEditText.getText().toString().trim());
        sendSignUpData.getLocation().setArea(areaEditText.getText().toString().trim());
        sendSignUpData.getLocation().setZipcode(pincodeEditText.getText().toString().trim());
        sendSignUpData.getLocation().setCountry(countryEditText.getText().toString().trim());
        sendSignUpData.getLocation().setState(stateEditText.getText().toString().trim());

        signUpData.setUser(sendSignUpData);
    }

    public class RegisterToGcmInBackground extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String signUpResult, connectedOrNot, msg, code;
        ProgressDialog pd;
        Context context;

        public RegisterToGcmInBackground(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(context, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                if (new CheckConnection(context).isConnectingToInternet()) {

                    connectedOrNot = "success";

                    regid = getRegistrationId();
                    if (regid.isEmpty()) {
                        if (gcm == null) {
                            try {
                                gcm = GoogleCloudMessaging.getInstance(context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            regid = gcm.register(SENDER_ID);
                            storeRegistrationId(context, regid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setPostSignupParameters();
                    signUpResult = postSignUp();
                    if (!signUpResult.isEmpty()) {
                        jObj = new JSONObject(signUpResult);
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
                pd.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (!signUpResult.isEmpty()) {
                        if (jObj.has("success")) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            Intent goToVerifyActivity = new Intent(context, VerifyUserActivity.class);
                            startActivity(goToVerifyActivity);
                            finish();
                        } else {

                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(context, "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 
