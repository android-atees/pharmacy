package in.ateesinfomedia.remedio.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.CategoryModel;
import in.ateesinfomedia.remedio.models.ProductsModel;
import in.ateesinfomedia.remedio.models.ReviewModel;
import in.ateesinfomedia.remedio.models.ReviewsModel;
import in.ateesinfomedia.remedio.models.SubCategoryModel;
import in.ateesinfomedia.remedio.view.adapter.SubCategoryAdapter;
import in.ateesinfomedia.remedio.widgets.searchview.MaterialSearchView;

import static in.ateesinfomedia.remedio.configurations.Global.COUNT;
import static in.ateesinfomedia.remedio.configurations.Global.ProductList;
import static in.ateesinfomedia.remedio.configurations.Global.ReviewList;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NetworkCallback, AdapterClickListner {

    private RecyclerView subCategoryrecyclerView;
    private Spinner categoryDropDown;
    private SubCategoryAdapter mAdapter;
    private List<SubCategoryModel> subCategoryModelArrayList = new ArrayList<>();
    private List<CategoryModel> categoryModelArrayList = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private HashMap<String,String> map = new HashMap<>();
    private String cateId;
    private int REQUEST_SUBCATEGORY_ID = 8907;
    private List<SubCategoryModel> mSubCateList = new ArrayList<>();
    private List<SubCategoryModel> filteredSubCatList = new ArrayList<>();
    private SubCategoryModel subCategoryModel;
    private int REQUEST_CATEGORY_PRODUCTS_ID = 7123;
    private List<ProductsModel> mProductList = new ArrayList<>();
    private List<ReviewModel> mReviewList = new ArrayList<>();
    private Toolbar toolbar;
    private Menu menu;
    private MenuItem mMenu;
    private MaterialSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //must be called before setContentView(...)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            Drawable background = this.getResources().getDrawable(R.drawable.ab_gradient);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }

        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchView = (MaterialSearchView) findViewById(R.id.sv_material);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subCategoryrecyclerView = (RecyclerView) findViewById(R.id.subCategoryRecycler);
        categoryDropDown = (Spinner) findViewById(R.id.categoriesDrop);

        categoryModelArrayList = (List<CategoryModel>) getIntent().getSerializableExtra("list");

        mSearchView.setHint("search by category name");
        mSearchView.setCloseIcon(getResources().getDrawable(R.drawable.ic_action_navigation_close));
        mSearchView.hideTintView();

        loadCategories();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categoryDropDown.setAdapter(dataAdapter);

        subCategoryrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        subCategoryrecyclerView.setItemAnimator(new DefaultItemAnimator());

//        Toast.makeText(this, "id  "+categoryModelArrayList.get(0).getId(), Toast.LENGTH_SHORT).show();
        getSubCategoies(categoryModelArrayList.get(0).getId());

        categoryDropDown.setOnItemSelectedListener(this);

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchView.hideTintView();
                filter(newText);
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mSearchView.hideTintView();
                mSearchView.setHint("search by category name");
            }

            @Override
            public void onSearchViewClosed() {
                mSearchView.setAdapter(null);
//                mAdapter = new RestaurantListAdapter(getActivity(), restaurantsList, RestaurantFragment.this,"main");
//                mRestaurantRecycler.setAdapter(mAdapter);

                mAdapter = new SubCategoryAdapter(CategoryActivity.this,mSubCateList,CategoryActivity.this);
                subCategoryrecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    public void filter(String text){
        List<SubCategoryModel> temp = new ArrayList();
        if (text.length() != 0){
            for(SubCategoryModel d: mSubCateList){
                //or use .contains(text)
                if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(d.getName()).find()
                        || Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(d.getName()).find()){
                    temp.add(d);
                }
            }
        }else{
            temp = filteredSubCatList;
        }
        //update recyclerview
        updateList(temp);
    }

    public void updateList(List<SubCategoryModel> list) {
        filteredSubCatList = list;

        if (filteredSubCatList.size() < 1 || filteredSubCatList == null){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();
            mAdapter = new SubCategoryAdapter(CategoryActivity.this,mSubCateList,CategoryActivity.this);
            subCategoryrecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

//            mTvRestaurantCount.setText("About "+String.valueOf(restaurantsList.size())+" restaurant listed");
        } else {

//        getView().showToast(Toast.makeText(getAppContext(),mRestaurantList.get(0).getRestaurantName(),Toast.LENGTH_SHORT));
            mAdapter = new SubCategoryAdapter(CategoryActivity.this,filteredSubCatList,CategoryActivity.this);
            subCategoryrecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

//            mTvRestaurantCount.setText("About "+String.valueOf(filteredRestaurantsList.size())+" restaurant listed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cart_menu,menu);
//        return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        this.menu = menu;
        this.mMenu = menu.findItem(R.id.action_search);
        menu.findItem(R.id.action_addcart).setVisible(false);
        menu.findItem(R.id.action_notification).setVisible(false);

//        if (manager.getCartCount() == 0){
//            ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
//        } else {
//            ActionItemBadge.update(this, menu.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
//        }
////        ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
//        if (COUNT.equals("0") || COUNT.isEmpty()){
//            ActionItemBadge.hide(menu.findItem(R.id.action_notification));
//        } else {
//            ActionItemBadge.update(this, menu.findItem(R.id.action_notification), FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
//        }

//        if (manager.isLogin()){
//            if (manager.getCartCount()> 0) {
//                ActionItemBadge.update(this, menu.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.GREY, manager.getCartCount());
//            } else {
//                ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
//            }
//
//        } else {
//            ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
//        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                mSearchView.showSearch(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCategories() {
        categories.clear();
        map.clear();
        for (int i = 0;i<categoryModelArrayList.size();i++){
            categories.add(categoryModelArrayList.get(i).getName());
            map.put(categoryModelArrayList.get(i).getName(),categoryModelArrayList.get(i).getId());
        }
    }

    private void getSubCategoies(String id) {
        Map<String,String> stringStringMap = new HashMap<>();
        stringStringMap.put("category_id",id);

        new NetworkManager(this).doPost(stringStringMap, Apis.API_POST_GET_SUBCATEGORIES,"REQUEST_SUBCATEGORY",REQUEST_SUBCATEGORY_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        getSelectedItemId(item);
    }

    private void getSelectedItemId(String item) {
        cateId = map.get(item);
        getSubCategoies(cateId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_SUBCATEGORY_ID){
                processSubCategory(response);
            } else if (requestId == REQUEST_CATEGORY_PRODUCTS_ID){
                processingSubCateProduct(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processingSubCateProduct(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "No products available now!!..", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                mProductList.clear();
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    ProductsModel productsModel = new ProductsModel();

                    productsModel.setId(jsonObject1.optString("id"));
                    productsModel.setBrand_id(jsonObject1.optString("brand_id"));
                    productsModel.setProduct_name(jsonObject1.optString("product_name"));
                    productsModel.setProduct_description(jsonObject1.optString("product_description"));
                    productsModel.setProduct_highlights(jsonObject1.optString("product_highlights"));
                    productsModel.setProduct_quantity(jsonObject1.optString("product_quantity"));
                    productsModel.setProduct_price(jsonObject1.optString("product_price"));
                    productsModel.setProduct_offer_price(jsonObject1.optString("product_offer_price"));
                    productsModel.setProduct_shipping_price(jsonObject1.optString("product_shipping_price"));
                    productsModel.setProduct_sku(jsonObject1.optString("product_sku"));
                    productsModel.setProduct_gst(jsonObject1.optString("product_gst"));
                    productsModel.setProduct_hsn(jsonObject1.optString("product_hsn"));
                    productsModel.setProduct_priority_points(jsonObject1.optString("product_priority_points"));
                    productsModel.setProduct_meta_tags(jsonObject1.optString("product_meta_tags"));
                    productsModel.setProduct_meta_description(jsonObject1.optString("product_meta_description"));
                    productsModel.setProduct_meta_keywords(jsonObject1.optString("product_meta_keywords"));
                    productsModel.setProduct_image1(jsonObject1.optString("product_image1"));
                    productsModel.setProduct_image2(jsonObject1.optString("product_image2"));
                    productsModel.setProduct_image3(jsonObject1.optString("product_image3"));
                    productsModel.setProduct_image4(jsonObject1.optString("product_image4"));
                    productsModel.setCreate_date(jsonObject1.optString("create_date"));
                    productsModel.setRecentlyadded(jsonObject1.optString("recentlyadded"));
                    productsModel.setTopsellted(jsonObject1.optString("topsellted"));
                    productsModel.setToprated(jsonObject1.optString("toprated"));
                    productsModel.setPopular(jsonObject1.optString("popular"));
                    productsModel.setProduct_total(jsonObject1.optString("product_total"));
                    productsModel.setProduct_total_offer(jsonObject1.optString("product_total_offer"));
                    productsModel.setGst_amount(jsonObject1.optString("gst_amount"));
                    productsModel.setSale_count(jsonObject1.optString("sale_count"));
                    productsModel.setView_count(jsonObject1.optString("view_count"));
                    productsModel.setOffer(jsonObject1.optString("offer"));
                    productsModel.setIsCart(jsonObject1.optString("isCart"));

                    JSONArray jsonArray1 = jsonObject1.optJSONArray("reviews");
                    if (jsonArray1 != null){
                        for (int m = 0;m<jsonArray1.length();m++) {
                            JSONObject jsonObject2 = jsonArray1.optJSONObject(m);

                            ReviewModel reviewModel = new ReviewModel();

                            reviewModel.setId(jsonObject2.optString("id"));
                            reviewModel.setRating(jsonObject2.optString("rating"));
                            reviewModel.setReview(jsonObject2.optString("review"));
                            reviewModel.setProduct_id(jsonObject2.optString("product_id"));
                            reviewModel.setCreate_date(jsonObject2.optString("create_date"));
                            reviewModel.setFirstname(jsonObject2.optString("firstname"));

                            mReviewList.add(reviewModel);
                        }
                        ReviewList = mReviewList;
                    }

                    mProductList.add(productsModel);
                }
                ProductList = mProductList;
                LoadingDialog.cancelLoading();
                Intent intent = new Intent(this,ProductsActivity.class);
//                intent.putExtra("myList", (Serializable) mProductList);
                intent.putExtra("name", subCategoryModel.getName());
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processSubCategory(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                mSubCateList.clear();
                for (int i = 0;i<jsonArray.length();i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    SubCategoryModel subCategoryModel = new SubCategoryModel();
                    subCategoryModel.setId(jsonObject1.optString("id"));
                    subCategoryModel.setName(jsonObject1.optString("sub_category_1_name"));
                    subCategoryModel.setDate(jsonObject1.optString("create_date"));
                    subCategoryModel.setImage(jsonObject1.optString("image"));

                    mSubCateList.add(subCategoryModel);
                }

                LoadingDialog.cancelLoading();
                mAdapter = new SubCategoryAdapter(this,mSubCateList,this);
                subCategoryrecyclerView.setAdapter(mAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void itemClicked(int position, Object object) {
        subCategoryModel = (SubCategoryModel) object;
        doGetProducts();
    }

    private void doGetProducts() {
        Map<String,String> map = new HashMap<>();
        map.put("subcategory",subCategoryModel.getId());

        new NetworkManager(this).doPost(map,Apis.API_POST_GET_SUBCATEGORIES_PRODUCTS,"REQUEST_CATEGORY_PRODUCTS",REQUEST_CATEGORY_PRODUCTS_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
