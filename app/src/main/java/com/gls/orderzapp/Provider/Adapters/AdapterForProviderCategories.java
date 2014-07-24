package com.gls.orderzapp.Provider.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.StartUpActivity;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.Provider.Beans.ProductLogo;
import com.gls.orderzapp.Provider.Beans.ProductPrice;
import com.gls.orderzapp.Provider.Beans.ProviderDetails;
import com.gls.orderzapp.R;
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
 * Created by prajyot on 7/4/14.
 */
public class AdapterForProviderCategories {
    Context context;
    List<ProviderDetails> providerDetailsList = new ArrayList<>();
    //    List<ProductDetails> productDetailsList = new ArrayList<>();
    LinearLayout linearLayoutCategoryDetails;
    TextView textProviderName, txtProviderArea;
    ImageView imageProvider;
    GridView categoryDetailsGridView;
    int i;
    int textViewHeight;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public AdapterForProviderCategories(Context context, List<ProviderDetails> providerDetailsList) {
        this.context = context;
        this.providerDetailsList = providerDetailsList;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }

    public void setProductCategories() {
        for (i = 0; i < providerDetailsList.size(); i++) {

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linearLayoutCategoryDetails = (LinearLayout) li.inflate(R.layout.startup_activity_contents, null);
            textProviderName = (TextView) linearLayoutCategoryDetails.findViewById(R.id.text_provider_name);
            categoryDetailsGridView = (GridView) linearLayoutCategoryDetails.findViewById(R.id.grid_categories);
            txtProviderArea = (TextView) linearLayoutCategoryDetails.findViewById(R.id.txt_provider_area);
            imageProvider = (ImageView) linearLayoutCategoryDetails.findViewById(R.id.image_provider);

            if (providerDetailsList.get(i).getProvider().getProvidername() != null) {
                Typeface pName = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
                textProviderName.setTypeface(pName);
                textProviderName.setText(providerDetailsList.get(i).getProvider().getProvidername());

            }
            if (providerDetailsList.get(i).getBranch().getLocation().getArea() != null) {
                String area = providerDetailsList.get(i).getBranch().getLocation().getArea();

                Typeface aName = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
                txtProviderArea.setTypeface(aName);
                txtProviderArea.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                txtProviderArea.setText(area);
            }
            try {
                imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                imageLoader.displayImage(providerDetailsList.get(i).getProvider().getProviderlogo(), imageProvider, options, new SimpleImageLoadingListener() {
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
            }catch (Exception e){
                e.printStackTrace();
            }

            categoryDetailsGridView.setTag(providerDetailsList.get(i));
//            productDetailsList.clear();
            if (providerDetailsList.get(i).isLoadmoreproduct() == true) {

                ProductDetails productDetails = new ProductDetails();
                ProductPrice productPrice = new ProductPrice();
                productPrice.setValue(0);
                productDetails.setPrice(productPrice);
                productDetails.setProductname("more");
                productDetails.setFoodtype("none");
                ProductLogo productLogo = new ProductLogo();
                productLogo.setImage("more_image_to_load_more");
                productDetails.setProductlogo(productLogo);
                providerDetailsList.get(i).getProducts().add(productDetails);
            }
            categoryDetailsGridView.setAdapter(new GridAdapterProviderCategories(context, providerDetailsList.get(i).getProducts(), providerDetailsList.get(i).getBranch(), (ProviderDetails) categoryDetailsGridView.getTag()));
            setListViewHeightBasedOnChildren(categoryDetailsGridView);
            if (providerDetailsList.get(i).getProducts().size() > 0) {
                StartUpActivity.linearLayoutCategories.addView(linearLayoutCategoryDetails);
            }
        }
    }

    public void setListViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        int numberOfRows;

        if (listAdapter.getCount() % 3 == 0) {
            numberOfRows = listAdapter.getCount() / 3;
        } else {
            numberOfRows = (listAdapter.getCount() / 3) + 1;
        }

        for (int i = 0; i < numberOfRows; i++) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new LinearLayout.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (numberOfRows);
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }
}
