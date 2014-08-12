package com.gls.orderzapp.ProductConfiguration.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import com.gls.orderzapp.CreateOrder.CreateOrderBeans.ProductConfiguration;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 7/7/14.
 */
public class PopUpForConfigurationCharges {
    Context context;
    View view;
    List<ProductConfiguration> productConfigurationList;

    public PopUpForConfigurationCharges(Context context, View view, List<ProductConfiguration> productConfigurationList) {
        this.context = context;
        this.view = view;
        this.productConfigurationList = productConfigurationList;

        displayConfigurationCharges();
    }

    public void displayConfigurationCharges() {
        PopupMenu popupMenu = new PopupMenu(context, view);
//        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = li.inflate(R.layout.popup_view, null);

        popupMenu.getMenuInflater().inflate(R.menu.configuration_popup, popupMenu.getMenu());

        Log.d("popup", "popup");
        for (int i = 0; i < productConfigurationList.size(); i++) {
            if (productConfigurationList.get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                popupMenu.getMenu().add(productConfigurationList.get(i).getFoodType() + "            " + context.getResources().getString(R.string.rs) + " " + productConfigurationList.get(i).getProd_configprice().getValue());
            } else if (productConfigurationList.get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                popupMenu.getMenu().add(productConfigurationList.get(i).getProd_configname() + "            " + context.getResources().getString(R.string.rs) + " " + productConfigurationList.get(i).getProd_configprice().getValue());
            }
        }
        popupMenu.show();
    }
}
