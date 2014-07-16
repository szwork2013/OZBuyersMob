package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.gls.orderzapp.Verification.RegeneratePostData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by prajyot on 23/4/14.
 */
public class RegenerateVerificationToken extends Activity {
    EditText editMobile;
    Button submitMobileNumber;
    RegeneratePostData regeneratePostData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regenerate_verification_token);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);

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
        editMobile = (EditText) findViewById(R.id.editMobile);
        submitMobileNumber = (Button) findViewById(R.id.buttonSubmitMobileNumber);
    }

    public void submitMobileNumber(View view) {
        if (editMobile.getText().toString().trim().length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter a correct mobile number", Toast.LENGTH_LONG).show();
            return;
        }
        new SubmitMobileNumberAsync().execute();
    }

    public String postMobileNumberRegenerateOtp() {
        String resultPostMobileNumberRegenerateOtp = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(regeneratePostData);
            resultPostMobileNumberRegenerateOtp = ServerConnection.executePost1(jsonToSendOverServer, "/api/regenerateverificationtoken");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPostMobileNumberRegenerateOtp;
    }

    public void setPostMobileNumberData() {
        regeneratePostData = new RegeneratePostData();
        regeneratePostData.setMobileno("91" + editMobile.getText().toString().trim());
    }

    private class SubmitMobileNumberAsync extends AsyncTask<String, Integer, String> {
        String resultPostMobileNumber, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(RegenerateVerificationToken.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setPostMobileNumberData();
                    resultPostMobileNumber = postMobileNumberRegenerateOtp();
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
}
