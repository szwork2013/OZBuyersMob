package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.MyOrders.Beans.OrderDetails;
import com.gls.orderzapp.MyOrders.MyOrderDetailAdapters.AdapterForSubOrders;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.google.gson.Gson;

/**
 * Created by prajyot on 7/5/14.
 */
public class DetailedMyOrderActivity extends Activity {
    public static LinearLayout listProducts;
    OrderDetails orderDetails;
    Context context;
    TextView order_no, billing_address_textview, paymentMode, grand_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_order);
        setTitle("Detailed Order");
        context = DetailedMyOrderActivity.this;
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        Log.d("order details", getIntent().getStringExtra("ORDER"));
        findViewsById();
        setOrderData();
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
        order_no = (TextView) findViewById(R.id.order_no);
        billing_address_textview = (TextView) findViewById(R.id.billing_address_textview);
        paymentMode = (TextView) findViewById(R.id.payment_mode);
        grand_total = (TextView) findViewById(R.id.grand_total);
    }

    public void setOrderData() {
        orderDetails = new Gson().fromJson(getIntent().getStringExtra("ORDER"), OrderDetails.class);
        try {
            if (orderDetails.getOrderid() != null) {
                order_no.setText(orderDetails.getOrderid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (orderDetails.getSuborder() != null) {
            new AdapterForSubOrders(context, orderDetails.getSuborder()).setMultipleProvidersList();
        }

        if (orderDetails.getTotal_order_price() != null) {
            grand_total.setText(String.format("%.2f", Double.parseDouble(orderDetails.getTotal_order_price())));
        }
        try {
            if (orderDetails.getPayment().getMode() != null && orderDetails.getPayment().getMode().equals("COD")) {
                paymentMode.setText("Cash On Delivery");
            } else {
                paymentMode.setText("Payment by Card");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < orderDetails.getSuborder().size(); i++) {
            Log.d("billing address", new Gson().toJson(orderDetails.getSuborder().get(i).getBilling_address()));

            try {
                if (orderDetails.getSuborder().get(0).getBilling_address() != null) {
                    billing_address_textview.setText(orderDetails.getSuborder().get(i).getBilling_address().getAddress1() + "\n" +
                            orderDetails.getSuborder().get(i).getBilling_address().getAddress2() + "\n" +
                            orderDetails.getSuborder().get(i).getBilling_address().getArea() + "\n" +
                            orderDetails.getSuborder().get(i).getBilling_address().getCity() + "\n" +
                            orderDetails.getSuborder().get(i).getBilling_address().getZipcode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}