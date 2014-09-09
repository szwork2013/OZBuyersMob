package com.gls.orderzapp.Cart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 17/7/14.
 */
public class CityAreaListAdapter extends BaseAdapter {

    Context context;
    List<String> list;

    public CityAreaListAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

//        Log.d("list", new Gson().toJson(list));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.city_area_list_item, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);

        if(list.get(position) != null || !list.get(position).isEmpty() || list.get(position).length() >= 1) {
            name.setText(Character.toUpperCase(list.get(position).charAt(0)) + list.get(position).substring(1));
        }
        return convertView;
    }
}
