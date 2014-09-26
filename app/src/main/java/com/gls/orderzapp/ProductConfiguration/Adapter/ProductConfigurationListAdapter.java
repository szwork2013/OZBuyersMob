package com.gls.orderzapp.ProductConfiguration.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gls.orderzapp.Provider.Beans.ProductDetails;
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
import java.util.List;

/**
 * Created by prajyot on 3/7/14.
 */
public class ProductConfigurationListAdapter extends BaseAdapter {
    Context context;
    List<ProductDetails> cakeList;
    DisplayImageOptions options;
    //    String[] mKeys;
    ImageLoader imageLoader;
    EditText edttxt_message_on_cake;
    EditText tempEditText = null;
    String tag = "";

    public ProductConfigurationListAdapter(Context context, List<ProductDetails> cakeList) {
        this.context = context;
        this.cakeList = cakeList;
//        mKeys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);
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
        return cakeList.size();
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
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.product_configuration_item_list, null);
        }
        ImageView product_image = (ImageView) convertView.findViewById(R.id.product_image);
        TextView product_name = (TextView) convertView.findViewById(R.id.product_name);
        CheckBox message_checkbox = (CheckBox) convertView.findViewById(R.id.message_checkbox);
        final TextView message_price = (TextView) convertView.findViewById(R.id.message_price);
        edttxt_message_on_cake = (EditText) convertView.findViewById(R.id.edttxt_message_on_cake);
        final TextView egg_price = (TextView) convertView.findViewById(R.id.egg_price);
        RadioGroup egg_eggless_group = (RadioGroup) convertView.findViewById(R.id.egg_eggless_group);
        RadioButton egg = (RadioButton) convertView.findViewById(R.id.egg);
        RadioButton eggless = (RadioButton) convertView.findViewById(R.id.eggless);
        LinearLayout ll_message = (LinearLayout) convertView.findViewById(R.id.ll_message);
        LinearLayout ll_food_type = (LinearLayout) convertView.findViewById(R.id.ll_food_type);

        egg_eggless_group.setTag(position + 100);
        message_checkbox.setTag(position + "_" + cakeList.get(position).getCartCount());
        edttxt_message_on_cake.setTag(cakeList.get(position).getCartCount());

        Log.d("CakeList", new Gson().toJson(Cart.hm));

        for (int i = 0; i < cakeList.get(position).getProductconfiguration().getConfiguration().size(); i++) {
            if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getFoodType().equalsIgnoreCase("eggless")) {
                    eggless.setChecked(true);
                    egg.setChecked(false);
                    message_price.setText("0.0");
                    if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                        Cart.addFoodTypeConfiguration(cakeList.get(position).getCartCount(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configname(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configprice(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).isChecked(),
                                "eggless");
                    }
                    egg_price.setText((cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configprice().getValue() * Double.parseDouble(cakeList.get(position).getQuantity())) + "");
                } else {
                    egg.setChecked(true);
                    eggless.setChecked(false);
                    message_price.setText("0.0");
                    egg_price.setText("0.0");
                    if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                        Cart.addFoodTypeConfiguration(cakeList.get(position).getCartCount(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configname(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configprice(),
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).isChecked(),
                                "egg");

                    }
                }
            }

            if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                ll_message.setVisibility(View.VISIBLE);
                if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).isChecked() == true) {
                    message_checkbox.setChecked(true);
                    edttxt_message_on_cake.setText(cakeList.get(position).getMessageonproduct());
                }

            } else if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                ll_food_type.setVisibility(View.VISIBLE);
            }
        }
        Log.d("cart after for loop", new Gson().toJson(Cart.hm));

        message_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    for (int i = 0; i < cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().size(); i++) {
                        if (cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                            cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().get(i).setChecked(true);
                            message_price.setText((cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().get(i).getProd_configprice().getValue() * Double.parseDouble(cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getQuantity())) + "");
                        }
                    }
                } else {
                    for (int i = 0; i < cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().size(); i++) {
                        if (cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                            cakeList.get(Integer.parseInt((((CheckBox) buttonView).getTag() + "").split("_")[0])).getProductconfiguration().getConfiguration().get(i).setChecked(false);
                        }
                    }
                    message_price.setText("0.0");
                }
            }
        });

        Log.d("cart after Msg Config", new Gson().toJson(Cart.hm));



        egg_eggless_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.eggless:
                        for (int i = 0; i < cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().size(); i++) {
                            if (cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                                Cart.addFoodTypeConfiguration(cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getCartCount(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configtype(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configname(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configprice(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).isChecked(),
                                        "eggless");
                                egg_price.setText((cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configprice().getValue() * Double.parseDouble(cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getQuantity())) + "");
                            }
                        }
                        break;
                    case R.id.egg:
                        egg_price.setText("0.0");
                        for (int i = 0; i < cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().size(); i++) {
                            if (cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                                Cart.addFoodTypeConfiguration(cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getCartCount(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configtype(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configname(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).getProd_configprice(),
                                        cakeList.get(Integer.parseInt(group.getTag() + "") - 100).getProductconfiguration().getConfiguration().get(i).isChecked(),
                                        "egg");
                            }
                        }
                        break;
                }
            }
        });
        Log.d("cart after egg group", new Gson().toJson(Cart.hm));

        if (cakeList.get(position).getProductlogo().getImage() != null) {
            imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage(cakeList.get(position).getProductlogo().getImage(), product_image, options, new SimpleImageLoadingListener() {
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
        }


        if (cakeList.get(position).getProductname() != null) {
            product_name.setText(cakeList.get(position).getProductname() + " - " + cakeList.get(position).getQuantity() + cakeList.get(position).getPrice().getUom());
        }


        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int j = 0; j < cakeList.size(); j++) {
                    if (tag.equalsIgnoreCase(cakeList.get(position).getCartCount())) {
                        Log.d("Run without", "Click");
                        Cart.addMessageOnCake(cakeList.get(position), tempEditText.getText().toString().trim());
                    }
                }
            }
        };

        edttxt_message_on_cake.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (tempEditText != null) {
                        tempEditText.removeTextChangedListener(textWatcher);
                    }
                    tempEditText = ((EditText) v);
                    tag = v.getTag() + "";
                    tempEditText.addTextChangedListener(textWatcher);

                }
            }
        });


        return convertView;
    }
}
