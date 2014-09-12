package com.gls.orderzapp.DrawerDetails.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.DrawerDetails.Bean.LevelFourCategoryDoc;
import com.gls.orderzapp.DrawerDetails.Bean.LevelFourCategoryProvider;
import com.gls.orderzapp.MainApp.StartUpActivity;
import com.gls.orderzapp.Provider.Beans.BranchInfo;
import com.gls.orderzapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by avi on 9/10/14.
 */
public class DrawerExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<LevelFourCategoryDoc> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<LevelFourCategoryProvider>> _listDataChild;

    public DrawerExpandableListAdapter(Context context, List<LevelFourCategoryDoc> listDataHeader,
                                 HashMap<String, List<LevelFourCategoryProvider>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._listDataHeader.get(groupPosition).getProvider().get(childPosititon).getProvidername();
    }
    public Object getProviderID(int groupPosition, int childPosititon) {

        return this._listDataHeader.get(groupPosition).getProvider().get(childPosititon).getProviderid();
    }
    public Object getCategoryID(int groupPosition) {

        return this._listDataHeader.get(groupPosition).getCategoryid();
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        final String getcategoryid=(String)getCategoryID(groupPosition);
        final String getproviderid=(String)getProviderID(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        final TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartUpActivity st=(StartUpActivity)_context;
                st.stringValue(getproviderid,getcategoryid);

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getProvider().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getCategoryname();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}