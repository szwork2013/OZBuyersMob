package com.gls.orderzapp.MyOrders.MyOrdersListAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.MyOrdersListActivity;
import com.gls.orderzapp.MyOrders.Beans.SubOrderDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.TrackingView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prajyot on 3/6/14.
 */
public class SubOrderListAdapter extends BaseAdapter {

    Context context;
    List<SubOrderDetails> subOrderDetailsList;
    public static boolean isCancelled;

    DisplayImageOptions options;
    public static ListView listDetailedTrack;
    public static LinearLayout llApproval, llOrderProcessing, llShipping, llDelivery;
    ImageLoader imageLoader;
    int pos;
    Typeface tfRobotoNormal;
    Typeface tfRobotoBold;
    List<String> serverSideTrack = new ArrayList<>();
//    int count;

    public SubOrderListAdapter(Context context, List<SubOrderDetails> subOrderDetailsList, int pos) {
        this.context = context;
        this.subOrderDetailsList = subOrderDetailsList;
        this.pos = pos;

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        tfRobotoNormal = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        tfRobotoBold = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
    }

    @Override
    public int getCount() {
        return subOrderDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return subOrderDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.sub_order_contents, null);

        TextView textSubOrderNumber = (TextView) convertView.findViewById(R.id.text_suborder_no);
        TextView subOrderNumber = (TextView) convertView.findViewById(R.id.sub_order_no);
        ListView listSubOrders = (ListView) convertView.findViewById(R.id.sub_order_list);
        TextView textSeller = (TextView) convertView.findViewById(R.id.text_seller);
        TextView seller = (TextView) convertView.findViewById(R.id.seller);
        ImageView imageProvider = (ImageView) convertView.findViewById(R.id.provider_logo);
        TextView textSubTotal = (TextView) convertView.findViewById(R.id.text_sub_total);
        TextView subTotal = (TextView) convertView.findViewById(R.id.sub_total);
        TextView text_order_cancelled = (TextView) convertView.findViewById(R.id.text_order_cancelled);
        final ImageView imageApproval = (ImageView) convertView.findViewById(R.id.imageApproval);
        final ImageView imageOrderProcessing = (ImageView) convertView.findViewById(R.id.imageOrderProcessing);
        final ImageView imageShipping = (ImageView) convertView.findViewById(R.id.imageshipping);
        final ImageView imageDelivery = (ImageView) convertView.findViewById(R.id.imageDelivery);
        llApproval = (LinearLayout) convertView.findViewById(R.id.approval);
        llOrderProcessing = (LinearLayout) convertView.findViewById(R.id.order_processing);
        llShipping = (LinearLayout) convertView.findViewById(R.id.shipping);
        llDelivery = (LinearLayout) convertView.findViewById(R.id.delivery);
        listDetailedTrack = (ListView) convertView.findViewById(R.id.listDetailedTrack);

