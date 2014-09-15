package com.gls.orderzapp.Provider.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.MainApp.MoreProductsListActivity;
import com.gls.orderzapp.MainApp.ProductDetailsActivity;
import com.gls.orderzapp.MainApp.StartUpActivity;
import com.gls.orderzapp.Provider.Beans.BranchInfo;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
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
 * Created by avinash on 24/6/14.
 */
public class GridAdapterProduct extends BaseAdapter implements Animation.AnimationListener{
    Context context;
    List<ProductDetails> productDetailsList = new ArrayList<>();
    ProductDetails productDetails;
    ProviderDetails providerDetails;
    BranchInfo branchDetails;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    DisplayImageOptions options;
    Animation slide_down, slide_up, zoom_in, zoom_out;


    public GridAdapterProduct(Context context, List<ProductDetails> productDetailsList, BranchInfo branchDetails, ProviderDetails providerDetails) {
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

        slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);
        slide_down.setAnimationListener(this);

        slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);
        slide_up.setAnimationListener(this);

        zoom_in = AnimationUtils.loadAnimation(context, R.anim.zoomin);
        zoom_in.setAnimationListener(this);

        zoom_out = AnimationUtils.loadAnimation(context, R.anim.zoomout);
        zoom_out.setAnimationListener(this);
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
            TextView original_price = (TextView) convertView.findViewById(R.id.original_price);
            LinearLayout linear_layout_dicsount = (LinearLayout) convertView.findViewById(R.id.linear_layout_dicsount);

            String imagelogo = new Gson().toJson(productDetailsList.get(position));
            if (productDetailsList.get(position).getFoodtype() != null) {
                if (productDetailsList.get(position).getFoodtype().equalsIgnoreCase("veg")) {
                    vegImage.setVisibility(View.VISIBLE);
                    nonVegImage.setVisibility(View.GONE);
                } else if (productDetailsList.get(position).getFoodtype().equalsIgnoreCase("both")) {
                    vegImage.setVisibility(View.VISIBLE);
                    nonVegImage.setVisibility(View.VISIBLE);
                } else if (productDetailsList.get(position).getFoodtype().equalsIgnoreCase("none")) {
                    vegImage.setVisibility(View.INVISIBLE);
                    nonVegImage.setVisibility(View.INVISIBLE);
                } else {
                    vegImage.setVisibility(View.GONE);
                    nonVegImage.setVisibility(View.VISIBLE);
                }
            }

            if (productDetailsList.get(position).getDiscount() != null && productDetailsList.get(position).getDiscount().getCode() != null && !productDetailsList.get(position).getDiscount().getCode().equalsIgnoreCase("none")) {
                linear_layout_dicsount.setVisibility(View.VISIBLE);
                text_discount.setText(productDetailsList.get(position).getDiscount().getPercent() + "");
                StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
                original_price.setText(String.format("%.2f", productDetailsList.get(position).getPrice().getValue()), TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) original_price.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, String.format("%.2f", productDetailsList.get(position).getPrice().getValue()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }else{
                linear_layout_dicsount.setVisibility(View.GONE);
            }

            if (imagelogo != null && imagelogo.contains("productlogo")) {
                if (imagelogo.contains("more_image_to_load_more")) {
                    imageProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.add_icon));
                    textProductName.setTypeface(pName);
                    textProductName.setText(productDetailsList.get(position).getProductname());
                    textRupees.setVisibility(View.GONE);
                } else {
                    try {
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
                                    ImageLoader.getInstance().displayImage(imageUri, (ImageView) view);
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    textProductName.setText(productDetailsList.get(position).getProductname());
                    textPrice.setText(String.format("%.2f", productDetailsList.get(position).getPrice().getValue()));
//                    textRupees.setText(productDetailsList.get(position).getPrice().getCurrency()+"");
                }
            } else {
                textProductName.setText(productDetailsList.get(position).getProductname());
                textPrice.setText(String.format("%.2f", productDetailsList.get(position).getPrice().getValue()));
//                textRupees.setText(productDetailsList.get(position).getPrice().getCurrency()+"");
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productDetails = productDetailsList.get(position);
                    if (productDetails.getProductname() != null && productDetails.getProductname().equals("more")) {
                        Intent goToMoreProducts = new Intent(context, MoreProductsListActivity.class);
                        goToMoreProducts.putExtra("MoreProductsListActivity", "GridAdapterProduct");
                        context.startActivity(goToMoreProducts);
                    } else {
                        if (providerDetails != null) {
                            MoreProductsListActivity.added_to_cart.setVisibility(View.VISIBLE);
                            MoreProductsListActivity.added_to_cart.startAnimation(slide_down);

                            ProductDetails productDetailsToAddIntoTheCart = new ProductDetails();
                            if (providerDetails.getProvider().getProviderbrandname() != null) {
                                productDetailsToAddIntoTheCart.setProviderName(providerDetails.getProvider().getProviderbrandname());
                            } else {
                                productDetailsToAddIntoTheCart.setProviderName("");
                            }
                            if (productDetailsList.get(position).getMin_weight() != null) {
                                productDetailsToAddIntoTheCart.setQuantity(productDetailsList.get(position).getMin_weight().getValue() + "");
                            }
                            if (providerDetails.getBranch().getBranchid() != null) {
                                productDetailsToAddIntoTheCart.setBranchid(providerDetails.getBranch().getBranchid());
                            }
                            if (productDetailsList.get(position).getPrice() != null) {
                                productDetailsToAddIntoTheCart.setPrice(productDetailsList.get(position).getPrice());
                                productDetailsToAddIntoTheCart.setOrignalUom(productDetailsList.get(position).getPrice().getUom());
                            }
                            if (branchDetails.getLocation() != null) {
                                productDetailsToAddIntoTheCart.setLocation(branchDetails.getLocation());
                            }

                            if (branchDetails.getContact_supports() != null && !branchDetails.getContact_supports().isEmpty()) {
                                productDetailsToAddIntoTheCart.setContact_supports(branchDetails.getContact_supports());
                            }else{
                                List<String>cont_no=new ArrayList<String>();
                                cont_no.add("91-20-67211800");
                                productDetailsToAddIntoTheCart.setContact_supports(cont_no);
                            }

                            if (productDetailsList.get(position).getProductconfiguration() != null) {
                                productDetailsToAddIntoTheCart.setProductconfiguration(productDetailsList.get(position).getProductconfiguration());
                            }
                            productDetailsToAddIntoTheCart.setMessageonproduct("none");

                            if (providerDetails.getProvider().getProviderid() != null) {
                                productDetailsToAddIntoTheCart.setProviderid(providerDetails.getProvider().getProviderid());
                            }

                            if (providerDetails.getProvider().getPaymentmode().getOnline() != null) {
                                productDetailsToAddIntoTheCart.getPaymentmode().setOnline(providerDetails.getProvider().getPaymentmode().getOnline());
                            }
                            if (providerDetails.getProvider().getPaymentmode().getCod() != null) {
                                productDetailsToAddIntoTheCart.getPaymentmode().setCod(providerDetails.getProvider().getPaymentmode().getCod());
                            }
                            if (providerDetails.getBranch().getDelivery() != null) {
                                productDetailsToAddIntoTheCart.getDelivery().setIsprovidehomedelivery(providerDetails.getBranch().getDelivery().getIsprovidehomedelivery());

                                productDetailsToAddIntoTheCart.getDelivery().setIsprovidepickup(providerDetails.getBranch().getDelivery().getIsprovidepickup());

                                productDetailsToAddIntoTheCart.getDelivery().setIsdeliverychargeinpercent(providerDetails.getBranch().getDelivery().isIsdeliverychargeinpercent());
                            }
                            if (providerDetails.getBranch().getNote() != null) {
                                productDetailsToAddIntoTheCart.setNote(providerDetails.getBranch().getNote());
                            }
                            if (productDetailsList.get(position).getProductid() != null) {
                                productDetailsToAddIntoTheCart.setProductid(productDetailsList.get(position).getProductid());
                            }
                            if (productDetailsList.get(position).getProductname() != null) {
                                productDetailsToAddIntoTheCart.setProductname(productDetailsList.get(position).getProductname());
                            }
                            if (productDetailsList.get(position).getPrefereddeliverydate() != null) {
                                productDetailsToAddIntoTheCart.setPrefereddeliverydate(productDetailsList.get(position).getPrefereddeliverydate());
                            } else {
                                productDetailsToAddIntoTheCart.setPrefereddeliverydate("");
                            }
                            if (productDetailsList.get(position).getTimeslot() != null) {
                                productDetailsToAddIntoTheCart.getTimeslot().setFrom(productDetailsList.get(position).getTimeslot().getFrom());
                                productDetailsToAddIntoTheCart.getTimeslot().setTo(productDetailsList.get(position).getTimeslot().getTo());

                            } else {
                                productDetailsToAddIntoTheCart.getTimeslot().setFrom(0);
                                productDetailsToAddIntoTheCart.getTimeslot().setTo(0);
                            }
                            if (productDetailsList.get(position).getDiscount() != null) {
                                productDetailsToAddIntoTheCart.setDiscount(productDetailsList.get(position).getDiscount());
                            }
                            if (productDetailsList.get(position).getFoodtype() != null) {
                                productDetailsToAddIntoTheCart.setFoodtype(productDetailsList.get(position).getFoodtype());
                            }
                            if (productDetailsList.get(position).getMax_weight() != null) {
                                productDetailsToAddIntoTheCart.setMax_weight(productDetailsList.get(position).getMax_weight());
                            }
                            if (productDetailsList.get(position).getMin_weight() != null) {
                                productDetailsToAddIntoTheCart.setMin_weight(productDetailsList.get(position).getMin_weight());
                            }
                            if (productDetailsList.get(position).getProductimage() != null) {
                                productDetailsToAddIntoTheCart.setProductimage(productDetailsList.get(position).getProductimage());
                            }
                            if (productDetailsList.get(position).getProductlogo() != null) {
                                productDetailsToAddIntoTheCart.setProductlogo(productDetailsList.get(position).getProductlogo());
                            }
                            if (productDetailsList.get(position).getProductdescription() != null) {
                                productDetailsToAddIntoTheCart.setProductdescription(productDetailsList.get(position).getProductdescription());
                            }
                            Cart.addToCart(productDetailsToAddIntoTheCart, context);
                            Cart.numberTextOnCart.startAnimation(zoom_in);
                        }
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    productDetails = productDetailsList.get(position);
                    if (!productDetails.getProductname().equals("more")) {
                        productDetails.setBranchid(providerDetails.getBranch().getBranchid());
//                        popUp(productDetails);
                        Intent goToProductDetailsActivity = new Intent(context, ProductDetailsActivity.class);
                        goToProductDetailsActivity.putExtra("PRODUCT_DETAILS", new Gson().toJson(productDetails));
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation == slide_down) {
            MoreProductsListActivity.added_to_cart.startAnimation(slide_up);
            MoreProductsListActivity.added_to_cart.setVisibility(View.GONE);
        }else if(animation == zoom_in){
            Cart.numberTextOnCart.startAnimation(zoom_out);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

