package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gls.orderzapp.MyOrders.Beans.SuccessResponseForMyOrders;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 5/5/14.
 */
public class MyOrdersListActivity extends Activity {
    public ListView mainOrderList;
    Context context;
    TextView txt_noOrder;
    public static SuccessResponseForMyOrders successResponseForMyOrders;
    public static ArrayList<ArrayList<String>> actualList = new ArrayList<>();
    public List<ArrayList<ArrayList<String>>> serverTrackingStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_list);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        findViewsById();
        context = MyOrdersListActivity.this;
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
    protected void onResume() {
        super.onResume();
    }

    public void findViewsById() {
        mainOrderList = (ListView) findViewById(R.id.listMainOrder);
        txt_noOrder = (TextView) findViewById(R.id.txt_noOrder);
    }

    public String getMyOrders(String queryString) {
        String resultOfMyOrders = "";
        try {
            resultOfMyOrders = ServerConnection.executeGet(getApplicationContext(), "/api/myorder" + "?criteriastatus=" + queryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfMyOrders;
    }

    public void noOrder(String status) {
        txt_noOrder.setVisibility(View.VISIBLE);
        if (status != null) {
            if (status.equals("packing")) {
                txt_noOrder.setText("No orders to Pack");
                mainOrderList.setVisibility(View.GONE);
            } else if (status.equals("delivery")) {
                txt_noOrder.setText("No orders to Deliver");
                mainOrderList.setVisibility(View.GONE);
            } else if (status.equals("approved")) {
                txt_noOrder.setText("No orders to approve");
                mainOrderList.setVisibility(View.GONE);
            }
        }
    }

    public void orderExists() {
        mainOrderList.setVisibility(View.VISIBLE);
        txt_noOrder.setVisibility(View.GONE);
    }

}
