package com.gls.orderzapp.MyOrders.MyOrdersListAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.DetailedOrderActivity;
import com.gls.orderzapp.MyOrders.Beans.OrderDetails;
import com.gls.orderzapp.R;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by prajyot on 3/6/14.
 */
public class MainOrderListAdapter extends BaseAdapter {

    Context context;
    List<OrderDetails> myOrderList;
    ListView subOrderList;

    public MainOrderListAdapter(Context context, List<OrderDetails> myOrderList) {
        this.context = context;
        this.myOrderList = myOrderList;
    }

    @Override
    public int getCount() {
        return myOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return myOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int position1 = position;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.myorder_list_content, null);


        TextView textOrderNumber = (TextView) convertView.findViewById(R.id.text_order_no);
        TextView txt_order_date = (TextView) convertView.findViewById(R.id.txt_order_date);
        TextView txt_expected_delivery_date = (TextView) convertView.findViewById(R.id.txt_expected_delivery_date);
        TextView orderNumber = (TextView) convertView.findViewById(R.id.order_no);
        TextView grandTotal = (TextView) convertView.findViewById(R.id.grand_total);
        subOrderList = (ListView) convertView.findViewById(R.id.subOrderList);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailedOrderActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ORDER", new Gson().toJson(myOrderList.get(position1)));
                context.startActivity(i);
            }
        });
        if (myOrderList.get(position).getOrderid() != null) {
            orderNumber.setText(myOrderList.get(position).getOrderid());
        }
        grandTotal.setText(String.format("%.2f", Double.parseDouble(myOrderList.get(position).getTotal_order_price())));

        try {

            final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

            final DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
            TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");

            outputFormat.setTimeZone(tz);

            Date order_date = inputFormat.parse(myOrderList.get(position).getCreatedate());
            txt_order_date.setText(outputFormat.format(order_date));
            Date preferd_delivery_date = inputFormat.parse(myOrderList.get(position).getPreferred_delivery_date());
            txt_expected_delivery_date.setText(outputFormat.format(preferd_delivery_date));


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myOrderList.get(position).getSuborder() != null) {
            subOrderList.setAdapter(new SubOrderListAdapter(context, myOrderList.get(position).getSuborder(), position));
            setListViewHeightBasedOnChildren(subOrderList);
        }

        return convertView;
    }

    public static void setListViewHeightBasedOnChildren(ListView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new LinearLayout.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getDividerHeight() * listAdapter.getCount());
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}
