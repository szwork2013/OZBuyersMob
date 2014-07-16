package com.gls.orderzapp.Provider.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.MoreProductsListActivity;
import com.gls.orderzapp.Provider.Beans.ProviderDetails;
import com.gls.orderzapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 27/5/14.
 */
public class GridAdapterForMoreProduct {
    public static int provider_position = 0;
    int product_position = 0;
    public static String last_product_id = "";

    Context context;
    List<ProviderDetails> providerDetailsList = new ArrayList<>();
    //    List<ProductDetails> productDetailsList = new ArrayList<>();
    SwipeRefreshLayout linearLayoutCategoryDetails;
    TextView textProviderName, txtProviderArea;
    ImageView imageProvider;
    GridView categoryDetailsGridView;
    GridAdapterProduct gridadpte;
    int i;
    int textViewHeight;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public GridAdapterForMoreProduct(Context context, List<ProviderDetails> providerDetailsList) {
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
            linearLayoutCategoryDetails = (SwipeRefreshLayout) li.inflate(R.layout.loadmore_activity_contents, null);

            final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) linearLayoutCategoryDetails.findViewById(R.id.swipe);
            swipeView.setEnabled(false);

            textProviderName = (TextView) linearLayoutCategoryDetails.findViewById(R.id.text_provider_name);
            categoryDetailsGridView = (GridView) linearLayoutCategoryDetails.findViewById(R.id.grid_categories);
            txtProviderArea = (TextView) linearLayoutCategoryDetails.findViewById(R.id.txt_provider_area);
            imageProvider = (ImageView) linearLayoutCategoryDetails.findViewById(R.id.image_provider);

            product_position = providerDetailsList.get(provider_position).getProducts().size() - 1;

            for (int j = 0; j < providerDetailsList.get(provider_position).getProducts().size(); j++) {
                Log.d("Product id's" + j + " :-", providerDetailsList.get(provider_position).getProducts().get(j).getProductid());
            }


            Typeface aName = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            txtProviderArea.setTypeface(aName);
            txtProviderArea.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

            swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_purple, android.R.color.holo_orange_dark, android.R.color.holo_green_light);
            swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeView.setRefreshing(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                            MoreProductsListActivity moreProductsListActivity = (MoreProductsListActivity) context;
                            Log.d("Provider_Position" + provider_position, "Product Position" + product_position);
                            Log.d("Last Product id", providerDetailsList.get(provider_position).getProducts().get(product_position).getProductid());
                            moreProductsListActivity.new GetMoreProductAsync().execute(providerDetailsList.get(provider_position).getProducts().get(product_position).getProductid());
                            gridadpte.notifyDataSetChanged();
                        }
                    }, 3000);

                }
            });

            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));

            categoryDetailsGridView.setTag(providerDetailsList.get(i));
            provider_position = 0;
            gridadpte = new GridAdapterProduct(context, providerDetailsList.get(i).getProducts(), providerDetailsList.get(i).getBranch(), (ProviderDetails) categoryDetailsGridView.getTag());

            categoryDetailsGridView.setAdapter(gridadpte);


            categoryDetailsGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem == 0)
                        swipeView.setEnabled(true);
                    else
                        swipeView.setEnabled(false);

                }
            });
            Log.d("more producrt", "more product");
            if (providerDetailsList.get(i).getProducts().size() > 0) {
//                MoreProductsListActivity.moreProductslinear.addView(linearLayoutCategoryDetails);
            }
        }
    }
}