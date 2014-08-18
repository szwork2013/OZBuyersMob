package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gls.orderzapp.MyOrders.Beans.OrderDetails;
import com.gls.orderzapp.MyOrders.MyOrderDetailAdapters.AdapterForSubOrders;
import com.gls.orderzapp.MyOrders.MyOrderDetailAdapters.DisplayPickupAddressesAdapter;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by prajyot on 7/5/14.
 */
public class DetailedMyOrderActivity extends Activity {
    public static LinearLayout listProducts;
    OrderDetails orderDetails;
    Context context;
    TextView order_no, billing_address_textview, shipping_address_textview, paymentMode, txt_expected_delivery_date, grand_total;
    LinearLayout ll_home_delivery_address;
    int pickupAddress = 0;
    ListView address_list;

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
        shipping_address_textview = (TextView) findViewById(R.id.shipping_address_textview);
        paymentMode = (TextView) findViewById(R.id.payment_mode);
        ll_home_delivery_address = (LinearLayout) findViewById(R.id.ll_home_delivery_address);
        txt_expected_delivery_date = (TextView) findViewById(R.id.txt_expected_delivery_date);
        address_list = (ListView) findViewById(R.id.address_list);
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

        if(orderDetails.getTotal_order_price() != null){
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

            if(orderDetails.getSuborder().get(i).getDeliverytype().equalsIgnoreCase("pickup")){
                pickupAddress++;
            }

            if(orderDetails.getSuborder().get(0).getPrefdeldtime() != null) {
                try {

                    final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                    final DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                    TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");

                    outputFormat.setTimeZone(tz);

                    Date order_date = inputFormat.parse(orderDetails.getSuborder().get(0).getPrefdeldtime());
                    txt_expected_delivery_date.setText(outputFormat.format(order_date));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
            try {
                if (orderDetails.getSuborder().get(i).getDelivery_address() != null) {
                    ll_home_delivery_address.setVisibility(View.VISIBLE);
//                    shipping_address_textview.setVisibility(View.VISIBLE);
                    shipping_address_textview.setText(orderDetails.getSuborder().get(0).getDelivery_address().getAddress1() + "\n" +
                            orderDetails.getSuborder().get(0).getDelivery_address().getAddress2() + "\n" +
                            orderDetails.getSuborder().get(0).getDelivery_address().getArea() + "\n" +
                            orderDetails.getSuborder().get(0).getDelivery_address().getCity() + "\n" +
                            orderDetails.getSuborder().get(0).getDelivery_address().getZipcode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(pickupAddress > 0){
            address_list.setAdapter(new DisplayPickupAddressesAdapter(getApplicationContext(), orderDetails.getSuborder()));
            setListViewHeightBasedOnChildren(address_list);
        }
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