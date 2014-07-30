package com.gls.orderzapp.CreateOrder.OrderResponseAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.OrderedSubOrderDetails;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by avi on 7/30/14.
 */
public class HomeDeliveryAddressAdapter extends BaseAdapter {
    Context context;
    List<OrderedSubOrderDetails> orderedSubOrderDetailsList;

    public HomeDeliveryAddressAdapter(Context context, List<OrderedSubOrderDetails> orderedSubOrderDetailsList) {
        this.context = context;
        this.orderedSubOrderDetailsList = orderedSubOrderDetailsList;


    }

    @Override
    public int getCount() {
        return orderedSubOrderDetailsList.size();
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
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.address_list_item, null);
        TextView sellerName = (TextView) convertView.findViewById(R.id.txt_sellerName);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        LinearLayout ll_pikup_address= (LinearLayout)convertView.findViewById(R.id.ll_pikup_address);
        View view=(View)convertView.findViewById(R.id.view);
        if (orderedSubOrderDetailsList.get(position).getPickup_address() != null) {
            if(orderedSubOrderDetailsList.get(position).getDeliverytype().equalsIgnoreCase("home")){
                address.setVisibility(View.GONE);
                        ll_pikup_address.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                sellerName.setText(orderedSubOrderDetailsList.get(position).getProductprovider().getProvidername());
            }
        }
        return convertView;

    }
}

