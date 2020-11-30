package in.ateesinfomedia.relief.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.NotificationModel;
import in.ateesinfomedia.relief.models.ReviewModel;
import in.ateesinfomedia.relief.models.detailpro.ProductDetail;
import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;
import in.ateesinfomedia.relief.models.products.ProductModel;
import in.ateesinfomedia.relief.view.adapter.ReviewAdapter;
import in.ateesinfomedia.relief.view.adapter.ThumbnailAdapter;
import me.himanshusoni.quantityview.QuantityView;

import static in.ateesinfomedia.relief.configurations.Global.COUNT;
import static in.ateesinfomedia.relief.configurations.Global.ReviewList;
import static in.ateesinfomedia.relief.configurations.Global.isChanged;

public class ProductDetailsActivityOld extends AppCompatActivity implements AdapterClickListner, QuantityView.OnQuantityChangeListener, NetworkCallback {

    ProductModel productsModel;
    ProductDetail productDetail;
    ImageView thumbnail;
    TextView itemname,itemdetails;
    RecyclerView thumbnailRecycler;
    Button addToCart;
    private ThumbnailAdapter mAdapter;
    private ReviewAdapter mReviewAdapter;
    private String[] strings;
    private QuantityView quantityView;
    private TextView mPrice;
    private int value = 0;
    private ImageView mOfferTag;
    private TextView mOfferStrikePrice;
    private TextView mOfferOffPrice;
    private MyPreferenceManager manager;
    private int REQUEST_ADD_TO_CART_ID = 9090;
    private int REQUEST_ISCART_ID = 9087;
    private Toolbar toolbar;
    private MenuItem mMenu1;
    private MenuItem mMenu;
    private int REQUEST_GET_NOTIFICATION_ID = 8390;
    private List<NotificationModel> notificationModelList = new ArrayList<>();
    private TextView mAddReview;
    private RecyclerView mReviewRecycler;
    private List<ReviewModel> mReviewList = new ArrayList<>();
    private LinearLayout mReviewLay;
    private int REQUEST_CHECK_PRODUCT_PURCHASED_ID = 9876;
    private ReviewModel lastReviewModel;
    private List<ReviewModel> mFilteredReviewList = new ArrayList<>();

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
        quantityView = (QuantityView) findViewById(R.id.quantityCountPicker);
        mOfferTag = (ImageView) findViewById(R.id.offer_tag);
        mOfferStrikePrice = (TextView) findViewById(R.id.strikeprice);
        mOfferOffPrice = (TextView) findViewById(R.id.offPrice);
        manager = new MyPreferenceManager(this);

        mPrice = (TextView) findViewById(R.id.price);
        quantityView.setMinQuantity(1);
        quantityView.setMaxQuantity(10);
        quantityView.setQuantity(1);

        quantityView.setOnQuantityChangeListener(this);

        productsModel = (ProductModel) getIntent().getSerializableExtra("details");

        //loadImagesToArray();
        //checkIsCart();

        ArrayList<MediaGalleryEntries> imageList = productsModel.getMedia_gallery_entries();
        if (imageList != null && !imageList.isEmpty() && imageList.get(0).getFile() != null) {
            Glide.with(this)
                    .load(Apis.PRODUCT_IMAGE_BASE_URL+ imageList.get(0).getFile())
                    .into(thumbnail);
        }

        itemname.setText(productsModel.getName());
        //float offer = Float.valueOf(productsModel.getProduct_total_offer());
        float realPrice = Float.valueOf(productsModel.getPrice());

