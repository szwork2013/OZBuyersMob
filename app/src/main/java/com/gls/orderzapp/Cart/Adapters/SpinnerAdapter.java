package com.gls.orderzapp.Cart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 12/8/14.
 */
public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    public SpinnerAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.weight_spinner_items, null);
        }
        TextView text = (TextView) view.findViewById(R.id.text);

         if(list.get(i) != null)
            text.setText(list.get(i));

        return view;
    }
}
