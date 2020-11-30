package in.ateesinfomedia.relief.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.AppHelpers;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.NotificationModel;
import in.ateesinfomedia.relief.models.ProductsModel;
import in.ateesinfomedia.relief.models.ReviewModel;
import in.ateesinfomedia.relief.models.cart.CartCountResponse;
import in.ateesinfomedia.relief.models.detailpro.ConfigProductOptions;
import in.ateesinfomedia.relief.models.detailpro.ConfigValues;
import in.ateesinfomedia.relief.models.detailpro.CustomAttributes;
import in.ateesinfomedia.relief.models.detailpro.OptionsPrice;
import in.ateesinfomedia.relief.models.detailpro.ProductDetail;
import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;
import in.ateesinfomedia.relief.models.products.ProductModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.adapter.AgeSpinnerAdapter;
import in.ateesinfomedia.relief.view.adapter.ColorSpinnerAdapter;
import in.ateesinfomedia.relief.view.adapter.ReviewAdapter;
import in.ateesinfomedia.relief.view.adapter.SizeSpinnerAdapter;
import in.ateesinfomedia.relief.view.adapter.SliderAdapter;
import in.ateesinfomedia.relief.view.adapter.ThumbnailAdapter;
import me.himanshusoni.quantityview.QuantityView;

import static in.ateesinfomedia.relief.configurations.Global.COUNT;
import static in.ateesinfomedia.relief.configurations.Global.ProductDetailModel;
import static in.ateesinfomedia.relief.configurations.Global.ReviewList;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;
import static in.ateesinfomedia.relief.configurations.Global.isChanged;

public class ProductDetailsActivity extends AppCompatActivity implements AdapterClickListner, NetworkCallback {

