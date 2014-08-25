package com.gls.orderzapp.ProductConfiguration.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.PopupMenu;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.ProductConfiguration;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by avi on 7/31/14.
 */
public class PopUpForDisplayProductConfigurationOnFinalOrder {
    Context context;
    View view;
    List<ProductConfiguration> productConfigurationList;

    public PopUpForDisplayProductConfigurationOnFinalOrder(Context context, View view, List<ProductConfiguration> productConfigurationList) {
        this.context = context;
        this.view = view;
        this.productConfigurationList = productConfigurationList;
    }

    public void displayConfigurationCharges() {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.configuration_popup, popupMenu.getMenu());
        for (int i = 0; i < productConfigurationList.size(); i++) {
            if (productConfigurationList.get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                popupMenu.getMenu().add(productConfigurationList.get(i).getData().getFtp() + "            " + context.getResources().getString(R.string.rs) + " " + productConfigurationList.get(i).getProd_configprice().getValue());
            } else if (productConfigurationList.get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                popupMenu.getMenu().add(productConfigurationList.get(i).getProd_configname() + "            " + context.getResources().getString(R.string.rs) + " " + productConfigurationList.get(i).getProd_configprice().getValue());
            }
        }
        popupMenu.show();
    }
}

