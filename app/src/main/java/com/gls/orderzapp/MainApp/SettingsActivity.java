package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
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
import com.gls.orderzapp.SignUp.Location;
import com.gls.orderzapp.User.SettingsUserData;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.User.UserDetails;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prajyot on 2/4/14.
 */
public class SettingsActivity extends ActionBarActivity {
    public static String userID;
    public static boolean avoidFirstClick = false;
    Spinner languageSpinner;


    EditText userNameEditText, passwordEditText, address1EditText,
            address2EditText, edit_state, edit_city;
    ArrayList<String> arrayListLanguage;
    Button saveButton;
    int position;
    String username, password, address1, address2, language;
    UserDetails userDetails;
    SettingsUserData settingsUserData;
    SuccessResponseOfUser successResponseOfUser;
    SuccessResponseForCountryList successResponseForCountryList;
    SuccessResponseForCityList successResponseForCityList;
    SuccessResponseForStatesList successResponseForStatesList;
    CityAreaListAdapter cityCountryListAdapter, cityStateListAdapter, cityListAdapter;

    SuccessResponseForAreaList successResponseForAreaList;
    SuccessResponseForZipCodeList successResponseForZipCodeList;
//**************for validation
List<String> areaList = new ArrayList<String>();
    List<String> zipcodeList = new ArrayList<String>();
    ArrayAdapter<String> zipcodeAdapter, adapter;
Spinner country_spinner, state_spinner, city_spinner;
    AutoCompleteTextView auto_area, pincodeEditText;
    ListView listOfAreas, listOfZipCode;
    View country_view, state_view, city_view;
    TextView state_textview, city_textview;
    String country = "", state = "", city = "", area = "", zipcode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        findViewsById();
        languageSpinnerActions();
        try {
            successResponseOfUser = new Gson().fromJson(loadUserPreference(), SuccessResponseOfUser.class);
            new GetCountryListAsync().execute();
            country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    country = parent.getItemAtPosition(position) + "";
                    if(country.equalsIgnoreCase(successResponseOfUser.getSuccess().getUser().getLocation().getCountry()) && country.equalsIgnoreCase("india")) {
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
                        if(country.equalsIgnoreCase(loadCountryPreference())){
                            edit_city.setText(loadCityPreference());
                            edit_state.setText(loadStatePreference());
                            pincodeEditText.setText(loadPinCodePreference());
                            auto_area.setText(loadAreaPreference());

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
//                            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, areaList);
//                            adapter.notifyDataSetChanged();
//                            auto_area.setAdapter(adapter);
//                            zipcodeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, zipcodeList);
//                            pincodeEditText.setAdapter(zipcodeAdapter);
                        }else
                        {
                            edit_city.setText("");
                            edit_state.setText("");
                            pincodeEditText.setText("");
                            auto_area.setText("");

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
//                            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, areaList);
//                            adapter.notifyDataSetChanged();
//                            auto_area.setAdapter(adapter);
//                            zipcodeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.area, zipcodeList);
//                            pincodeEditText.setAdapter(zipcodeAdapter);
                        }


                    }
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
                    if(city.equalsIgnoreCase(loadCityPreference()))
                    {
                        auto_area.setText(loadAreaPreference());
                        pincodeEditText.setText(loadPinCodePreference());
                    }else {
                        auto_area.setText("");
                        pincodeEditText.setText("");
                        areaList.clear();
                        zipcodeList.clear();
                    }
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
            Log.d("Log","4");
            displayUserData();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public String loadUserPreference() throws Exception{
        String user = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user = spLoad.getString("USER_DATA", null);
        return user;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void displayUserData() throws Exception {
        try {
            userNameEditText.setText(successResponseOfUser.getSuccess().getUser().getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            address1EditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getAddress1());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            address2EditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getAddress2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            edit_city.setText(successResponseOfUser.getSuccess().getUser().getLocation().getCity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            auto_area.setText(successResponseOfUser.getSuccess().getUser().getLocation().getArea());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pincodeEditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getZipcode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            edit_state.setText(successResponseOfUser.getSuccess().getUser().getLocation().getState());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            username = userNameEditText.getText().toString().trim();
            address1 = address1EditText.getText().toString().trim();
            address2 = address2EditText.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userID = successResponseOfUser.getSuccess().getUser().getUserid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        language = loadPreferenceslang();
    }

    public void languageSpinnerActions() {
        arrayListLanguage = new ArrayList<>();
        arrayListLanguage.add("English");
        arrayListLanguage.add("Hindi");
        arrayListLanguage.add("Marathi");
        arrayListLanguage.add("Gujarati");

        for (int i = 0; i < arrayListLanguage.size(); i++) {
            Log.d("prefe", loadPreferenceslang());
            if (arrayListLanguage.get(i).equalsIgnoreCase(loadPreferenceslang())) {
                position = arrayListLanguage.indexOf(arrayListLanguage.get(i));
            }
        }
        languageSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.language_spinner_items, arrayListLanguage));
        languageSpinner.setSelection(position);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (avoidFirstClick == false) {
                    avoidFirstClick = true;
                } else {

                    String lang = languageSpinner.getItemAtPosition(position).toString();
//                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor edit = sp.edit();
                    switch (lang.toLowerCase()) {
                        case "english":
                            savePreferences("en");
                            break;
                        case "hindi":
                            savePreferences("hi");
                            break;
                        case "marathi":
                            savePreferences("ma");
                            break;
                        case "gujarati":
                            savePreferences("gu");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        avoidFirstClick = false;
        super.onBackPressed();
    }

    public void findViewsById() {
        languageSpinner = (Spinner) findViewById(R.id.spinner);
        userNameEditText = (EditText) findViewById(R.id.editTextUserName);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        address1EditText = (EditText) findViewById(R.id.editTextAddress1);
        address2EditText = (EditText) findViewById(R.id.editTextAddress2);
        saveButton = (Button) findViewById(R.id.save_button);
//*****for validation
        pincodeEditText = (AutoCompleteTextView) findViewById(R.id.edtTextPincode);
        auto_area = (AutoCompleteTextView) findViewById(R.id.auto_area);
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



    public void storeUserPreferences() {
//        successResponseOfUser = new Gson().fromJson(loadPreferencesUser(), SuccessResponseOfUser.class);
//        SuccessResponseOfUser user = new Gson().fromJson(userData, SuccessResponseOfUser.class);
        Location location = new Location();
        location.setAddress1(address1EditText.getText().toString().trim());
        location.setAddress2(address2EditText.getText().toString().trim());
        Log.d("AreaStore",area);
        location.setArea(area);
        location.setCity(city);
        location.setState(state);
        location.setCountry(country);
        location.setZipcode(zipcode);
        successResponseOfUser.getSuccess().getUser().setLocation(location);

        successResponseOfUser.getSuccess().getUser().setUsername(userNameEditText.getText().toString().trim());
        successResponseOfUser.getSuccess().getUser().setPreffered_lang(loadPreferenceslang());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_DATA", new Gson().toJson(successResponseOfUser));
        editor.commit();
    }

    public String loadPreferenceslang() {
        String lang = "";
        String language = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            language = spLoad.getString("DEFAULT_LANGUAGE", "en");
            switch (language) {
                case "en":
                    lang = "EN";
                    break;
                case "hi":
                    lang = "HI";
                    break;
                case "ma":
                    lang = "MR";
                    break;
                case "gu":
                    lang = "GU";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lang;

    }

    public String savePreferences(String value) {
        String languagePreference = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("DEFAULT_LANGUAGE", value);
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return languagePreference;
    }

    public void saveSettings(View view) {
        if (userNameEditText.getText().toString().trim().equals(username)
                && passwordEditText.getText().toString().trim().equals(password)
                && address1EditText.getText().toString().trim().equals(address1)
                && address2EditText.getText().toString().trim().equals(address2)
//                && cityEditText.getText().toString().trim().equals(city)
//                && areaEditText.getText().toString().trim().equals(area)
                && pincodeEditText.getText().toString().trim().equals(zipcode)
//                && editTextState.getText().toString().trim().equals(state)
//                && countryEditText.getText().toString().trim().equals(country)
                && loadPreferenceslang().equals(language)) {
            Toast.makeText(getApplicationContext(), "No changes were made", Toast.LENGTH_LONG).show();
        } else {
            if (userNameEditText.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter username", Toast.LENGTH_LONG).show();
                return;
            }
            if (address1EditText.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter address1", Toast.LENGTH_LONG).show();
                return;
            }
            if (address2EditText.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter address2", Toast.LENGTH_LONG).show();
                return;
            }

//            if (cityEditText.getText().toString().trim().length() == 0) {
//                Toast.makeText(getApplicationContext(), "Please enter city", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (areaEditText.getText().toString().trim().length() == 0) {
//                Toast.makeText(getApplicationContext(), "Please enter area", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (pincodeEditText.getText().toString().trim().length() < 6) {
//                Toast.makeText(getApplicationContext(), "Please enter a valid pincode", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (editTextState.getText().toString().trim().length() == 0) {
//                Toast.makeText(getApplicationContext(), "Please enter your state", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (countryEditText.getText().toString().trim().length() == 0) {
//                Toast.makeText(getApplicationContext(), "Please enter your country", Toast.LENGTH_LONG).show();
//                return;
//            }
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
                area=auto_area.getText().toString().trim();
            }

            new SaveSettingsAsync().execute();
        }
    }


    public String postSettingsData() {
        String resultOfSaveSettings = "";
        String jsonPostSettingsData;
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonPostSettingsData = gson.toJson(settingsUserData);
            Log.d("PostUserData",new Gson().toJson(settingsUserData));
            Log.d("user", userID);
            resultOfSaveSettings = ServerConnection.executePut(jsonPostSettingsData, "/api/user/" + userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfSaveSettings;
    }

    public void setPostSettingsData() throws Exception {
        area=auto_area.getText().toString().trim();
        userDetails = new UserDetails();
        settingsUserData = new SettingsUserData();
        Location location = new Location();
        location.setAddress1(address1EditText.getText().toString().trim());
        location.setAddress2(address2EditText.getText().toString().trim());
        location.setCity(city);
        location.setArea(area);
        location.setCountry(country);
        location.setState(state);
        location.setZipcode(pincodeEditText.getText().toString().trim());
        userDetails.setLocation(location);
        userDetails.setPreffered_lang(loadPreferenceslang());
        userDetails.setUsername(userNameEditText.getText().toString().trim());
        if (passwordEditText.getText().toString().trim().length() > 0) {
            userDetails.setPassword(passwordEditText.getText().toString().trim());
        }
        settingsUserData.setUserdata(userDetails);
    }



    public class SaveSettingsAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultOfSaveSettings, msg, code;
        JSONObject jObj, jObjError, jObjSuccess;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SettingsActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setPostSettingsData();
                    resultOfSaveSettings = postSettingsData();
                    if (!resultOfSaveSettings.isEmpty()) {
                        Log.d("result", resultOfSaveSettings);
                        jObj = new JSONObject(resultOfSaveSettings);
                        if (jObj.has("success")) {
                            storeUserPreferences();
                           // successResponseOfUser = new Gson().fromJson(resultOfSaveSettings, SuccessResponseOfUser.class);
                            jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
                        } else {
                            jObjError = jObj.getJSONObject("error");
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
                    if (!resultOfSaveSettings.isEmpty()) {
                        if (jObj.has("success")) {

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            finish();
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

    //********************************
    //********************************
    //      To get country and city---
    //**********************************
    //**********************************
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
        String userCountry = "";
        String countryName=successResponseOfUser.getSuccess().getUser().getLocation().getCountry();
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userCountry = spLoad.getString("USER_COUNTRY", countryName);
        return userCountry;
    }

    public String loadStatePreference() {
        String userState = "";
        String stateName=successResponseOfUser.getSuccess().getUser().getLocation().getState();
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userState = spLoad.getString("USER_STATE", stateName);
        return userState;
    }

    public String loadCityPreference() {
        String userCity = "";
        String cityName=successResponseOfUser.getSuccess().getUser().getLocation().getCity();
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userCity = spLoad.getString("USER_CITY",cityName);
        return userCity;
    }
    public String loadAreaPreference() {
        String userArea = "";
        String areaName=successResponseOfUser.getSuccess().getUser().getLocation().getArea();
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_AREA", areaName);
        return userArea;
    }
    public String loadPinCodePreference() {
        String userPinCode = "";
        String pinCodeName=successResponseOfUser.getSuccess().getUser().getLocation().getZipcode();
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userPinCode = spLoad.getString("USER_PINCODE", pinCodeName);
        return userPinCode;
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


    class GetCountryListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultGetCountry, msg, code;
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
//                progressDialog = ProgressDialog.show(SignUpActivity.this, "", "");
//                progressDialog.setCancelable(true);
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
//                            progressDialog.dismiss();
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
//                            progressDialog.dismiss();
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
//                            progressDialog.dismiss();
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
//                progressDialog.dismiss();
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