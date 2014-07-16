package com.gls.orderzapp.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.MyOrdersListActivity;
import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by avinash on 23/5/14.
 */
public class DetailedTrackListAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    SubOrderDetails subOrder;
    int pos;
    int parent_position;

    public DetailedTrackListAdapter(Context context, List<String> list, SubOrderDetails subOrder, int pos, int parent_position) {
        this.context = context;
        this.list = list;
        this.subOrder = subOrder;
        this.pos = pos;
        this.parent_position = parent_position;
    }

    @Override
    public int getCount() {
        return list.size();
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
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.detailed_track_list_item, null);
        }

        ImageView imageTick = (ImageView) convertView.findViewById(R.id.imageTick);
        TextView textStatus = (TextView) convertView.findViewById(R.id.textStatus);
        textStatus.setText(list.get(position).split("-")[0]);
//        Log.d("status", AdapterForMainOrder.actualList.get(pos));
        if (list.get(position).split("-")[1].contains(MyOrdersListActivity.actualList.get(parent_position).get(pos))) {
            imageTick.setVisibility(View.VISIBLE);
        } else {
            imageTick.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
}
