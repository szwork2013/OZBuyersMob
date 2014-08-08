package com.gls.orderzapp.Utility;

import android.content.Intent;

import com.gls.orderzapp.AddressDetails.Adapter.AdapterForPickUpAddressList;
import com.gls.orderzapp.AddressDetails.Adapter.AdapterForSelectaddressList;
import com.gls.orderzapp.AddressDetails.Adapter.DeliveryChargesAndTypeAdapter;
import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.Cart.Adapters.CartAdapter;
import com.gls.orderzapp.Cart.Adapters.ProductCartAdapter;
import com.gls.orderzapp.Cart.Adapters.ProductListAdapter;
import com.gls.orderzapp.CreateOrder.CreateOrderAdapters.AdapterForMultipleProviders;
import com.gls.orderzapp.CreateOrder.OrderResponseAdapters.AdapterForFinalOrderMultipleProviders;
import com.gls.orderzapp.MainApp.CartActivity;
import com.gls.orderzapp.MainApp.ChangeAddressActivity;
import com.gls.orderzapp.MainApp.DeliveryPaymentActivity;
import com.gls.orderzapp.MainApp.DetailedOrderActivity;
import com.gls.orderzapp.MainApp.FinalOrderActivity;
import com.gls.orderzapp.MainApp.ForgotPasswordActivity;
import com.gls.orderzapp.MainApp.MyOrdersListActivity;
import com.gls.orderzapp.MainApp.OrderDetailsActivity;
import com.gls.orderzapp.MainApp.ProductConfigurationActivity;
import com.gls.orderzapp.MainApp.SelectAddressListActivity;
import com.gls.orderzapp.MainApp.SettingsActivity;
import com.gls.orderzapp.MainApp.SignInActivity;
import com.gls.orderzapp.MainApp.StartUpActivity;
import com.gls.orderzapp.MainApp.TabActivityForOrders;
import com.gls.orderzapp.MyOrders.MyOrderDetailAdapters.AdapterForSubOrders;
import com.gls.orderzapp.MyOrders.MyOrdersListAdapters.SubOrderListAdapter;
import com.gls.orderzapp.Provider.Adapters.GridAdapterProviderCategories;

/**
 * Created by avi on 8/1/14.
 */
