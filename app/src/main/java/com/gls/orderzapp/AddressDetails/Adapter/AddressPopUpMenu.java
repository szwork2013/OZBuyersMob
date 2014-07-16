package com.gls.orderzapp.AddressDetails.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.gls.orderzapp.MainApp.ChangeAddressActivity;
import com.gls.orderzapp.MainApp.SelectAddressListActivity;
import com.gls.orderzapp.R;

/**
 * Created by prajyot on 24/6/14.
 */
public class AddressPopUpMenu {
    Context context;
    View popUpView;

    public AddressPopUpMenu(Context context, View popUpView) {
        this.context = context;
        this.popUpView = popUpView;

    }

    public void handlePopupEvents() {
        PopupMenu popup = new PopupMenu(context, popUpView);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu_for_delivery_address, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.select_from_existing:
                        Intent goToSelectListAddressactivity = new Intent(context, SelectAddressListActivity.class);
                        ((Activity) context).startActivityForResult(goToSelectListAddressactivity, 1);

                        break;
                    case R.id.add_new:
                        Intent goToChangeAddressActivity = new Intent(context, ChangeAddressActivity.class);
                        ((Activity) context).startActivityForResult(goToChangeAddressActivity, 1);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
