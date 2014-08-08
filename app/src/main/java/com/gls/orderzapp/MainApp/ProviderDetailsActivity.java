package com.gls.orderzapp.MainApp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.Provider.Beans.ProviderBean;
import com.gls.orderzapp.Provider.Beans.ProviderDetails;
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

public class ProviderDetailsActivity extends Activity {

    ProviderBean provider;
    ImageView imageProvider;
    TextView textProviderName,textProviderDescription;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        Log.d("provider data", getIntent().getStringExtra("PROVIDER_DETAILS"));
        provider = new Gson().fromJson(getIntent().getStringExtra("PROVIDER_DETAILS"),ProviderBean.class);
        findViewId();
        showProviderDetails();
    }

    private void showProviderDetails() {
        try {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            imageLoader.displayImage(provider.getProviderlogo(), imageProvider, options, new SimpleImageLoadingListener() {
                boolean cacheFound;

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
        }catch (Exception e){
            e.printStackTrace();
        }
        if(provider.getProvidername() !=null){
            textProviderName.setText(provider.getProvidername());
        }
        if(provider.getProviderbrandname() !=null){
            textProviderDescription.setText(provider.getProviderbrandname());
        }
    }

    private void findViewId() {

        imageProvider = (ImageView) findViewById(R.id.image_provider);
        textProviderName = (TextView) findViewById(R.id.provider_name);
        textProviderDescription = (TextView) findViewById(R.id.provider_description);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.provider_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
