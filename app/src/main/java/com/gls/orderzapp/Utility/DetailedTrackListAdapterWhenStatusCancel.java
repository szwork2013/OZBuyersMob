package com.gls.orderzapp.Utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.MyOrdersListActivity;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by prajyot on 11/6/14.
 */
public class DetailedTrackListAdapterWhenStatusCancel extends BaseAdapter {
    Context context;
    List<String> list;
    List<String> serverTrackList;
    int pos;
    int parent_position;
    MyOrdersListActivity activity;

    public DetailedTrackListAdapterWhenStatusCancel(Context context, List<String> serverTrackList, List<String> list, int pos, int parent_position) {
        this.context = context;
        activity = (MyOrdersListActivity) this.context;
        this.list = list;
        this.serverTrackList = serverTrackList;
        this.pos = pos;
        this.parent_position = parent_position;

//        Log.d("list size", list.size()+"");
//        Log.d("server list", MyOrdersListActivity.serverTrackingStatus.size()+"");
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
            convertView = li.inflate(R.layout.detailed_track_list_item, null);
        }

        ImageView imageTick = (ImageView) convertView.findViewById(R.id.imageTick);
        TextView textStatus = (TextView) convertView.findViewById(R.id.textStatus);

        imageTick.setVisibility(View.INVISIBLE);
        Log.d("trackingstatus", activity.serverTrackingStatus.size()+"");
        for (int i = 0; i < activity.serverTrackingStatus.get(parent_position).get(pos).size(); i++) {
            Log.d("track status", activity.serverTrackingStatus.get(parent_position).get(pos).get(i));
            if (list.get(position).split("-")[1].contains(activity.serverTrackingStatus.get(parent_position).get(pos).get(i))) {
                imageTick.setVisibility(View.VISIBLE);
            } else {

                continue;

            }
        }

        textStatus.setText(list.get(position).split("-")[0]);
        return convertView;
    }
}
