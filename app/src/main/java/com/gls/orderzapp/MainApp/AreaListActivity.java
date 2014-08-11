package com.gls.orderzapp.MainApp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CityAreaListAdapter;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForAreaList;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prajyot on 6/8/14.
 */
public class AreaListActivity extends Activity {
    TextView city, state, country;
    LinearLayout llEditCurrentLocation;
    ListView areaListView;
    String area = "";
    List<String> areaList = new ArrayList<>();
    SuccessResponseForAreaList successResponseForAreaList;
    CityAreaListAdapter areaListAdapter;
    public static boolean locationChanged = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_details);
        findViewsById();



        llEditCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEditLocation = new Intent(getApplicationContext(), CityStateCountryActivity.class);
                startActivity(goToEditLocation);
            }
        });

        ActionBar actionBar = getActionBar();

        actionBar.setCustomView(R.layout.area_filter_layout);

        final EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.search);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                areaList.clear();
                for(int i = 0; i < successResponseForAreaList.getSuccess().getArea().size(); i++) {

                    if ((successResponseForAreaList.getSuccess().getArea().get(i)).toLowerCase().contains((search.getText().toString().trim()).toLowerCase())) {
                        areaList.add(successResponseForAreaList.getSuccess().getArea().get(i));
                    }
                }
                areaListAdapter.notifyDataSetChanged();
            }
        });

        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                area = parent.getItemAtPosition(position)+"";
                storeArea();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCurrentLocation();
        new GetAreaListAsync().execute();
    }

    public void storeArea(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_AREA", area);
        editor.commit();
    }

    public void findViewsById(){
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        country = (TextView) findViewById(R.id.country);
        llEditCurrentLocation = (LinearLayout) findViewById(R.id.llEditCurrentLocation);
        areaListView = (ListView) findViewById(R.id.areaList);
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

    public void displayCurrentLocation(){
        city.setText(loadCityPreference());
        state.setText(loadStatePreference());
        country.setText(loadCountryPreference());
    }

    public String getAreaList() throws Exception{
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location/area?city=" + loadCityPreference());
        return resultGetCountryList;
    }

    class GetAreaListAsync extends AsyncTask<String, Integer, String> {
        String connectedOrNot, resultGetArea, msg, code;
        JSONObject jObj;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AreaListActivity.this, "", "");
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
                            areaList.clear();
                            successResponseForAreaList = new Gson().fromJson(resultGetArea, SuccessResponseForAreaList.class);
                            areaList.addAll(successResponseForAreaList.getSuccess().getArea());
                            Collections.sort(areaList, new CustomComparator());
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
                            areaListAdapter = new CityAreaListAdapter(getApplicationContext(), areaList);
                            areaListView.setAdapter(areaListAdapter);
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
