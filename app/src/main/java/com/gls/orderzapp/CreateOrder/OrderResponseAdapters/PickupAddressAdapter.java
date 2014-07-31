package com.gls.orderzapp.CreateOrder.OrderResponseAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.Address;
import com.gls.orderzapp.CreateOrder.OrderResponseBeans.OrderedSubOrderDetails;
import com.gls.orderzapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 2/7/14.
 */
public class PickupAddressAdapter extends BaseAdapter {
    Context context;
    List<OrderedSubOrderDetails> orderedSubOrderDetailsList=new ArrayList<>();
     public  int p_size=0;
    public PickupAddressAdapter(Context context, List<OrderedSubOrderDetails> orderedSubOrderDetailsList) {
    this.context = context;
        this.orderedSubOrderDetailsList.clear();
        this.orderedSubOrderDetailsList = orderedSubOrderDetailsList;
        Log.d("orderedSubOrderDetailsList", new Gson().toJson(orderedSubOrderDetailsList));
//        p_size=0;
        for(int i=0;i<orderedSubOrderDetailsList.size();i++)
        {
            if(orderedSubOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("pickup"))
            {
                Log.d("PICKUP P_name",orderedSubOrderDetailsList.get(i).getProductprovider().getProvidername());
                p_size++;
                Log.d("p_size++",p_size+"");
            }
        }
    }

    @Override
    public int getCount() {
//        return orderedSubOrderDetailsList.size();
        return p_size;
    }

    @Override
    public Object getItem(int position) {
        return orderedSubOrderDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.address_list_item, null);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView sellerName = (TextView) convertView.findViewById(R.id.txt_sellerName);
//        if (orderedSubOrderDetailsList.get(position).getPickup_address() != null) {
            if(orderedSubOrderDetailsList.get(position).getDeliverytype().equalsIgnoreCase("pickup")){
                sellerName.setText(orderedSubOrderDetailsList.get(position).getProductprovider().getProvidername());
                address.setText(orderedSubOrderDetailsList.get(position).getPickup_address().getAddress1() + ", "
                    + orderedSubOrderDetailsList.get(position).getPickup_address().getAddress2() + ", "
                    + orderedSubOrderDetailsList.get(position).getPickup_address().getArea() + ",\n"
                    + orderedSubOrderDetailsList.get(position).getPickup_address().getCity() + ", "
                    + orderedSubOrderDetailsList.get(position).getPickup_address().getZipcode() + ".\n"
                    + orderedSubOrderDetailsList.get(position).getPickup_address().getState() + ", "
                    + orderedSubOrderDetailsList.get(position).getPickup_address().getCountry());
        }
        return convertView;

    }
}
