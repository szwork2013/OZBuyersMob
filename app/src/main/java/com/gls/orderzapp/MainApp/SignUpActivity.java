package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CityAreaListAdapter;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForAreaList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForCityList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForCountryList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForStatesList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForZipCodeList;
import com.gls.orderzapp.R;
import com.gls.orderzapp.SignUp.SendSignUpData;
import com.gls.orderzapp.SignUp.SignUpDataInUserObject;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SignUpActivity extends ActionBarActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    String regid = "";
    Context context;
    AutoCompleteTextView auto_area, pincodeEditText;
    EditText mobileNoEditText, passwordEditText, usernameEditText, address1EditText, address2EditText, cityEditText, areaEditText,
             countryEditText, stateEditText, emailEditText, firstnameEditText, edit_state, edit_city;
    Button signUpButton;

    //    String SENDER_ID = "926441694335";

    TextView state_textview, city_textview;
    View country_view, state_view, city_view;

    String SENDER_ID = "13920985466";
    SignUpDataInUserObject signUpData;
    SendSignUpData sendSignUpData;

    //******for auto area
    Spinner country_spinner, state_spinner, city_spinner;
    ListView listOfAreas, listOfZipCode;
    SuccessResponseForCountryList successResponseForCountryList;
    SuccessResponseForStatesList successResponseForStatesList;
    SuccessResponseForCityList successResponseForCityList;
    SuccessResponseForAreaList successResponseForAreaList;
    SuccessResponseForZipCodeList successResponseForZipCodeList;
    List<String> areaList = new ArrayList<String>();
    List<String> zipcodeList = new ArrayList<String>();
    CityAreaListAdapter cityCountryListAdapter, cityStateListAdapter, cityListAdapter;
    String country = "", state = "", city = "", area = "", zipcode = "";
    ArrayAdapter<String> zipcodeAdapter, adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        context = SignUpActivity.this;
        setContentView(R.layout.sign_up);

        findViewsById();
        new GetCountryListAsync().execute();

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = parent.getItemAtPosition(position) + "";
                if(country.equalsIgnoreCase("india")) {
                    edit_city.setVisibility(View.GONE);
                    edit_state.setVisibility(View.GONE);
                    state_spinner.setVisibility(View.VISIBLE);
                    city_spinner.setVisibility(View.VISIBLE);
                    state_textview.setVisibility(View.VISIBLE);
                    state_view.setVisibility(View.VISIBLE);
                    city_textview.setVisibility(View.VISIBLE);
                    city_view.setVisibility(View.VISIBLE);
                    new GetStatesListAsync().execute();
                }else{
                    areaList.clear();
                    zipcodeList.clear();
                    edit_city.setVisibility(View.VISIBLE);
                    edit_state.setVisibility(View.VISIBLE);
                    state_spinner.setVisibility(View.GONE);
                    city_spinner.setVisibility(View.GONE);
                    state_textview.setVisibility(View.GONE);
                    state_view.setVisibility(View.GONE);
                    city_textview.setVisibility(View.GONE);
                    city_view.setVisibility(View.GONE);
                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, areaList);
                    adapter.notifyDataSetChanged();
                    auto_area.setAdapter(adapter);
                    zipcodeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, zipcodeList);
                    pincodeEditText.setAdapter(zipcodeAdapter);
                    progressDialog.dismiss();
                }
//                new GetStatesListAsync().execute();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position) + "";
//                if(country.equalsIgnoreCase("india")) {
                    new GetCityListAsync().execute();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position) + "";
                Log.d("cityy", city);
                areaList.clear();
                zipcodeList.clear();
