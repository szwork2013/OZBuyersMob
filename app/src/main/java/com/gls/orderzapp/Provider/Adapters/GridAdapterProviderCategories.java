package com.gls.orderzapp.Provider.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gls.orderzapp.MainApp.MoreProductsListActivity;
import com.gls.orderzapp.MainApp.ProductDetailsActivity;
import com.gls.orderzapp.Provider.Beans.BranchInfo;
import com.gls.orderzapp.Provider.Beans.ProductConfiguration;
import com.gls.orderzapp.Provider.Beans.ProductConfigurationDetails;
import com.gls.orderzapp.Provider.Beans.ProductConfigurationPrice;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.Provider.Beans.ProductPrice;
import com.gls.orderzapp.Provider.Beans.ProviderDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.google.gson.Gson;
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
public class GridAdapterProviderCategories extends BaseAdapter {
    public static String branch_id = "";
    Context context;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    ProductDetails productDetails;
    ProviderDetails providerDetails;
    BranchInfo branchDetails;
    ImageView imageProduct, close_dialog;
    TextView textProductName, textProductDescription, textProductPrice;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    ProductDetails productDetailsToAddIntoTheCart;

    public GridAdapterProviderCategories(Context context, List<ProductDetails> productDetailsList, BranchInfo branchDetails, ProviderDetails providerDetails) {
        this.context = context;
        this.productDetailsList = productDetailsList;
        this.branchDetails = branchDetails;
        this.providerDetails = providerDetails;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();

    }

