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
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.gls.orderzapp.Utility.UtilityClassForLanguagePreferance;
import com.gls.orderzapp.Verification.VerificationPostData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by avinash on 2/4/14.
 */
public class VerifyUserActivity extends ActionBarActivity {

    EditText verificationEditText;
    Button verifyButton;
    Context context;
    VerificationPostData verificationPostData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = VerifyUserActivity.this;
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
//        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        setContentView(R.layout.verify_user_activity);
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
        verificationEditText = (EditText) findViewById(R.id.editTextVerifyUser);
        verifyButton = (Button) findViewById(R.id.verifyUser_button);

//        UtilityClassForLanguagePreferance.applyTypeface(UtilityClassForLanguagePreferance.getParentView(verificationEditText), UtilityClassForLanguagePreferance.getTypeFace(context));
    }

    public void verify(View view) {
        if (verificationEditText.getText().toString().trim().length() != 0) {
            new VerifyUserAsync().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Enter otp", Toast.LENGTH_LONG).show();
        }
    }

    public void regenerateToken(View view) {
        Intent goToRegenerateActivity = new Intent(VerifyUserActivity.this, RegenerateVerificationToken.class);
        startActivity(goToRegenerateActivity);
    }

    public String postVerify() {
        String resultVerify = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(verificationPostData);
            resultVerify = ServerConnection.executePost1(jsonToSendOverServer, "/api/user/verify");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVerify;
    }

    public void setPostVerifyData() {
        verificationPostData = new VerificationPostData();
        verificationPostData.setOtp(verificationEditText.getText().toString().trim());
    }

    class VerifyUserAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String resultVerify, connectedOrNot, msg, code;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
//            pd = ProgressDialog.show(VerifyUserActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setPostVerifyData();
                    resultVerify = postVerify();
                    if (!resultVerify.isEmpty()) {
                        jObj = new JSONObject(resultVerify);
                        if (jObj.has("success")) {
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("USER_DATA", resultVerify);
                            edit.putString("USER_DATA_DELIVERY_ADDRESS", resultVerify);
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
//                pd.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (!resultVerify.isEmpty()) {
                        if (jObj.has("success")) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
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

