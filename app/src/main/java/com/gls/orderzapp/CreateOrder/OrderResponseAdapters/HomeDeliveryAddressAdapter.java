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
    String p_name;
     public static int  size=0;
    public HomeDeliveryAddressAdapter(Context context, String p_name) {
        this.context = context;
        this.p_name=p_name;
        size++;


    }

    @Override
    public int getCount() {
        return size;
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
        convertView = li.inflate(R.layout.homedelivery_providerlist, null);
        TextView sellerName = (TextView) convertView.findViewById(R.id.txt_sellerName);
        sellerName.setText(p_name);

        return convertView;

    }
}