        LinearLayout llParentApproved = (LinearLayout) convertView.findViewById(R.id.llParentApproved);
        LinearLayout llParentOrdersProcessing = (LinearLayout) convertView.findViewById(R.id.llParentOrdersProcessing);
        LinearLayout llParentShipping = (LinearLayout) convertView.findViewById(R.id.llParentShipping);
        LinearLayout llParentDelivery = (LinearLayout) convertView.findViewById(R.id.llParentDelivery);


//        if(MyOrdersListActivity.actualList.get(pos).get(position).equals("cancelled")){
        if (subOrderDetailsList.get(position).getStatus() != null && subOrderDetailsList.get(position).getStatus().equals("cancelled")) {
            String prevStatus = "";
            text_order_cancelled.setVisibility(View.VISIBLE);
            text_order_cancelled.setText("Order cancelled on");
            Log.d("if", "if");
            Log.d("MyOrdersAct", subOrderDetailsList.get(position).getStatus());
//            Log.d("track", subOrderDetailsList.get(position).getTracking().get(subOrderDetailsList.get(position).getTracking().size()-2).getStatus());
            //TrackingView.trackOrder(context, MyOrdersListActivity.serverTrackingStatus.get(pos).get(position).get(MyOrdersListActivity.serverTrackingStatus.get(pos).get(position).size()-2));


//            prevStatus = subOrderDetailsList.get(position).getTracking().get((subOrderDetailsList.get(position).getTracking().size()-2)).getStatus();

            prevStatus = subOrderDetailsList.get(position).getTracking().get(subOrderDetailsList.get(position).getTracking().size() - 2).getStatus();

            Log.d("prevsattus", prevStatus);
            if (prevStatus.equals("orderstart")) {
                imageApproval.setVisibility(View.VISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);

                TrackingView.orderDetailsWhenCancelled(context, "orderstart", subOrderDetailsList.get(position), position, pos);
            } else if (prevStatus.equals("accepted")) {
                imageApproval.setVisibility(View.VISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);

                TrackingView.orderDetailsWhenCancelled(context, "accepted", subOrderDetailsList.get(position), position, pos);
            } else if (prevStatus.equals("inproduction") || subOrderDetailsList.get(position).getStatus().equals("packing")) {
                imageApproval.setVisibility(View.INVISIBLE);
                imageOrderProcessing.setVisibility(View.VISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                Log.d("prevsattus", prevStatus);

                TrackingView.orderDetailsWhenCancelled(context, "orderprocessing", subOrderDetailsList.get(position), position, pos);
            } else if (prevStatus.equals("factorytostore")) {
                imageApproval.setVisibility(View.INVISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.VISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                Log.d("factstore", prevStatus);

                TrackingView.orderDetailsWhenCancelled(context, "shipping", subOrderDetailsList.get(position), position, pos);
            } else {
                imageApproval.setVisibility(View.INVISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.VISIBLE);

                TrackingView.orderDetailsWhenCancelled(context, "delivery", subOrderDetailsList.get(position), position, pos);
            }

            TrackingView.trackOrder(context, MyOrdersListActivity.serverTrackingStatus.get(pos).get(position).get(MyOrdersListActivity.serverTrackingStatus.get(pos).get(position).size() - 2));

            llParentApproved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageApproval.setVisibility(View.VISIBLE);
                    imageOrderProcessing.setVisibility(View.INVISIBLE);
                    imageShipping.setVisibility(View.INVISIBLE);
                    imageDelivery.setVisibility(View.INVISIBLE);
                    TrackingView.detailedTrackWhenClickedWhileOrderIsCancelled(context, subOrderDetailsList.get(position), "accepted", position, pos);
                }
            });

