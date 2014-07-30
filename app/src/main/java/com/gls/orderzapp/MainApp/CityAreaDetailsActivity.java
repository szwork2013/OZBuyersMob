package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CityAreaListAdapter;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForAreaList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForCityList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForCountryList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForStatesList;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 17/7/14.
 */
public class CityAreaDetailsActivity extends Activity {
    Spinner country_spinner, state_spinner, city_spinner;
    ListView listOfAreas;
    SuccessResponseForCountryList successResponseForCountryList;
    SuccessResponseForStatesList successResponseForStatesList;
    SuccessResponseForCityList successResponseForCityList;
    SuccessResponseForAreaList successResponseForAreaList;
    CityAreaListAdapter cityCountryListAdapter, cityStateListAdapter;
    String country = "", state = "", city = "", area = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Select Area");
        setContentView(R.layout.city_area_details);
        findViewsById();
        new GetCountryListAsync().execute();

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = parent.getItemAtPosition(position)+"";
                new GetStatesListAsync().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position)+"";
                new GetCityListAsync().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position)+"";
                new GetAreaListAsync().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listOfAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                area = parent.getItemAtPosition(position)+"";
                storeArea();
                storeCity();
                storeState();
                storeCountry();
                finish();
            }
        });


    }

    public void findViewsById(){
        country_spinner = (Spinner) findViewById(R.id.country_spinner);
        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        listOfAreas = (ListView) findViewById(R.id.listOfAreas);
    }

    public void storeArea(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_AREA", area);
        editor.commit();
    }

    public void storeCity(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= sp.edit();
        editor.putString("USER_CITY", city);
        editor.commit();
    }

    public void storeState(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_STATE", state);
        editor.commit();
    }

    public void storeCountry(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_COUNTRY", country);
        editor.commit();
    }

    public String loadCountryPreference(){
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_COUNTRY", "IN");
        return userArea;
    }

    public String loadStatePreference(){
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_STATE", "Maharashtra");
        return userArea;
    }

    public String loadCityPreference(){
        String userArea = "";
        SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userArea = spLoad.getString("USER_CITY", "Pune");
        return userArea;
    }

    public String getCountryList() throws Exception{
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=country&value=country");
        return resultGetCountryList;
    }

    public String getStatesList() throws Exception{
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=state&value="+country);
        return resultGetCountryList;
    }

    public String getCitiesList() throws Exception{
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=city&value="+state);
        return resultGetCountryList;
    }

    public String getAreaList() throws Exception{
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location/area?city="+city);
        return resultGetCountryList;
    }

    class GetCountryListAsync extends AsyncTask<String, Integer, String>{
        String connectedOrNot, resultGetCountry, msg, code;
        JSONObject jObj;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CityAreaDetailsActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetCountry = getCountryList();
                    if (!resultGetCountry.isEmpty()){
                        Log.d("resultGetCountry", resultGetCountry);
                        jObj = new JSONObject(resultGetCountry);
                        if(jObj.has("success")){
                            successResponseForCountryList = new Gson().fromJson(resultGetCountry, SuccessResponseForCountryList.class);
//                            listCountry.addAll(successResponseForCountryList.getSuccess().getCountry());
                        }else{
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try{
                progressDialog.dismiss();
                if(connectedOrNot.equalsIgnoreCase("success")){
                    if(!resultGetCountry.isEmpty()){
                        if(jObj.has("success")){
                            cityCountryListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForCountryList.getSuccess().getCountry());
                            country_spinner.setAdapter(cityCountryListAdapter);
                            country_spinner.setSelection(successResponseForCountryList.getSuccess().getCountry().indexOf(loadCountryPreference()));
                        }else{
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class GetStatesListAsync extends AsyncTask<String, Integer, String>{
        String connectedOrNot, resultGetStates, msg, code;
        JSONObject jObj;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CityAreaDetailsActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetStates = getStatesList();
                    if (!resultGetStates.isEmpty()){
                        Log.d("resultGetCountry", resultGetStates);
                        jObj = new JSONObject(resultGetStates);
                        if(jObj.has("success")){
//                            listState.clear();

                            successResponseForStatesList = new Gson().fromJson(resultGetStates, SuccessResponseForStatesList.class);
//                            listState.addAll(successResponseForStatesList.getSuccess().getStates());
                        }else{
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try{
                progressDialog.dismiss();
                if(connectedOrNot.equalsIgnoreCase("success")){
                    if(!resultGetStates.isEmpty()){
                        if(jObj.has("success")){
                            cityStateListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForStatesList.getSuccess().getStates());
                            state_spinner.setAdapter(cityStateListAdapter);
                            state_spinner.setSelection(successResponseForStatesList.getSuccess().getStates().indexOf(loadStatePreference()));
                        }else{
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class GetCityListAsync extends AsyncTask<String, Integer, String>{
        String connectedOrNot, resultGetCities, msg, code;
        JSONObject jObj;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CityAreaDetailsActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetCities = getCitiesList();
                    if (!resultGetCities.isEmpty()){
                        Log.d("resultGetCountry", resultGetCities);
                        jObj = new JSONObject(resultGetCities);
                        if(jObj.has("success")){
                            successResponseForCityList = new Gson().fromJson(resultGetCities, SuccessResponseForCityList.class);
                        }else{
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try{
                progressDialog.dismiss();
                if(connectedOrNot.equalsIgnoreCase("success")){
                    if(!resultGetCities.isEmpty()){
                        if(jObj.has("success")){
                            cityStateListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForCityList.getSuccess().getCity());
                            city_spinner.setAdapter(cityStateListAdapter);
                            city_spinner.setSelection(successResponseForCityList.getSuccess().getCity().indexOf(loadCityPreference()));
                        }else{
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class GetAreaListAsync extends AsyncTask<String, Integer, String>{
        String connectedOrNot, resultGetArea, msg, code;
        JSONObject jObj;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CityAreaDetailsActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultGetArea = getAreaList();
                    if (!resultGetArea.isEmpty()){
                        Log.d("getArea", resultGetArea);
                        jObj = new JSONObject(resultGetArea);
                        if(jObj.has("success")){

                            successResponseForAreaList = new Gson().fromJson(resultGetArea, SuccessResponseForAreaList.class);
//                            listOfAreas.addAll(successResponseForAreaList.getSuccess().getArea());
                        }else{
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
            try{
                progressDialog.dismiss();
                if(connectedOrNot.equalsIgnoreCase("success")){
                    if(!resultGetArea.isEmpty()){
                        if(jObj.has("success")){
                            cityCountryListAdapter = new CityAreaListAdapter(getApplicationContext(), successResponseForAreaList.getSuccess().getArea());
                            listOfAreas.setAdapter(cityCountryListAdapter);
                        }else{
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Server is not responding, please try again later", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_area_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
