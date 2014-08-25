package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.R;
import com.gls.orderzapp.SignIn.ForgotPasswordPostData;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by prajyot on 23/4/14.
 */
public class ForgotPasswordActivity extends Activity {
    public static TextView textOtp, otp;
    LinearLayout llMobileNumber, llOtp;
    EditText editMobileNumber, editOtp;
    //    TextView textRegenerateToken;
    Button buttonMobileSubmit, buttonOtpSubmit;
    ForgotPasswordPostData forgotPasswordPostData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);

        findViewsById();

        if (loadPreference() == false) {
            llMobileNumber.setVisibility(View.GONE);
            llOtp.setVisibility(View.VISIBLE);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.regenarate_verification_tokken, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.reg_verification_tokken:
                Intent regeneratIntent = new Intent(ForgotPasswordActivity.this, RegenerateVerificationToken.class);
                startActivity(regeneratIntent);
                this.finish();
//                llMobileNumber.setVisibility(View.VISIBLE);
//                llOtp.setVisibility(View.GONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void findViewsById() {
        llMobileNumber = (LinearLayout) findViewById(R.id.llMobileNumber);
        llOtp = (LinearLayout) findViewById(R.id.llOtp);
        editMobileNumber = (EditText) findViewById(R.id.editMobile);
        editOtp = (EditText) findViewById(R.id.editOtp);
        buttonMobileSubmit = (Button) findViewById(R.id.buttonSubmitMobileNumber);
        buttonOtpSubmit = (Button) findViewById(R.id.buttonSubmitOtp);
//        textRegenerateToken = (TextView)    findViewById(R.id.textRegenerateToken);
        textOtp = (TextView) findViewById(R.id.textOtp);
        otp = (TextView) findViewById(R.id.otp);
    }

    public void submitMobileNumber(View view) {
        if (editMobileNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_LONG).show();
            return;
        }
        new SubmitMobileNumberAsync().execute();
    }

//    public void regenerateToken(View view){
//        llMobileNumber.setVisibility(View.VISIBLE);
//        llOtp.setVisibility(View.GONE);
//    }

    public void submitOtp(View view) {
        if (editOtp.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "PLease enter otp", Toast.LENGTH_LONG).show();
            return;
        }
        new SubmitOtpAsync().execute();
    }

    public void savePreference(boolean isverified) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("ISVERIFIED", isverified);
        edit.commit();
    }

    public boolean loadPreference() {
        boolean preference = true;
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preference = spLoad.getBoolean("ISVERIFIED", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preference;
    }

    public String postMobileNumberForgotPassword() {
        String resultForPostMobileNumber = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(forgotPasswordPostData);
            resultForPostMobileNumber = ServerConnection.executePost1(jsonToSendOverServer, "/api/forgotpassword");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultForPostMobileNumber;
    }

    public void setMobileNumberForgotPasswordPostData() {
        forgotPasswordPostData = new ForgotPasswordPostData();
        forgotPasswordPostData.setMobileno("91" + editMobileNumber.getText().toString().trim());
    }

    public String postOtpForgotPassword() {
        String resultForPostMobileNumber = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(forgotPasswordPostData);
            resultForPostMobileNumber = ServerConnection.executePost1(jsonToSendOverServer, "/api/resetpassword");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultForPostMobileNumber;
    }

    public void setOtpForgotPasswordPostData() {
        forgotPasswordPostData = new ForgotPasswordPostData();
        forgotPasswordPostData.setOtp(editOtp.getText().toString().trim());
    }

    public static class SmsReceiver extends BroadcastReceiver {
        final SmsManager sms = SmsManager.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {
            //---get the SMS message passed in---
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String str = "";

            if (bundle != null) {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    if (msgs[i].getOriginatingAddress().equals("LM-HLPHND")) {

                        str += msgs[i].getMessageBody().toString();
                        str += "\n";
                    }
                }

                //---display the new SMS message---
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

                if (!str.trim().equals("")) {
                    otp.setText(str);
                }
            }
        }
    }

    private class SubmitMobileNumberAsync extends AsyncTask<String, Integer, String> {
        String resultPostMobileNumber, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setMobileNumberForgotPasswordPostData();
                    resultPostMobileNumber = postMobileNumberForgotPassword();
                    if (!resultPostMobileNumber.isEmpty()) {
                        jObj = new JSONObject(resultPostMobileNumber);
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
//            progressDialog.dismiss();
            try {
                if (connectedOrNot.equals("success")) {
                    if (!resultPostMobileNumber.isEmpty()) {
                        if (jObj.has("success")) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            savePreference(false);
                            llMobileNumber.setVisibility(View.GONE);
                            llOtp.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server not responding please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SubmitOtpAsync extends AsyncTask<String, Integer, String> {
        String resultPostOtp, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setOtpForgotPasswordPostData();
                    resultPostOtp = postOtpForgotPassword();
                    if (!resultPostOtp.isEmpty()) {
                        jObj = new JSONObject(resultPostOtp);
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
//            progressDialog.dismiss();
            try {
                if (connectedOrNot.equals("success")) {
                    if (!resultPostOtp.isEmpty()) {
                        if (jObj.has("success")) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            savePreference(true);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server not responding please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;
}
