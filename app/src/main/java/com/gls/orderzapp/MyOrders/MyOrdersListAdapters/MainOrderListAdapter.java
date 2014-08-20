package com.gls.orderzapp.MyOrders.MyOrdersListAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.MainApp.CurrentOrdersActivity;
import com.gls.orderzapp.MainApp.DetailedMyOrderActivity;
import com.gls.orderzapp.MainApp.TabActivityForOrders;
import com.gls.orderzapp.MyOrders.Beans.OrderDetails;
import com.gls.orderzapp.MyOrders.Beans.PostSubOrderId;
import com.gls.orderzapp.MyOrders.MyOrderDetailAdapters.CancelOrderItemAdapter;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.CheckConnection;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

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
    public static LinearLayout list_cancel_order;
    PostSubOrderId data;
    AlertDialog alertDialog;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int position1 = position;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.myorder_list_content, null);


        TextView textOrderNumber = (TextView) convertView.findViewById(R.id.text_order_no);
        TextView txt_order_date = (TextView) convertView.findViewById(R.id.txt_order_date);
        TextView orderNumber = (TextView) convertView.findViewById(R.id.order_no);
        TextView grandTotal = (TextView) convertView.findViewById(R.id.grand_total);
        final Button btn_cancel_order=(Button)convertView.findViewById(R.id.btn_cancel_order);
        subOrderList = (ListView) convertView.findViewById(R.id.subOrderList);
        if(context.getClass().getSimpleName().equalsIgnoreCase("CurrentOrdersActivity"))
        {
            btn_cancel_order.setVisibility(View.VISIBLE);
        }else{
            btn_cancel_order.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailedMyOrderActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ORDER", new Gson().toJson(myOrderList.get(position1)));
                context.startActivity(i);
            }
        });
        if (myOrderList.get(position).getOrderid() != null) {
            orderNumber.setText(myOrderList.get(position).getOrderid());
        }
        grandTotal.setText(String.format("%.2f", Double.parseDouble(myOrderList.get(position).getTotal_order_price())));
        btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View dialogView = li.inflate(R.layout.cancel_order_view, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(dialogView);
                boolean isOrderCancellable = false;
                 list_cancel_order= (LinearLayout) dialogView.findViewById(R.id.ll_suborder_ids);
                final Button btn_confirm_cancel_order=(Button) dialogView.findViewById(R.id.btn_confirm_cancel_order);
                TextView noCancellableOrder = (TextView) dialogView.findViewById(R.id.noCancellableOrder);
                TextView txt_order_cancel_warning= (TextView) dialogView.findViewById(R.id.txt_order_cancel_warning);
                LinearLayout llNoOrderToCancel = (LinearLayout) dialogView.findViewById(R.id.llNoOrderToCancel);
                if(myOrderList.get(position).getPayment().getMode().equalsIgnoreCase("COD"))
                {
                    txt_order_cancel_warning.setVisibility(View.GONE);
                }else{txt_order_cancel_warning.setVisibility(View.VISIBLE);}

                list_cancel_order.removeAllViews();
                Log.d("myOrderSubOrder",new Gson().toJson(myOrderList.get(position).getSuborder()));
//                CancelOrderItemAdapter.suborderid.clear();
                for(int j = 0; j < myOrderList.get(position).getSuborder().size(); j++){
                    if(myOrderList.get(position).getSuborder().get(j).getStatus().equalsIgnoreCase("orderreceived") || myOrderList.get(position).getSuborder().get(j).getStatus().equalsIgnoreCase("accepted")){
                        isOrderCancellable = true;
                    }
                }

                if(isOrderCancellable == true){
                    llNoOrderToCancel.setVisibility(View.GONE);
                    btn_confirm_cancel_order.setText("Confirm Cancel Order");
                }else{
                    llNoOrderToCancel.setVisibility(View.VISIBLE);
                    btn_confirm_cancel_order.setText("Close");
                }
                new CancelOrderItemAdapter(context,myOrderList.get(position).getSuborder(),myOrderList.get(position).getOrderid()).getView();
                // set prompts.xml to alertdialog builder

                btn_confirm_cancel_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
if(btn_confirm_cancel_order.getText().toString().equalsIgnoreCase("Close")){
    alertDialog.dismiss();
}else {
    getSubOrderIds();
    new GetCancelOrderAsync().execute();
    alertDialog.dismiss();
}
                    }

                });

                alertDialog = alertDialogBuilder.create();

                alertDialog.setInverseBackgroundForced(true);

                // show it
                alertDialog.show();


            }
        });
        try {

            final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

            final DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
            TimeZone tz = TimeZone.getTimeZone("Asia/Calcutta");

            outputFormat.setTimeZone(tz);
            Date order_date = inputFormat.parse(myOrderList.get(position).getCreatedate());
            txt_order_date.setText(outputFormat.format(order_date));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myOrderList.get(position).getSuborder() != null) {
            subOrderList.setAdapter(new SubOrderListAdapter(context, myOrderList.get(position).getSuborder(), position));
            setListViewHeightBasedOnChildren(subOrderList);
        }

        return convertView;
    }
    public void getSubOrderIds()
    {
         data=new PostSubOrderId();
        data.setSuborderids(CancelOrderItemAdapter.suborderid);

    }
    public String getCancelOrder() {
        String resultOfCancelOrder = "";
        String jsonToSendOverServer = "";
        try {

            GsonBuilder gBuild = new GsonBuilder();
            Gson gson = gBuild.disableHtmlEscaping().create();
            jsonToSendOverServer = gson.toJson(data);
            Log.d("jsonToSendOverServer", jsonToSendOverServer+"    ---"+CancelOrderItemAdapter.mainOrderId);
            resultOfCancelOrder = ServerConnection.executePost1(jsonToSendOverServer, "/api/cancelorder/"+CancelOrderItemAdapter.mainOrderId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultOfCancelOrder;
    }
    private class GetCancelOrderAsync extends AsyncTask<String, Integer, String> {
        JSONObject jObj;
        String connectedOrNot, resultOfCancelOrder, msg, code;
//        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {

//            progressDialog = ProgressDialog.show(context, "", "");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (new CheckConnection(context).isConnectingToInternet()) {
                    connectedOrNot = "success";
                    resultOfCancelOrder = getCancelOrder();
                    if (!resultOfCancelOrder.isEmpty()) {
                        Log.d("resultOfCancelOrder", resultOfCancelOrder);
                        jObj = new JSONObject(resultOfCancelOrder);
                        if (jObj.has("success")) {
                            Log.d("Successresponse for resultOfCancelOrder", resultOfCancelOrder);
                        } else {
                            JSONObject jObjError = jObj.getJSONObject("error");
                            msg = jObjError.getString("message");
                            code = jObjError.getString("code");
                        }
                    }
                } else {
                    connectedOrNot = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connectedOrNot;
        }

        @Override
        protected void onPostExecute(String connectedOrNot) {
//            progressDialog.dismiss();
            try {
                if (connectedOrNot.equalsIgnoreCase("success")) {
                    if (!resultOfCancelOrder.isEmpty()) {
                        if (jObj.has("success")) {
                            Intent intent=new Intent(context,TabActivityForOrders.class);
                            (context).startActivity(intent);
                        } else {

                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Server is not responding please try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        params.height = totalHeight + (gridView.getDividerHeight() * listAdapter.getCount());
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}
