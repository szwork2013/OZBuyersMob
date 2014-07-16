package com.gls.orderzapp.ProductConfiguration.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    String[] mKeys;
    ImageLoader imageLoader;
    EditText edttxt_message_on_cake;
    EditText tempEditText = null;
//    TextWatcher textWatcher;

    public ProductConfigurationListAdapter(Context context, List<ProductDetails> cakeList) {
        this.context = context;
        this.cakeList = cakeList;
        mKeys = Cart.hm.keySet().toArray(new String[Cart.hm.size()]);
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

        for (int i = 0; i < cakeList.get(position).getProductconfiguration().getConfiguration().size(); i++) {
            if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                ll_message.setVisibility(View.VISIBLE);

            } else if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                ll_food_type.setVisibility(View.VISIBLE);
            }
        }

        egg.setChecked(true);
        message_price.setText("0.0");
        egg_price.setText("0.0");

        message_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    for (int i = 0; i < cakeList.get(position).getProductconfiguration().getConfiguration().size(); i++) {
                        if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                            cakeList.get(position).getProductconfiguration().getConfiguration().get(i).setChecked(true);
                            message_price.setText((cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configprice().getValue() * Double.parseDouble(cakeList.get(position).getQuantity())) + "");
                        }
                    }
                } else {
                    for (int i = 0; i < cakeList.get(position).getProductconfiguration().getConfiguration().size(); i++) {
                        if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("msg")) {
                            cakeList.get(position).getProductconfiguration().getConfiguration().get(i).setChecked(false);
                        }
                    }
                    message_price.setText("0.0");
                }
            }
        });


        egg_eggless_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.eggless:
                        for (int i = 0; i < cakeList.get(position).getProductconfiguration().getConfiguration().size(); i++) {
                            if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).setFoodType("eggless");
                                egg_price.setText((cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configprice().getValue() * Double.parseDouble(cakeList.get(position).getQuantity())) + "");
                            }
                        }
                        break;
                    case R.id.egg:
                        egg_price.setText("0.0");
                        for (int i = 0; i < cakeList.get(position).getProductconfiguration().getConfiguration().size(); i++) {
                            if (cakeList.get(position).getProductconfiguration().getConfiguration().get(i).getProd_configtype().equalsIgnoreCase("ftp")) {
                                cakeList.get(position).getProductconfiguration().getConfiguration().get(i).setFoodType("egg");
                            }
                        }
                        break;
                }
            }
        });

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

                Cart.addMessageOnCake(mKeys[position], cakeList.get(position), tempEditText.getText().toString().trim());
            }
        };

        edttxt_message_on_cake.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (tempEditText != null) {
                    tempEditText.removeTextChangedListener(textWatcher);
                }
                tempEditText = ((EditText) view);

                tempEditText.addTextChangedListener(textWatcher);
                return false;
            }
        });

//        edttxt_message_on_cake.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if (tempEditText != null) {
//                        tempEditText.removeTextChangedListener(textWatcher);
//                    }
//                    tempEditText = ((EditText) v);
//
//                    tempEditText.addTextChangedListener(textWatcher);
//                }
//            }
//        });

        return convertView;
    }

//    public void setOnTouchListener(EditText edittext){
//        if (tempEditText != null) {
//            tempEditText.removeTextChangedListener(textWatcher);
//        }
//        tempEditText = edittext;
//
//        tempEditText.addTextChangedListener(textWatcher);
//    }

}
