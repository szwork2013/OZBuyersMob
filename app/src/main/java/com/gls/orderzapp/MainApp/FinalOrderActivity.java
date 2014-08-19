package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseAdapters.AdapterForFinalOrderMultipleProviders;
import com.gls.orderzapp.CreateOrder.OrderResponseAdapters.HomeDeliveryAddressAdapter;
import com.gls.orderzapp.CreateOrder.OrderResponseAdapters.PickupAddressAdapter;
import com.gls.orderzapp.CreateOrder.OrderResponseBeans.SuccessResponseForCreateOrder;
import com.gls.orderzapp.Payment.PaymentSuccessResponse;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ResetStaticData;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by prajyot on 6/5/14.
 */
public class FinalOrderActivity extends Activity {
    Context context;
    public static LinearLayout listProducts,  ll_txn_details;
    TextView orderNumber, billing_address_textview,  paymentMode, grand_total,
            txt_expected_delivery_date, bank_name, transaction_id, card_type, txn_amount;
    SuccessResponseForCreateOrder successResponseForCreateOrder;
    PaymentSuccessResponse paymentSuccessResponse;
    String paymentResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=FinalOrderActivity.this;
        setContentView(R.layout.final_order);
        Cart.hm.clear();

        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);

        findViewsById();
        paymentResponse = getIntent().getStringExtra("TXN_DETAILS");
        if(!paymentResponse.isEmpty()){
            paymentSuccessResponse = new Gson().fromJson(paymentResponse, PaymentSuccessResponse.class);
        }
        successResponseForCreateOrder = new Gson().fromJson(getIntent().getStringExtra("FINAL_ORDER"), SuccessResponseForCreateOrder.class);
        Log.d("successResponseForCreateOrder",new Gson().toJson(successResponseForCreateOrder));
        setOrderDetailsData();

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
        listProducts = (LinearLayout) findViewById(R.id.listProducts);
        orderNumber = (TextView) findViewById(R.id.order_no);
         ll_txn_details = (LinearLayout) findViewById(R.id.ll_txn_details);
        billing_address_textview = (TextView) findViewById(R.id.billing_address_textview);
        paymentMode = (TextView) findViewById(R.id.payment_mode);
        grand_total = (TextView) findViewById(R.id.grand_total);
        txt_expected_delivery_date = (TextView) findViewById(R.id.txt_expected_delivery_date);
        bank_name = (TextView) findViewById(R.id.bank_name);
        transaction_id = (TextView) findViewById(R.id.transaction_id);
        card_type = (TextView) findViewById(R.id.card_type);
        txn_amount = (TextView) findViewById(R.id.txn_amount);
    }

    @Override
    public void onBackPressed() {
        successResponseForCreateOrder = null;
        ResetStaticData.ResetData();
        Intent goToStartUpActivity = new Intent(FinalOrderActivity.this, StartUpActivity.class);
        startActivity(goToStartUpActivity);
        finish();
    }

    public void setOrderDetailsData() {
        try {
            if (successResponseForCreateOrder.getSuccess().getOrder().getOrderid() != null) {
                orderNumber.setText(successResponseForCreateOrder.getSuccess().getOrder().getOrderid());
            }
            if (successResponseForCreateOrder.getSuccess().getOrder().getTotal_order_price() != 0) {
                grand_total.setText(String.format("%.2f", successResponseForCreateOrder.getSuccess().getOrder().getTotal_order_price()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //*****************
        if(successResponseForCreateOrder.getSuccess().getOrder().getPreferred_delivery_date() != null) {
            try {

                final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                final DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");

                outputFormat.setTimeZone(tz);

                Date order_date = inputFormat.parse(successResponseForCreateOrder.getSuccess().getOrder().getPreferred_delivery_date());
                txt_expected_delivery_date.setText(outputFormat.format(order_date));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //*******************
        new AdapterForFinalOrderMultipleProviders(context, successResponseForCreateOrder.getSuccess().getOrder().getSuborder()).setMultipleProvidersList();
        try {
            if (successResponseForCreateOrder.getSuccess().getOrder().getPayment().getMode() != null && successResponseForCreateOrder.getSuccess().getOrder().getPayment().getMode().equals("COD")) {
                paymentMode.setText("Cash On Delivery");
                ll_txn_details.setVisibility(View.GONE);

            } else {
                paymentMode.setText("Payment by card");
                ll_txn_details.setVisibility(View.VISIBLE);
            }
            if(!paymentResponse.isEmpty()) {
                if (paymentSuccessResponse.getmMap() != null) {
                    if (paymentSuccessResponse.getmMap().getBANKNAME() != null) {
                        bank_name.setText(paymentSuccessResponse.getmMap().getBANKNAME());
                    }
                    if (paymentSuccessResponse.getmMap().getTXNID() != null) {
                        transaction_id.setText(paymentSuccessResponse.getmMap().getTXNID());
                    }
                    if (paymentSuccessResponse.getmMap().getPAYMENTMODE() != null) {
                        card_type.setText(paymentSuccessResponse.getmMap().getPAYMENTMODE());
                    }
                    if (paymentSuccessResponse.getmMap().getTXNAMOUNT() != null) {
                        txn_amount.setText(paymentSuccessResponse.getmMap().getTXNAMOUNT());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < successResponseForCreateOrder.getSuccess().getOrder().getSuborder().size(); i++) {

                if (successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address() != null && i == 0) {
                    billing_address_textview.setText(successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getAddress1() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getAddress2() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getArea() + ",\n" +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getCity() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getZipcode() + "\n" +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getState() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getCountry());
                }
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public void continueShopping(View view) {
        successResponseForCreateOrder = null;
        ResetStaticData.ResetData();
        Intent goToStartUpActivity = new Intent(FinalOrderActivity.this, StartUpActivity.class);
        startActivity(goToStartUpActivity);
        finish();
    }
}