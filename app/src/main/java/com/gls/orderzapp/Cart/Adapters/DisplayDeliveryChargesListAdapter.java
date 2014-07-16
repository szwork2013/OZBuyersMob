package com.gls.orderzapp.Cart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.Cart.Beans.DeliveryChargeDetails;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 30/6/14.
 */
public class DisplayDeliveryChargesListAdapter extends BaseAdapter {

    Context context;
    List<DeliveryChargeDetails> deliveryChargeDetailse;

    public DisplayDeliveryChargesListAdapter(Context context, List<DeliveryChargeDetails> deliveryChargeDetailse) {
        this.context = context;
        this.deliveryChargeDetailse = deliveryChargeDetailse;
    }

    @Override
    public int getCount() {
        return deliveryChargeDetailse.size();
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
        convertView = li.inflate(R.layout.delivery_charges_list_item, null);

        TextView provider_name = (TextView) convertView.findViewById(R.id.provider_name);
        TextView charges = (TextView) convertView.findViewById(R.id.charges);

        if (deliveryChargeDetailse.get(position).isDelivery() == true) {
//            charges.setText(deliveryChargeDetailse.get(position).getDeliverycharge());
        } else {
            charges.setText("No delivery available in this area");
        }
        return convertView;
    }
}
