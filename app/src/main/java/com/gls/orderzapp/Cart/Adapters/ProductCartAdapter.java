package com.gls.orderzapp.Cart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gls.orderzapp.MainApp.ProductCartActivity;
import com.gls.orderzapp.MainApp.WebViewActivity;
import com.gls.orderzapp.R;
import com.gls.orderzapp.Utility.Cart;
import com.gls.orderzapp.Utility.ServerConnection;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avinash on 7/4/14.
 */

public class ProductCartAdapter extends BaseAdapter {

    ImageButton imgbtn_productcart_delete;
    Button btn_productcart_privacy;
    Context context;
    Spinner spnr_weight;
    ImageView img_productcart_product;
    com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    String[] mKeys;
    ArrayList<String> arrayListweight;
    TextView txt_productcart_productname, txt_currency, txt_prodctcart_productprice, txt_prodctcart_total, txt_provider_name, txt_provider_area, txt_provider_note;
    EditText txt_prodctcart_quantity;
    EditText temp_qty = null;
    Spinner tempSpinner = null;
    public static String measure = null;
    public static double min_weight = 0, max_weight = 0;

    public ProductCartAdapter(Context context) {
        this.context = context;
        mKeys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);

    }

    @Override
    public int getCount() {
        return Cart.hm.size();
    }

    @Override
    public Object getItem(int position) {
        return Cart.hm.get(mKeys[position]);
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
                convertView = li.inflate(R.layout.productcart_list, null);
            }
            txt_productcart_productname = (TextView) convertView.findViewById(R.id.txt_productcart_productname);
            txt_prodctcart_productprice = (TextView) convertView.findViewById(R.id.txt_prodctcart_productprice);
            txt_provider_name = (TextView) convertView.findViewById(R.id.txt_provider_name);
            txt_provider_area = (TextView) convertView.findViewById(R.id.txt_provider_area);
            txt_prodctcart_total = (TextView) convertView.findViewById(R.id.txt_prodctcart_total);
            txt_prodctcart_quantity = (EditText) convertView.findViewById(R.id.txt_prodctcart_quantity);
            img_productcart_product = (ImageView) convertView.findViewById(R.id.img_productcart_product);
            imgbtn_productcart_delete = (ImageButton) convertView.findViewById(R.id.imgbtn_productcart_delete);
            btn_productcart_privacy = (Button) convertView.findViewById(R.id.btn_productcart_privacy);
            spnr_weight = (Spinner) convertView.findViewById(R.id.spnr_weight);
            LinearLayout llPolicyButton = (LinearLayout) convertView.findViewById(R.id.llPolicyButton);
            txt_currency = (TextView) convertView.findViewById(R.id.txt_currency);

            txt_provider_note = (TextView) convertView.findViewById(R.id.txt_provider_note);

            if (Cart.hm.get(mKeys[position]).getProductname() != null) {
                txt_productcart_productname.setText(Cart.hm.get(mKeys[position]).getProductname());
            }
            if (Cart.hm.get(mKeys[position]).getProviderName() != null) {
                txt_provider_name.setText(Cart.hm.get(mKeys[position]).getProviderName());
            }
            if (Cart.hm.get(mKeys[position]).getQuantity() != null) {
                txt_prodctcart_quantity.setSelection(txt_prodctcart_quantity.getText().length());
                txt_prodctcart_quantity.setText(Cart.hm.get(mKeys[position]).getQuantity());
            }

            txt_prodctcart_productprice.setText(Cart.hm.get(mKeys[position]).getPrice().getValue() + "");
            if (Cart.hm.get(mKeys[position]).getPrice().getCurrency() != null) {
                txt_currency.setText(Cart.hm.get(mKeys[position]).getPrice().getCurrency());
            }
            if (Cart.hm.get(mKeys[position]).getLocation().getArea() != null) {
                txt_provider_area.setText(Cart.hm.get(mKeys[position]).getLocation().getArea());
            }
            if (Cart.hm.get(mKeys[position]).getNote() != null) {
                txt_provider_note.setText(Cart.hm.get(mKeys[position]).getNote());
            }
            if (Cart.hm.get(mKeys[position]).getPrice().getUom() != null) {
                measure = Cart.hm.get(mKeys[position]).getPrice().getUom();
            }
            min_weight = Cart.hm.get(mKeys[position]).getMin_weight().getValue();
            max_weight = Cart.hm.get(mKeys[position]).getMax_weight().getValue();

            String imageLogo = new Gson().toJson(Cart.hm.get(mKeys[position]));
            if (imageLogo != null && imageLogo.contains("productlogo")) {
                imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                imageLoader.displayImage(Cart.hm.get(mKeys[position]).getProductlogo().getImage(), img_productcart_product, new SimpleImageLoadingListener() {
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

            imgbtn_productcart_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mKeys[position] != null) {
                        Cart.deleteFromCart(mKeys[position]);
                        ProductCartActivity.displayCart();
                    }
                }
            });

            llPolicyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Cart.hm.get(mKeys[position]).getProviderid() != null && Cart.hm.get(mKeys[position]).getBranchid() != null) {
                        Intent goToPrivacyPolicy = new Intent(context, WebViewActivity.class);
                        goToPrivacyPolicy.putExtra("URL", ServerConnection.url + "/api/branchpolicy/" + Cart.hm.get(mKeys[position]).getProviderid() + "/" + Cart.hm.get(mKeys[position]).getBranchid() + "?type=all&response_type=html");
                        context.startActivity(goToPrivacyPolicy);
                    }
                }
            });

            Double temp_total = 0.00;
            temp_total = Double.parseDouble(txt_prodctcart_quantity.getText().toString()) * Double.parseDouble(txt_prodctcart_productprice.getText().toString());
            txt_prodctcart_total.setText(String.format("%.2f", temp_total));
            final TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (isDouble(temp_qty.getText().toString().trim()) == true) {
                        if (temp_qty.getText().toString().trim().length() > 0) {
                            if (temp_qty.getText().toString().equals(".")) {
                                temp_qty.setText("0.");
                                temp_qty.setSelection(temp_qty.getText().length());

                            }

                            String fixed_rate = ((TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(1))).getText().toString();
                            TextView tempText = (TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(7));
                            if (Cart.hm.get(mKeys[position]).getPrice().getUom() != null) {
                                measure = Cart.hm.get(mKeys[position]).getPrice().getUom();
                                if (measure.equalsIgnoreCase("Kg")) {
                                    tempText.setText(String.format("%.2f", ((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                                } else if (measure.equalsIgnoreCase("lb")) {
                                    tempText.setText(String.format("%.2f", ((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                                } else if (measure.equalsIgnoreCase("Gm")) {
                                    tempText.setText(String.format("%.2f", (((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate))) / 1000)));
                                } else if (measure.equalsIgnoreCase("No")) {
                                    tempText.setText(String.format("%.2f", ((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate)))));
                                }
                                if ((Double.parseDouble(temp_qty.getText().toString())) >= min_weight && (Double.parseDouble(temp_qty.getText().toString())) <= max_weight) {
//                                    Cart.updateCart(mKeys[position], Cart.hm.get(mKeys[position]), temp_qty.getText().toString().trim(), measure);
                                } else {
                                    Toast.makeText(context, "Please enter a weight between " + min_weight + " " + measure + " and " + max_weight + " " + measure, Toast.LENGTH_LONG).show();

                                    temp_qty.setText("");
                                }
                            }
                        } else {
                            double qty_if_zero = 0.00;
                            String fixed_rate = ((TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(1))).getText().toString();
                            TextView tempText = (TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(7));
                            if (Cart.hm.get(mKeys[position]).getPrice().getUom() != null) {
                                measure = Cart.hm.get(mKeys[position]).getPrice().getUom();
                                if (measure.equalsIgnoreCase("Kg")) {
                                    tempText.setText(String.format("%.2f", qty_if_zero * (Double.parseDouble(fixed_rate))));
                                } else if (measure.equalsIgnoreCase("lb")) {
                                    tempText.setText(String.format("%.2f", qty_if_zero * (Double.parseDouble(fixed_rate))));

                                } else if (measure.equalsIgnoreCase("Gm")) {
                                    tempText.setText(String.format("%.2f", ((qty_if_zero * (Double.parseDouble(fixed_rate))) / 1000)));

                                } else if (measure.equalsIgnoreCase("No")) {
                                    tempText.setText(String.format("%.2f", qty_if_zero * (Double.parseDouble(fixed_rate))));
                                }

                                if (temp_qty.getText().toString().trim().length() > 0) {
                                    if (min_weight > Double.parseDouble(temp_qty.getText().toString())) {
                                        Toast.makeText(context, "Please enter a weight between " + min_weight + " " + measure + " and " + max_weight + " " + measure, Toast.LENGTH_LONG).show();
                                        temp_qty.setFocusable(true);
                                        temp_qty.setText("");
                                    }
                                }


//                                Cart.updateCart(mKeys[position], Cart.hm.get(mKeys[position]), "0", measure);

                            }
                        }
                    }

                }
            };

            txt_prodctcart_quantity.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (temp_qty != null) {
                        temp_qty.removeTextChangedListener(textWatcher);
                    }
                    temp_qty = ((EditText) view);
                    if (Cart.hm.get(mKeys[position]).getPrice().getUom() != null) {
                        if (Cart.hm.get(mKeys[position]).getPrice().getUom().equalsIgnoreCase("no")) {
                            temp_qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            temp_qty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        }
                    }
                    temp_qty.addTextChangedListener(textWatcher);
                    return false;
                }
            });

            spnr_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                    tempSpinner = ((Spinner) parent);
                    temp_qty = (EditText) (((LinearLayout) tempSpinner.getParent()).getChildAt(3));
                    if (((TextView) view).getText() != null) {
                        if (((TextView) view).getText().toString().equalsIgnoreCase("Kg")) {
                            if (temp_qty.getText().toString().trim().length() > 0) {
                                String fixed_rate = ((TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(1))).getText().toString();
                                TextView tempText = (TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(7));
                                measure = "Kg";
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate)))));
//                                Cart.updateCart(mKeys[position], Cart.hm.get(mKeys[position]), temp_qty.getText().toString().trim(), measure);

                            }
                        } else if (((TextView) view).getText().toString().equalsIgnoreCase("lb")) {
                            if (temp_qty.getText().toString().trim().length() > 0) {
                                String fixed_rate = ((TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(1))).getText().toString();
                                TextView tempText = (TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(7));
                                measure = "lb";
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate)))));
//                                Cart.updateCart(mKeys[position], Cart.hm.get(mKeys[position]), temp_qty.getText().toString().trim(), measure);
                            }
                        } else if (((TextView) view).getText().toString().equalsIgnoreCase("Gm")) {
                            if (temp_qty.getText().toString().trim().length() > 0) {
                                String fixed_rate = ((TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(1))).getText().toString();
                                TextView tempText = (TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(7));
                                measure = "Gm";
                                tempText.setText(String.format("%.2f", (((Double.parseDouble(temp_qty.getText().toString())) / 1000) * (Double.parseDouble(fixed_rate)))));
//                                Cart.updateCart(mKeys[position], Cart.hm.get(mKeys[position]), temp_qty.getText().toString().trim(), measure);
                            }
                        } else if (((TextView) view).getText().toString().equalsIgnoreCase("No")) {

                            if (temp_qty.getText().toString().trim().length() > 0) {
                                String fixed_rate = ((TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(1))).getText().toString();
                                TextView tempText = (TextView) (((LinearLayout) (temp_qty.getParent())).getChildAt(7));
                                measure = "No";
                                tempText.setText(String.format("%.2f", ((Double.parseDouble(temp_qty.getText().toString())) * (Double.parseDouble(fixed_rate)))));
//                                Cart.updateCart(mKeys[position], Cart.hm.get(mKeys[position]), temp_qty.getText().toString().trim(), measure);

                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            WeightSpinnerActions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void WeightSpinnerActions() {
        arrayListweight = new ArrayList<>();
        if (measure != null) {
            if (measure.equalsIgnoreCase("No")) {
                arrayListweight.add("No");
                txt_prodctcart_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (measure.equalsIgnoreCase("Kg")) {
                arrayListweight.add("Kg");
                arrayListweight.add("Gm");
                txt_prodctcart_quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (measure.equalsIgnoreCase("Gm")) {
                arrayListweight.add("Gm");
                arrayListweight.add("Kg");
                txt_prodctcart_quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (measure.equalsIgnoreCase("lb")) {
                arrayListweight.add("lb");
                txt_prodctcart_quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            for (int i = 0; i < arrayListweight.size(); i++) {
                spnr_weight.setAdapter(new ArrayAdapter<String>(context.getApplicationContext(), R.layout.weight_spinner_items, arrayListweight));
            }
        }
    }

    public boolean isDouble(String isDouble) {
        try {
            Double.valueOf(isDouble);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}