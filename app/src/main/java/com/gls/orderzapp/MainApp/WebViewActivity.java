package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Help.SuccessResponseForSupportContact;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by avinash on 16/6/14.
 */
public class WebViewActivity extends Activity {
    String url;
    LinearLayout ll_support_numbers, ll_phone_email;
    TextView email;
    List<String> contact_supports = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        setTitle(getIntent().getStringExtra("ACTIVITY_NAME"));

        ll_support_numbers = (LinearLayout) findViewById(R.id.ll_support_numbers);
        ll_phone_email = (LinearLayout) findViewById(R.id.ll_phone_email);
        email = (TextView) findViewById(R.id.email);
        if(getIntent().getStringExtra("ACTIVITY_NAME").equals("Help") || getIntent().getStringExtra("ACTIVITY_NAME").equals("About")){
            displaySupportNumbers();
        }
        WebView web_view = (WebView) findViewById(R.id.web_view);
        url = getIntent().getStringExtra("URL");
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.getSettings().setDisplayZoomControls(true);
        web_view.getSettings().setDefaultFontSize(20);
        web_view.setWebViewClient(new MyWebViewClient());
        web_view.loadUrl(url);

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

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;

        }
    }

    public void displaySupportNumbers(){
        ll_phone_email.setVisibility(View.VISIBLE);
        new GetSupportNumbers().execute();
    }

    public String getSupportNumbers() throws Exception{
        String supportNumbers = "";
        supportNumbers = ServerConnection.executeGet(getApplicationContext(), "/api/orderzapp/contactsupport");
        return supportNumbers;
    }

    public class GetSupportNumbers extends AsyncTask<String, Integer, String> {
        public JSONObject jObj;
        String connectedOrNot, msg, code, resultSupportNumbers;
        ProgressDialog progressDialog;
        SuccessResponseForSupportContact successResponseForSupportContact;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(WebViewActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultSupportNumbers = getSupportNumbers();
                    if (!resultSupportNumbers.isEmpty()) {
                        Log.d("support numbers", resultSupportNumbers);
                        jObj = new JSONObject(resultSupportNumbers);
                        if(jObj.has("success")){
                            successResponseForSupportContact = new Gson().fromJson(resultSupportNumbers, SuccessResponseForSupportContact.class);
                            for(int a = 0; a < successResponseForSupportContact.getSuccess().getOz_contactsupport().getPhone().size(); a++){
                                if(a<2) {
                                    contact_supports.add(successResponseForSupportContact.getSuccess().getOz_contactsupport().getPhone().get(a));
                                }
                            }
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
                    if (!resultSupportNumbers.isEmpty()) {
                        if (jObj.has("success")) {
                            for (int i = 0; i < contact_supports.size(); i++) {
                                final TextView number = new TextView(getApplicationContext());
                                number.setTextColor(Color.parseColor("#304f6c"));
                                if(i == contact_supports.size()-1) {
                                    number.setText(contact_supports.get(i));
                                }else{
                                    number.setText(contact_supports.get(i)+", ");
                                }
                                number.setId(i+100);
                                ll_support_numbers.addView(number, i+1);

                                number.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + contact_supports.get(view.getId() - 100)));
                                        startActivity(callIntent);
                                    }
                                });

                            }

                            if(successResponseForSupportContact.getSuccess().getOz_contactsupport().getEmail() != null) {
                                email.setText(successResponseForSupportContact.getSuccess().getOz_contactsupport().getEmail());

                                email.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(Intent.ACTION_SENDTO);
                                        i.setType("text/plain");
                                        i.setData(Uri.parse("mailto:" + successResponseForSupportContact.getSuccess().getOz_contactsupport().getEmail()));
                                        i.putExtra(Intent.EXTRA_SUBJECT, "");
                                        i.putExtra(Intent.EXTRA_TEXT   , "");
                                        try {
                                            startActivity(Intent.createChooser(i, "Send mail..."));
                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(WebViewActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
