package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.DisplayDeliveryChargesListAdapter;
import com.gls.orderzapp.Cart.Adapters.ProductCartAdapter;
import com.gls.orderzapp.Cart.Beans.BranchIdsForGettingDeliveryCharges;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForDeliveryCharges;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;


/**
 * Created by avinash on 7/4/14.
 */
public class ProductCartActivity extends ActionBarActivity {
    public static ListView lst_product_cart;
    public static TextView txt_sub_total;
    static Context context;
    static Button btn_place_an_order;
    static RelativeLayout summary_details;
    static LinearLayout delivery_layout, lstview;
    static TextView txt_summary, textDeliveryCharges;
    static ProductCartAdapter productCartAdapter;
    ActionBar actionBar;
    EditText editPincode;
    Boolean isEditTextVisible;
    ListView list_delivery_charges;
    BranchIdsForGettingDeliveryCharges branchIdsForGettingDeliveryCharges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productcart);
        context = ProductCartActivity.this;
        findViewsById();
        displayCart();


        btn_place_an_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Cart.checkForPrductConfigurarion() == true) {
                    Intent product_config = new Intent(ProductCartActivity.this, ProductConfigurationActivity.class);
                    startActivity(product_config);
                } else if (Cart.deleteFromCartIfQuantityIsZero() > 0) {
                    if (Cart.deleteFromCartIfQuantityIsZero() > 0) {
                        Intent deliveryActivity = new Intent(ProductCartActivity.this, DeliveryPaymentActivity.class);
                        startActivity(deliveryActivity);
                    } else {
                        displayCart();
                    }
                }
            }
        });

//        Log.d("sorted cart", new Gson().toJson(sortByValues(Cart.hm)));
        editPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editPincode.getText().toString().trim().length() == 6) {
                    new GetDeliveryChargesAsync().execute();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void displayCart() {
        if (Cart.getCount() > 0) {
            productCartAdapter = new ProductCartAdapter(context);
            lst_product_cart.setAdapter(productCartAdapter);
            setListViewHeightBasedOnChildren(lst_product_cart);
        } else {
            delivery_layout.setVisibility(View.GONE);
            btn_place_an_order.setVisibility(View.GONE);
            summary_details.setVisibility(View.GONE);
            lstview.removeAllViews();
            txt_summary.setText("No Product(s) added to cart");
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView gridView) {
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
        params.height = (totalHeight + (gridView.getDividerHeight() * listAdapter.getCount()));
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    private void findViewsById() {
        lst_product_cart = (ListView) findViewById(R.id.lst_product_cart);
        btn_place_an_order = (Button) findViewById(R.id.btn_place_an_order);
        summary_details = (RelativeLayout) findViewById(R.id.summary_details);
        txt_sub_total = (TextView) findViewById(R.id.txt_sub_total);
        txt_summary = (TextView) findViewById(R.id.txt_summary);
        editPincode = (EditText) findViewById(R.id.editPincode);
        textDeliveryCharges = (TextView) findViewById(R.id.text_delivery_charges);
        list_delivery_charges = (ListView) findViewById(R.id.list_delivery_charges);
        delivery_layout = (LinearLayout) findViewById(R.id.delivery_layout);
        lstview = (LinearLayout) findViewById(R.id.lstview);


        Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        txt_summary.setTypeface(tf);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent aboutUs = new Intent(context, AboutUsActivity.class);
            startActivity(aboutUs);
            return true;
        }
        if (id == R.id.action_help) {
            Intent help = new Intent(context, HelpActivity.class);
            startActivity(help);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent setting = new Intent(context, SettingsActivity.class);
            startActivity(setting);
            return true;
        }
        if (id == R.id.action_privacypolicy) {
//            Intent privacyPolicy = new Intent(context, PrivacyPolicyActivity.class);
//            startActivity(privacyPolicy);
            return true;
        }
        if (id == R.id.action_termncondition) {
//            Intent termsNcondition = new Intent(context, TermsAndconditionActivity.class);
//            startActivity(termsNcondition);
            return true;
        }
        if (id == R.id.action_signup) {
            Intent signUp = new Intent(context, SignUpActivity.class);
            startActivity(signUp);
            return true;
        }
        if (id == R.id.action_signin) {
            Intent signIn = new Intent(context, SignInActivity.class);
            startActivity(signIn);
            return true;
        }
        if (id == R.id.menu_search) {

            if (isEditTextVisible == false) {
                isEditTextVisible = true;
                actionBar.setCustomView(R.layout.startup_activity_action_bar);
                EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                        | ActionBar.DISPLAY_SHOW_HOME);
            } else {
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String getDeliverCharges() {
        String resultOfDeliveryCharges = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(branchIdsForGettingDeliveryCharges);
            resultOfDeliveryCharges = ServerConnection.executePost1(jsonToSendOverServer, "/api/deliverycharge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfDeliveryCharges;
    }

    public void setDataForGettingDeliveryCharges() {
        branchIdsForGettingDeliveryCharges = new BranchIdsForGettingDeliveryCharges();
        for (int i = 0; i < Cart.getCount(); i++) {
            branchIdsForGettingDeliveryCharges.getBranchids().add(Cart.getCartDetails().get(i).getBranchid());
        }
//        branchIdsForGettingDeliveryCharges.setZipcode(editPincode.getText().toString().trim());
    }

    private class GetDeliveryChargesAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String connectedOrNot, resultOfGetDeliveryCharges, msg, code;
        ProgressDialog progressDialog;
        SuccessResponseForDeliveryCharges successResponseForDeliveryCharges;
        boolean isDeliveryAvailable = false;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProductCartActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setDataForGettingDeliveryCharges();
                    resultOfGetDeliveryCharges = getDeliverCharges();
                    if (!resultOfGetDeliveryCharges.isEmpty()) {
                        jObj = new JSONObject(resultOfGetDeliveryCharges);
                        if (jObj.has("success")) {
                            successResponseForDeliveryCharges = new Gson().fromJson(resultOfGetDeliveryCharges, SuccessResponseForDeliveryCharges.class);
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
            progressDialog.dismiss();
            try {
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultOfGetDeliveryCharges.isEmpty()) {
                        if (jObj.has("success")) {

                            list_delivery_charges.setAdapter(new DisplayDeliveryChargesListAdapter(getApplicationContext(), successResponseForDeliveryCharges.getSuccess().getDeliverycharge()));

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