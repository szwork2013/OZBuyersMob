package com.gls.orderzapp.Utility;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gls.orderzapp.MainApp.MyOrdersListActivity;
import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.MyOrders.MyOrdersListAdapters.MainOrderListAdapter;
import com.gls.orderzapp.MyOrders.MyOrdersListAdapters.SubOrderListAdapter;
import com.gls.orderzapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 23/5/14.
 */
public class TrackingView {
    public static void trackOrder(Context context, String status) {

        Log.d("track ball status", status);
        switch (status) {

            case "ordercomplete":
                Log.d("complete", status);
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_green);
                break;
            case "homedelivery":
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_green);
                break;
            case "storepickup":
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_green);
                break;
            case "factorytostore":
                Log.d("factory to store", status);
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_white);
                break;
            case "packing":
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_white);
                break;
            case "inproduction":
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_white);
                break;
            case "rejected":
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_red);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_white);
                break;
            case "accepted":
                Log.d("status", status);
                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_white);
                break;
            case "orderstart":

                SubOrderListAdapter.llApproval.setBackgroundResource(R.drawable.tracking_round_green);
                SubOrderListAdapter.llOrderProcessing.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llShipping.setBackgroundResource(R.drawable.tracking_round_white);
                SubOrderListAdapter.llDelivery.setBackgroundResource(R.drawable.tracking_round_white);
                break;

        }
    }

    public static void detailedTrack(Context context, SubOrderDetails subOrder, String clickedItem, int position, int parent_position) {
        List<String> list = new ArrayList<>();
        DetailedTrackListAdapter detailedTrackListAdapter;
        switch (clickedItem) {
            case "orderstart":
                list.clear();
                list.add("Orderstart-orderstart,accepted,inproduction,packing,factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapter(context, list, subOrder, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;

            case "accepted":
                list.clear();

                list.add("Accepted-accepted,inproduction,packing,factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapter(context, list, subOrder, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;
            case "rejected":
                list.clear();
                list.add("Rejected-rejected,inproduction,packing,factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapter(context, list, subOrder, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;

            case "orderprocessing":
                list.clear();
                list.add("In Production-inproduction,packing,factorytostore,storepickup,homedelivery,ordercomplete");
                list.add("Packing-packing,factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapter(context, list, subOrder, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;

            case "shipping":
                list.clear();
                list.add("Factory to Store-factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapter(context, list, subOrder, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;

            case "delivery":
                list.clear();
                list.add("Store Pickup-storepickup,homedelivery,ordercomplete");
                list.add("Home delivery-homedelivery,ordercomplete");
                list.add("Order complete-ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapter(context, list, subOrder, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;
        }
    }

    public static void orderDetailsWhenCancelled(Context context, String clickedItem, SubOrderDetails subOrder, int position, int parent_position) {
        List<String> serverList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        DetailedTrackListAdapterWhenStatusCancel detailedTrackListAdapter;
        serverList.clear();
//        list.clear();

        for (int i = 0; i < subOrder.getTracking().size(); i++) {
            serverList.add(subOrder.getTracking().get(i).getStatus());
        }
        switch (clickedItem) {

            case "accepted":
                list.clear();
                list.add("Accepted-accepted,inproduction,packing,factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapterWhenStatusCancel(context, serverList, list, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;
            case "orderprocessing":
//                Log.d("")
                list.clear();
                list.add("In Production-inproduction,packing,factorytostore,storepickup,homedelivery,ordercomplete");
                list.add("Packing-packing,factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapterWhenStatusCancel(context, serverList, list, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;

            case "shipping":
                list.clear();
                list.add("Factory to Store-factorytostore,storepickup,homedelivery,ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapterWhenStatusCancel(context, serverList, list, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;

            case "delivery":
                list.clear();
                list.add("Store Pickup-storepickup,homedelivery,ordercomplete");
                list.add("Home delivery-homedelivery,ordercomplete");
                list.add("Order complete-ordercomplete");
                detailedTrackListAdapter = new DetailedTrackListAdapterWhenStatusCancel(context, serverList, list, position, parent_position);
                detailedTrackListAdapter.notifyDataSetChanged();
                SubOrderListAdapter.listDetailedTrack.setAdapter(detailedTrackListAdapter);
                setListViewHeightBasedOnChildren(SubOrderListAdapter.listDetailedTrack);
                break;
        }

    }

    public static void detailedTrackWhenClicked(Context context, String clickedItem, int position, int parent_position) {
        try {
            MyOrdersListActivity activity = (MyOrdersListActivity) context;
            int index = activity.mainOrderList.getFirstVisiblePosition();

            View v = activity.mainOrderList.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            switch (clickedItem) {
                case "accepted":

                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).setStatus("accepted");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;
                case "orderstart":

                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).setStatus("orderstart");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;
                case "rejected":

                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).setStatus("rejected");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;
                case "orderprocessing":
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).setStatus("inproduction");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;
                case "shipping":
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).setStatus("factorytostore");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;
                case "delivery":
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).setStatus("delivery");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detailedTrackWhenClickedWhileOrderIsCancelled(Context context, SubOrderDetails subOrderDetails, String clickedItem, int position, int parent_position) {
        try {
            MyOrdersListActivity activity = (MyOrdersListActivity) context;
            int index = activity.mainOrderList.getFirstVisiblePosition();
            Log.d("cancelled", "cancelled");
            View v = activity.mainOrderList.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            switch (clickedItem) {
                case "accepted":
//                    SubOrderDetails.statusBeforeCancelled = "accepted";
//                    subOrderDetails.getTracking().get(subOrderDetails.getTracking().size()-2).setStatus("accepted");
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).getTracking().get(subOrderDetails.getTracking().size() - 2).setStatus("accepted");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;

                case "orderstart":
//                    subOrderDetails.getTracking().get(subOrderDetails.getTracking().size()-2).setStatus("orderstart");
//                    PastOrdersActivity.statusBeforeCancelled = "orderprocessing";
//
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).getTracking().get(subOrderDetails.getTracking().size() - 2).setStatus("orderstart");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;

                case "orderprocessing":
//                    subOrderDetails.getTracking().get(subOrderDetails.getTracking().size()-2).setStatus("inproduction");
//                    PastOrdersActivity.statusBeforeCancelled = "inproduction";
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).getTracking().get(subOrderDetails.getTracking().size() - 2).setStatus("inproduction");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;

                case "shipping":
//                    subOrderDetails.getTracking().get(subOrderDetails.getTracking().size()-2).setStatus("factorytostore");
//                    PastOrdersActivity.statusBeforeCancelled = "factorytostore";
                    Log.d("prev status", clickedItem);
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).getTracking().get(subOrderDetails.getTracking().size() - 2).setStatus("factorytostore");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;

                case "delivery":
//                    subOrderDetails.getTracking().get(subOrderDetails.getTracking().size()-2).setStatus("delivery");
//                    PastOrdersActivity.statusBeforeCancelled = "delivery";
                    MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders().get(parent_position).getSuborder().get(position).getTracking().get(subOrderDetails.getTracking().size() - 2).setStatus("delivery");

                    activity.mainOrderList.setAdapter(new MainOrderListAdapter(context, MyOrdersListActivity.successResponseForMyOrders.getSuccess().getOrders()));
                    activity.mainOrderList.setSelectionFromTop(index, top);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        params.height = totalHeight + (gridView.getDividerHeight() * (listAdapter.getCount() + 1));
//        Log.d("height", params.height+" ");
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}
