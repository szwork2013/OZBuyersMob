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
import com.gls.orderzapp.MainApp.DeliveryAddressActivity;
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
        staticDeliveryAddressActivity();
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

    }
    public static void staticCart()
    {
        Cart.hm.clear();
        Cart.numberTextOnCart.setText("");
        Cart.productCount=0;
    }
    public static void staticGridAdapterProviderCategories()
    {
        GridAdapterProviderCategories.branch_id=null;
    }
    public static void staticSubOrderListAdapter()
    {
        SubOrderListAdapter.listDetailedTrack.removeAllViews();
        SubOrderListAdapter.llApproval.removeAllViews();
        SubOrderListAdapter.llDelivery.removeAllViews();
        SubOrderListAdapter.llOrderProcessing.removeAllViews();
    }
    public static void staticAdapterForSubOrders()
    {
        AdapterForSubOrders.ll.removeAllViews();
    }
    public static void staticTabActivityForOrders()
    {
        TabActivityForOrders.isload=false;
    }
    public static void staticStartUpActivity()
    {
        StartUpActivity.isFirstTime = true;
        StartUpActivity.searchString=null;
        StartUpActivity.linearLayoutCategories.removeAllViews();
    }
    public static void staticSignInActivity()
    {
        SignInActivity.islogedin=false;
    }
    public static void staticSettingsActivity()
    {
        SettingsActivity.userID=null;
        SettingsActivity.avoidFirstClick=false;
    }
    public static void staticSelectAddressListActivity()
    {
        SelectAddressListActivity.isAddNewaddress=false;
    }
    public static void staticProductConfigurationActivity()
    {
        ProductConfigurationActivity.cakeproductDetailes.clear();
        ProductConfigurationActivity.product_configuration_list.removeAllViews();
    }
    public static void staticOrderDetailsActivity()
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
    public static void staticMyOrdersListActivity()
    {
        MyOrdersListActivity.actualList.clear();
        MyOrdersListActivity.successResponseForMyOrders=null;

    }
    public static void staticForgotPasswordActivity()
    {
        ForgotPasswordActivity.otp.setText("");
        ForgotPasswordActivity.textOtp.setText("");
    }
    public static void staticFinalOrderActivity()
    {
        FinalOrderActivity.listProducts.removeAllViews();
        FinalOrderActivity.ll_txn_details.removeAllViews();
    }
    public static void staticDetailedOrderActivity()
    {
        DetailedOrderActivity.listProducts.removeAllViews();
    }
    public static void staticDeliveryPaymentActivity()
    {
        DeliveryPaymentActivity.payment_mode =null;
        DeliveryPaymentActivity.user_id=null;
        DeliveryPaymentActivity.shipping_address_textview.setText("");
        DeliveryPaymentActivity.billing_address_textview.setText("");
        DeliveryPaymentActivity.ll_deliver_charge_type.removeAllViews();
    }
    public static void staticDeliveryAddressActivity()
    {
        DeliveryAddressActivity.date_selected = false;
        DeliveryAddressActivity.isload=false;
    }
    public static void staticChangeAddressActivity()
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
    public static void staticCartActivity()
    {
        CartActivity.area_text.setText("");
        CartActivity.grand_total.setText("");
        CartActivity.llCartList.removeAllViews();
    }

    public static void staticAdapterForFinalOrderMultipleProviders()
    {
        AdapterForFinalOrderMultipleProviders.ll.removeAllViews();
    }
    public static void staticAdapterForMultipleProviders()
    {
        AdapterForMultipleProviders.ll.removeAllViews();
    }
    public static void staticProductListAdapter()
    {
        ProductListAdapter.min_weight=0;
        ProductListAdapter.max_weight=0;
    }
    public static void staticProductCartAdapter()
    {
        ProductCartAdapter.measure=null;
        ProductCartAdapter.max_weight=0;
        ProductCartAdapter.min_weight=0;
    }
    public static void staticCartAdapter()
    {
        CartAdapter.llCartListItemView.removeAllViews();
        CartAdapter.llProductList.removeAllViews();
        CartAdapter.listText.clear();
        CartAdapter.sub_total.setText("");
        CartAdapter.productList.clear();
    }
    public static void staticDisplayDeliveryChargesAndType()
    {
        DisplayDeliveryChargesAndType.order_instruction=null;
        DisplayDeliveryChargesAndType.deliveryType=null;
        DisplayDeliveryChargesAndType.listOfDeliveryCharges=null;
        DisplayDeliveryChargesAndType.listOfPickupAddresses=null;
        DisplayDeliveryChargesAndType.listPickUpButtons.clear();
    }
    public static void staticAdapterForPickUpAddressList()
    {
        // clear static veriable from  AdapterForPickUpAddressList
        AdapterForPickUpAddressList.pickupAddressFromList=null;
        AdapterForPickUpAddressList.isPickUpAddressSelected=false;
    }
    public static void staticAdapterForSelectaddressList()
    {
        // clear static veriable from  AdapterForSelectaddressList
        AdapterForSelectaddressList.deliveryaddressid=null;
        AdapterForSelectaddressList.deliveryAddressList=null;
    }
    public static void staticDeliveryChargesAndTypeAdapter()
    {
        // clear static veriable from  DeliveryChargesAndTypeAdapter
        DeliveryChargesAndTypeAdapter.llDeliveryChargeAndType.removeAllViews();
        DeliveryChargesAndTypeAdapter.branchIdsForGettingDeliveryCharges=null;
        DeliveryChargesAndTypeAdapter.successResponseForDeliveryCharges=null;
        DeliveryChargesAndTypeAdapter.successResponseOfUserDeliveryAddresDetails=null;
    }

}