            llParentOrdersProcessing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageApproval.setVisibility(View.INVISIBLE);
                    imageOrderProcessing.setVisibility(View.VISIBLE);
                    imageShipping.setVisibility(View.INVISIBLE);
                    imageDelivery.setVisibility(View.INVISIBLE);
                    TrackingView.detailedTrackWhenClickedWhileOrderIsCancelled(context, subOrderDetailsList.get(position), "orderprocessing", position, pos);
                }
            });
            llParentShipping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageApproval.setVisibility(View.INVISIBLE);
                    imageOrderProcessing.setVisibility(View.INVISIBLE);
                    imageShipping.setVisibility(View.VISIBLE);
                    imageDelivery.setVisibility(View.INVISIBLE);
                    TrackingView.detailedTrackWhenClickedWhileOrderIsCancelled(context, subOrderDetailsList.get(position), "shipping", position, pos);
                }
            });

            llParentDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageApproval.setVisibility(View.INVISIBLE);
                    imageOrderProcessing.setVisibility(View.INVISIBLE);
                    imageShipping.setVisibility(View.INVISIBLE);
                    imageDelivery.setVisibility(View.VISIBLE);
                    TrackingView.detailedTrackWhenClickedWhileOrderIsCancelled(context, subOrderDetailsList.get(position), "delivery", position, pos);
                }
            });
        } else {

            Log.d("else", "else");
            text_order_cancelled.setVisibility(View.GONE);
            TrackingView.trackOrder(context, MyOrdersListActivity.actualList.get(pos).get(position));

            if (subOrderDetailsList.get(position).getStatus().equals("orderstart")) {
                imageApproval.setVisibility(View.VISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                TrackingView.detailedTrack(context, subOrderDetailsList.get(position), "orderstart", position, pos);
            } else if (subOrderDetailsList.get(position).getStatus().equals("accepted")) {
                imageApproval.setVisibility(View.VISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                TrackingView.detailedTrack(context, subOrderDetailsList.get(position), "accepted", position, pos);
            } else if (subOrderDetailsList.get(position).getStatus().equals("rejected")) {
                imageApproval.setVisibility(View.VISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                TrackingView.detailedTrack(context, subOrderDetailsList.get(position), "rejected", position, pos);
            } else if (subOrderDetailsList.get(position).getStatus().equals("inproduction") || subOrderDetailsList.get(position).getStatus().equals("packing")) {
                imageApproval.setVisibility(View.INVISIBLE);
                imageOrderProcessing.setVisibility(View.VISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                TrackingView.detailedTrack(context, subOrderDetailsList.get(position), "orderprocessing", position, pos);
            } else if (subOrderDetailsList.get(position).getStatus().equals("factorytostore")) {
                imageApproval.setVisibility(View.INVISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.VISIBLE);
                imageDelivery.setVisibility(View.INVISIBLE);
                TrackingView.detailedTrack(context, subOrderDetailsList.get(position), "shipping", position, pos);

            } else {
                imageApproval.setVisibility(View.INVISIBLE);
                imageOrderProcessing.setVisibility(View.INVISIBLE);
                imageShipping.setVisibility(View.INVISIBLE);
                imageDelivery.setVisibility(View.VISIBLE);
                TrackingView.detailedTrack(context, subOrderDetailsList.get(position), "delivery", position, pos);
            }

            llParentApproved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageApproval.setVisibility(View.VISIBLE);
                    imageOrderProcessing.setVisibility(View.INVISIBLE);
                    imageShipping.setVisibility(View.INVISIBLE);
                    imageDelivery.setVisibility(View.INVISIBLE);
                    if (MyOrdersListActivity.actualList.get(pos).get(position).equals("accepted")) {
                        TrackingView.detailedTrackWhenClicked(context, "accepted", position, pos);
                    } else if (MyOrdersListActivity.actualList.get(pos).get(position).equals("orderstart")) {
                        TrackingView.detailedTrackWhenClicked(context, "orderstart", position, pos);
                    } else if (MyOrdersListActivity.actualList.get(pos).get(position).equals("rejected")) {
                        TrackingView.detailedTrackWhenClicked(context, "rejected", position, pos);
                    }
                }
            });

            llParentOrdersProcessing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageApproval.setVisibility(View.INVISIBLE);
                    imageOrderProcessing.setVisibility(View.VISIBLE);
                    imageShipping.setVisibility(View.INVISIBLE);
                    imageDelivery.setVisibility(View.INVISIBLE);
                    TrackingView.detailedTrackWhenClicked(context, "orderprocessing", position, pos);
                }
            });
            llParentShipping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageApproval.setVisibility(View.INVISIBLE);
                    imageOrderProcessing.setVisibility(View.INVISIBLE);
                    imageShipping.setVisibility(View.VISIBLE);
                    imageDelivery.setVisibility(View.INVISIBLE);
                    TrackingView.detailedTrackWhenClicked(context, "shipping", position, pos);
                }
            });

            llParentDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageApproval.setVisibility(View.INVISIBLE);
                    imageOrderProcessing.setVisibility(View.INVISIBLE);
                    imageShipping.setVisibility(View.INVISIBLE);
                    imageDelivery.setVisibility(View.VISIBLE);
                    TrackingView.detailedTrackWhenClicked(context, "delivery", position, pos);
                }
            });


        }

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(subOrderDetailsList.get(position).getProductprovider().getProviderlogo(), imageProvider, options, new SimpleImageLoadingListener() {
            boolean cacheFound;

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                List<String> memCache = MemoryCacheUtil.findCacheKeysForImageUri(imageUri, ImageLoader.getInstance().getMemoryCache());
                cacheFound = !memCache.isEmpty();
                if (!cacheFound) {
                    File discCache = DiscCacheUtil.findInCache(imageUri, ImageLoader.getInstance().getDiscCache());
                    if (discCache != null) {
                        cacheFound = discCache.exists();
                    }
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (cacheFound) {
//                        MemoryCacheUtil.removeFromCache(imageUri, ImageLoader.getInstance().getMemoryCache());
//                        DiscCacheUtil.removeFromCache(imageUri, ImageLoader.getInstance().getDiscCache());

                    ImageLoader.getInstance().displayImage(imageUri, (ImageView) view);
                }
            }
        });
        subOrderNumber.setTypeface(tfRobotoNormal);
        seller.setTypeface(tfRobotoNormal);
        subTotal.setTypeface(tfRobotoNormal);
        textSubOrderNumber.setTypeface(tfRobotoBold);
        textSeller.setTypeface(tfRobotoBold);
        textSubTotal.setTypeface(tfRobotoBold);
        subOrderNumber.setText(subOrderDetailsList.get(position).getSuborderid());
        seller.setText(subOrderDetailsList.get(position).getProductprovider().getProvidername());
        subTotal.setText(String.format("%.2f", Double.parseDouble(subOrderDetailsList.get(position).getSuborder_price())));
        listSubOrders.setAdapter(new SubOrderProductListAdapter(context, subOrderDetailsList.get(position).getProducts()));
        setListViewHeightBasedOnChildren(listSubOrders);

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
