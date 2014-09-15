package com.gls.orderzapp.Provider.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.Provider.Beans.SelectCityDetails;
import com.gls.orderzapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by amit on 10/9/14.
 */
public class SelectCityAdapter extends ArrayAdapter {
    Context context;
    List<String> selectcity;

    public SelectCityAdapter(Context context, int resource, List<String> selectcity){
        super(context,resource,selectcity);
        this.context = context;

        this.selectcity = selectcity;

        selectcity.removeAll(Arrays.asList(null,""));

       // this.selectcity.add(0,"ALL");


    }


    @Override
    public int getCount() {
        return selectcity.size();
    }

    @Override
    public Object getItem(int i) {
        return selectcity.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.select_city_item, null);
        }

        TextView cityname = (TextView) convertView.findViewById(R.id.CityNameText);

        if(!selectcity.get(position).trim().isEmpty() || !selectcity.get(position).equals("") ) {

            String city = Character.toUpperCase(selectcity.get(position).charAt(0)) + selectcity.get(position).substring(1);

            cityname.setText(city);

           // cityname.setText(selectcity.get(position));
        }
       // if()

        return convertView;
    }
}
