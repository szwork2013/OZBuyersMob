package com.gls.orderzapp.MyOrders.MyOrdersListAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.MyOrders.Beans.ProductConfiguration;
import com.gls.orderzapp.MyOrders.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.util.List;

/**
 * Created by prajyot on 5/5/14.
 */
public class SubOrderProductListAdapter extends BaseAdapter {
    Context context;
    List<ProductDetails> productDetailsList;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Typeface tfRobotoNormal;

    public SubOrderProductListAdapter(Context context, List<ProductDetails> productDetailsList) {
        this.context = context;
        this.productDetailsList = productDetailsList;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        tfRobotoNormal = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }

    @Override
    public int getCount() {
        return productDetailsList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = li.inflate(R.layout.sub_order_list_content, null);
//        }
//        ImageView imageProductLogo = (ImageView) convertView.findViewById(R.id.product_logo);
//        TextView txt_product_name = (TextView) convertView.findViewById(R.id.txt_product_name);
//        TextView textProductPrice = (TextView) convertView.findViewById(R.id.product_price);
//        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
//        TextView uom = (TextView) convertView.findViewById(R.id.txt_uom);
//        TextView actual_price = (TextView) convertView.findViewById(R.id.actual_price);
//        final ImageView image = (ImageView) convertView.findViewById(R.id.image);
//
//        String imageLogo = new Gson().toJson(productDetailsList.get(position));
//        try {
//            if (imageLogo != null && imageLogo.contains("productlogo")) {
//                imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
//                imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//                imageLoader.displayImage(productDetailsList.get(position).getProductlogo(), imageProductLogo, options, new SimpleImageLoadingListener() {
//                    boolean cacheFound;
//
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                        List<String> memCache = MemoryCacheUtil.findCacheKeysForImageUri(imageUri, ImageLoader.getInstance().getMemoryCache());
//                        cacheFound = !memCache.isEmpty();
//                        if (!cacheFound) {
//                            File discCache = DiscCacheUtil.findInCache(imageUri, ImageLoader.getInstance().getDiscCache());
//                            if (discCache != null) {
//                                cacheFound = discCache.exists();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        if (cacheFound) {
////                        MemoryCacheUtil.removeFromCache(imageUri, ImageLoader.getInstance().getMemoryCache());
////                        DiscCacheUtil.removeFromCache(imageUri, ImageLoader.getInstance().getDiscCache());
//
//                            ImageLoader.getInstance().displayImage(imageUri, (ImageView) view);
//                        }
//                    }
//                });
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show();
//                if(isVisible == false){
//                    isVisible = true;
//                    image.setVisibility(View.VISIBLE);
//                }else{
//                    isVisible = false;
//                    image.setVisibility(View.GONE);
//                }
//            }
//        });
//        textProductPrice.setTypeface(tfRobotoNormal);
//        quantity.setTypeface(tfRobotoNormal);
//        actual_price.setTypeface(tfRobotoNormal);
//        txt_product_name.setTypeface(tfRobotoNormal);
//        textProductPrice.setText(String.format("%.2f", productDetailsList.get(position).getBaseprice()));
//        quantity.setText(String.format("%.2f", productDetailsList.get(position).getQty()));
//        uom.setText(productDetailsList.get(position).getUom());
//        txt_product_name.setText(productDetailsList.get(position).getProductname());
//        actual_price.setText(String.format("%.2f", productDetailsList.get(position).getOrderprice()));

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.my_suborder_list_content, null);
        }

        ImageView imageProductLogo = (ImageView) convertView.findViewById(R.id.product_logo);
        TextView txt_product_name = (TextView) convertView.findViewById(R.id.product_name);
        TextView order_price = (TextView) convertView.findViewById(R.id.order_price);
        ImageView product_price_details_image = (ImageView) convertView.findViewById(R.id.product_price_details_image);

        String imageLogo = new Gson().toJson(productDetailsList.get(position));
        try {
            if (imageLogo != null && imageLogo.contains("productlogo")) {
                imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                imageLoader.displayImage(productDetailsList.get(position).getProductlogo(), imageProductLogo, options, new SimpleImageLoadingListener() {
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
            }

            if(productDetailsList.get(position).getProductname() != null){
                txt_product_name.setText(productDetailsList.get(position).getProductname());
            }

            order_price.setText(String.format("%.2f", productDetailsList.get(position).getOrderprice()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayPriceDetailsInDialog(productDetailsList.get(position));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    void displayPriceDetailsInDialog(ProductDetails productDetails){
        try {
            LayoutInflater li = LayoutInflater.from(context);
            View dialogView = li.inflate(R.layout.my_order_price_details_dialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(dialogView);

            TextView product_base_price = (TextView) dialogView.findViewById(R.id.product_base_price);
            TextView product_quantity = (TextView) dialogView.findViewById(R.id.product_quantity);
            TextView product_configuration_price = (TextView) dialogView.findViewById(R.id.product_configuration_price);
            TextView total_product_price = (TextView) dialogView.findViewById(R.id.total_product_price);
            LinearLayout llCloseDialogImage = (LinearLayout) dialogView.findViewById(R.id.llCloseDialogImage);


            product_base_price.setText(productDetails.getBaseprice() + "");


            product_quantity.setText(productDetails.getQty()+" "+productDetails.getUom());

            product_configuration_price.setText(configurationPrice(productDetails.getProductconfiguration())+"");

            total_product_price.setText(productDetails.getOrderprice()+"");
            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.setInverseBackgroundForced(true);

            // show it
            alertDialog.show();


            llCloseDialogImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });


        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public double configurationPrice(List<ProductConfiguration> list) {
        double configurationPrice = 0.0;
        try {
            for (int i = 0; i < list.size(); i++) {
                configurationPrice = configurationPrice + list.get(i).getProd_configprice().getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurationPrice;
    }
}