    @Override
    public int getCount() {
        return productDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return productDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.startup_activity_grid_contents, null);
            }

            Typeface pName = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
            ImageView imageProduct = (ImageView) convertView.findViewById(R.id.image_product);
            ImageView vegImage = (ImageView) convertView.findViewById(R.id.veg);
            ImageView nonVegImage = (ImageView) convertView.findViewById(R.id.non_veg);
            TextView textProductName = (TextView) convertView.findViewById(R.id.text_provider_name);
            TextView textPrice = (TextView) convertView.findViewById(R.id.text_price);
            TextView textRupees = (TextView) convertView.findViewById(R.id.text_rupees);
            TextView text_discount = (TextView) convertView.findViewById(R.id.txt_discount_onimage);
            LinearLayout linear_layout_dicsount = (LinearLayout) convertView.findViewById(R.id.linear_layout_dicsount);

            String imagelogo = new Gson().toJson(productDetailsList.get(position));

            if (productDetailsList.get(position).getFoodtype() != null) {
                if (productDetailsList.get(position).getFoodtype().equalsIgnoreCase("veg")) {
                    vegImage.setVisibility(View.VISIBLE);
                    nonVegImage.setVisibility(View.GONE);
                } else if (productDetailsList.get(position).getFoodtype() != null && productDetailsList.get(position).getFoodtype().equalsIgnoreCase("both")) {
                    vegImage.setVisibility(View.VISIBLE);
                    nonVegImage.setVisibility(View.VISIBLE);
                } else if (productDetailsList.get(position).getFoodtype() != null && productDetailsList.get(position).getFoodtype().equalsIgnoreCase("none")) {
                    vegImage.setVisibility(View.INVISIBLE);
                    nonVegImage.setVisibility(View.INVISIBLE);
                } else {
                    vegImage.setVisibility(View.GONE);
                    nonVegImage.setVisibility(View.VISIBLE);
                }
            }
            if (productDetailsList.get(position).getDiscount() != null && !productDetailsList.get(position).getDiscount().getCode().equalsIgnoreCase("none")) {
                linear_layout_dicsount.setVisibility(View.VISIBLE);
                text_discount.setText(productDetailsList.get(position).getDiscount().getPercent() + "");
            }
            if (imagelogo != null && imagelogo.contains("productlogo")) {
                if (imagelogo.contains("more_image_to_load_more")) {
                    imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.add_icon));
                    textProductName.setTypeface(pName);
                    textProductName.setText(productDetailsList.get(position).getProductname());
                    textRupees.setVisibility(View.GONE);
                } else {
                    imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                    imageLoader.displayImage(productDetailsList.get(position).getProductlogo().getImage(), imageProduct, options, new SimpleImageLoadingListener() {
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
                    if (productDetailsList.get(position).getProductname() != null) {
                        textProductName.setText(productDetailsList.get(position).getProductname());
                    }
                    textPrice.setText(String.format("%.2f", productDetailsList.get(position).getPrice().getValue()));
//                    textRupees.setText(productDetailsList.get(position).getPrice().getCurrency()+"");
                }
            } else {
                if (productDetailsList.get(position).getProductname() != null) {
                    textProductName.setText(productDetailsList.get(position).getProductname());
                }
                textPrice.setText(String.format("%.2f", productDetailsList.get(position).getPrice().getValue()));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productDetails = productDetailsList.get(position);
                    if (productDetails.getProductname() != null) {
                        if (productDetails.getProductname().equals("more")) {
                            branch_id = providerDetails.getBranch().getBranchid();
                            Intent goToMoreProducts = new Intent(context, MoreProductsListActivity.class);
                            goToMoreProducts.putExtra("MoreProductsListActivity", new Gson().toJson(providerDetails));
                            context.startActivity(goToMoreProducts);
                        } else {
                            productDetailsToAddIntoTheCart = new ProductDetails();

                            if (providerDetails.getProvider().getPaymentmode() != null) {
                                if (providerDetails.getProvider().getPaymentmode().getOnline() != null) {
                                    productDetailsToAddIntoTheCart.getPaymentmode().setOnline(providerDetails.getProvider().getPaymentmode().getOnline());
                                }
                                if (providerDetails.getProvider().getPaymentmode().getCod() != null) {
                                    productDetailsToAddIntoTheCart.getPaymentmode().setCod(providerDetails.getProvider().getPaymentmode().getCod());
                                }
                            }
                            if (providerDetails.getBranch().getDelivery() != null) {
                                    productDetailsToAddIntoTheCart.getDelivery().setIsprovidehomedelivery(providerDetails.getBranch().getDelivery().getIsprovidehomedelivery());

                                    productDetailsToAddIntoTheCart.getDelivery().setIsprovidepickup(providerDetails.getBranch().getDelivery().getIsprovidepickup());

                                    productDetailsToAddIntoTheCart.getDelivery().setIsdeliverychargeinpercent(providerDetails.getBranch().getDelivery().isIsdeliverychargeinpercent());
                            }
                            if (providerDetails.getProvider().getProviderbrandname() != null) {
                                productDetailsToAddIntoTheCart.setProviderName(providerDetails.getProvider().getProviderbrandname());
                            }else{
                                productDetailsToAddIntoTheCart.setProviderName("");
                            }
                            if (providerDetails.getProducts().get(position).getMin_weight() != null) {
                                Log.d("minweight", (providerDetails.getProducts().get(position).getMin_weight().getValue()+"").split("\\.")[0]);
                                if(providerDetails.getProducts().get(position).getPrice().getUom().equalsIgnoreCase("no") || providerDetails.getProducts().get(position).getPrice().getUom().equalsIgnoreCase("lb")) {
                                    productDetailsToAddIntoTheCart.setQuantity((providerDetails.getProducts().get(position).getMin_weight().getValue() + "").split("\\.")[0]);
                                }else{
                                    productDetailsToAddIntoTheCart.setQuantity(providerDetails.getProducts().get(position).getMin_weight().getValue() + "");
                                }
                            }
                            if (providerDetails.getBranch().getBranchid() != null) {
                                productDetailsToAddIntoTheCart.setBranchid(providerDetails.getBranch().getBranchid());
                            }
                            if (providerDetails.getProducts().get(position).getPrice() != null) {
                                ProductPrice price = new ProductPrice();
                                price.setUom(providerDetails.getProducts().get(position).getPrice().getUom());
                                price.setValue(providerDetails.getProducts().get(position).getPrice().getValue());
                                productDetailsToAddIntoTheCart.setPrice(price);
//                                if (Cart.hm.size() == 1) {
//                                    productDetailsToAddIntoTheCart.getPrice().setUom("Gm");
//                                    int hmSize = Cart.hm.size();
//                                    String[] keys = Cart.hm.keySet().toArray(new String[hmSize]);
//                                    Log.d("After Setting",Cart.hm.get(keys[0]).getPrice().getUom());
//
//                                }
                            }
                            if (branchDetails.getLocation() != null) {
                                productDetailsToAddIntoTheCart.setLocation(branchDetails.getLocation());
                            }
                            if (providerDetails.getProducts().get(position).getProductconfiguration() != null) {
                                ProductConfiguration productConfiguration = new ProductConfiguration();
                                productConfiguration.setCategoryid(providerDetails.getProducts().get(position).getProductconfiguration().getCategoryid());
                                productConfiguration.setCategoryname(providerDetails.getProducts().get(position).getProductconfiguration().getCategoryname());
                                for(int i = 0; i < providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration().size(); i++){
                                    ProductConfigurationDetails pcd = new ProductConfigurationDetails();
                                    pcd.setProd_configtype(providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype());
                                    pcd.setProd_configname(providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration().get(i).getProd_configname());
                                    pcd.setChecked(providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration().get(i).isChecked());
                                    pcd.setProd_configprice(providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration().get(i).getProd_configprice());
                                    pcd.setFoodType(providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration().get(i).getFoodType());

                                    productConfiguration.getConfiguration().add(pcd);
                                }
//                                productConfiguration.setConfiguration(providerDetails.getProducts().get(position).getProductconfiguration().getConfiguration());
                                productDetailsToAddIntoTheCart.setProductconfiguration(productConfiguration);
                            }
                            productDetailsToAddIntoTheCart.setMessageonproduct("none");
                            if (providerDetails.getProvider().getProviderid() != null) {
                                productDetailsToAddIntoTheCart.setProviderid(providerDetails.getProvider().getProviderid());
                            }
                            if (providerDetails.getBranch().getNote() != null) {
                                productDetailsToAddIntoTheCart.setNote(providerDetails.getBranch().getNote());
                            }
                            if (providerDetails.getProducts().get(position).getProductid() != null) {
                                productDetailsToAddIntoTheCart.setProductid(providerDetails.getProducts().get(position).getProductid());
                            }
                            if (providerDetails.getProducts().get(position).getProductname() != null) {
                                productDetailsToAddIntoTheCart.setProductname(providerDetails.getProducts().get(position).getProductname());
                            }
                            if (providerDetails.getProducts().get(position).getFoodtype() != null) {
                                productDetailsToAddIntoTheCart.setFoodtype(providerDetails.getProducts().get(position).getFoodtype());
                            }
                            if (providerDetails.getProducts().get(position).getMax_weight() != null) {
                                productDetailsToAddIntoTheCart.setMax_weight(providerDetails.getProducts().get(position).getMax_weight());
                            }
                            if (providerDetails.getProducts().get(position).getMin_weight() != null) {
                                productDetailsToAddIntoTheCart.setMin_weight(providerDetails.getProducts().get(position).getMin_weight());
                            }
                            if (providerDetails.getProducts().get(position).getProductimage() != null) {
                                productDetailsToAddIntoTheCart.setProductimage(providerDetails.getProducts().get(position).getProductimage());
                            }
                            if (providerDetails.getProducts().get(position).getDiscount() != null) {
                                productDetailsToAddIntoTheCart.setDiscount(providerDetails.getProducts().get(position).getDiscount());
                            }
                            if (providerDetails.getProducts().get(position).getProductlogo() != null) {
                                productDetailsToAddIntoTheCart.setProductlogo(providerDetails.getProducts().get(position).getProductlogo());
                            }
                            if (providerDetails.getProducts().get(position).getProductdescription() != null) {
                                productDetailsToAddIntoTheCart.setProductdescription(providerDetails.getProducts().get(position).getProductdescription());
                            }
//                        if(productDetailsToAddIntoTheCart!=null) {
                            Cart.addToCart(productDetailsToAddIntoTheCart, context);
                            productDetailsToAddIntoTheCart=null;
//                        }
                        }
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    productDetails = productDetailsList.get(position);
                    if (productDetails.getProductname() != null && !productDetails.getProductname().equals("more")) {
                        productDetails.setBranchid(providerDetails.getBranch().getBranchid());
//                        popUp(productDetails);
                        Intent goToProductDetailsActivity = new Intent(context, ProductDetailsActivity.class);
                        goToProductDetailsActivity.putExtra("PRODUCT_DETAILS",new Gson().toJson(productDetails));
                        context.startActivity(goToProductDetailsActivity);

                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
