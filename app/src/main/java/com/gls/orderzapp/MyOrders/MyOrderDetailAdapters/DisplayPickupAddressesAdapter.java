package com.gls.orderzapp.MyOrders.MyOrderDetailAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 5/8/14.
 */
public class DisplayPickupAddressesAdapter extends BaseAdapter {
    Context context;
    List<SubOrderDetails> subOrderDetailsList;
    List<SubOrderDetails> pickUpList = new ArrayList<>();

    public DisplayPickupAddressesAdapter(Context context, List<SubOrderDetails> subOrderDetailsList) {
        this.context = context;
        this.subOrderDetailsList = subOrderDetailsList;

        for (int i = 0; i < subOrderDetailsList.size(); i++) {
            if (subOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("pickup")) {

                pickUpList.add(subOrderDetailsList.get(i));

            }
        }
    }

    @Override
    public int getCount() {
        return pickUpList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.address_list_item, null);
        }
        TextView txt_sellerName = (TextView) convertView.findViewById(R.id.txt_sellerName);
        TextView address = (TextView) convertView.findViewById(R.id.address);

        if (pickUpList.get(position).getProductprovider().getProviderbrandname() != null) {
            txt_sellerName.setText(pickUpList.get(position).getProductprovider().getProviderbrandname());
        }

        if (pickUpList.get(position).getPickup_address() != null) {
            address.setText(pickUpList.get(position).getPickup_address().getAddress1() + ", "
                    + pickUpList.get(position).getPickup_address().getAddress2() + ", "
                    + pickUpList.get(position).getPickup_address().getArea() + ",\n"
                    + pickUpList.get(position).getPickup_address().getCity() + ", "
                    + pickUpList.get(position).getPickup_address().getZipcode() + ".\n"
                    + pickUpList.get(position).getPickup_address().getState() + ", "
                    + pickUpList.get(position).getPickup_address().getCountry());
        }
        return convertView;
    }
}