    private ProductDetail productDetail;
    private ImageView thumbnail;
    private TextView itemname,itemdetails;
    private RecyclerView thumbnailRecycler;
    private Button addToCart, mBuyNow;
    private ThumbnailAdapter mAdapter;
    private ReviewAdapter mReviewAdapter;
    private String[] strings;
    private TextView mPrice;
    private int value = 0;
    private ImageView mOfferTag;
    private TextView mOfferStrikePrice;
    private TextView mOfferOffPrice;
    private TextView mOutOfStock;
    public ViewPager viewPager;
    private MyPreferenceManager manager;
    private int REQUEST_ADD_TO_CART_ID = 9090;
    private int REQUEST_ISCART_ID = 9087;
    private Toolbar toolbar;
    private MenuItem mMenu1, mMenu2, mMenu3, mMenu;
    private int REQUEST_GET_NOTIFICATION_ID = 8390;
    private List<NotificationModel> notificationModelList = new ArrayList<>();
    private TextView mAddReview;
    private RecyclerView mReviewRecycler;
    private List<ReviewModel> mReviewList = new ArrayList<>();
    private LinearLayout mReviewLay, mMarkersLayout;
    private int REQUEST_CHECK_PRODUCT_PURCHASED_ID = 9876;
    private ReviewModel lastReviewModel;
    private List<ReviewModel> mFilteredReviewList = new ArrayList<>();
    private int REQUEST_ADD_CART_SIMPLE = 9008;
    private String sku;
    private static final String TAG = ProductDetailsActivity.class.getName();
    private LinearLayout configMain, configAge, configColor, configSize;
    private Spinner ageSpinner, colorSpinner, sizeSpinner;
    private String mAgeId, mColorId, mSizeId, mAgeValue, mColorValue, mSizeValue, mAgeAttr, mColorAttr, mSizeAttr;
    private boolean mAgeBool, mColorBool, mSizeBool;
    private int REQUEST_ADD_CART_CONFIG = 9108;
    private Button decrementBtn, incrementBtn;
    private TextView qtyText;
    private int productQty = 0;
    private int REQUEST_CART_COUNT = 3117;
    private Gson gson = new Gson();
    private Menu menuCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            Drawable background = this.getResources().getDrawable(R.drawable.ab_gradient);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }

        setContentView(R.layout.activity_product_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        itemname = (TextView) findViewById(R.id.itemName);
        itemdetails = (TextView) findViewById(R.id.itemdetails);
        mAddReview = (TextView) findViewById(R.id.tvAddReview);
        mReviewLay = (LinearLayout) findViewById(R.id.reviewLay);
        thumbnailRecycler = (RecyclerView) findViewById(R.id.thumbnailrecycler);
        mReviewRecycler = (RecyclerView) findViewById(R.id.reviewrecycler);
        addToCart = (Button) findViewById(R.id.btn_add);
        mBuyNow = (Button) findViewById(R.id.btn_buy_now);
        mOfferTag = (ImageView) findViewById(R.id.offer_tag);
        mOfferStrikePrice = (TextView) findViewById(R.id.strikeprice);
        mOfferOffPrice = (TextView) findViewById(R.id.offPrice);
        mOutOfStock = (TextView) findViewById(R.id.outOfStock);
        manager = new MyPreferenceManager(this);
        viewPager = findViewById(R.id.viewPager);
        mMarkersLayout = findViewById(R.id.layout_markers);
        mPrice = (TextView) findViewById(R.id.price);
        configMain = (LinearLayout) findViewById(R.id.config_spinner_main);
        configAge = (LinearLayout) findViewById(R.id.config_spinner_age_main);
        configColor = (LinearLayout) findViewById(R.id.config_spinner_color_main);
        configSize = (LinearLayout) findViewById(R.id.config_spinner_size_main);
        ageSpinner = (Spinner) findViewById(R.id.config_spinner_age);
        colorSpinner = (Spinner) findViewById(R.id.config_spinner_color);
        sizeSpinner = (Spinner) findViewById(R.id.config_spinner_size);
        decrementBtn = (Button) findViewById(R.id.product_detail_decrement);
        incrementBtn = (Button) findViewById(R.id.product_detail_increment);
        qtyText = (TextView) findViewById(R.id.product_detail_qty);
        mAgeBool = false;
        mColorBool = false;
        mSizeBool = false;

        productDetail = ProductDetailModel;

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productQty == 10) {
                    Toast.makeText(ProductDetailsActivity.this, "You reached the maximum limit.", Toast.LENGTH_SHORT).show();
                } else {
                    productQty ++;
                    setQuantityTxt();
                }
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productQty == 1) {
                    Toast.makeText(ProductDetailsActivity.this, "You reached the minimum limit.", Toast.LENGTH_SHORT).show();
                } else {
                    productQty --;
                    setQuantityTxt();
                }
            }
        });

        if (productDetail.getMedia_gallery_entries() != null) {
            viewPager.setAdapter(new SliderAdapter(
                    productDetail.getMedia_gallery_entries(),
                    ProductDetailsActivity.this,
                    R.layout.holder_detail_slider,
                    "detail"
            ));
            AppHelpers.addMarkers(
                    0,
                    productDetail.getMedia_gallery_entries(),
                    mMarkersLayout,
                    ProductDetailsActivity.this
            );
        }

        itemname.setText(productDetail.getName());

        sku = productDetail.getSku();

        ArrayList<CustomAttributes> customAttributes = productDetail.getCustom_attributes();

        if (customAttributes != null && !customAttributes.isEmpty()) {

            for (int index=0; index<customAttributes.size(); index ++) {
                if (customAttributes.get(index).getAttribute_code().equals("description")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        itemdetails.setText(Html.fromHtml(customAttributes.get(index).getValue().toString(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        itemdetails.setText(Html.fromHtml(customAttributes.get(index).getValue().toString()));
                    }
                    break;
                }
            }

        }

        if (productDetail.getType_id() != null && productDetail.getType_id().equals("simple")) {
            float price = Float.parseFloat(productDetail.getPrice());
            mPrice.setText("₹ " + String.format("%.2f", price));

            mOfferStrikePrice.setVisibility(View.INVISIBLE);
            mOfferOffPrice.setVisibility(View.INVISIBLE);

            configMain.setVisibility(View.GONE);

            if (productDetail.getExtension_attributes() != null
                    && productDetail.getExtension_attributes().getStock_item() != null
                    && productDetail.getExtension_attributes().getStock_item().getIs_in_stock()) {
                mOutOfStock.setVisibility(View.INVISIBLE);
                addToCart.setVisibility(View.VISIBLE);
                mBuyNow.setVisibility(View.GONE);

                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postAddSimpleProduct(manager.getCartId());
                    }
                });
            } else {
                mOutOfStock.setVisibility(View.VISIBLE);
                addToCart.setVisibility(View.GONE);
                mBuyNow.setVisibility(View.GONE);
            }
        } else if (productDetail.getType_id() != null
                && productDetail.getType_id().equals("configurable")) {
            mOfferStrikePrice.setVisibility(View.INVISIBLE);
            mOfferOffPrice.setVisibility(View.INVISIBLE);

            configMain.setVisibility(View.VISIBLE);
            configAge.setVisibility(View.GONE);
            configSize.setVisibility(View.GONE);
            configColor.setVisibility(View.GONE);

            if (productDetail.getConfigurable_product_options() != null
                    && !productDetail.getConfigurable_product_options().isEmpty()) {
                ArrayList<ConfigProductOptions> configOptionList = productDetail.getConfigurable_product_options();
                for (int index=0; index<configOptionList.size(); index ++) {
                    if (configOptionList.get(index).getLabel().equals("Color")) {
                        configColor.setVisibility(View.VISIBLE);
                        mColorId = configOptionList.get(index).getValues().get(0).getValue();
                        mColorValue = "color - " + configOptionList.get(index).getValues().get(0).getLabel() + " ";
                        mColorBool = true;
                        mColorAttr = configOptionList.get(index).getAttribute_id();
                        ColorSpinnerAdapter colorAdapter = new ColorSpinnerAdapter(getApplicationContext(), configOptionList.get(index).getValues());
                        colorSpinner.setAdapter(colorAdapter);
                        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                ConfigValues mColor = (ConfigValues) colorSpinner.getAdapter().getItem(i);
                                if (mColor != null) {
                                    mColorId = mColor.getValue();
                                    mColorValue = "color - " + mColor.getLabel() + " ";
                                    updateConfigPrice();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else if (configOptionList.get(index).getLabel().equals("Age")) {
                        configAge.setVisibility(View.VISIBLE);
                        mAgeId = configOptionList.get(index).getValues().get(0).getValue();
                        mAgeValue = "age - " + configOptionList.get(index).getValues().get(0).getLabel() + " ";
                        mAgeBool = true;
                        mAgeAttr = configOptionList.get(index).getAttribute_id();
                        AgeSpinnerAdapter ageAdapter = new AgeSpinnerAdapter(getApplicationContext(), configOptionList.get(index).getValues());
                        ageSpinner.setAdapter(ageAdapter);
                        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                ConfigValues mAge = (ConfigValues) ageSpinner.getAdapter().getItem(i);
                                if (mAge != null) {
                                    mAgeId = mAge.getValue();
                                    mAgeValue = "age - " + mAge.getLabel() + " ";
                                    updateConfigPrice();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else if (configOptionList.get(index).getLabel().equals("Quantity Size")) {
                        configSize.setVisibility(View.VISIBLE);
                        mSizeId = configOptionList.get(index).getValues().get(0).getValue();
                        mSizeValue = "quantity_size - " + configOptionList.get(index).getValues().get(0).getLabel() + " ";
                        mSizeBool = true;
                        mSizeAttr = configOptionList.get(index).getAttribute_id();
                        SizeSpinnerAdapter sizeAdapter = new SizeSpinnerAdapter(getApplicationContext(), configOptionList.get(index).getValues());
                        sizeSpinner.setAdapter(sizeAdapter);
                        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                ConfigValues mSize = (ConfigValues) sizeSpinner.getAdapter().getItem(i);
                                if (mSize != null) {
                                    mSizeId = mSize.getValue();
                                    mSizeValue = "quantity_size - " + mSize.getLabel() + " ";
                                    updateConfigPrice();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            if (productDetail.getIs_in_stock() != null && productDetail.getIs_in_stock()) {
                mOutOfStock.setVisibility(View.INVISIBLE);
                addToCart.setVisibility(View.VISIBLE);
                mBuyNow.setVisibility(View.GONE);

                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postAddConfigProduct(manager.getCartId());
                    }
                });
            } else {
                mOutOfStock.setVisibility(View.VISIBLE);
                addToCart.setVisibility(View.GONE);
                mBuyNow.setVisibility(View.GONE);
            }

        }

        setQuantityTxt();

    }

    private void setQuantityTxt() {
        qtyText.setText("" + productQty);
    }

    private void updateConfigPrice() {
        ArrayList<OptionsPrice> optionsPrice = productDetail.getOptions_price();
        if (optionsPrice != null && !optionsPrice.isEmpty()) {
            for (int index = 0; index < optionsPrice.size(); index++) {
                boolean flagAge = false;
                boolean flagColor = false;
                boolean flagSize = false;
                ArrayList<String> options = productDetail.getOptions_price().get(index).getOptions();
                if (options != null && !options.isEmpty()) {
                    for (int index1 = 0; index1 < options.size(); index1++) {
                        //Log.d(TAG, "\n");
                        //Log.d(TAG, options.get(index1));
                        //Log.d(TAG, mAgeValue);
                        //Log.d(TAG, mSizeValue);
                        //Log.d(TAG, mColorValue);
                        if (mAgeBool && options.get(index1).equals(mAgeValue)) {
                            flagAge = true;
                        }
                        if (mSizeBool && options.get(index1).equals(mSizeValue)) {
                            flagSize = true;
                        }
                        if (mColorBool && options.get(index1).equals(mColorValue)) {
                            flagColor = true;
                        }
                    }
                    if (mAgeBool == flagAge && mSizeBool == flagSize && mColorBool == flagColor) {
                        float price = Float.parseFloat(productDetail.getOptions_price().get(index).getPrice());
                        mPrice.setText("₹ " + String.format("%.2f", price));
                        break;
                    }
                }
            }
        }
    }

    private void postAddSimpleProduct(String quote_id) {
        try {
            JSONObject map = new JSONObject();
            JSONObject cartItem = new JSONObject();
            cartItem.put("sku", sku);
            cartItem.put("qty", productQty);
            cartItem.put("quote_id", quote_id);
            map.put("cartItem",cartItem);
            Log.d("doJSON Request",map.toString());

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_ADD_CART_SIMPLE,
                    Object.class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_ADD_CART_SIMPLE",
                    REQUEST_ADD_CART_SIMPLE,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postAddConfigProduct(String quote_id) {
        try {
            JSONObject map = new JSONObject();
            JSONObject cartItem = new JSONObject();
            JSONObject prodOptions = new JSONObject();
            JSONObject extAttribute = new JSONObject();
            JSONArray configItemOptionArray = new JSONArray();
            if (mAgeBool) {
                JSONObject configItemOption = new JSONObject();
                configItemOption.put("option_id", mAgeAttr);
                configItemOption.put("option_value", mAgeId);
                configItemOptionArray.put(configItemOption);
            }
            if (mSizeBool) {
                JSONObject configItemOption = new JSONObject();
                configItemOption.put("option_id", mSizeAttr);
                configItemOption.put("option_value", mSizeId);
                configItemOptionArray.put(configItemOption);
            }
            if (mColorBool) {
                JSONObject configItemOption = new JSONObject();
                configItemOption.put("option_id", mColorAttr);
                configItemOption.put("option_value", mColorId);
                configItemOptionArray.put(configItemOption);
            }
            cartItem.put("sku", sku);
            cartItem.put("qty", productQty);
            cartItem.put("quote_id", quote_id);
            extAttribute.put("configurable_item_options", configItemOptionArray);
            prodOptions.put("extension_attributes", extAttribute);
            cartItem.put("product_option", prodOptions);
            map.put("cartItem",cartItem);
            Log.d("doJSON Request",map.toString());

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_ADD_CART_CONFIG,
                    Object.class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_ADD_CART_SIMPLE",
                    REQUEST_ADD_CART_CONFIG,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processJsonAddCartSimple(String response) {
        Log.d(TAG, response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String pName = jsonObject.optString("name");
            if (pName != null && !pName.equals("") && !pName.isEmpty()) {
                Toast.makeText(this, "" + pName + " added to cart successfully", Toast.LENGTH_SHORT).show();

                getCartCount();
            } else {
                String message = jsonObject.optString("message");
                LoadingDialog.cancelLoading();
                dialogWarning(ProductDetailsActivity.this, "" + message);
            }
        } catch (Exception e) {
            LoadingDialog.cancelLoading();
            dialogWarning(ProductDetailsActivity.this, "Something went wrong!!!");
            e.printStackTrace();
        }
    }

    private void getCartCount() {
        String cartId = manager.getCartId();
        String getUrl = Apis.API_GET_CART_COUNT + cartId;

        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                CartCountResponse[].class,
                null,
                Apis.ACCESS_TOKEN,
                "TAG_CART_COUNT",
                REQUEST_CART_COUNT,
                this
        );
    }

    private void processJsonCartCount(String response) {
        Log.d(TAG, response);
        LoadingDialog.cancelLoading();
        try {
            Type type = new TypeToken<ArrayList<CartCountResponse>>(){}.getType();
            ArrayList<CartCountResponse> cartCountResponseArrayList = gson.fromJson(response, type);
            if (cartCountResponseArrayList != null && !cartCountResponseArrayList.isEmpty()) {
                manager.saveCartCount(cartCountResponseArrayList.get(0).getCount());

                if (manager.getCartCount() == 0){
                    ActionItemBadge.hide(menuCart.findItem(R.id.action_addcart));
                } else {
                    ActionItemBadge.update(this, menuCart.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse cartData = gson.fromJson(response, type);
                if (cartData != null) {
                    String message = cartData.getMessage();
                    LoadingDialog.cancelLoading();
                    dialogWarning(this, message);
                } else {
                    serverErrorDialog();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        this.mMenu = menu.findItem(R.id.action_addcart);
        this.mMenu1 = menu.findItem(R.id.action_notification);
        this.mMenu2 = menu.findItem(R.id.action_search);
        this.mMenu3 = menu.findItem(R.id.action_filter);

        menuCart = menu;

        ActionItemBadge.hide(menu.findItem(R.id.action_filter));
        ActionItemBadge.hide(menu.findItem(R.id.action_search));
        ActionItemBadge.hide(menu.findItem(R.id.action_notification));

        if (manager.getCartCount() == 0){
            ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
        } else {
            ActionItemBadge.update(this, menu.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
        }
        if (COUNT.equals("0") || COUNT.isEmpty() || COUNT.equals("null") ){
            ActionItemBadge.hide(menu.findItem(R.id.action_notification));
        } else {
            ActionItemBadge.update(this, menu.findItem(R.id.action_notification), FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_addcart:

                Intent intent = new Intent(ProductDetailsActivity.this,CartActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void serverErrorDialog() {
        try {
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(int position, Object object) {
        String image = (String) object;
        Glide.with(this).load(Apis.PRODUCT_IMAGE_BASE_URL+ image).into(thumbnail);
    }

    @Override
    public void buttonClicked(int position, Object object) {
        //
    }

    private void strikeThroughText(TextView price){
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_ADD_CART_SIMPLE){
                processJsonAddCartSimple(response);
            } else if (requestId == REQUEST_ADD_CART_CONFIG) {
                processJsonAddCartSimple(response);
            } else if (requestId == REQUEST_CART_COUNT) {
                processJsonCartCount(response);
            }
        } else {
            LoadingDialog.cancelLoading();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menuCart != null) {
            if (manager.getCartCount() == 0){
                ActionItemBadge.hide(menuCart.findItem(R.id.action_addcart));
            } else {
                ActionItemBadge.update(this, menuCart.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}