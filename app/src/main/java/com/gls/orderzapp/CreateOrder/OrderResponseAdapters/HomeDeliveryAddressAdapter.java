package com.gls.orderzapp.CreateOrder.OrderResponseAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.CreateOrder.OrderResponseBeans.OrderedSubOrderDetails;
import com.gls.orderzapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 7/30/14.
 */
public class HomeDeliveryAddressAdapter extends BaseAdapter {
    Context context;
    List<OrderedSubOrderDetails> orderedSubOrderDetailsList=new ArrayList<>();
    List<OrderedSubOrderDetails> deliveryAddress = new ArrayList<>();
     public  int  size=0;
    public HomeDeliveryAddressAdapter(Context context,List<OrderedSubOrderDetails> orderedSubOrderDetailsList) {
        this.context = context;
        this.orderedSubOrderDetailsList.clear();
        this.orderedSubOrderDetailsList=orderedSubOrderDetailsList;
        Log.d("orderedSubOrderDetailsList",orderedSubOrderDetailsList.size()+" "+this.orderedSubOrderDetailsList.size());
//size=0;
        for(int i=0;i<orderedSubOrderDetailsList.size();i++)
        {
            Log.d("i----",i+"");
            if(orderedSubOrderDetailsList.get(i).getDeliverytype().equalsIgnoreCase("home"))
            {
                Log.d("HomeDELIVERY P_name",orderedSubOrderDetailsList.get(i).getProductprovider().getProvidername());
                size++;
                deliveryAddress.add(orderedSubOrderDetailsList.get(i));
                Log.d("size++",size+"");
            }
        }


    }

    @Override
    public int getCount() {
        return deliveryAddress.size();
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
        convertView = li.inflate(R.layout.homedelivery_providerlist, null);
        TextView sellerName = (TextView) convertView.findViewById(R.id.txt_sellerName);
        if(deliveryAddress.get(position).getProductprovider().getProviderbrandname() != null)
         sellerName.setText(deliveryAddress.get(position).getProductprovider().getProviderbrandname());
        return convertView;

    }
}