        /*if (realPrice > offer){
            mOfferTag.setVisibility(View.VISIBLE);
            if (productsModel.getProduct_total().contains(".")){
                String[] arrStr = productsModel.getProduct_total().split("\\.", 2);
                if (arrStr[1].length()>=2){
                    String second = arrStr[1].substring(0,2);
                    mOfferStrikePrice.setText("₹ "+arrStr[0]+"."+second);
                } else {
                    mOfferStrikePrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
                }
            } else {
                mOfferStrikePrice.setText("₹ " + productsModel.getProduct_total());
            }
            strikeThroughText(mOfferStrikePrice);

            float price = Float.valueOf(productsModel.getProduct_total_offer());
            float strikePrice = Float.valueOf(productsModel.getProduct_total());
            float percen = (strikePrice-price)/strikePrice*100;

            mOfferOffPrice.setText(Math.round(percen)+"% off");

        } else {
            mOfferTag.setVisibility(View.GONE);
        }*/
        /*if (productsModel.getProduct_total_offer().contains(".")){
            String[] arrStr = productsModel.getProduct_total_offer().split("\\.", 2);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                mPrice.setText("₹ "+arrStr[0]+"."+second);
            } else {
                mPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }
        } else {
            mPrice.setText("₹ " + productsModel.getProduct_total_offer());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            itemdetails.setText(Html.fromHtml(productsModel.getProduct_description(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            itemdetails.setText(Html.fromHtml(productsModel.getProduct_description()));
        }*/

        mReviewList.clear();
        mReviewList = ReviewList;