public class ResetStaticData {
    public static void ResetData()
    {
       try {
           staticAdapterForPickUpAddressList();
           staticAdapterForSelectaddressList();
           staticDeliveryChargesAndTypeAdapter();
           staticDisplayDeliveryChargesAndType();
           staticCartAdapter();
           staticProductCartAdapter();
           staticProductListAdapter();
           staticAdapterForMultipleProviders();
           staticAdapterForFinalOrderMultipleProviders();
           staticCartActivity();
           staticChangeAddressActivity();
           staticDeliveryPaymentActivity();
           staticDetailedOrderActivity();
           staticFinalOrderActivity();
           staticForgotPasswordActivity();
           staticMyOrdersListActivity();
           staticOrderDetailsActivity();
           staticProductConfigurationActivity();
           staticSelectAddressListActivity();
           staticSettingsActivity();
           staticSignInActivity();
           staticStartUpActivity();
           staticTabActivityForOrders();
           staticAdapterForSubOrders();
           staticSubOrderListAdapter();
           staticGridAdapterProviderCategories();
           staticCart();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public static void staticCart () throws Exception
    {
        Cart.hm.clear();
        Cart.numberTextOnCart.setText("");
        Cart.productCount=0;
    }

    public static void staticGridAdapterProviderCategories() throws Exception
    {
        GridAdapterProviderCategories.branch_id=null;
    }
    public static void staticSubOrderListAdapter() throws Exception
    {
        SubOrderListAdapter.listDetailedTrack.removeAllViews();
        SubOrderListAdapter.llApproval.removeAllViews();
        SubOrderListAdapter.llDelivery.removeAllViews();
        SubOrderListAdapter.llOrderProcessing.removeAllViews();
    }
    public static void staticAdapterForSubOrders() throws Exception
    {
        AdapterForSubOrders.ll.removeAllViews();
    }
    public static void staticTabActivityForOrders() throws Exception
    {
        TabActivityForOrders.isload=false;
    }
    public static void staticStartUpActivity() throws Exception
    {
        StartUpActivity.isFirstTime = true;
        StartUpActivity.searchString=null;
        StartUpActivity.linearLayoutCategories.removeAllViews();
    }
    public static void staticSignInActivity() throws Exception
    {
        SignInActivity.islogedin=false;
    }
    public static void staticSettingsActivity() throws Exception
    {
        SettingsActivity.userID=null;
        SettingsActivity.avoidFirstClick=false;
    }
    public static void staticSelectAddressListActivity() throws Exception
    {
        SelectAddressListActivity.isAddNewaddress=false;
    }
    public static void staticProductConfigurationActivity() throws Exception
    {
//        ProductConfigurationActivity.cakeproductDetailes.clear();
        ProductConfigurationActivity.product_configuration_list.removeAllViews();
    }
    public static void staticOrderDetailsActivity() throws Exception
    {
        OrderDetailsActivity.llProductsList.removeAllViews();
        OrderDetailsActivity.llayout_delivery_address.removeAllViews();
        OrderDetailsActivity.textGrandTotal.setText("");OrderDetailsActivity.grandTotal.setText("");
        OrderDetailsActivity.billingAddressTextView.setText("");OrderDetailsActivity.shippingAddressTextView.setText("");
        OrderDetailsActivity.delivery_type.setText("");OrderDetailsActivity.payment_mode.setText("");
        OrderDetailsActivity.createOrderCartList=null;
        OrderDetailsActivity.deliveryChargeDetails=null;
//
    }
    public static void staticMyOrdersListActivity() throws Exception
    {
        MyOrdersListActivity.actualList.clear();
        MyOrdersListActivity.successResponseForMyOrders=null;

    }
    public static void staticForgotPasswordActivity() throws Exception
    {
        ForgotPasswordActivity.otp.setText("");
        ForgotPasswordActivity.textOtp.setText("");
    }
    public static void staticFinalOrderActivity() throws Exception
    {
        FinalOrderActivity.listProducts.removeAllViews();
        FinalOrderActivity.ll_txn_details.removeAllViews();
    }
    public static void staticDetailedOrderActivity() throws Exception
    {
        DetailedOrderActivity.listProducts.removeAllViews();
    }
    public static void staticDeliveryPaymentActivity() throws Exception
    {
        DeliveryPaymentActivity.payment_mode =null;
        DeliveryPaymentActivity.user_id=null;
        DeliveryPaymentActivity.shipping_address_textview.setText("");
        DeliveryPaymentActivity.billing_address_textview.setText("");
        DeliveryPaymentActivity.ll_deliver_charge_type.removeAllViews();
    }

    public static void staticChangeAddressActivity() throws Exception
    {

        ChangeAddressActivity.edittext_address1.setText("");
        ChangeAddressActivity.edittext_address2.setText("");
        ChangeAddressActivity.edittext_area.setText("");
        ChangeAddressActivity.edittext_city.setText("");
        ChangeAddressActivity.edittext_state.setText("");
        ChangeAddressActivity.edittext_country.setText("");
        ChangeAddressActivity.edittext_zipcode.setText("");
        ChangeAddressActivity.isAddressChanged=false;
    }
    public static void staticCartActivity() throws Exception
    {
        CartActivity.area_text.setText("");
        CartActivity.grand_total.setText("");
        CartActivity.llCartList.removeAllViews();
    }

    public static void staticAdapterForFinalOrderMultipleProviders() throws Exception
    {
        AdapterForFinalOrderMultipleProviders.ll.removeAllViews();
    }
    public static void staticAdapterForMultipleProviders() throws Exception
    {
        AdapterForMultipleProviders.ll.removeAllViews();
    }
    public static void staticProductListAdapter() throws Exception
    {
        ProductListAdapter.min_weight=0;
        ProductListAdapter.max_weight=0;
    }
    public static void staticProductCartAdapter() throws Exception
    {
        ProductCartAdapter.measure=null;
        ProductCartAdapter.max_weight=0;
        ProductCartAdapter.min_weight=0;
    }
    public static void staticCartAdapter() throws Exception
    {
        CartAdapter.llCartListItemView.removeAllViews();
        CartAdapter.llProductList.removeAllViews();
        CartAdapter.listText.clear();
        CartAdapter.sub_total.setText("");
//        CartAdapter.productList.clear();
    }
    public static void staticDisplayDeliveryChargesAndType() throws Exception
    {
        DisplayDeliveryChargesAndType.order_instruction=null;
        DisplayDeliveryChargesAndType.checkForDeliveryModeList.clear();
        DisplayDeliveryChargesAndType.deliveryType.clear();
        DisplayDeliveryChargesAndType.listOfDeliveryCharges=null;
//        DisplayDeliveryChargesAndType.listOfPickupAddresses=null;
        DisplayDeliveryChargesAndType.listPickUpButtons.clear();
    }
    public static void staticAdapterForPickUpAddressList() throws Exception
    {
        // clear static veriable from  AdapterForPickUpAddressList
        AdapterForPickUpAddressList.pickupAddressFromList=null;
        AdapterForPickUpAddressList.isPickUpAddressSelected=false;
    }
    public static void staticAdapterForSelectaddressList() throws Exception
    {
        // clear static veriable from  AdapterForSelectaddressList
        AdapterForSelectaddressList.deliveryaddressid=null;
        AdapterForSelectaddressList.deliveryAddressList=null;
    }
    public static void staticDeliveryChargesAndTypeAdapter() throws Exception
    {
        // clear static veriable from  DeliveryChargesAndTypeAdapter
        DeliveryChargesAndTypeAdapter.llDeliveryChargeAndType.removeAllViews();
        DeliveryChargesAndTypeAdapter.branchIdsForGettingDeliveryCharges=null;
        DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges=null;
        DeliveryChargesAndTypeAdapter.successResponseOfUserDeliveryAddresDetails=null;
    }

}
