package com.gls.orderzapp.MyOrders.MyOrderDetailAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.R;

import java.util.List;

/**
 * Created by avi on 8/14/14.
 */
public class CancelOrderAdapter extends BaseAdapter {
    Context context;
    List<SubOrderDetails> subOrderDetailsList;
   public static LinearLayout ll_cnl_order;
    Button btn_confirm_cancel_order;
    int pos;
    public CancelOrderAdapter(Context context, List<SubOrderDetails> subOrderDetailsList, int pos)
    {
        this.context=context;
        this.subOrderDetailsList=subOrderDetailsList;
        this.pos=pos;
    }
    @Override
    public int getCount() {
        return subOrderDetailsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.ll_cancel_order_iteam, null);
        ll_cnl_order=(LinearLayout) convertView.findViewById(R.id.ll_cnl_order);
        ll_cnl_order.removeAllViews();
//        new CancelOrderItemAdapter(context,subOrderDetailsList.get(pos)).getView();

        return convertView;
    }
}
