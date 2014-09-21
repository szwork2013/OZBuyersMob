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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.CountryCode.CountryCode;
import com.gls.orderzapp.CountryCode.CountryCodeAdapter;
import com.gls.orderzapp.CountryCode.SuccessResponseForCountryCode;
import com.gls.orderzapp.R;
import com.gls.orderzapp.SignIn.SignInPostData;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by prajyot on 2/4/14.
 */
public class SignInActivity extends Activity {
    public static boolean islogedin = false;
    TextView helpingHandTv, forgotPasswordText;
    EditText mobileNumberEditText, passwordEditText;
    Button signInButton;
    Context context;
    String regid = "";
    String SENDER_ID = "13920985466";
    GoogleCloudMessaging gcm;
    SignInPostData signInPostData;
    boolean backPresed = false;
    Spinner countryCodeSpinner;
    String countryCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SignInActivity.this;
        //Get a Tracker (should auto-report)
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
//        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        setContentView(R.layout.sign_in);

        findViewsById();
        new CountryCodeAsync().execute();
        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               countryCode = ((CountryCode)adapterView.getItemAtPosition(i)).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStart(this);


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
    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public void onBackPressed() {
//        super.onBackPressed();
        backPresed = true;
        islogedin = false;

        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    public void findViewsById() {
        helpingHandTv = (TextView) findViewById(R.id.textViewHelpingHand);
        mobileNumberEditText = (EditText) findViewById(R.id.mobileNumberEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.buttonSignIn);
        forgotPasswordText = (TextView) findViewById(R.id.textForgotPassword);
        countryCodeSpinner = (Spinner) findViewById(R.id.countryCodeSpinner);

    }

    public void forgotPassword(View view) {
        Intent goToForgotPasswordActivity = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(goToForgotPasswordActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin_menu, menu);
        View view = (View) menu.findItem(R.id.signup).getActionView();

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //do stuff here
                Intent goToSignUp = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(goToSignUp);
                finish();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.signup:
                Intent goToSignUp = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(goToSignUp);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signIn(View view) {
        if (mobileNumberEditText.getText().toString().trim().length() < 10) {
            Toast.makeText(context, "Enter a correct mobile number", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "Enter password", Toast.LENGTH_LONG).show();
            return;
        }


        if (checkPlayServices()) {
            setPostSignInParameters();
            new SignInAsync().execute();
        }

    }

    public String postSignIn() {
        String resultSignIn = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(signInPostData);
            resultSignIn = ServerConnection.executePost1(jsonToSendOverServer, "/api/user/signin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSignIn;
    }

    public void setPostSignInParameters() {

        signInPostData = new SignInPostData();
        signInPostData.setMobileno(countryCode + mobileNumberEditText.getText().toString().trim());
        signInPostData.setPassword(passwordEditText.getText().toString().trim());

    }

    public String loadCountryCodePreference() throws Exception{
        String countryCode = "";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sp.getString("USER_DATA","").isEmpty()){
            countryCode = "91";
        }else {
            SuccessResponseOfUser successResponseOfUser = new Gson().fromJson(sp.getString("USER_DATA", ""), SuccessResponseOfUser.class);
            countryCode = successResponseOfUser.getSuccess().getUser().getCountrycode();
        }
        return countryCode;
    }

    class SignInAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String resultSignIn, connectedOrNot, msg, code;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "Signing in ...");

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
                            Log.d("gcmid", regid);
                            System.out.print(regid);
                            storeRegistrationId(context, regid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    resultSignIn = postSignIn();
                    if (!resultSignIn.isEmpty()) {
                        jObj = new JSONObject(resultSignIn);
                        if (jObj.has("success")) {
                            Log.d("login user", resultSignIn);
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("USER_DATA", resultSignIn);
                            edit.putString("USER_DATA_DELIVERY_ADDRESS", resultSignIn);
                            edit.commit();
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                            code = jObjError.getString("code");
                        }
                    }
                } else {
                    connectedOrNot = "error";
                }
            } catch (JSONException je) {
                je.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
                progressDialog.dismiss();
                try {
                    if (connectedOrNot.equals("success")) {
                        if (!resultSignIn.isEmpty()) {
                            if (jObj.has("success")) {
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                islogedin = true;
                                backPresed = false;
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("USER", resultSignIn);
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                if (code.equals("AU003")) {
                                    Intent goToVerifyActivity = new Intent(SignInActivity.this, VerifyUserActivity.class);
                                    startActivity(goToVerifyActivity);
                                    finish();
                                }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCountryCodeList() throws Exception{
        String resultGetCountryCode = "";
        resultGetCountryCode = ServerConnection.executeGet(getApplicationContext(), "/api/countrycode");

        Log.d("response of resultGetCountryCode",resultGetCountryCode);
        return resultGetCountryCode;
    }

    class CountryCodeAsync extends AsyncTask<String,Integer,String>{
        String resultGetCountryCode, msg, code, connectedOrNot;
        JSONObject jObj;
        SuccessResponseForCountryCode successResponseForCountryCode;
        @Override
        protected String doInBackground(String... strings) {
            try {
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetCountryCode = getCountryCodeList();
                    if(!resultGetCountryCode.isEmpty()){
                       jObj = new JSONObject(resultGetCountryCode);
                        if(jObj.has("success")){
                           successResponseForCountryCode = new Gson().fromJson(resultGetCountryCode, SuccessResponseForCountryCode.class);
                        }else{
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                            code = jObjError.getString("code");
                        }
                    }
                }else{
                    connectedOrNot = "error";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if(!resultGetCountryCode.isEmpty()){
                        if(jObj.has("success")){
                            CountryCodeAdapter objCountryCodeAdapter = new CountryCodeAdapter(getApplicationContext(), 0 ,successResponseForCountryCode.getSuccess().getCountrycode());
                            countryCodeSpinner.setAdapter(objCountryCodeAdapter);
                            for(int i = 0 ; i < successResponseForCountryCode.getSuccess().getCountrycode().size() ; i++) {
                                if(loadCountryCodePreference().equals(successResponseForCountryCode.getSuccess().getCountrycode().get(i).getCode()))
                                countryCodeSpinner.setSelection(i);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Server is not responding please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
