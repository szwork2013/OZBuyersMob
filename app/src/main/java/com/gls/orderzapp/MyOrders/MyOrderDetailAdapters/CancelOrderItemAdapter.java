package com.gls.orderzapp.MyOrders.MyOrderDetailAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.MyOrders.MyOrdersListAdapters.MainOrderListAdapter;
import com.gls.orderzapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avi on 8/14/14.
 * //
 */


public class CancelOrderItemAdapter {
    public static String mainOrderId = "";
    public static List<String> suborderid = new ArrayList<>();
    Context context;
    List<SubOrderDetails> subOrderDetailsList;
    CheckBox cancel_suborderid_list_iteam;
    LinearLayout ll;
    int i = 0;

    public CancelOrderItemAdapter(Context context, List<SubOrderDetails> subOrderDetailsList, String orderId) {
        this.mainOrderId = orderId;
        this.context = context;
        this.subOrderDetailsList = subOrderDetailsList;
    }

    public void getView() {
        for (i = 0; i < subOrderDetailsList.size(); i++) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ll = (LinearLayout) li.inflate(R.layout.cancel_suborder_list_iteam, null);
            cancel_suborderid_list_iteam = (CheckBox) ll.findViewById(R.id.cancel_suborderid_list_iteam);
            if (subOrderDetailsList.get(i).getStatus().equalsIgnoreCase("cancelledbyconsumer") || subOrderDetailsList.get(i).getStatus().equalsIgnoreCase("cancelled")) {
            } else {
                if (subOrderDetailsList.get(i).getStatus().equalsIgnoreCase("orderreceived") || subOrderDetailsList.get(i).getStatus().equalsIgnoreCase("accepted")) {
                    cancel_suborderid_list_iteam.setClickable(true);
                    cancel_suborderid_list_iteam.setEnabled(true);
                } else {
                    cancel_suborderid_list_iteam.setClickable(false);
                    cancel_suborderid_list_iteam.setEnabled(false);
                    cancel_suborderid_list_iteam.setTextColor(Color.GRAY);
                }
                cancel_suborderid_list_iteam.setId(1000 + i);
                cancel_suborderid_list_iteam.setText(subOrderDetailsList.get(i).getSuborderid());
                cancel_suborderid_list_iteam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            suborderid.add(subOrderDetailsList.get((compoundButton).getId() - 1000).getSuborderid());
                        } else {
                            for (int j = 0; j < suborderid.size(); j++) {
                                if (suborderid.get(j).equalsIgnoreCase(subOrderDetailsList.get((compoundButton).getId() - 1000).getSuborderid())) {
                                    suborderid.remove(subOrderDetailsList.get((compoundButton).getId() - 1000).getSuborderid());
                                }
                            }
                        }
                    }
                });
                if (subOrderDetailsList.size() > 0) {
                    MainOrderListAdapter.list_cancel_order.addView(ll);
                }
            }
        }
    }
}


//public class CancelOrderItemAdapter extends BaseAdapter {
//    Context context;
//    String suborder_id;
//    String status;
//    int suborderid_position;
//    CheckedTextView cancel_suborderid_list_iteam;
//    public CancelOrderItemAdapter( Context context,String suborder_id,String status,int suborderid_position)
//    {
//        this.context=context;
//        this.suborder_id=suborder_id;
//        this.status=status;
//        this.suborderid_position=suborderid_position;
//    }
//    @Override
//    public int getCount() {
//        return 1;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = li.inflate(R.layout.cancel_suborder_list_iteam, null);
//        cancel_suborderid_list_iteam= (CheckedTextView) view.findViewById(R.id.cancel_suborderid_list_iteam);
//        cancel_suborderid_list_iteam.setText(suborder_id);
//        return view;
//    }
//}
