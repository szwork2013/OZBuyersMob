package com.gls.orderzapp.Cart.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.MainApp.CartActivity;
import com.gls.orderzapp.Provider.Beans.ProductDetails;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
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
 * Created by prajyot on 10/7/14.
 */
public class ProductListAdapter {
    public double min_weight, max_weight;
    Context context;
    List<ProductDetails> productDetailsList;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ArrayList<String> arrayListweight;
    EditText tempEditText = null;
    String measure = "";
    EditText edittext_quantity;
    LinearLayout llDeleteImage;
    Spinner spinner_weight, tempSpinner;
    ImageView delete_image;
    int parentIndex;
    List<String> list = new ArrayList<>();
    String uom, orignal_uom;
    String tag = "";

    public ProductListAdapter(Context context, List<ProductDetails> productDetailsList, int parentIndex) {
        this.productDetailsList = productDetailsList;
        this.context = context;
        this.parentIndex = parentIndex;

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }

    public void getProductView() {
        try {
            for (int i = 0; i < productDetailsList.size(); i++) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout llproductDetails = (LinearLayout) li.inflate(R.layout.cart_product_list, null);
                ImageView product_image = (ImageView) llproductDetails.findViewById(R.id.product_image);
                TextView product_name = (TextView) llproductDetails.findViewById(R.id.product_name);
                TextView unit_price = (TextView) llproductDetails.findViewById(R.id.unit_price);
                edittext_quantity = (EditText) llproductDetails.findViewById(R.id.edittext_quantity);
                spinner_weight = (Spinner) llproductDetails.findViewById(R.id.spinner_weight);
                delete_image = (ImageView) llproductDetails.findViewById(R.id.delete_image);
                llDeleteImage = (LinearLayout) llproductDetails.findViewById(R.id.llDeleteImage);
                LinearLayout ll_discount = (LinearLayout) llproductDetails.findViewById(R.id.ll_discount);
                TextView txt_discount_onimage = (TextView) llproductDetails.findViewById(R.id.txt_discount_onimage);
                TextView calculated_price = (TextView) llproductDetails.findViewById(R.id.calculated_price);

                if (productDetailsList.get(i).getProductlogo() != null) {
                    if (productDetailsList.get(i).getProductlogo().getImage() != null) {
                        loadImage(product_image, productDetailsList.get(i).getProductlogo().getImage());
                    }
                }

                if(productDetailsList.get(i).getDiscount() != null && productDetailsList.get(i).getDiscount().getCode() != null &&
                         !productDetailsList.get(i).getDiscount().getCode().equalsIgnoreCase("none")){
                    ll_discount.setVisibility(View.VISIBLE);
                    txt_discount_onimage.setText(productDetailsList.get(i).getDiscount().getPercent()+"");
                }else {
                    ll_discount.setVisibility(View.GONE);
                }
                Log.d(productDetailsList.get(i).getProductname() + "ssssssssssssss   ", productDetailsList.get(i).getPrice().getUom());
                if (productDetailsList.get(i).getProductname() != null) {
                    product_name.setText(productDetailsList.get(i).getProductname());
                }
                if (productDetailsList.get(i).getPrice() != null) {
                    unit_price.setText(productDetailsList.get(i).getPrice().getValue() + "");
                    if (productDetailsList.get(i).getPrice().getUom() != null) {
                        measure = productDetailsList.get(i).getPrice().getUom();
                        orignal_uom = productDetailsList.get(i).getOrignalUom();
                    }
                }
                if (productDetailsList.get(i).getQuantity() != null) {
                    edittext_quantity.setText(productDetailsList.get(i).getQuantity());
                }

                if (productDetailsList.get(i).getPrice().getUom() != null) {
                    Log.d("Product qty", productDetailsList.get(i).getQuantity());
                    Log.d("Product price", productDetailsList.get(i).getPrice().getValue() + "");
                    calculated_price.setText(String.format("%.2f", productDetailsList.get(i).getPrice().getValue() * Double.parseDouble(productDetailsList.get(i).getQuantity())));
                    Log.d("Text", calculated_price.getText().toString());
                }

                uomSpinnerActions();
                quantityEditTextActions();
                deleteProduct();
                min_weight = productDetailsList.get(i).getMin_weight().getValue();
                max_weight = productDetailsList.get(i).getMax_weight().getValue();

                spinner_weight.setTag(productDetailsList.get(i).getCartCount());
                edittext_quantity.setTag(productDetailsList.get(i).getCartCount());
                delete_image.setTag(productDetailsList.get(i).getCartCount());
                llDeleteImage.setTag(productDetailsList.get(i).getCartCount());

                list.add(edittext_quantity.getTag() + "-" + parentIndex);

                CartAdapter.llProductList.addView(llproductDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImage(ImageView imageView, String imageUrl) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(imageUrl, imageView, new SimpleImageLoadingListener() {
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

    public void uomSpinnerActions() {
        arrayListweight = new ArrayList<>();
        if (measure != null) {
            if (measure.equalsIgnoreCase("No")) {
                arrayListweight.add("No");
                edittext_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (measure.equalsIgnoreCase("Kg")) {
                arrayListweight.add("Kg");
                arrayListweight.add("Gm");
                edittext_quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (measure.equalsIgnoreCase("Gm")) {
                arrayListweight.add("Gm");
                arrayListweight.add("Kg");
                edittext_quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (measure.equalsIgnoreCase("lb")) {
                arrayListweight.add("lb");
                edittext_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            for (int i = 0; i < arrayListweight.size(); i++) {
                spinner_weight.setAdapter(new SpinnerAdapter(context.getApplicationContext(), arrayListweight));
            }
        }


        spinner_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                tag = ((Spinner) parent).getTag() + "";
                tempEditText = (EditText) ((LinearLayout) parent.getParent()).getChildAt(3);
                uom = Cart.returnUom(tag);
                orignal_uom = Cart.returnOrignalUom(tag);
                if (((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString().equalsIgnoreCase("Kg")) {
                    if (tempEditText.getText().toString().trim().length() > 0) {
                        if (isDouble(tempEditText.getText().toString().trim()) == true) {
                            String fixed_rate = ((TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(1))).getText().toString();
                            TextView tempText = (TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(7));
                            measure = "Kg";
                            if (orignal_uom.equalsIgnoreCase("gm")) {
                                tempText.setText(String.format("%.2f", (((Double.parseDouble(tempEditText.getText().toString())) * 1000) * (Double.parseDouble(fixed_rate)))));
                            } else {
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                            }
                            Cart.updateCart(tag, tempEditText.getText().toString().trim(), measure);
                            for (int i = 0; i < list.size(); i++) {
                                if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                    CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                }
                            }
                        }
                    } else {
                        measure = "Kg";
                        Cart.updateCart(tag, "0", measure);
                    }
                } else if (((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString().equalsIgnoreCase("lb")) {
                    if (tempEditText.getText().toString().trim().length() > 0) {
                        if (isDouble(tempEditText.getText().toString().trim()) == true) {
                            String fixed_rate = ((TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(1))).getText().toString();
                            TextView tempText = (TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(7));
                            measure = "lb";
                            tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                            Cart.updateCart(tag, tempEditText.getText().toString().trim(), measure);
                            for (int i = 0; i < list.size(); i++) {
                                if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                    CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                }
                            }
                        }
                    } else {
                        measure = "lb";
                        Cart.updateCart(tag, "0", measure);
                    }
                } else if (((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString().equalsIgnoreCase("Gm")) {
                    if (tempEditText.getText().toString().trim().length() > 0) {
                        if (isDouble(tempEditText.getText().toString().trim()) == true) {
                            String fixed_rate = ((TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(1))).getText().toString();
                            TextView tempText = (TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(7));
                            measure = "Gm";
                            Log.d("original", orignal_uom);
                            if (orignal_uom.equalsIgnoreCase("kg")) {
                                tempText.setText(String.format("%.2f", (((Double.parseDouble(tempEditText.getText().toString())) / 1000) * (Double.parseDouble(fixed_rate)))));
                            } else {
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                            }
                            Cart.updateCart(tag, tempEditText.getText().toString().trim(), measure);
                            for (int i = 0; i < list.size(); i++) {
                                if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                    CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                }
                            }
                        }
                    } else {
                        measure = "Gm";
                        Cart.updateCart(tag, "0", measure);
                    }
                } else if (((TextView) ((LinearLayout) view).getChildAt(0)).getText().toString().equalsIgnoreCase("No")) {

                    if (tempEditText.getText().toString().trim().length() > 0) {
                        if (isDouble(tempEditText.getText().toString().trim()) == true) {
                            String fixed_rate = ((TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(1))).getText().toString();
                            TextView tempText = (TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(7));
                            measure = "No";
                            tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                            Cart.updateCart(tag, tempEditText.getText().toString().trim(), measure);
                            for (int i = 0; i < list.size(); i++) {
                                if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                    CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                }
                            }
                        }
                    }
                } else {
                    measure = "No";
                    Cart.updateCart(tag, "0", measure);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        spinner_weight.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                tempSpinner = ((Spinner) v);
//                tag = v.getTag() + "";
//                return false;
//            }
//        });
    }

    public void deleteProduct() {
        delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cart.deleteFromCart(view.getTag() + "");
                CartActivity cartActivity = (CartActivity) context;
                cartActivity.displayCart();

            }
        });

        llDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cart.deleteFromCart(view.getTag() + "");
                CartActivity cartActivity = (CartActivity) context;
                cartActivity.displayCart();

            }
        });

    }

    public void quantityEditTextActions() {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String fixed_rate = ((TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(1))).getText().toString();
                final TextView tempText = (TextView) (((LinearLayout) (tempEditText.getParent())).getChildAt(7));

                if (tempEditText.getText().toString().trim().length() > 0) {
                    if (isDouble(tempEditText.getText().toString().trim()) == true) {
                        uom = Cart.returnUom(tag);
                        orignal_uom = Cart.returnOrignalUom(tag);
                        Log.d("OrignalUOM", orignal_uom);
                        min_weight = Cart.returnMinWeight(tag);
                        max_weight = Cart.returnMaxWeight(tag);
                        if (uom.equalsIgnoreCase("Kg")) {
                            if (orignal_uom.equalsIgnoreCase("kg")) {
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                            } else {
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate))) * 1000));
                            }
                        } else if (uom.equalsIgnoreCase("lb")) {
                            tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                        } else if (uom.equalsIgnoreCase("Gm")) {
                            if (orignal_uom.equalsIgnoreCase("kg")) {
                                tempText.setText(String.format("%.2f", (((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate))) / 1000)));
                            } else {
                                tempText.setText(String.format("%.2f", (((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate))))));
                            }
                        } else if (uom.equalsIgnoreCase("No")) {
                            tempText.setText(String.format("%.2f", ((Double.parseDouble(tempEditText.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                        }
                        if (uom.equalsIgnoreCase(orignal_uom)) {
                            min_weight = min_weight;
                            max_weight = max_weight;
                        } else if (uom.equalsIgnoreCase("kg") && orignal_uom.equalsIgnoreCase("gm")) {
                            min_weight = min_weight / 1000;
                            max_weight = max_weight / 1000;
                        } else if (uom.equalsIgnoreCase("gm") && orignal_uom.equalsIgnoreCase("kg")) {
                            min_weight = min_weight / 0.001;
                            max_weight = max_weight / 0.001;
                        }


                        if (min_weight == max_weight) {

                            Cart.updateCart(tag, tempEditText.getText().toString().trim(), uom);
                            for (int i = 0; i < list.size(); i++) {
                                if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                    CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                }
                            }
                        } else {
                            if (max_weight == 0.0) {
                                if ((Double.parseDouble(tempEditText.getText().toString())) >= min_weight) {
                                    Cart.updateCart(tag, tempEditText.getText().toString().trim(), uom);
                                    for (int i = 0; i < list.size(); i++) {
                                        if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                            CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                        }
                                    }
                                } else {
                                    Cart.updateCart(tag, "0", uom);
                                    for (int i = 0; i < list.size(); i++) {
                                        if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                            CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                        }
                                    }
//                                            tempEditText.setText("");
                                    Toast.makeText(context, "minimum order of " + min_weight + " " + uom + " is required to place the order for this product", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                if (min_weight == 0.0) {
                                    if ((Double.parseDouble(tempEditText.getText().toString())) <= max_weight) {
                                        Cart.updateCart(tag, tempEditText.getText().toString().trim(), uom);
                                        for (int i = 0; i < list.size(); i++) {
                                            if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                                CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                            }
                                        }

                                    } else {
                                        Cart.updateCart(tag, "0", uom);
                                        for (int i = 0; i < list.size(); i++) {
                                            if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                                CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                            }
                                        }
//                                                    tempEditText.setText("");
                                        Toast.makeText(context, "Cannot order more than " + max_weight + " " + uom + " of this product per order", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    if ((Double.parseDouble(tempEditText.getText().toString())) >= min_weight && (Double.parseDouble(tempEditText.getText().toString())) <= max_weight) {
                                        Cart.updateCart(tag, tempEditText.getText().toString().trim(), uom);
                                        for (int i = 0; i < list.size(); i++) {
                                            if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                                CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                            }
                                        }

                                    } else {
                                        Cart.updateCart(tag, "0", uom);
                                        for (int i = 0; i < list.size(); i++) {
                                            if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                                                CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                                            }
                                        }
                                        Toast.makeText(context, "Enter weight between  " + min_weight + " " + uom + " and " + max_weight + " " + uom, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
//                            }
//                        }
                    }
                } else {
                    uom = Cart.returnUom(tag);
                    Cart.updateCart(tag, "0", uom);
                    for (int i = 0; i < list.size(); i++) {
                        if (tag.equalsIgnoreCase(list.get(i).split("-")[0])) {
                            CartAdapter.changeSubTotal(tag, list.get(i).split("-")[1]);
                        }
                    }
                    tempText.setText("0");
                }
            }


        };

        edittext_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (tempEditText != null) {
                        tempEditText.removeTextChangedListener(watcher);
                    }
                    tag = v.getTag() + "";
                    tempEditText = ((EditText) v);
                    tempEditText.addTextChangedListener(watcher);
                }
            }

        });

        edittext_quantity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (tempEditText != null) {
                    tempEditText.removeTextChangedListener(watcher);
                }
                tag = v.getTag() + "";
                tempEditText = ((EditText) v);
                tempEditText.addTextChangedListener(watcher);
                return false;
            }
        });
    }


    public boolean isDouble(String value) {
        boolean isStringParsable = true;
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            isStringParsable = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isStringParsable;
    }
}

