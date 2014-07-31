package com.gls.orderzapp.Utility;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderProductDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.ProductConfiguration;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SuccessResponseForDeliveryChargesAndType;
import com.gls.orderzapp.MainApp.CartActivity;
import com.gls.orderzapp.Provider.Beans.DeliveryType;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by prajyot on 10/4/14.
 */
public class Cart {

    public static HashMap<String, ProductDetails> hm = new HashMap<String, ProductDetails>();

    static TextView numberTextOnCart;
    public static int productCount = 0;
//    static Animation zoomin, zoomout;

    public static void addToCart(ProductDetails productDetails, Context context) {

        ProductDetails localProduct = new ProductDetails();
        try {
            productCount++;
            productDetails.setCartCount(productCount + "");
            localProduct = productDetails;

            hm.put(productCount +"" , localProduct);
            setTextOnCartCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCount() {
        return hm.size();
    }

    public static void cartCount(Menu menu) {
        try {
            RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.cart).getActionView();
            ImageView imageCart = (ImageView) badgeLayout.findViewById(R.id.img_badge);
            numberTextOnCart = (TextView) badgeLayout.findViewById(R.id.text_badge);
            //do not show the count if count is 0
            if (getCount() > 0) {
                setTextOnCartCount();
            } else {
                numberTextOnCart.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cartCountLoadMore(Menu menu) {
        try {
            RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.loadmore_cart).getActionView();
            ImageView imageCart = (ImageView) badgeLayout.findViewById(R.id.img_badge);
            numberTextOnCart = (TextView) badgeLayout.findViewById(R.id.text_badge);
            //do not show the count if count is 0
            if (getCount() > 0) {
                setTextOnCartCount();
            } else {
                numberTextOnCart.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTextOnCartCount() {
        try {
            numberTextOnCart.setVisibility(View.VISIBLE);
//            numberTextOnCart.startAnimation(zoomin);
            numberTextOnCart.setText(Cart.getCount() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromCart(String productId) {
        try {
//            Log.d("before delete count", productId);
            hm.remove(productId);

//            Log.d("after delete", new Gson().toJson(hm));
//            ProductCartActivity.txt_sub_total.setText(Cart.subTotal() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double subTotal() {
        double sub_total = 0.00;
        try {
            String[] keys = hm.keySet().toArray(new String[hm.size()]);
            for (int i = 0; i < getCount(); i++) {
                if (hm.get(keys[i]).getPrice().getUom().equalsIgnoreCase("Kg")) {
                    double temp_product_price = hm.get(keys[i]).getPrice().getValue() * Double.parseDouble(hm.get(keys[i]).getQuantity());
                    sub_total = sub_total + temp_product_price;
                } else if (hm.get(keys[i]).getPrice().getUom().equalsIgnoreCase("lb")) {
                    double temp_product_price = hm.get(keys[i]).getPrice().getValue() * Double.parseDouble(hm.get(keys[i]).getQuantity());
                    sub_total = sub_total + temp_product_price;
                } else if (hm.get(keys[i]).getPrice().getUom().equalsIgnoreCase("Gm")) {
                    double temp_product_price = ((Double.parseDouble(hm.get(keys[i]).getPrice().getValue() + "")) * (Double.parseDouble(hm.get(keys[i]).getQuantity()))) / 1000;
                    sub_total = sub_total + temp_product_price;
                } else if (hm.get(keys[i]).getPrice().getUom().equalsIgnoreCase("No")) {
                    double temp_product_price = hm.get(keys[i]).getPrice().getValue() * Double.parseDouble(hm.get(keys[i]).getQuantity());
                    sub_total = sub_total + temp_product_price;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub_total;
    }


    public static double currentProviderSubTotal(List<CreateOrderProductDetails> list) {
        double total = 0.00;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUom().equalsIgnoreCase("kg") || list.get(i).getUom().equalsIgnoreCase("no") || list.get(i).getUom().equalsIgnoreCase("lb")) {
                    total = total + Double.parseDouble(list.get(i).getOrderprice());
                } else {
                    total = total + (Double.parseDouble(list.get(i).getOrderprice())) / 1000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public static double providerSubTotalInCart(List<ProductDetails> list) {
        double total = 0.00;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPrice().getUom().equalsIgnoreCase("kg") || list.get(i).getPrice().getUom().equalsIgnoreCase("no") || list.get(i).getPrice().getUom().equalsIgnoreCase("lb")) {
                    total = total + Double.parseDouble(list.get(i).getQuantity()) * list.get(i).getPrice().getValue();
                } else {
                    total = total + (Double.parseDouble(list.get(i).getQuantity()) * list.get(i).getPrice().getValue()) / 1000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public static void updateCart(String position, String quantity, String measure) {
        ProductDetails localProduct;
        try {
            int hmSize = hm.size();
            String[] keys = hm.keySet().toArray(new String[hmSize]);
            for (int i = 0; i < hmSize; i++) {
                if (keys[i].equals(position)) {
                    localProduct = hm.get(keys[i]);
                    localProduct.getPrice().setUom(measure);
                    localProduct.setQuantity(quantity);
                    hm.put(position, localProduct);
                }
            }
            Log.d("cart", new Gson().toJson(hm));
            CartActivity.grand_total.setText(String.format("%.2f", Cart.subTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String returnUom(String cartCount){
        String uom = "";
        String[] mKeys = Cart.hm.keySet().toArray(new String[hm.size()]);
        try{
            for(int i = 0; i < mKeys.length; i++){
                if(mKeys[i].equalsIgnoreCase(cartCount)){
                    uom = Cart.hm.get(mKeys[i]).getPrice().getUom();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return uom;
    }

    public static void addMessageOnCake(String position, ProductDetails productDetails, String messageoncake) {
        try {
            int hmSize = hm.size();
            String[] keys = hm.keySet().toArray(new String[hmSize]);

            for (int i = 0; i < hmSize; i++) {
                if (keys[i].equals(productDetails.getCartCount())) {
                    productDetails.setMessageonproduct(messageoncake);
                    hm.put(keys[i], productDetails);

                    Log.d("add message", new Gson().toJson(productDetails));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ProductDetails> getCartDetails() {
        ArrayList<ProductDetails> al = new ArrayList<>();
        String[] keys = hm.keySet().toArray(new String[hm.size()]);
        try {
            for (int i = 0; i < getCount(); i++) {
                al.add(hm.get(keys[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return al;
    }

public static int deleteConfigObject()
{
    try{
        String[] keys = hm.keySet().toArray(new String[hm.size()]);
        final int keySize = keys.length;
        for (int i = 0; i < keySize; i++) {
//            if (Cart.hm.get(keys[i]).getMessageonproduct().equalsIgnoreCase("none")) {
                for(int k=0;k<Cart.hm.get(keys[i]).getProductconfiguration().getConfiguration().size();k++)
                {
                if(Cart.hm.get(keys[i]).getProductconfiguration().getConfiguration().get(k).getProd_configtype().equalsIgnoreCase("msg")){
                        if(Cart.hm.get(keys[i]).getProductconfiguration().getConfiguration().get(k).isChecked()==false || Cart.hm.get(keys[i]).getMessageonproduct().isEmpty() || Cart.hm.get(keys[i]).getMessageonproduct().equalsIgnoreCase("none"))
                        {
                            Cart.hm.get(keys[i]).getProductconfiguration().getConfiguration().remove(k);

                        }
                }
                }
//            }
        }

        Log.d("delete conig", new Gson().toJson(hm));


    }catch(Exception e){e.printStackTrace();}
    return getCount();
}
    public static int deleteFromCartIfQuantityIsZero() {
        try {
            String[] keys = hm.keySet().toArray(new String[hm.size()]);
            final int keySize = keys.length;
            for (int i = 0; i < keySize; i++) {
                if (Double.parseDouble(Cart.hm.get(keys[i]).getQuantity()) == 0.0) {
                    Cart.deleteFromCart(keys[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm.size();
    }

    public static boolean checkForPrductConfigurarion() {
        boolean isCake = false;
        try {
            String[] keys = hm.keySet().toArray(new String[hm.size()]);
            final int keySize = keys.length;

            for (int i = 0; i < keySize; i++) {
                Log.d("config", new Gson().toJson(Cart.hm.get(keys[i])));
                if (Cart.hm.get(keys[i]).getProductconfiguration().getConfiguration().size() <= 0) {

                } else {
                    isCake = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCake;
    }

    public static double configurredProductPrice(List<CreateOrderProductDetails> list) {
        double configurationTotal = 0.0;
        try {
            for (int i = 0; i < list.size(); i++) {
//                Log.d("list", new Gson().toJson(list));
                for (int j = 0; j < list.get(i).getProductconfiguration().size(); j++) {
                    if (list.get(i).getProductconfiguration().get(j).isChecked() == true) {
                        configurationTotal = configurationTotal + Double.parseDouble(list.get(i).getProductconfiguration().get(j).getProd_configprice().getValue());
                    }

                    if (list.get(i).getProductconfiguration().get(j).getFoodType().equalsIgnoreCase("eggless")) {
                        configurationTotal = configurationTotal + Double.parseDouble(list.get(i).getProductconfiguration().get(j).getProd_configprice().getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurationTotal;
    }

    public static List<ProductConfiguration> configurationList(CreateOrderProductDetails list) {
        List<ProductConfiguration> configurationList = new ArrayList<>();
        try {
            for (int j = 0; j < list.getProductconfiguration().size(); j++) {
                if (list.getProductconfiguration().get(j).isChecked() == true) {
                    configurationList.add(list.getProductconfiguration().get(j));
                }

                if (list.getProductconfiguration().get(j).getFoodType().equalsIgnoreCase("eggless") || list.getProductconfiguration().get(j).getFoodType().equalsIgnoreCase("egg")) {
                    configurationList.add(list.getProductconfiguration().get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurationList;
    }

    public static double configurationPrice(CreateOrderProductDetails list) {
        double configurationPrice = 0.0;
        try {
            for (int j = 0; j < list.getProductconfiguration().size(); j++) {
                configurationPrice = configurationPrice + Double.parseDouble(list.getProductconfiguration().get(j).getProd_configprice().getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurationPrice;
    }

    public static double returnDeliveryCharges(SuccessResponseForDeliveryChargesAndType succesResponseForDeliveryChargesAndType) {
        double deliveryCharges = 0.0;
        try {
            for (int i = 0; i < succesResponseForDeliveryChargesAndType.getSuccess().getDeliverycharge().size(); i++) {
                if (DisplayDeliveryChargesAndType.deliveryType.get(i).split("_")[1].equalsIgnoreCase("home")) {
                    deliveryCharges = deliveryCharges + succesResponseForDeliveryChargesAndType.getSuccess().getDeliverycharge().get(i).getCharge();
                } else {
                    deliveryCharges = deliveryCharges + 0.0;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveryCharges;
    }

    public static void saveDeliveryTypeInfoInCart(String branchId, String deliveryType){
        try{
            String[] keys = Cart.hm.keySet().toArray(new String[hm.size()]);
            for(int i = 0; i < hm.size(); i++){
                if(hm.get(keys[i]).getBranchid().equals(branchId)){
                    hm.get(keys[i]).getDeliveryType().setDeliveryType(deliveryType);
                    hm.get(keys[i]).getDeliveryType().setBranchId(branchId);
                }
            }

            Log.d("cart after saving deliverytype", new Gson().toJson(hm));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveDeliveryChargesInfoInCart(){

    }

    public static void saveOrderInstructions(String branchid, String orderInstruction){
        String[] keys = hm.keySet().toArray(new String[hm.size()]);
        for(int i = 0; i < hm.size(); i++){
            if(hm.get(keys[i]).getBranchid().equals(branchid)){
                hm.get(keys[i]).getDeliveryType().setOrderinstructions(orderInstruction);
            }
        }
    }
}