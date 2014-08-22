package com.gls.orderzapp.ProductConfiguration.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;


import com.gls.orderzapp.MyOrders.Beans.ProductConfiguration;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 5/8/14.
 */
public class ConfigurationPopupMenuMyOrders {
    Context context;
    View view;
    List<ProductConfiguration> productConfigurationList;

    public ConfigurationPopupMenuMyOrders(Context context, View view, List<ProductConfiguration> productConfigurationList) {
        this.context = context;
        this.view = view;
        this.productConfigurationList = productConfigurationList;
    }

    public void displayConfigurationCharges() {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.configuration_popup, popupMenu.getMenu());
        String food_configName="";
        for (int i = 0; i < productConfigurationList.size(); i++) {
            if (productConfigurationList.get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                popupMenu.getMenu().add(  productConfigurationList.get(i).getData().getFtp()+"            " + context.getResources().getString(R.string.rs) + " " + productConfigurationList.get(i).getProd_configprice().getValue());
            } else if (productConfigurationList.get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                food_configName="Message";
                popupMenu.getMenu().add(food_configName + "            " + context.getResources().getString(R.string.rs) + " " + productConfigurationList.get(i).getProd_configprice().getValue());
            }
        }
        popupMenu.show();
    }
}
