package com.gls.orderzapp.CountryCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by amit on 1/9/14.
 */
public class CountryCodeAdapter extends ArrayAdapter {
    Context context;
    List<CountryCode> countryCodeList;

    public CountryCodeAdapter(Context context, int resource, List<CountryCode> countryCodeList) {
        super(context, resource, countryCodeList);

        this.context = context;
        this.countryCodeList = countryCodeList;
    }


//    public CountryCodeAdapter(Context context, List<CountryCode> countryCodeList){
//        this.context = context;
//        this.countryCodeList = countryCodeList;
//    }

    @Override
    public int getCount() {
        return countryCodeList.size();
    }

    @Override
    public Object getItem(int i) {
        return countryCodeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.sing_in_country_code, null);
        }

        TextView countryCodeTextView = (TextView) view.findViewById(R.id.countryCodeTextView);
        TextView countryName = (TextView) view.findViewById(R.id.countryName);

        if(countryCodeList.get(position).getCode()!=null) {
            countryCodeTextView.setText(countryCodeList.get(position).getCode());
        }
        if(countryCodeList.get(position).getCountry()!=null) {
            countryName.setText(Character.toUpperCase(countryCodeList.get(position).getCountry().charAt(0)) + countryCodeList.get(position).getCountry().substring(1));
        }

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.country_code_spinner_view, null);
        }
        TextView code = (TextView) view.findViewById(R.id.code);

        code.setText(countryCodeList.get(position).getCode());

        return view;
    }
}
