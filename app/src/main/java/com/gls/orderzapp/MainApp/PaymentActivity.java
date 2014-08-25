package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.SuccessResponseForCreateOrder;
import com.gls.orderzapp.OZConstants;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;


/**
 * Created by prajyot on 19/6/14.
 */
public class PaymentActivity extends Activity {
    SuccessResponseForCreateOrder successResponseForCreateOrder;
    SuccessResponseOfUser successResponseOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        successResponseForCreateOrder = new Gson().fromJson(getIntent().getStringExtra("FINAL_ORDER"), SuccessResponseForCreateOrder.class);
        try {
            successResponseOfUser = new Gson().fromJson(loadPreferencesUser(), SuccessResponseOfUser.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Changed by ramee on 4th-Aug-2014
        //Renamed the func1(0 to makePayment()
        makePayment();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
//        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
//        com.google.android.gms.analytics.GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public void makePayment() {
        //Getting the Service Instance. PaytmPGService.getStagingService() will return
        // the Service pointing to Staging Environment.
        //and PaytmPGService.getProductionService() will return the Service pointing to
        // Production Environment.

        //The Product Flavors will pick up the right paytm environment for the APK
        PaytmPGService Service = null;
        if (OZConstants.PAYTM_STAGING_OR_PRODUCTION == "STAGING") {
            Service = PaytmPGService.getStagingService();
        } else if (OZConstants.PAYTM_STAGING_OR_PRODUCTION == "PRODUCTION") {
            Service = PaytmPGService.getProductionService();
        }

        //Create new order Object having all order information.
        Log.d("orderid", successResponseForCreateOrder.getSuccess().getOrder().getOrderid());
        PaytmOrder Order = new PaytmOrder(successResponseForCreateOrder.getSuccess().getOrder().getOrderid(), successResponseOfUser.getSuccess().getUser().getUserid(), successResponseForCreateOrder.getSuccess().getOrder().getTotal_order_price() + "",
                successResponseOfUser.getSuccess().getUser().getEmail(), successResponseOfUser.getSuccess().getUser().getMobileno());

        //Create new Merchant Object having all merchant configuration.
//        PaytmMerchant Merchant = new PaytmMerchant("Giantl00830321943927", "WAP", "Retail", "giantleapsystems", "javas",
//                ServerConnection.url + "/api/paytm/generatechecksum", ServerConnection.url + "/api/orderzapp/payment");

        PaytmMerchant Merchant = new PaytmMerchant(OZConstants.PAYTM_MERCHANT_ID, "WAP", OZConstants.PAYTM_INDUSTRY_TYPE_ID, "giantleapsystems", "merchant",
                ServerConnection.url + "/api/paytm/generatechecksum", ServerConnection.url + "/api/orderzapp/payment");


        //Create Client Certificate object holding the information related to Client Certificate. Filename must be without .p12
        //        extension.
        //For example, if suppose client.p12 is stored in raw folder, then filename must be client.
        //PaytmClientCertificate Certificate = new PaytmClientCertificate("Paytm@197", "Giantleapsystems1");

        //Set PaytmOrder, PaytmMerchant and PaytmClientCertificate Object. Call this method and set both objects before
        //starting transaction.
        Service.initialize(Order, Merchant, null);

        //Start the Payment Transaction. Before starting the transaction ensure that initialize method is called.
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                // Some UI Error Occurred in Payment Gateway Activity.
                // This may be due to initialization of views in Payment Gateway Activity or may be due to
                // initialization of webview.
                // Error Message details the error occurred.
                Toast.makeText(getApplicationContext(), "UI Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTransactionSuccess(Bundle inResponse) {
                // After successful transaction this method gets called.
                // Response bundle contains the merchant response parameters.
                Toast.makeText(getApplicationContext(), "Payment Transaction Success", Toast.LENGTH_SHORT).show();

                //successResponseForCreateOrder.getSuccess().getOrder().ge

                Log.d("bundle", new Gson().toJson(inResponse));

                Intent goToFinalOrderActivity = new Intent(PaymentActivity.this, FinalOrderActivity.class);
                goToFinalOrderActivity.putExtra("FINAL_ORDER", getIntent().getStringExtra("FINAL_ORDER"));
                goToFinalOrderActivity.putExtra("TXN_DETAILS", new Gson().toJson(inResponse));
                goToFinalOrderActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToFinalOrderActivity);
            }

            @Override
            public void onTransactionFailure(String inErrorMessage, Bundle inResponse) {
                // This method gets called if transaction failed.
                // Here in this case transaction is completed, but with a failure.
                // Error Message describes the reason for failure.
                // Response bundle contains the merchant response parameters.
                Toast.makeText(getApplicationContext(), "Payment TXN Failure", Toast.LENGTH_SHORT).show();
                PaymentActivity.this.finish();
            }

            @Override
            public void networkNotAvailable() {
                // If network is not available, then this method gets called.
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                // This method gets called if client authentication failed.
                // Failure may be due to following reasons
                // 1. Server error or downtime.
                // 2. Server unable to generate checksum or checksum response is not in proper format.
                // 3. Server failed to authenticate that client. That is value of payt_STATUS is 2.
                // Error Message describes the reason for failure.
                Log.d("error mesage", inErrorMessage);
                Toast.makeText(getApplicationContext(), "Authentication Error  " + inErrorMessage, Toast.LENGTH_SHORT).show();
                PaymentActivity.this.finish();
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {


                // This page gets called if some error occurred while loading some URL in Webview.
                // Error Code and Error Message describes the error.
                // Failing Url is the Url that failed to load.
                Toast.makeText(getApplicationContext(), "webview Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String loadPreferencesUser() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            user = spLoad.getString("USER_DATA", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
