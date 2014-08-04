package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.CreateOrder.CreateOrderAdapters.AdapterForMultipleProviders;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderAddressDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderCartList;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderData;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderProductDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.DeliveryChargeDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.DeliveryTypes;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.FtpConfigurationData;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.MsgConfigurationData;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.ProductConfiguration;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.ProductConfigurationPrice;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.SignUp.Location;
import com.gls.orderzapp.User.SuccessResponseOfUser;
import com.gls.orderzapp.Utility.Cart;
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
 * Created by prajyot on 24/4/14.
 */
public class OrderDetailsActivity extends Activity {
    public static LinearLayout llProductsList, llayout_delivery_address;
    public static TextView textGrandTotal, grandTotal, billingAddressTextView, shippingAddressTextView, delivery_type, payment_mode;
    TextView expected_delivery_date, delivery_address_text;
    CreateOrderAddressDetails orderBillingAddressDetails, orderDeliveryAddressDetails;
    public static CreateOrderCartList createOrderCartList;
    RadioGroup payment_mode_group;
    RadioButton cash_on_delivery, credit_card;
    CreateOrderData createOrderData;
    public static DeliveryChargeDetails deliveryChargeDetails;
//    public static DeliveryTypes deliveryTypes;
    Context context;
    ArrayList<ProductDetails> cartDetails = new ArrayList<>();
    SuccessResponseOfUser successResponseOfUserBillingAddresDetails, successResponseOfUserDeliveryAddresDetails;
    ListView address_list;
    ProductConfiguration productConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);


        context = OrderDetailsActivity.this;
        ((GoogleAnalyticsUtility) getApplication()).getTracker(GoogleAnalyticsUtility.TrackerName.APP_TRACKER);
        findViewsById();

        orderBillingAddressDetails = new Gson().fromJson(getIntent().getStringExtra("BILLING_ADDRESS"), CreateOrderAddressDetails.class);
        orderDeliveryAddressDetails = new Gson().fromJson(getIntent().getStringExtra("DELIVERY_ADDRESS"), CreateOrderAddressDetails.class);

        setOrderAddressDetails();
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

    public void setOrderAddressDetails() {
        try {
// Billing address
            setBillingAddress();

// Delivery Address
            setDeliveryAddress();

// Set order data to send over server and display data.
            setOrderData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBillingAddress() {
        try {
            successResponseOfUserBillingAddresDetails = new Gson().fromJson(loadPreferencesUserDataForBillingAddress(), SuccessResponseOfUser.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setDeliveryAddress() {
        try {
            successResponseOfUserDeliveryAddresDetails = new Gson().fromJson(loadPreferencesUserDataForDeliveryAddress(), SuccessResponseOfUser.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void findViewsById() {
//        orderDetailsList = (ListView)findViewById(R.id.listView);
        llProductsList = (LinearLayout) findViewById(R.id.listProducts);
//        txt_delivery_date=(TextView) findViewById(R.id.txt_delivery_date);
        textGrandTotal = (TextView) findViewById(R.id.grand_total_text);
        llayout_delivery_address = (LinearLayout) findViewById(R.id.llayout_delivery_address);
        grandTotal = (TextView) findViewById(R.id.grand_total);
        delivery_type = (TextView) findViewById(R.id.delivery_type);
        billingAddressTextView = (TextView) findViewById(R.id.billing_address_textview);
        shippingAddressTextView = (TextView) findViewById(R.id.shipping_address_textview);
        expected_delivery_date = (TextView) findViewById(R.id.expected_delivery_date);
        payment_mode_group = (RadioGroup) findViewById(R.id.payment_mode_group);
        cash_on_delivery = (RadioButton) findViewById(R.id.cash_on_delivery);
        credit_card = (RadioButton) findViewById(R.id.credit_card);
        payment_mode = (TextView) findViewById(R.id.payment_mode);
        address_list = (ListView) findViewById(R.id.address_list);
        delivery_address_text = (TextView) findViewById(R.id.delivery_address_text);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        date_selected = false;
    }

    public void setOrderData() {
        try {
            cartDetails = Cart.getCartDetails();
            createOrderCartList = new CreateOrderCartList();
            createOrderData = new CreateOrderData();

            for (int i = 0; i < cartDetails.size(); i++) {
                CreateOrderProductDetails createOrderProductDetails = new CreateOrderProductDetails();
                if (cartDetails.get(i).getBranchid() != null) {
                    createOrderProductDetails.setBranchid(cartDetails.get(i).getBranchid());
                }
                if (cartDetails.get(i).getPrice().getUom() != null) {
                    if (cartDetails.get(i).getPrice().getUom().equalsIgnoreCase("kg") || cartDetails.get(i).getPrice().getUom().equalsIgnoreCase("no") || cartDetails.get(i).getPrice().getUom().equalsIgnoreCase("lb")) {
                        createOrderProductDetails.setOrderprice((cartDetails.get(i).getPrice().getValue() * Double.parseDouble(cartDetails.get(i).getQuantity())) + "");
                        createOrderProductDetails.setUom(cartDetails.get(i).getPrice().getUom());
                        createOrderProductDetails.setQty(Double.parseDouble(cartDetails.get(i).getQuantity()) + "");
                    } else if (cartDetails.get(i).getPrice().getUom().equalsIgnoreCase("Gm")) {
                        createOrderProductDetails.setOrderprice(((cartDetails.get(i).getPrice().getValue()) / 1000 * Double.parseDouble(cartDetails.get(i).getQuantity())) + "");
                        createOrderProductDetails.setQty(Double.parseDouble(cartDetails.get(i).getQuantity()) / 1000.00 + "");
                        createOrderProductDetails.setUom("kg");

                    }
                }
                try {
                    Log.d("configuration", new Gson().toJson(cartDetails.get(i).getProductconfiguration()));
                    if (cartDetails.get(i).getProductconfiguration().getConfiguration().size() > 0) {
                        for (int j = 0; j < cartDetails.get(i).getProductconfiguration().getConfiguration().size(); j++) {
                            if (cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getFoodType() != null) {
                                if (cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getFoodType().equalsIgnoreCase("eggless") || cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).isChecked() == true) {

                                    productConfiguration = new ProductConfiguration();
                                    ProductConfigurationPrice productConfigurationPrice = new ProductConfigurationPrice();
                                    productConfigurationPrice.setValue(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getProd_configprice().getValue() * Double.parseDouble(cartDetails.get(i).getQuantity()) + "");
                                    createOrderProductDetails.setOrderprice((Double.parseDouble(createOrderProductDetails.getOrderprice()) + Double.parseDouble(productConfigurationPrice.getValue())) + "");
                                    if (cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getProd_configtype().equalsIgnoreCase("msg")) {
                                        MsgConfigurationData msgConfigurationData = new MsgConfigurationData();
                                        msgConfigurationData.setMsg(cartDetails.get(i).getMessageonproduct());
                                        productConfiguration.setData(msgConfigurationData);
                                    } else if (cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getProd_configtype().equalsIgnoreCase("ftp")) {
                                        FtpConfigurationData ftpConfigurationData = new FtpConfigurationData();
                                        ftpConfigurationData.setFtp(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getFoodType());
                                        productConfiguration.setData(ftpConfigurationData);
                                    }
                                    productConfigurationPrice.setUom(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getProd_configprice().getUom());
                                    productConfiguration.setProd_configprice(productConfigurationPrice);
                                    productConfiguration.setChecked(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).isChecked());
                                    productConfiguration.setFoodType(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getFoodType());
                                    productConfiguration.setProd_configtype(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getProd_configtype());
                                    productConfiguration.setProd_configname(cartDetails.get(i).getProductconfiguration().getConfiguration().get(j).getProd_configname());
                                    createOrderProductDetails.getProductconfiguration().add(productConfiguration);

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cartDetails.get(i).getProductid() != null) {
                    createOrderProductDetails.setProductid(cartDetails.get(i).getProductid());
                }
                if (cartDetails.get(i).getLocation() != null) {
                    createOrderProductDetails.setLocation(cartDetails.get(i).getLocation());
                }
                if (cartDetails.get(i).getProductname() != null) {
                    createOrderProductDetails.setProductname(cartDetails.get(i).getProductname());
                }
                if (cartDetails.get(i).getProviderName() != null) {
                    createOrderProductDetails.setProvidername(cartDetails.get(i).getProviderName());
                }
                createOrderProductDetails.setCartCount(cartDetails.get(i).getCartCount());

                if (!orderBillingAddressDetails.getDate().isEmpty()) {
                    createOrderCartList.setPreferred_delivery_date(orderBillingAddressDetails.getDate());
                }

                createOrderCartList.getCart().add(createOrderProductDetails);
            }
            Collections.sort(createOrderCartList.getCart(), new CustomComparator());

            createOrderCartList.setBilling_address(orderBillingAddressDetails);
            createOrderCartList.setDelivery_address(orderDeliveryAddressDetails);

            if (DeliveryPaymentActivity.payment_mode.equalsIgnoreCase("Cash on Delivery")) {
                createOrderCartList.setPaymentmode("COD");
            } else {
                createOrderCartList.setPaymentmode("PAYTM");
            }
            createOrderData.setOrderdata(createOrderCartList);
            new AdapterForMultipleProviders(context, createOrderCartList.getCart(), orderDeliveryAddressDetails, orderBillingAddressDetails.getDate()).setMultipleProvidersList();
//            createOrderCartList.setDeliverycharges(createOrderCartList.getDeliverycharges());
            displayOrderData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayOrderData() throws Exception {

        textGrandTotal.setText("Grand Total:");
        payment_mode.setText(DeliveryPaymentActivity.payment_mode);
        expected_delivery_date.setText(createOrderData.getOrderdata().getBilling_address().getDate());

        billingAddressTextView.setText(createOrderData.getOrderdata().getBilling_address().getAddress1() + ", " +
                createOrderData.getOrderdata().getBilling_address().getAddress2() + ", " +
                createOrderData.getOrderdata().getBilling_address().getArea() + ",\n" +
                createOrderData.getOrderdata().getBilling_address().getCity() + ". " +
                createOrderData.getOrderdata().getBilling_address().getZipcode() + "\n" +
                createOrderData.getOrderdata().getBilling_address().getState() + ", " +
                createOrderData.getOrderdata().getBilling_address().getCountry());

                llayout_delivery_address.setVisibility(View.VISIBLE);
                shippingAddressTextView.setVisibility(View.VISIBLE);
                shippingAddressTextView.setText(createOrderData.getOrderdata().getDelivery_address().getAddress1() + ", " +
                createOrderData.getOrderdata().getDelivery_address().getAddress2() + ", " +
                createOrderData.getOrderdata().getDelivery_address().getArea() + ",\n" +
                createOrderData.getOrderdata().getDelivery_address().getCity() + ". " +
                createOrderData.getOrderdata().getDelivery_address().getZipcode() + "\n" +
                createOrderData.getOrderdata().getDelivery_address().getState() + ", " +
                createOrderData.getOrderdata().getDelivery_address().getCountry());


//        }

    }

    public void confirmOrder(View view) {
        if (!createOrderData.getOrderdata().getBilling_address().getDate().isEmpty()) {

            new PlaceOrderAsync().execute();

        } else {

            Toast.makeText(getApplicationContext(), "Please Select a delivery date", Toast.LENGTH_LONG).show();

        }
    }

    public String placeAnOrder() {
        String resultPlaceAnOrder = "";
        String jsonToSendOverServer = "";
        try {
            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(createOrderData);
            Log.d("order", jsonToSendOverServer);
            resultPlaceAnOrder = ServerConnection.executePost1(jsonToSendOverServer, "/api/createorder");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPlaceAnOrder;
    }

    public class PlaceOrderAsync extends AsyncTask<String, Integer, String> {
        String resultPlaceAnOrder, connectedOrNot, msg, code;
        ProgressDialog progressDialog;
        JSONObject jObj;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(OrderDetailsActivity.this, "", "Placing your order...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(getApplicationContext()).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultPlaceAnOrder = placeAnOrder();
                    if (!resultPlaceAnOrder.isEmpty()) {
                        Log.d("result create order", resultPlaceAnOrder);
                        jObj = new JSONObject(resultPlaceAnOrder);
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
            progressDialog.dismiss();
            try {
                if (connectedOrNot.equals("success")) {
                    if (!resultPlaceAnOrder.isEmpty()) {
                        if (jObj.has("success")) {

                            if (DeliveryPaymentActivity.payment_mode.equalsIgnoreCase("Cash on Delivery")) {
                                Intent goToFinalOrderActivity = new Intent(OrderDetailsActivity.this, FinalOrderActivity.class);
                                goToFinalOrderActivity.putExtra("FINAL_ORDER", resultPlaceAnOrder);
                                goToFinalOrderActivity.putExtra("TXN_DETAILS", "");
                                goToFinalOrderActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goToFinalOrderActivity);
                            } else {
                                Intent goToPaymentActivity = new Intent(OrderDetailsActivity.this, PaymentActivity.class);
                                goToPaymentActivity.putExtra("FINAL_ORDER", resultPlaceAnOrder);
                                startActivity(goToPaymentActivity);
                            }
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

    public class CustomComparator implements Comparator<CreateOrderProductDetails> {
        @Override
        public int compare(CreateOrderProductDetails o1, CreateOrderProductDetails o2) {
            return o1.getBranchid().compareTo(o2.getBranchid());
        }
    }

    public String loadPreferencesUserDataForBillingAddress() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            user = spLoad.getString("USER_DATA", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public String loadPreferencesUserDataForDeliveryAddress() throws Exception {
        String user = "";
        try {
            SharedPreferences spLoad = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            user = spLoad.getString("USER_DATA_DELIVERY_ADDRESS", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    private class AddressAdapter extends BaseAdapter {

        Context context;
        List<CreateOrderProductDetails> createOrderProductDetailsList;
        String providerName;
        ArrayList<String> providers;
        List<Location> providersAddress;

        AddressAdapter(Context context, List<CreateOrderProductDetails> createOrderProductDetailsList) {
            this.context = context;
            this.createOrderProductDetailsList = createOrderProductDetailsList;

            providers = new ArrayList<>();
            providersAddress = new ArrayList<>();

            for (int i = 0; i < createOrderProductDetailsList.size(); i++) {

                providerName = createOrderProductDetailsList.get(i).getProvidername();

//                providerArea = createOrderProductDetailsList.get(i).getLocation().getArea();
                if (providers.contains(providerName)) {

                    continue;

                } else {

                    providers.add(providerName);
                    providersAddress.add(createOrderProductDetailsList.get(i).getLocation());
                }
            }
        }

        @Override
        public int getCount() {
            return providers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.address_list_item, null);

            TextView address = (TextView) convertView.findViewById(R.id.address);
//            address.setText(providers.get(position));
            if (providersAddress.get(position) != null) {
                address.setText(providersAddress.get(position).getAddress1() + ", "
                        + providersAddress.get(position).getAddress2() + ", "
                        + providersAddress.get(position).getArea() + ", \n"
                        + providersAddress.get(position).getCity() + ". "
                        + providersAddress.get(position).getZipcode() + "\n"
                        + providersAddress.get(position).getState() + ", "
                        + providersAddress.get(position).getCountry());
            }
            return convertView;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new LinearLayout.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getDividerHeight());
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

}