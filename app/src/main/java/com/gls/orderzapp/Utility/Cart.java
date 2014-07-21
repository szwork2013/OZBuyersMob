package com.gls.orderzapp.Utility;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gls.orderzapp.AddressDetails.Adapter.DisplayDeliveryChargesAndType;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.CreateOrderProductDetails;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.ProductConfiguration;
import com.gls.orderzapp.CreateOrder.CreateOrderBeans.SuccessResponseForDeliveryChargesAndType;
import com.gls.orderzapp.MainApp.CartActivity;
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

    public static HashMap<String, ProductDetails> hm = new HashMap<>();
    static TextView numberTextOnCart;
    public static int productCount = 0;
//    static Animation zoomin, zoomout;

    public static void addToCart(ProductDetails productDetails, Context context) {
        try {
            productCount++;
            productDetails.setCartCount(productCount + "");
            hm.put(productCount + "", productDetails);
//                Log.d("produc count", productCount+"");
//                Log.d("cart count", new Gson().toJson(hm));
            setTextOnCartCount();
//            zoomin = AnimationUtils.loadAnimation(context, R.anim.zoomin);
//            zoomout = AnimationUtils.loadAnimation(context, R.anim.zoomout);
//            numberTextOnCart.setAnimation(zoomin);
//            numberTextOnCart.setAnimation(zoomout);
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
        try {
            int hmSize = hm.size();
            String[] keys = hm.keySet().toArray(new String[hmSize]);
            for (int i = 0; i < hmSize; i++) {
                Log.d("keys", keys[i]);
                Log.d("position", position);
                if (keys[i].equals(position)) {

                    Log.d("keys inner", keys[i]);
                    Log.d("position inner", position);
//                    productDetails.setQuantity(quantity);
//                    productDetails.getPrice().setUom(measure);
                    hm.get(keys[i]).setQuantity(quantity);
                    hm.get(keys[i]).getPrice().setUom(measure);
                    hm.put(position, hm.get(keys[i]));
                }
            }
            Log.d("update cart", new Gson().toJson(hm));
            //ProductCartActivity.txt_sub_total.setText(Math.round(Cart.subTotal()*100.0)/100.0+"");
//            ProductCartActivity.txt_sub_total.setText(String.format("%.2f", Cart.subTotal()));
            CartActivity.grand_total.setText(String.format("%.2f", Cart.subTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return getCount();
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

                if (list.getProductconfiguration().get(j).getFoodType().equalsIgnoreCase("eggless")) {
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
                if (DisplayDeliveryChargesAndType.delivery_mode[i].equalsIgnoreCase("home")) {
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


}