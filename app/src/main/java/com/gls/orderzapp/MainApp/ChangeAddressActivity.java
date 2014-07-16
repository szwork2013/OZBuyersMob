package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.GoogleAnalyticsUtility;

/**
 * Created by prajyot on 20/6/14.
 */
public class ChangeAddressActivity extends Activity {
    TextView text_address;
    public static EditText edittext_address1, edittext_address2, edittext_area, edittext_city, edittext_state, edittext_country, edittext_zipcode;
    Button save_address;
    public static boolean isAddressChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        setContentView(R.layout.change_address_activity);
        findViewsById();
        isAddressChanged = false;
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
        save_address = (Button) findViewById(R.id.save_address);
        edittext_address1 = (EditText) findViewById(R.id.edittext_address1);
        edittext_address2 = (EditText) findViewById(R.id.edittext_address2);
        edittext_area = (EditText) findViewById(R.id.edittext_area);
        edittext_city = (EditText) findViewById(R.id.edittext_city);
        edittext_state = (EditText) findViewById(R.id.edittext_state);
        edittext_country = (EditText) findViewById(R.id.edittext_country);
        edittext_zipcode = (EditText) findViewById(R.id.edittext_zipcode);
        text_address = (TextView) findViewById(R.id.text_address);
    }

    public void saveAddress(View view) {


        if (edittext_address1.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 1", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_address2.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter address 2", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_city.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your city", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_area.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your area", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_zipcode.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter your Pincode", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_country.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your Country", Toast.LENGTH_LONG).show();
            return;
        }
        if (edittext_state.getText().toString().trim().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your State", Toast.LENGTH_LONG).show();
            return;
        }
        DeliveryPaymentActivity.shipping_address_textview.setText(edittext_address1.getText().toString() + ", " +
                edittext_address2.getText().toString() + ", " +
                edittext_area.getText().toString() + ", \n" +
                edittext_city.getText().toString() + ". " +
                edittext_zipcode.getText().toString() + "\n" +
                edittext_state.getText().toString() + ", " +
                edittext_country.getText().toString() + ".");
        isAddressChanged = true;
        finish();
    }
}