        if (mReviewList.size() == 0 || mReviewList == null){
        } else {
            mFilteredReviewList.clear();
            for (int o = 0;o<mReviewList.size();o++){
                String pro_id = mReviewList.get(o).getProduct_id();
                /*if (pro_id.equals(productsModel.getId())){
                    ReviewModel reviewModel = new ReviewModel();
                    reviewModel = mReviewList.get(o);
                    mFilteredReviewList.add(reviewModel);
                }*/
            }
            mReviewRecycler.setNestedScrollingEnabled(true);
            mReviewAdapter = new ReviewAdapter(this,mFilteredReviewList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mReviewRecycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
            mReviewRecycler.setItemAnimator(new DefaultItemAnimator());
            mReviewRecycler.setAdapter(mReviewAdapter);
        }

        thumbnailRecycler.setNestedScrollingEnabled(true);
        mAdapter = new ThumbnailAdapter(this,strings,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        thumbnailRecycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        thumbnailRecycler.setItemAnimator(new DefaultItemAnimator());
        thumbnailRecycler.setAdapter(mAdapter);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (productsModel.getIsCart().equals("1")){
                    Intent intent = new Intent(ProductDetailsActivity.this,CartActivity.class);
                    startActivity(intent);
                } else {
                    doAddItemToCart();
                }*/
            }
        });

        mAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doCheckTheProductAlreadyPurchased();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        this.mMenu = menu.findItem(R.id.action_addcart);
        this.mMenu1 = menu.findItem(R.id.action_notification);

        if (manager.getCartCount() == 0){
            ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
        } else {
            ActionItemBadge.update(this, menu.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
        }
        if (COUNT.equals("0") || COUNT.isEmpty()){
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

                Intent intent = new Intent(ProductDetailsActivityOld.this,CartActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_notification:

                //getNotifications();

                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        /*float price = Float.valueOf(productsModel.getProduct_total_offer()) * quantityView.getQuantity();
        float strikeprice = Float.valueOf(productsModel.getProduct_total()) * quantityView.getQuantity();
        String priceValue = String.valueOf(price);
        String strikepriceValue = String.valueOf(strikeprice);

        if (priceValue.contains(".")){
                String[] arrStr = priceValue.split("\\.");
                Log.d("ttttttt",arrStr[0]);
                if (arrStr[1].length()>=2){
                    String second = arrStr[1].substring(0,2);
                    mPrice.setText("₹ "+arrStr[0]+"."+second);
                } else {
                    mPrice.setText("₹ "+arrStr[0]+"."+arrStr[1]);
                }
            if (strikeprice > price){
                String[] arrStrin = strikepriceValue.split("\\.");
                Log.d("ttttttt",arrStrin[0]);
                if (arrStrin[1].length()>=2){
                    String second = arrStrin[1].substring(0,2);
                    mOfferStrikePrice.setText("₹ "+arrStrin[0]+"."+second);
                } else {
                    mOfferStrikePrice.setText("₹ "+arrStrin[0]+"."+arrStrin[1]);
                }
            } else {
            }

        } else {
            if (strikeprice > price){
                mPrice.setText("₹ "+price);
                mOfferStrikePrice.setText("₹ "+strikeprice);
            } else {
                mPrice.setText("₹ "+price);
            }
        }*/

        if (String.valueOf(newQuantity).isEmpty()){
            value = 1;
        } else {
            value = quantityView.getQuantity();
        }
    }

    @Override
    public void onLimitReached() {

        Toast.makeText(this, "Limit over..", Toast.LENGTH_SHORT).show();
    }

    private void strikeThroughText(TextView price){
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_ADD_TO_CART_ID){
                //processAddToCart(response);
            } else if (requestId == REQUEST_ISCART_ID){
                //processIsCart(response);
            } else if (requestId == REQUEST_GET_NOTIFICATION_ID){
                //processJsonNotification(response);
            } else if (requestId == REQUEST_CHECK_PRODUCT_PURCHASED_ID){
                //processJsonProductPurchased(response);
            }
        } else {
            LoadingDialog.cancelLoading();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager.getCartCount()> 0) {
            ActionItemBadge.update(this, mMenu, FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
        }

        if (COUNT.isEmpty() || COUNT.equals("null") || COUNT.equals("0")){
        } else {
            ActionItemBadge.update(this, mMenu1, FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
        }

        if (isChanged){
            //checkIsCart();
            isChanged = false;
            Log.d("yssssssssss","yes");
        }
    }

    /*private void doCheckTheProductAlreadyPurchased() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("product_id",productsModel.getId());

        new NetworkManager(this).doPost(map,Apis.API_POST_CHECK_PRODUCT_PURCHASED,"REQUEST_CHECK_PRODUCT_PURCHASED",REQUEST_CHECK_PRODUCT_PURCHASED_ID,this);

        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void processJsonProductPurchased(String response) {
        LoadingDialog.cancelLoading();
//        Log.d("responseeeeeeeeeee",response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            boolean error = jsonObject.optBoolean("error");
            if (error){
                Toast.makeText(this, "You didn't purchased this item earlier.", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ProductDetailsActivity.this,ReviewActivity.class);
//                intent.putExtra("pro_id",productsModel.getId());
//                intent.putExtra("myReview",lastReviewModel);
//                startActivity(intent);
            } else {

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(0);

                    ReviewModel reviewModel = new ReviewModel();
                    reviewModel.setId(jsonObject1.optString("id"));
                    reviewModel.setRating(jsonObject1.optString("rating"));
                    reviewModel.setReview(jsonObject1.optString("review"));
                    reviewModel.setProduct_id(jsonObject1.optString("product_id"));
                    reviewModel.setCreate_date(jsonObject1.optString("create_date"));
                    reviewModel.setFirstname(jsonObject1.optString("firstname"));

                    lastReviewModel = reviewModel;

                    Intent intent = new Intent(ProductDetailsActivity.this,ReviewActivity.class);
                    intent.putExtra("pro_id",productsModel.getId());
                    intent.putExtra("myReview",lastReviewModel);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProductDetailsActivity.this, ReviewActivity.class);
                    intent.putExtra("pro_id", productsModel.getId());
                    intent.putExtra("myReview", lastReviewModel);
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void processJsonNotification(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "There is no notifications for you.", Toast.LENGTH_SHORT).show();
            } else {
                notificationModelList.clear();
                JSONArray jsonArray = jsonObject.optJSONArray("data");

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    NotificationModel notificationModel = new NotificationModel();

                    notificationModel.setId(jsonObject1.optString("id"));
                    notificationModel.setDate(jsonObject1.optString("create_date"));
                    notificationModel.setMessage(jsonObject1.optString("messages"));

                    notificationModelList.add(notificationModel);
                }
                LoadingDialog.cancelLoading();
                Intent intent = new Intent(ProductDetailsActivity.this,NotificationActivity.class);
                intent.putExtra("notification_data", (Serializable) notificationModelList);
                startActivity(intent);
                COUNT = "0";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    private void processIsCart(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                productsModel.setIsCart("0");
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray != null && jsonArray.length() >0){
                    mReviewList.clear();
                    for (int i = 0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.setId(jsonObject1.optString("id"));
                        reviewModel.setRating(jsonObject1.optString("rating"));
                        reviewModel.setReview(jsonObject1.optString("review"));
                        reviewModel.setProduct_id(jsonObject1.optString("product_id"));
                        reviewModel.setCreate_date(jsonObject1.optString("create_date"));
                        reviewModel.setFirstname(jsonObject1.optString("firstname"));

                        mReviewList.add(reviewModel);
                    }

                    if (mReviewList.size() == 0 || mReviewList == null){
//                        mReviewLay.setVisibility(View.GONE);
                    } else {
                        Log.d("againnnnnn","yes");
                        mFilteredReviewList.clear();
                        for (int o = 0;o<mReviewList.size();o++){
                            String pro_id = mReviewList.get(o).getProduct_id();
                            if (pro_id.equals(productsModel.getId())){
                                ReviewModel reviewModel = new ReviewModel();
                                reviewModel = mReviewList.get(o);
                                mFilteredReviewList.add(reviewModel);
                            }
                        }
                        mReviewAdapter = new ReviewAdapter(this,mFilteredReviewList);
                        mReviewRecycler.setAdapter(mReviewAdapter);
                    }
                }
            } else {
                LoadingDialog.cancelLoading();
                String isCart = jsonObject.optString("isCart");
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray != null){
                    mReviewList.clear();
                    for (int i = 0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                        ReviewModel reviewModel = new ReviewModel();
                        reviewModel.setId(jsonObject1.optString("id"));
                        reviewModel.setRating(jsonObject1.optString("rating"));
                        reviewModel.setReview(jsonObject1.optString("review"));
                        reviewModel.setProduct_id(jsonObject1.optString("product_id"));
                        reviewModel.setCreate_date(jsonObject1.optString("create_date"));
                        reviewModel.setFirstname(jsonObject1.optString("firstname"));

                        mReviewList.add(reviewModel);
                    }

                    if (mReviewList.size() == 0 || mReviewList == null){
//                        mReviewLay.setVisibility(View.GONE);
                    } else {
                        Log.d("againnnnnn","yes");
                        mFilteredReviewList.clear();
                        for (int o = 0;o<mReviewList.size();o++){
                            String pro_id = mReviewList.get(o).getProduct_id();
                            if (pro_id.equals(productsModel.getId())){
                                ReviewModel reviewModel = new ReviewModel();
                                reviewModel = mReviewList.get(o);
                                mFilteredReviewList.add(reviewModel);
                            }
                        }
                        mReviewAdapter = new ReviewAdapter(this,mFilteredReviewList);
                        mReviewRecycler.setAdapter(mReviewAdapter);
                    }
                }
                if (isCart.equals("1")){
                    productsModel.setIsCart("1");
                } else {
                    productsModel.setIsCart("0");
                }
                setIsCart();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    private void processAddToCart(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                manager.saveCartCount(jsonObject.getInt("count"));
                ActionItemBadge.update(this, mMenu, FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
                Toast.makeText(this, "Product added to the cart", Toast.LENGTH_SHORT).show();
                productsModel.setIsCart("1");
                setIsCart();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNotifications() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map, Apis.API_POST_GET_NOTIFICATIONS,"REQUEST_GET_NOTIFICATION",REQUEST_GET_NOTIFICATION_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    private void setIsCart() {
        if (productsModel.getIsCart().equals("1")){
            addToCart.setText("Go To Cart");
        } else {
            addToCart.setText("Add To Cart");
        }
    }

    private void checkIsCart() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("product_id",productsModel.getId());

        new NetworkManager(this).doPost(map,Apis.API_POST_CHECK_IS_CART,"REQUEST_ISCART",REQUEST_ISCART_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    private void doAddItemToCart() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("product_id",productsModel.getId());
        if (value == 0){
            map.put("quantity", "1");
        } else {
            map.put("quantity", String.valueOf(value));
        }

        new NetworkManager(this).doPost(map,Apis.API_POST_ADD_TO_CART,"REQUEST_ADD_TO_CART",REQUEST_ADD_TO_CART_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    private void loadImagesToArray() {

        strings = new String[] {productsModel.getProduct_image1(),productsModel.getProduct_image2(),productsModel.getProduct_image3(),productsModel.getProduct_image4()};
    }*/
}