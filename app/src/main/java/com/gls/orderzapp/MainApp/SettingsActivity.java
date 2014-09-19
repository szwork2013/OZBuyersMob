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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

/**
 * Created by prajyot on 2/4/14.
 */
public class SettingsActivity extends ActionBarActivity {
    public static String userID;
    public static boolean avoidFirstClick = false;
    Spinner languageSpinner;
    EditText userNameEditText, passwordEditText, address1EditText,
            address2EditText, cityEditText, areaEditText, pincodeEditText, countryEditText, editTextState;
    ArrayList<String> arrayListLanguage;
    Button saveButton;
    int position;
    String username, password, address1, address2, city, area, zipcode, country, language, state;
    UserDetails userDetails;
    SettingsUserData settingsUserData;
    SuccessResponseOfUser successResponseOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        findViewsById();
        languageSpinnerActions();
        try {
            successResponseOfUser = new Gson().fromJson(loadUserPreference(), SuccessResponseOfUser.class);
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
            cityEditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getCity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            areaEditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getArea());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pincodeEditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getZipcode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            editTextState.setText(successResponseOfUser.getSuccess().getUser().getLocation().getState());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            countryEditText.setText(successResponseOfUser.getSuccess().getUser().getLocation().getCountry());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            username = userNameEditText.getText().toString().trim();
            address1 = address1EditText.getText().toString().trim();
            address2 = address2EditText.getText().toString().trim();
            city = cityEditText.getText().toString().trim();
            area = areaEditText.getText().toString().trim();
            zipcode = pincodeEditText.getText().toString().trim();
            country = countryEditText.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userID = successResponseOfUser.getSuccess().getUser().getUserid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            state = editTextState.getText().toString().trim();
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
        cityEditText = (EditText) findViewById(R.id.editTextCity);
        areaEditText = (EditText) findViewById(R.id.editTextArea);
        pincodeEditText = (EditText) findViewById(R.id.editTextPincode);
        saveButton = (Button) findViewById(R.id.save_button);
        countryEditText = (EditText) findViewById(R.id.editTextCountry);
        editTextState = (EditText) findViewById(R.id.editTextState);
    }



    public void storeUserPreferences() {
//        successResponseOfUser = new Gson().fromJson(loadPreferencesUser(), SuccessResponseOfUser.class);
//        SuccessResponseOfUser user = new Gson().fromJson(userData, SuccessResponseOfUser.class);
        Location location = new Location();
        location.setAddress1(address1EditText.getText().toString().trim());
        location.setAddress2(address2EditText.getText().toString().trim());
        location.setArea(areaEditText.getText().toString().trim());
        location.setCity(cityEditText.getText().toString().trim());
        location.setState(editTextState.getText().toString().trim());
        location.setCountry(countryEditText.getText().toString().trim());
        location.setZipcode(pincodeEditText.getText().toString().trim());
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
                && cityEditText.getText().toString().trim().equals(city)
                && areaEditText.getText().toString().trim().equals(area)
                && pincodeEditText.getText().toString().trim().equals(zipcode)
                && editTextState.getText().toString().trim().equals(state)
                && countryEditText.getText().toString().trim().equals(country)
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
            if (cityEditText.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter city", Toast.LENGTH_LONG).show();
                return;
            }
            if (areaEditText.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter area", Toast.LENGTH_LONG).show();
                return;
            }
            if (pincodeEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(getApplicationContext(), "Please enter a valid pincode", Toast.LENGTH_LONG).show();
                return;
            }
            if (editTextState.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter your state", Toast.LENGTH_LONG).show();
                return;
            }
            if (countryEditText.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter your country", Toast.LENGTH_LONG).show();
                return;
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
            Log.d("user", userID);
            resultOfSaveSettings = ServerConnection.executePut(jsonPostSettingsData, "/api/user/" + userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfSaveSettings;
    }

    public void setPostSettingsData() throws Exception {
        userDetails = new UserDetails();
        settingsUserData = new SettingsUserData();
        Location location = new Location();
        location.setAddress1(address1EditText.getText().toString().trim());
        location.setAddress2(address2EditText.getText().toString().trim());
        location.setCity(cityEditText.getText().toString().trim());
        location.setArea(areaEditText.getText().toString().trim());
        location.setCountry(countryEditText.getText().toString().trim());
        location.setState(editTextState.getText().toString().trim());
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
}