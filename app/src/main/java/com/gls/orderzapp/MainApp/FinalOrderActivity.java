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
    public  LinearLayout  linerlayout_delivery_address,ll_home_delivery_address;
    TextView orderNumber, billing_address_textview, shipping_address_textview, paymentMode, grand_total, delivery_type,
            txt_expected_delivery_date, bank_name, transaction_id, card_type, txn_amount;
    SuccessResponseForCreateOrder successResponseForCreateOrder;
    ListView address_list,lst_selller_name_homedelivery;
    int homedelivery=0,pickupdelivery=0;
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
        linerlayout_delivery_address = (LinearLayout) findViewById(R.id.linerlayout_delivery_address);
        ll_home_delivery_address = (LinearLayout) findViewById(R.id.ll_home_delivery_address);
         ll_txn_details = (LinearLayout) findViewById(R.id.ll_txn_details);
        billing_address_textview = (TextView) findViewById(R.id.billing_address_textview);
        shipping_address_textview = (TextView) findViewById(R.id.shipping_address_textview);
        paymentMode = (TextView) findViewById(R.id.payment_mode);
        delivery_type = (TextView) findViewById(R.id.delivery_type);
        grand_total = (TextView) findViewById(R.id.grand_total);
        txt_expected_delivery_date = (TextView) findViewById(R.id.txt_expected_delivery_date);
        address_list = (ListView) findViewById(R.id.address_list);
        lst_selller_name_homedelivery= (ListView) findViewById(R.id.lst_selller_name_homedelivery);
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
//            if (successResponseForCreateOrder.getSuccess().getOrder().getPreferred_delivery_date() != null) {
//                txt_expected_delivery_date.setText(successResponseForCreateOrder.getSuccess().getOrder().getPreferred_delivery_date());
//            }
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
            for (int k = 0; k < successResponseForCreateOrder.getSuccess().getOrder().getSuborder().size(); k++) {
                if(successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(k).getDeliverytype().equalsIgnoreCase("pickup"))
                {
                    pickupdelivery++;
                    Log.d("pickupdelivery size",pickupdelivery+"");
                }else if(successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(k).getDeliverytype().equalsIgnoreCase("home"))
                {
                    homedelivery++;
                    Log.d("homedelivery size",homedelivery+"");
                }
            }
            for (int i = 0; i < successResponseForCreateOrder.getSuccess().getOrder().getSuborder().size(); i++) {
//                Log.d("delivery type", successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDeliverytype());
                try {
                    if (i == 0) {
                        if (successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDeliverytype().equalsIgnoreCase("home")) {
                            delivery_type.setText("Home delivery");
                        } else {
                            delivery_type.setText("Pick-Up");
                        }
                    }
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }

                if (successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address() != null && i == 0) {
                    billing_address_textview.setText(successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getAddress1() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getAddress2() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getArea() + ",\n" +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getCity() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getZipcode() + "\n" +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getState() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getBilling_address().getCountry());
                }
                if (homedelivery > 0 &&
                        successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDeliverytype().equalsIgnoreCase("home")) {
                    ll_home_delivery_address.setVisibility(View.VISIBLE);
//                    lst_selller_name_homedelivery.setAdapter(new HomeDeliveryAddressAdapter(getApplicationContext(), successResponseForCreateOrder.getSuccess().getOrder().getSuborder()));
//                    setListViewHeightBasedOnChildren(lst_selller_name_homedelivery);
                    shipping_address_textview.setText(successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getAddress1() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getAddress2() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getArea() + ",\n" +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getCity() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getZipcode() + "\n" +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getState() + ", " +
                            successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDelivery_address().getCountry());
                } else {
                if( pickupdelivery > 0 &&
                        successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getDeliverytype().equalsIgnoreCase("pickup")){
                    linerlayout_delivery_address.setVisibility(View.VISIBLE);
                    Log.d("PickUpAddress", new Gson().toJson(successResponseForCreateOrder.getSuccess().getOrder().getSuborder().get(i).getPickup_address()));
                    address_list.setAdapter(new PickupAddressAdapter(context, successResponseForCreateOrder.getSuccess().getOrder().getSuborder()));
                    setListViewHeightBasedOnChildren(address_list);
                }
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

    public void setListViewHeightBasedOnChildren(ListView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new LinearLayout.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getDividerHeight());
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}