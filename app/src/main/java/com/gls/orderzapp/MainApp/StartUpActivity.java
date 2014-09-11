package com.gls.orderzapp.MainApp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Provider.Adapters.AdapterForProviderCategories;
import com.gls.orderzapp.Provider.Adapters.DrawerExpandableListAdapter;
import com.gls.orderzapp.Provider.Beans.BranchInfo;
import com.gls.orderzapp.Provider.Beans.ProviderSuccessResponse;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//import android.support.v7.app.ActionBar;

/**
 * Created by avinash on 2/4/14.
 */
public class StartUpActivity extends Activity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    public static ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static LinearLayout linearLayoutCategories;
    public static String searchString;
    public static boolean isFirstTime = true;
    public static Menu menu1;
    public static MenuItem signin, signup, logout;
    ActionBar actionBar;
    ImageView adBanner;
    TextView allCategories,cityName;
    ProviderSuccessResponse providerSuccessResponse;
    boolean isEditTextVisible = false;
    EditText searchProducts = null;
    List<String> listDataHeader;
    List<String> listDataHeaderID;
    HashMap<String, List<String>> listDataChild;
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
//        UtilityClassForLanguagePreferance.setLocale(getApplicationContext());
        //Get a Tracker (should auto-report)
        setContentView(R.layout.startup_activity);
        context = StartUpActivity.this;
        actionBar = getActionBar();
//        Set Actionbar title null
        actionBar.setTitle("");
        findViewsById();

//Get list of all product and provider
        new GetProviderAndProductListAsync().execute();
        drawerActions();

    }
    //*********************
    //********************
    //-------------Drawer start
    //*********************
    //*********************

    private void prepareDrawerListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
                                    for(int cat=0;cat<providerSuccessResponse.getSuccess().getProvider().size();cat++){
                                        listDataHeader.add(providerSuccessResponse.getSuccess().getProvider().get(cat).getBranch().getBranchname());
                                        listDataHeaderID.add(providerSuccessResponse.getSuccess().getProvider().get(cat).getBranch().getBranchid());
                            }
        for(int cont_no=0;cont_no<listDataHeader.size();cont_no++){
            List<String> temp= new ArrayList<String>();
            for(int i=0;i<providerSuccessResponse.getSuccess().getProvider().get(cont_no).getBranch().getContact_supports().size();i++){
            temp.add(providerSuccessResponse.getSuccess().getProvider().get(cont_no).getBranch().getContact_supports().get(i));}
            listDataChild.put(listDataHeader.get(cont_no), temp);
        }

        mDrawerList.setAdapter(new DrawerExpandableListAdapter(context, listDataHeader, listDataChild));
    }
    public void drawerActions() {

        try{

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            allCategories = (TextView) findViewById(R.id.txt_category_all);

            mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);

            // set a custom shadow that overlays the main content when the drawer

            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                    GravityCompat.START);

            // ActionBarDrawerToggle ties together the proper interactions
            // between the sliding drawer and the action bar icon
            mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                    mDrawerLayout,/* DrawerLayout object */
                    R.drawable.ic_navigation_drawer, /* nav drawer image to replace 'Up' caret */
                    R.string.drawer_open,/* "open drawer" description for accessibility */
                    R.string.drawer_close /* "close drawer" description for accessibility */
            ) {



                public void onDrawerClosed(View view) {
                    // getActionBar().setTitle(mTitle);
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        invalidateOptionsMenu(); // creates call to
                    }							// onPrepareOptionsMenu()

                }

                public void onDrawerOpened(View drawerView) {
                    // getActionBar().setTitle(mDrawerTitle);
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        invalidateOptionsMenu(); // creates call to
                    }				// onPrepareOptionsMenu()
                }
            };

            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }catch(Exception e){

            e.printStackTrace();

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        try{
            mDrawerToggle.syncState();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

//		pendinglist.clear();
        super.onConfigurationChanged(newConfig);

//		// Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//		mDrawerList.setIndicatorBounds(mDrawerList.getRight() - 40,
//				mDrawerList.getWidth());

    }

    //*********************
    //********************
    //------Drawer End
    //*********************
    //*********************

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
        cityName = (TextView) findViewById(R.id.cityName);
        //adBanner = (ImageView) findViewById(R.id.ad_banner);
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
        cityName.setText(loadSearchByCityPreference());

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
        moveTaskToBack(true);
        super.onBackPressed();
    }

    public void onCreateOptions(Menu menu) {
        try {
            menu.findItem(R.id.cart).getActionView().setOnClickListener(this);
        } catch (Exception e) {
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
            goToWebViewHelp.putExtra("URL", ServerConnection.url + "/api/faq?responsetype=html");
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
//        if (id == R.id.action_get_social) {
//            Intent goToGetSocialActivity = new Intent(StartUpActivity.this, GetSocialActivity.class);
//            startActivity(goToGetSocialActivity);
//            return true;
//        }
//        if (id == R.id.action_sellers_agreement) {
//            Intent goToWebViewSellersAgreement = new Intent(StartUpActivity.this, WebViewActivity.class);
//            goToWebViewSellersAgreement.putExtra("URL", ServerConnection.url + "/api/statictemplates?type=SA");
//            goToWebViewSellersAgreement.putExtra("ACTIVITY_NAME", "Sellers Agreement");
//            startActivity(goToWebViewSellersAgreement);
//            return true;
//        }
        if (id == R.id.feed_back) {
            new CheckSessionAsync().execute();
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

    public String getSessionStatus() throws Exception {
        String resultOfCheckSession = "";
        try {
            resultOfCheckSession = ServerConnection.executeGet(getApplicationContext(), "/api/isloggedin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfCheckSession;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Intent feedBack = new Intent(StartUpActivity.this, FeedBackActivity.class);
                    startActivity(feedBack);
//                    finish();

                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;
        }
    }

    public class CheckSessionAsync extends AsyncTask<String, Integer, String> {
        public JSONObject jObj;
        String connectedOrNot, msg, code, resultOfCheckSession;
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
                    resultOfCheckSession = getSessionStatus();
                    if (!resultOfCheckSession.isEmpty()) {
                        Log.d("check session", resultOfCheckSession);
                        jObj = new JSONObject(resultOfCheckSession);

                        if (jObj.has("success")) {
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
                            Log.d("Login success", "In doinbck");
                        } else {
                            Log.d("Login not success", "In doinbck");
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
                    if (!resultOfCheckSession.isEmpty()) {
                        if (jObj.has("success")) {
                            Intent deliveryPayment = new Intent(StartUpActivity.this, FeedBackActivity.class);
                            startActivity(deliveryPayment);
//                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            if (code.equals("AL001")) {
                                Intent goToSignin = new Intent(StartUpActivity.this, SignInActivity.class);
                                startActivityForResult(goToSignin, 1);
                            }
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
    public String loadSearchByCityPreference(){
        String city = "";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        city = sp.getString("SEARCH_CITY","Pune");
        return city;
    }

    public void gotoSelectCityActivity(View v){

        Intent intent = new Intent(StartUpActivity.this,SelectCityActivity.class);
        startActivityForResult(intent,3);

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
                        Log.d("search result", resultGetProviderAndProduct);
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
                            Log.d("","DrawerLoad");
                            prepareDrawerListData();


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
