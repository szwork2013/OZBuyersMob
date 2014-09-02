package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.util.List;

/**
 * Created by avi on 8/6/14.
 */
public class ProductDetailsActivity extends Activity {
    ProductDetails productDetails;
    ImageView imageProduct;
    TextView textProductName, textProductDescription;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        productDetails = new Gson().fromJson(getIntent().getStringExtra("PRODUCT_DETAILS"), ProductDetails.class);
        findViewsById();
        showProductDetails();
    }

    void showProductDetails() {

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        imageLoader.displayImage(productDetails.getProductlogo().getImage(), imageProduct, new SimpleImageLoadingListener() {
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
        if (productDetails.getProductname() != null) {
            textProductName.setText(productDetails.getProductname());
        }
        if (productDetails.getProductdescription() != null) {
            textProductDescription.setText(productDetails.getProductdescription());
        }
    }

    void findViewsById() {
        imageProduct = (ImageView) findViewById(R.id.image_product);
        textProductName = (TextView) findViewById(R.id.product_name);
        textProductDescription = (TextView) findViewById(R.id.product_description);
    }

}
