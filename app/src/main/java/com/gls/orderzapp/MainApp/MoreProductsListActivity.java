package com.gls.orderzapp.MainApp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.gls.orderzapp.Provider.Adapters.GridAdapterProduct;
import com.gls.orderzapp.Provider.Adapters.GridAdapterProviderCategories;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.Provider.Beans.ProviderDetails;
import com.gls.orderzapp.Provider.Beans.ProviderSuccessResponse;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 22/4/14.
 */
public class MoreProductsListActivity extends Activity implements View.OnClickListener {
    public Menu menu1;
    public boolean isFirstTime = true;
    ActionBar actionBar;
    Context context;
    GridView gridView;
    ProviderSuccessResponse providerSuccessResponse;
    ProviderDetails providerDetails;
    GridAdapterProduct gridAdapterProduct;
    List<ProductDetails> productDetailsList = new ArrayList<>();

    SwipeRefreshLayout swipeView;
    String lastProductId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);
        context = MoreProductsListActivity.this;
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);

        actionBar = getActionBar();
//        Set Actionbar title null
        actionBar.setTitle("");


        providerDetails = new ProviderDetails();
        providerDetails = new Gson().fromJson(getIntent().getStringExtra("MoreProductsListActivity"), ProviderDetails.class);
        gridView = (GridView) findViewById(R.id.grid_categories);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setEnabled(false);
        String[] s = new String[1];
        s[0] = "1";
        new GetMoreProductAsync().execute(s);
        swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_purple, android.R.color.holo_orange_dark, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeView.setRefreshing(false);
                        new GetMoreProductAsync().execute(lastProductId);
                    }
                }, 3000);
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.loadmore_activity_menu, menu);
        menu1 = menu;

        onCreateOptions(menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View view) {
        Intent productcart = new Intent(MoreProductsListActivity.this, CartActivity.class);
        startActivity(productcart);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstTime == true) {
            isFirstTime = false;
        } else {
            onCreateOptions(menu1);
        }
    }

    @Override
    public void onBackPressed() {
        isFirstTime = true;
        super.onBackPressed();
    }

    public void onCreateOptions(Menu menu) {
        menu.findItem(R.id.loadmore_cart).getActionView().setOnClickListener(this);
        Cart.cartCountLoadMore(menu);
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

    public String getMoreProducts() {
        String resultOfMoreProducts = "";
        try {
            Log.d("1", "1");
            resultOfMoreProducts = ServerConnection.executeGet(getApplicationContext(), "/api/loadmoreproduct/" + GridAdapterProviderCategories.branch_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfMoreProducts;
    }

    public String getMoreProducts(String lastproduct_id) {
        String resultOfMoreProducts = "";
        try {
            Log.d("2", "2");
            resultOfMoreProducts = ServerConnection.executeGet(getApplicationContext(), "/api/loadmoreproduct/" + GridAdapterProviderCategories.branch_id + "?productid=" + lastproduct_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfMoreProducts;
    }


    public class GetMoreProductAsync extends AsyncTask<String, Integer, String> {
        String resultGetProviderAndProduct, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
            Log.d("In Async", "1");
//            progressDialog = ProgressDialog.show(StartUpActivity.this, "", "");
//            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Log.d("In Async", "2");
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    Log.d("In Async", "4");
                    connectedOrNot = "success";
                    if (params[0].equals("1")) {
                        resultGetProviderAndProduct = getMoreProducts();
                    } else {
                        resultGetProviderAndProduct = getMoreProducts(params[0]);
                    }
                    Log.d("In Async", "5");

                    if (!resultGetProviderAndProduct.isEmpty()) {
                        jObj = new JSONObject(resultGetProviderAndProduct);
                        if (jObj.has("success")) {
                            Log.d("more resultGetProviderAndProduct", resultGetProviderAndProduct);
                            providerSuccessResponse = new ProviderSuccessResponse();
                            providerSuccessResponse = new Gson().fromJson(resultGetProviderAndProduct, ProviderSuccessResponse.class);
                            if (providerDetails.getProvider() != null) {
                                providerSuccessResponse.getSuccess().getProvider().get(0).setProvider(providerDetails.getProvider());
                            }
                            if (providerDetails.getProvider().getProviderbrandname() != null) {
                                providerSuccessResponse.getSuccess().getProvider().get(0).getProvider().setProviderbrandname(providerDetails.getProvider().getProviderbrandname());
                            }
                            if (providerDetails.getProvider().getProviderid() != null) {
                                providerSuccessResponse.getSuccess().getProvider().get(0).getProvider().setProviderid(providerDetails.getProvider().getProviderid());
                            }
                            if (providerDetails.getBranch() != null) {
                                providerSuccessResponse.getSuccess().getProvider().get(0).setBranch(providerDetails.getBranch());
                            }
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
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
                if (connectedOrNot.equals("success")) {
//                    MoreProductsListActivity.moreProductslinear.removeAllViews();
                    if (!resultGetProviderAndProduct.isEmpty()) {
                        if (jObj.has("success")) {

                            lastProductId = providerSuccessResponse.getSuccess().getProvider().get(0).getProducts().get(providerSuccessResponse.getSuccess().getProvider().get(0).getProducts().size() - 1).getProductid();
                            Toast.makeText(getApplicationContext(), providerSuccessResponse.getSuccess().getMessage(), Toast.LENGTH_LONG).show();
                            productDetailsList.addAll(providerSuccessResponse.getSuccess().getProvider().get(0).getProducts());

                            gridAdapterProduct = new GridAdapterProduct(context, productDetailsList, providerSuccessResponse.getSuccess().getProvider().get(0).getBranch(), providerDetails);
                            gridView.setAdapter(gridAdapterProduct);
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
