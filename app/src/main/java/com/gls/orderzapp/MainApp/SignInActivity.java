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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.R;
import com.gls.orderzapp.SignIn.SignInPostData;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
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
    SignInPostData signInPostData;
    boolean backPresed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SignInActivity.this;
        //Get a Tracker (should auto-report)
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
//        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        setContentView(R.layout.sign_in);

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
        setPostSignInParameters();

        new SignInAsync().execute();
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
        signInPostData.setMobileno("91" + mobileNumberEditText.getText().toString().trim());
        signInPostData.setPassword(passwordEditText.getText().toString().trim());

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
//                                SignInActivity.this.finish();
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
}
