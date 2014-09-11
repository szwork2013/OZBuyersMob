package com.gls.orderzapp.MainApp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.gls.orderzapp.Cart.Adapters.CityAreaListAdapter;
import com.gls.orderzapp.Provider.Adapters.SelectCityAdapter;
import com.gls.orderzapp.Provider.Beans.SuccessResponseForCity;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SelectCityActivity extends ActionBarActivity {

    ListView cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_select_user);
        findViewsById();
    }

    public void findViewsById(){
        cityList = (ListView) findViewById(R.id.CityList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCityList() throws Exception{
        String resultGetCityList = "";
        resultGetCityList = ServerConnection.executeGet(getApplicationContext(),"api/city/provider/service");
        Log.d("City List",resultGetCityList);
        return resultGetCityList;
    }

    class CitySelect extends AsyncTask<String,Integer,String>{
        String selectedCity,msg,connectedOrNot;
        JSONObject jobj;
        SuccessResponseForCity objSuccessResponseForCity;
        @Override
        protected String doInBackground(String... strings) {
            try{
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()){
                    connectedOrNot = "success";
                    selectedCity = getCityList();
                    if(!selectedCity.isEmpty()){
                        jobj = new JSONObject(selectedCity);
                        if(jobj.has("success")){
                            objSuccessResponseForCity = new Gson().fromJson(selectedCity ,SuccessResponseForCity.class);
                        }else{
                            JSONObject jObjError = jobj.getJSONObject("error");
                            msg = jObjError.getString("message");
                        }
                    }


                }else{
                    connectedOrNot = "error";
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                    if(connectedOrNot.equalsIgnoreCase("success")){
                        if(jobj.has("success")){
                            SelectCityAdapter objSelectCityAdapter = new SelectCityAdapter(getApplicationContext(),0,objSuccessResponseForCity.getSuccess().getCity());
                            cityList.setAdapter(objSelectCityAdapter);

                           // if(int i=0;i<obj)


                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
