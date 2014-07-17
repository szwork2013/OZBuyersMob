package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CityAreaListAdapter;
import com.gls.orderzapp.Cart.Beans.CountryList;
import com.gls.orderzapp.Cart.Beans.SuccessResponseForCountryList;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by prajyot on 17/7/14.
 */
public class CityAreaDetailsActivity extends Activity {
    EditText country_edit;
    ListView list;
    SuccessResponseForCountryList successResponseForCountryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_area_details);

        findViewsById();

        new GetCountryListAsync().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void findViewsById(){
        country_edit = (EditText) findViewById(R.id.country_edit);
        list = (ListView) findViewById(R.id.list);
    }

    public String getCountryList() throws Exception{
        String resultGetCountryList = "";
        resultGetCountryList = ServerConnection.executeGet(getApplicationContext(), "/api/location?key=country&value=country");
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
                            list.setAdapter(new CityAreaListAdapter(getApplicationContext(), successResponseForCountryList.getSuccess().getCountry()));
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
}