//                if(country.equalsIgnoreCase("india")) {
                    new GetZipCodeListAsync().execute();
                    new GetAreaListAsync().execute();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listOfZipCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                zipcode = adapterView.getItemAtPosition(i) + "";
            }
        });

        listOfAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                area = parent.getItemAtPosition(position) + "";
                Log.d("areaa", area);
                storeArea();
                storeCity();
                storeState();
                storeCountry();
                storeZipCode();
            }
        });
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

        mobileNoEditText = (EditText) findViewById(R.id.editTextMobileNUmber);
        firstnameEditText = (EditText) findViewById(R.id.editTextFirstName);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        usernameEditText = (EditText) findViewById(R.id.editTextUserName);
        address1EditText = (EditText) findViewById(R.id.editTextAddress1);
        address2EditText = (EditText) findViewById(R.id.editTextAddress2);
        cityEditText = (EditText) findViewById(R.id.editTextCity);
        areaEditText = (EditText) findViewById(R.id.editTextArea);
        pincodeEditText = (AutoCompleteTextView) findViewById(R.id.edtTextPincode);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        signUpButton = (Button) findViewById(R.id.buttonSignUp);
        countryEditText = (EditText) findViewById(R.id.editTextCountry);
        stateEditText = (EditText) findViewById(R.id.editTextState);
        auto_area = (AutoCompleteTextView) findViewById(R.id.auto_area);
        //************after auto area
        country_spinner = (Spinner) findViewById(R.id.country_spinner);
        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        listOfAreas = (ListView) findViewById(R.id.listOfAreas);
        listOfZipCode = (ListView) findViewById(R.id.listOfZipCode);
        edit_state = (EditText) findViewById(R.id.edit_state);
        edit_city = (EditText) findViewById(R.id.edit_city);
        country_view = findViewById(R.id.country_view);
        state_textview = (TextView) findViewById(R.id.state_textview);
        state_view = findViewById(R.id.state_view);
        city_textview = (TextView) findViewById(R.id.city_textview);
        city_view = findViewById(R.id.city_view);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
                return true;
            } else {
                Toast.makeText(context, "Device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void storeRegistrationId(Context context, String regId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("REG_ID", regId);
        edit.commit();
    }

    public String getRegistrationId() {
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(context);
        String regId = spLoad.getString("REG_ID", "");
        return regId;
    }

    public void signUp(View view) {
        if (mobileNoEditText.getText().toString().trim().length() < 10 && mobileNoEditText.getText().toString().trim().replaceAll(" ", "").length() < 10) {
            Toast.makeText(getApplicationContext(), "Please enter a correct mobile number", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordEditText.getText().toString().trim().length() < 3 || passwordEditText.getText().toString().trim().length() > 10) {
            Toast.makeText(getApplicationContext(), "Please enter a password between 3 - 10 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (emailEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a Email Address", Toast.LENGTH_LONG).show();
            return;
        }
        if (firstnameEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        }
        if (usernameEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_LONG).show();
            return;
        }
        if (address1EditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 1", Toast.LENGTH_LONG).show();
            return;
        }
        if (address2EditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 2", Toast.LENGTH_LONG).show();
            return;
        }

        if (auto_area.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your area", Toast.LENGTH_LONG).show();
            return;
        }
        if (auto_area.getText().toString().trim().length() > 0) {
            int j = 0;
            if(country.equalsIgnoreCase("india")) {
                for (int i = 0; i < areaList.size(); i++) {
                    if (auto_area.getText().toString().trim().equalsIgnoreCase(areaList.get(i).toString())) {
                        j++;
                    }
                }
                if (j == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter correct area ", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

//        if (pincodeEditText.getText().toString().trim().length() < 6) {
//            Toast.makeText(getApplicationContext(), "Please enter your Pincode", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (pincodeEditText.getText().toString().trim().length() > 0) {

            if(country.equalsIgnoreCase("india")) {
                if (pincodeEditText.getText().toString().trim().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Please enter your Pincode", Toast.LENGTH_LONG).show();
                    return;
                }
                int j = 0;
                for (int i = 0; i < zipcodeList.size(); i++) {
                    if (pincodeEditText.getText().toString().trim().equalsIgnoreCase(zipcodeList.get(i).toString())) {
                        j++;
                    }
                }
                if (j == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter correct PinCode ", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                if (pincodeEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter your Pincode", Toast.LENGTH_LONG).show();
                    return;
                }
            }
//        }

        if(!country.equalsIgnoreCase("india")){
            if(edit_state.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter state", Toast.LENGTH_LONG).show();
                return;
            }
            if(edit_city.getText().toString().trim().length() == 0){
                Toast.makeText(getApplicationContext(), "Please enter city", Toast.LENGTH_LONG).show();
                return;
            }
            state = edit_state.getText().toString().trim();
            city = edit_city.getText().toString().trim();
        }
        if (checkPlayServices()) {

            new RegisterToGcmInBackground(context).execute();

        }
    }

    public String postSignUp() {
        String signUpResultJson = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(signUpData);
            signUpResultJson = ServerConnection.executePost1(jsonToSendOverServer, "/api/user/signup");
        } catch (Exception e) {

            e.printStackTrace();
        }
        return signUpResultJson;
    }

    public void setPostSignupParameters() {
        signUpData = new SignUpDataInUserObject();
        sendSignUpData = new SendSignUpData();
        sendSignUpData.setUsertype("individual");
        sendSignUpData.setMobileno( mobileNoEditText.getText().toString().trim());
        sendSignUpData.setPassword(passwordEditText.getText().toString().trim());
        sendSignUpData.setUsername(usernameEditText.getText().toString().trim());
        sendSignUpData.setFirstname(firstnameEditText.getText().toString().trim());
        sendSignUpData.setGcmregistrationid(regid);
        sendSignUpData.setEmail(emailEditText.getText().toString().trim());
        sendSignUpData.getLocation().setAddress1(address1EditText.getText().toString().trim());
        sendSignUpData.getLocation().setAddress2(address2EditText.getText().toString().trim());
        sendSignUpData.getLocation().setZipcode(pincodeEditText.getText().toString().trim());
        sendSignUpData.getLocation().setCity(city);
        sendSignUpData.getLocation().setArea(auto_area.getText().toString().trim());
        sendSignUpData.getLocation().setCountry(country);
        sendSignUpData.getLocation().setState(state);

        signUpData.setUser(sendSignUpData);
    }

    //*******************after auto area
    public void storeArea() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_AREA", area);
        editor.commit();
    }

    public void storeCity() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_CITY", city);
        editor.commit();
    }

    public void storeState() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_STATE", state);
        editor.commit();
    }

    public void storeCountry() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_COUNTRY", country);
        editor.commit();
    }

    public void storeZipCode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_ZIPCODE", zipcode);
        editor.commit();
    }

    public String loadCountryPreference() {
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_COUNTRY", "IN");
        return userArea;
    }

    public String loadStatePreference() {
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_STATE", "Maharashtra");
        return userArea;
    }

    public String loadCityPreference() {
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_CITY", "Pune");
        return userArea;
    }

    public String getCountryList() throws Exception {
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/country");
        return resultGetCountryList;
    }

    public String getStatesList() throws Exception {
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=state&value=" + country);
        return resultGetCountryList;
    }

    public String getCitiesList() throws Exception {
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=city&value=" + state);
        return resultGetCountryList;
    }

    public String getAreaList() throws Exception {
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location/area?city=" + city);
        return resultGetCountryList;
    }

    public String getZipCodeList() throws Exception {
        String resultGetZipCodeList = "";
        resultGetZipCodeList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=zipcode&value=" + city);
        return resultGetZipCodeList;
    }

    public class RegisterToGcmInBackground extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String signUpResult, connectedOrNot, msg, code;
        ProgressDialog pd;
        Context context;

        public RegisterToGcmInBackground(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(context, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                if (new CheckConnection(context).isConnectingToInternet()) {

                    connectedOrNot = "success";

                    regid = getRegistrationId();
                    if (regid.isEmpty()) {
                        if (gcm == null) {
                            try {
                                gcm = GoogleCloudMessaging.getInstance(context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            regid = gcm.register(SENDER_ID);
                            Log.d("gcmid", regid);
                            System.out.print(regid);
                            storeRegistrationId(context, regid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setPostSignupParameters();
                    signUpResult = postSignUp();
                    if (!signUpResult.isEmpty()) {
                        jObj = new JSONObject(signUpResult);
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
                pd.dismiss();
                if (connectedOrNot.equals("success")) {
                    if (!signUpResult.isEmpty()) {
                        if (jObj.has("success")) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            Intent goToVerifyActivity = new Intent(context, VerifyUserActivity.class);
                            startActivity(goToVerifyActivity);
                            finish();
                        } else {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetCountryListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultGetCountry, msg, code;
        JSONObject jObj;
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignUpActivity.this, "", "");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetCountry = getCountryList();
                    if (!resultGetCountry.isEmpty()) {
                        Log.d("resultGetCountry", resultGetCountry);
                        jObj = new JSONObject(resultGetCountry);
                        if (jObj.has("success")) {
                            successResponseForCountryList = new Gson().fromJson(resultGetCountry, SuccessResponseForCountryList.class);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {

                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultGetCountry.isEmpty()) {
                        if (jObj.has("success")) {
                            cityCountryListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForCountryList.getSuccess().getCountry());
                            country_spinner.setAdapter(cityCountryListAdapter);
                            country_spinner.setSelection(successResponseForCountryList.getSuccess().getCountry().indexOf(loadCountryPreference()));
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    class GetStatesListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultGetStates, msg, code;
        JSONObject jObj;
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            if(country.equalsIgnoreCase("india")) {
            progressDialog = ProgressDialog.show(SignUpActivity.this, "", "");
            progressDialog.setCancelable(true);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetStates = getStatesList();
                    if (!resultGetStates.isEmpty()) {
                        jObj = new JSONObject(resultGetStates);
                        if (jObj.has("success")) {
                            successResponseForStatesList = new Gson().fromJson(resultGetStates, SuccessResponseForStatesList.class);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
//                progressDialog.dismiss();
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultGetStates.isEmpty()) {
                        if (jObj.has("success")) {
                            cityStateListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForStatesList.getSuccess().getStates());
                            state_spinner.setAdapter(cityStateListAdapter);
                            state_spinner.setSelection(successResponseForStatesList.getSuccess().getStates().indexOf(loadStatePreference()));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    class GetCityListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultGetCities, msg, code;
        JSONObject jObj;
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(SignUpActivity.this, "", "");
//            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetCities = getCitiesList();
                    if (!resultGetCities.isEmpty()) {
                        Log.d("resultGetCountry", resultGetCities);
                        jObj = new JSONObject(resultGetCities);
                        if (jObj.has("success")) {
                            successResponseForCityList = new Gson().fromJson(resultGetCities, SuccessResponseForCityList.class);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
//                progressDialog.dismiss();
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultGetCities.isEmpty()) {
                        if (jObj.has("success")) {
                            Collections.sort(successResponseForCityList.getSuccess().getCity(), new CustomComparator());
                            cityListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForCityList.getSuccess().getCity());
                            city_spinner.setAdapter(cityListAdapter);
                            city_spinner.setSelection(successResponseForCityList.getSuccess().getCity().indexOf(loadCityPreference()));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    class GetAreaListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultGetArea, msg, code;
        JSONObject jObj;
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            areaList.clear();
            Log.d("AreaList", "clear");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetArea = getAreaList();
                    if (!resultGetArea.isEmpty()) {
                        jObj = new JSONObject(resultGetArea);
                        if (jObj.has("success")) {
                            areaList.clear();
                            successResponseForAreaList = new Gson().fromJson(resultGetArea, SuccessResponseForAreaList.class);
                            areaList.addAll(successResponseForAreaList.getSuccess().getArea());
                            Log.d("arealist", new Gson().toJson(areaList));
                            Collections.sort(areaList, new CustomComparator());
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try {
//                progressDialog.dismiss();
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultGetArea.isEmpty()) {
                        if (jObj.has("success")) {
                            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, areaList);
                            adapter.notifyDataSetChanged();
                            auto_area.setAdapter(adapter);
                       } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    //******************
    //*****************
    //*****************
    //****************

    class GetZipCodeListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultZipCode, msg, code;
        JSONObject jObj;
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(SignUpActivity.this, "", "");
//            progressDialog.setCancelable(true);
            zipcodeList.clear();
            Log.d("ZipCodeList", "clear");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultZipCode = getZipCodeList();
                    if (!resultZipCode.isEmpty()) {
                        Log.d("resultGetCountry", resultZipCode);
                        jObj = new JSONObject(resultZipCode);
                        if (jObj.has("success")) {
                            zipcodeList.clear();
                            successResponseForZipCodeList = new Gson().fromJson(resultZipCode, SuccessResponseForZipCodeList.class);
                            zipcodeList.addAll(successResponseForZipCodeList.getSuccess().getZipcode());
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
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
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultZipCode.isEmpty()) {
                        if (jObj.has("success")) {
                            zipcodeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, zipcodeList);
                            pincodeEditText.setAdapter(zipcodeAdapter);

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    //****************
    //****************
    //****************
    //*****************

    class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String s, String s2) {
            return s.compareTo(s2);
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }
} 
