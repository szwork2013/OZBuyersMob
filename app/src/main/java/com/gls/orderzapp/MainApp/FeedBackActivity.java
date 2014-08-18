package com.gls.orderzapp.MainApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gls.orderzapp.FeedBack.FeedBackBean;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class FeedBackActivity extends ActionBarActivity {
    EditText edit_feed_back;
    Button save_feed_back_button;
    FeedBackBean feedBackBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);

        findViewsById();

    }

    public void findViewsById(){
        edit_feed_back = (EditText) findViewById(R.id.edit_feed_back);
        save_feed_back_button = (Button) findViewById(R.id.save_feed_back_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed_back, menu);
        return true;
    }

    public void setPostData(){
        feedBackBean = new FeedBackBean();
        feedBackBean.setFeedbacktext(edit_feed_back.getText().toString().trim());
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

    public void saveFeedBack(View view){
        if(edit_feed_back.getText().toString().trim().length() > 0){
            new FeedBackAsync().execute();
        }else{
            Toast.makeText(getApplicationContext(), "Enter some feed back to save", Toast.LENGTH_LONG).show();
        }
    }

    public String postFeedBackData(){
        String resultPostFeedBack = "";
        String jsonToSendOverServer = "";
        try{
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(feedBackBean);
            resultPostFeedBack = ServerConnection.executePost1(jsonToSendOverServer, "/api/feedback");
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultPostFeedBack;
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

    class FeedBackAsync extends AsyncTask<String , Integer, String>{
        String connectedOrNot, resultSaveFeedBack, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(FeedBackActivity.this, "", "");
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                if(new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    setPostData();
                    resultSaveFeedBack = postFeedBackData();
                    if(!resultSaveFeedBack.isEmpty()){
                        jObj = new JSONObject(resultSaveFeedBack);
                        if(jObj.has("success")){
                            JSONObject jObjSuccess = jObj.getJSONObject("success");
                            msg = jObjSuccess.getString("message");
                        }else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                            code = jObjError.getString("code");
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
        protected void onPostExecute(String connectedOrNot) {
            try{
                progressDialog.dismiss();
                if(connectedOrNot.equalsIgnoreCase("success")){
                    if(!resultSaveFeedBack.isEmpty()){
                        if(jObj.has("success")){
                            if(msg != null) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                            edit_feed_back.setText("");
                        }else{
                            if(msg != null) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Server is not responding please try again later", Toast.LENGTH_LONG).show();
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