package com.gls.orderzapp.MainApp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gls.orderzapp.Provider.Adapters.AdapterForProviderCategories;
import com.gls.orderzapp.Provider.Beans.ProviderSuccessResponse;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.gls.orderzapp.Utility.UtilityClassForLanguagePreferance;
import com.google.gson.Gson;

import org.json.JSONObject;

//import android.support.v7.app.ActionBar;

/**
 * Created by avinash on 2/4/14.
 */
public class StartUpActivity extends Activity implements View.OnClickListener {
    public static LinearLayout linearLayoutCategories;
    public static String searchString;
   public static boolean isFirstTime = true;
    ActionBar actionBar;
    ImageView adBanner;
    ProviderSuccessResponse providerSuccessResponse;
    boolean isEditTextVisible = false;
    public static Menu menu1;
    public static MenuItem signin, signup, logout;
    EditText searchProducts = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Here activity is brought to front, not created,
            // so finishing this will get you to the last viewed activity
            finish();
            return;
        }
        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        //Get a Tracker (should auto-report)
        setContentView(R.layout.startup_activity);
        context = StartUpActivity.this;
        actionBar = getActionBar();
//        Set Actionbar title null
        actionBar.setTitle("");
        findViewsById();
//Get list of all product and provider
        new GetProviderAndProductListAsync().execute();
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

    //Initialize all the view
    public void findViewsById() {
        linearLayoutCategories = (LinearLayout) findViewById(R.id.linear_layout_categories);
        adBanner = (ImageView) findViewById(R.id.ad_banner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.startup_activity_menu, menu);
        menu1 = menu;
        signin = menu.findItem(R.id.action_signin);
        signup = menu.findItem(R.id.action_signup);
        logout = menu.findItem(R.id.action_logout);
        Log.d("oncreateoptions", "oncreateoptions");
        onCreateOptions(menu);

        if (SignInActivity.islogedin == true) {
            signin.setVisible(false);
            signup.setVisible(false);
            logout.setVisible(true);
        } else {
            signin.setVisible(true);
            signup.setVisible(true);
            logout.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onresume", "onresume");
        if (isFirstTime == true) {
            Log.d("if", "if");
            isFirstTime = false;
        } else {
            Log.d("else", "else");
            onCreateOptions(menu1);
        }
    }

    @Override
    public void onBackPressed() {
        isFirstTime = true;
        super.onBackPressed();
    }

    public void onCreateOptions(Menu menu) {
        try {
            menu.findItem(R.id.cart).getActionView().setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        Cart.cartCount(menu);
        if (SignInActivity.islogedin == true) {
            signin.setVisible(false);
            signup.setVisible(false);
            logout.setVisible(true);
        } else {
            signin.setVisible(true);
            signup.setVisible(true);
            logout.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent goToWebViewAbout = new Intent(StartUpActivity.this, WebViewActivity.class);
            goToWebViewAbout.putExtra("URL", ServerConnection.url + "/api/statictemplates?type=AU");
            goToWebViewAbout.putExtra("ACTIVITY_NAME", "About");
            startActivity(goToWebViewAbout);
            return true;
        }
        if (id == R.id.action_help) {
            Intent goToWebViewHelp = new Intent(StartUpActivity.this, WebViewActivity.class);
            goToWebViewHelp.putExtra("URL", ServerConnection.url + "/api/statictemplates?type=HP");
            goToWebViewHelp.putExtra("ACTIVITY_NAME", "Help");
            startActivity(goToWebViewHelp);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent setting = new Intent(StartUpActivity.this, SettingsActivity.class);
            startActivity(setting);
            return true;
        }
        if (id == R.id.action_privacypolicy) {
            Intent goToWebViewPrivacyPolicy = new Intent(StartUpActivity.this, WebViewActivity.class);
            goToWebViewPrivacyPolicy.putExtra("URL", ServerConnection.url + "/api/statictemplates?type=PP");
            goToWebViewPrivacyPolicy.putExtra("ACTIVITY_NAME", "Privacy Policy");
            startActivity(goToWebViewPrivacyPolicy);
            return true;
        }
        if (id == R.id.action_termncondition) {
            Intent goToWebViewTAndC = new Intent(StartUpActivity.this, WebViewActivity.class);
            goToWebViewTAndC.putExtra("URL", ServerConnection.url + "/api/statictemplates?type=TC");
            goToWebViewTAndC.putExtra("ACTIVITY_NAME", "Terms and Conditions");
            startActivity(goToWebViewTAndC);
            return true;
        }
        if (id == R.id.action_signup) {
            Intent signUp = new Intent(StartUpActivity.this, SignUpActivity.class);
            startActivity(signUp);
            return true;
        }
        if (id == R.id.action_signin) {
            Intent signIn = new Intent(StartUpActivity.this, SignInActivity.class);
            startActivity(signIn);
            return true;
        }
        if (id == R.id.action_logout) {
            new LogOutAsync().execute();
            return true;
        }
        if (id == R.id.action_My_Orders) {
            Intent goToMyOrdersActivity = new Intent(this, TabActivityForOrders.class);
            startActivity(goToMyOrdersActivity);
            return true;
        }
        if (id == R.id.action_get_social) {
            Intent goToGetSocialActivity = new Intent(StartUpActivity.this, GetSocialActivity.class);
            startActivity(goToGetSocialActivity);
            return true;
        }
        if (id == R.id.action_sellers_agreement) {
            Intent goToWebViewSellersAgreement = new Intent(StartUpActivity.this, WebViewActivity.class);
            goToWebViewSellersAgreement.putExtra("URL", ServerConnection.url + "/api/statictemplates?type=SA");
            goToWebViewSellersAgreement.putExtra("ACTIVITY_NAME", "Sellers Agreement");
            startActivity(goToWebViewSellersAgreement);
            return true;
        }
        if (id == R.id.menu_search) {
            if (isEditTextVisible == false) {
                isEditTextVisible = true;
                actionBar.setCustomView(R.layout.startup_activity_action_bar);
                EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.searchfield);
                searchProducts = search;
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                        | ActionBar.DISPLAY_SHOW_HOME);
            } else {
                new GetProviderAndProductListAsync().execute();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent productcart = new Intent(StartUpActivity.this, CartActivity.class);
        startActivity(productcart);
    }

    //    To for API,to get list of  product and provider,
    public String getProviderAndProductsList(String param) {
        String resultGetProviderAndProduct = "";
        try {
            resultGetProviderAndProduct = ServerConnection.executeGet(getApplicationContext(), "/api/searchproduct/" + param);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultGetProviderAndProduct;
    }


    public String logOut() {
        String resultLogOut = "";
        try {
            resultLogOut = ServerConnection.executeGet(getApplicationContext(), "/api/logout");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultLogOut;
    }

    class LogOutAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String resultLogOut, connectedOrNot, msg, code;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(StartUpActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultLogOut = logOut();
//                    Log.d("Provider list ", resultGetProvider);
                    if (!resultLogOut.isEmpty()) {
                        jObj = new JSONObject(resultLogOut);
                        if (jObj.has("success")) {
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
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
            try {
                progressDialog.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (!resultLogOut.isEmpty()) {
                        if (jObj.has("success")) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            Intent goToSignInActivity = new Intent(StartUpActivity.this, StartUpActivity.class);
                            goToSignInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goToSignInActivity);
                            SignInActivity.islogedin = false;
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {

                                Intent goToSignInActivity = new Intent(StartUpActivity.this, SignInActivity.class);
                                goToSignInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(goToSignInActivity);
                                finish();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Class for to get list of all the product and provider
    class GetProviderAndProductListAsync extends AsyncTask<String, Integer, String> {
        String resultGetProviderAndProduct, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(StartUpActivity.this, "", "Getting Products...");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    if (searchProducts == null) {
                        resultGetProviderAndProduct = getProviderAndProductsList("");
                    } else {
                        if (searchProducts.getText().toString().trim().length() > 0) {
                            searchString = searchProducts.getText().toString().trim();
                            resultGetProviderAndProduct = getProviderAndProductsList(searchString.replaceAll(" ", "%20"));
                        } else {
                            resultGetProviderAndProduct = getProviderAndProductsList("");
                        }
                    }
                    if (!resultGetProviderAndProduct.isEmpty()) {
                        Log.d("search result", new Gson().toJson(resultGetProviderAndProduct));
                        jObj = new JSONObject(resultGetProviderAndProduct);
                        if (jObj.has("success")) {
                            providerSuccessResponse = new Gson().fromJson(resultGetProviderAndProduct, ProviderSuccessResponse.class);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
//                            code = jObjError.getString("code");
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
                    StartUpActivity.linearLayoutCategories.removeAllViews();
                    if (!resultGetProviderAndProduct.isEmpty()) {
                        if (jObj.has("success")) {
                            new AdapterForProviderCategories(context, providerSuccessResponse.getSuccess().getProvider()).setProductCategories();
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
