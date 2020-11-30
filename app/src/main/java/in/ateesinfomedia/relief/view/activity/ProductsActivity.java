package in.ateesinfomedia.relief.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.NotificationModel;
import in.ateesinfomedia.relief.models.ProductsModel;
import in.ateesinfomedia.relief.models.cart.CartCountResponse;
import in.ateesinfomedia.relief.models.cart.CartModel;
import in.ateesinfomedia.relief.models.detailpro.ConfigProductDetailResponse;
import in.ateesinfomedia.relief.models.detailpro.ProductDetail;
import in.ateesinfomedia.relief.models.products.ProductModel;
import in.ateesinfomedia.relief.models.products.ProductResponse;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.adapter.ProductsAdapter;
import in.ateesinfomedia.relief.view.fragment.FilterFragment;
import in.ateesinfomedia.relief.widgets.searchview.MaterialSearchView;

import static in.ateesinfomedia.relief.configurations.Global.COUNT;
import static in.ateesinfomedia.relief.configurations.Global.CatId;
import static in.ateesinfomedia.relief.configurations.Global.CatProductCount;
import static in.ateesinfomedia.relief.configurations.Global.CatProductList;
import static in.ateesinfomedia.relief.configurations.Global.DefaultPageSize;
import static in.ateesinfomedia.relief.configurations.Global.MaxPriceFilterLimit;
import static in.ateesinfomedia.relief.configurations.Global.ProductDetailModel;
import static in.ateesinfomedia.relief.configurations.Global.ProductList;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class ProductsActivity extends AppCompatActivity implements AdapterClickListner,
        NetworkCallback, FilterFragment.OnFilterListener {

    private List<ProductModel> mProductsList = new ArrayList<>();
    private List<ProductModel> filteredProductsList = new ArrayList<>();
    private RecyclerView mPProductRecycler;
    private ProductsAdapter mAdapter;
    private String title;
    private String sku;
    private Toolbar toolbar;
    private MenuItem mMenu;
    private MenuItem mMenu1;
    private MyPreferenceManager manager;
    private int REQUEST_GET_NOTIFICATION_ID = 8111;
    private List<NotificationModel> notificationModelList = new ArrayList<>();
    private MaterialSearchView mSearchView;
    private MenuItem mMenu2;
    private int REQUEST_SIMPLE_PRODUCT = 9895;
    private int REQUEST_CONFIG_PRODUCT = 9896;
    private int REQUEST_CREATE_CART_ID = 9897;
    private int REQUEST_ADD_CART_SIMPLE = 9898;
    private final int REQUEST_PRODUCTS = 4899;
    private static final String TAG = ProductsActivity.class.getName();
    private Gson gson = new Gson();
    private List<ProductModel> mCatProductList = new ArrayList<>();
    private GridLayoutManager layoutManager;
    private int page_number = 1;
    private boolean isLoading = true;
    private int pastVisibleItems = 0, visibleItemCount = 0, totalItemCount = 0, previousTotal = 0;
    private int view_threshold = 10;
    private int productTotalCount = 0;
    private SearchView productSearchView;
    private boolean isSearchBool = false;
    private String searchText = "";
    private int REQUEST_CART_COUNT = 9117;
    private Menu menuCart;
    private MenuItem mMenu3;
    private int priceFrom = 0, priceTo = MaxPriceFilterLimit;

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

        setContentView(R.layout.activity_products);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchView = (MaterialSearchView) findViewById(R.id.sv_material);
        productSearchView = findViewById(R.id.product_searchView);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new MyPreferenceManager(this);
        mPProductRecycler = (RecyclerView) findViewById(R.id.productsRecycler);

        mProductsList.clear();
//        mProductsList = (List<ProductsModel>) getIntent().getSerializableExtra("myList");
        //mProductsList = ProductList;
        mProductsList = CatProductList;
        productTotalCount = CatProductCount;
        title = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(title);

        mSearchView.setHint("search by product name");
        mSearchView.setCloseIcon(getResources().getDrawable(R.drawable.ic_action_navigation_close));
        mSearchView.hideTintView();

        layoutManager = new GridLayoutManager(this, 2);
        mPProductRecycler.setLayoutManager(layoutManager);
        mPProductRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ProductsAdapter(this,mProductsList,this);
        mPProductRecycler.setAdapter(mAdapter);

        mPProductRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + view_threshold)) {
                        if (productTotalCount > totalItemCount) {
                            page_number++;
                            isLoading = true;
                            if (isSearchBool) {
                                performSearch();
                            } else {
                                performPagination();
                            }
                        }
                    }
                }
            }
        });

        productSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    isSearchBool = true;
                    isLoading = true;
                    page_number = 1;
                    searchText = query;
                    performSearch();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
                mSearchView.setHint("search by product name");
            }

            @Override
            public void onSearchViewClosed() {
                mSearchView.setAdapter(null);
//                mAdapter = new RestaurantListAdapter(getActivity(), restaurantsList, RestaurantFragment.this,"main");
//                mRestaurantRecycler.setAdapter(mAdapter);

                mAdapter = new ProductsAdapter(ProductsActivity.this,mProductsList,ProductsActivity.this);
                mPProductRecycler.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        this.mMenu = menu.findItem(R.id.action_addcart);
        this.mMenu1 = menu.findItem(R.id.action_notification);
        this.mMenu2 = menu.findItem(R.id.action_search);
        this.mMenu3 = menu.findItem(R.id.action_filter);

        menuCart = menu;

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

                Intent intent = new Intent(ProductsActivity.this,CartActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_notification:

                getNotifications();

                return true;
            case R.id.action_search:

                mSearchView.showSearch(true);

                return true;
            case R.id.action_filter:

                showFilterDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        FilterFragment dialog = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("from", priceFrom);
        bundle.putInt("to", priceTo);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "MyFilterDialog");
    }

    public void filter(String text){
        List<ProductModel> temp = new ArrayList();
        if (text.length() != 0){
            for(ProductModel d: mProductsList){
                //or use .contains(text)
                if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(d.getName()).find()){
                    temp.add(d);
                }
            }
        }else{
            temp = filteredProductsList;
        }
        //update recyclerview
        updateList(temp);
    }

    public void updateList(List<ProductModel> list) {
        filteredProductsList = list;

        if (filteredProductsList.size() < 1 || filteredProductsList == null){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();
            mAdapter = new ProductsAdapter(this,mProductsList,this);
            mPProductRecycler.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new ProductsAdapter(this,filteredProductsList,this);
            mPProductRecycler.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void getNotifications() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map, Apis.API_POST_GET_NOTIFICATIONS,"REQUEST_GET_NOTIFICATION",REQUEST_GET_NOTIFICATION_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");

    }

    @Override
    public void itemClicked(int position, Object object) {

        ProductModel productModel = (ProductModel) object;
        if (productModel.getType_id() != null &&
                productModel.getType_id().equals("configurable")) {
            getConfigProductDetail(productModel.getSku());
        } else {
            getSimpleProductDetail(productModel.getSku());
        }
    }

    @Override
    public void buttonClicked(int position, Object object) {
        ProductModel productModel = (ProductModel) object;
        if (productModel.getType_id() != null &&
                productModel.getType_id().equals("configurable")) {
            sku = productModel.getSku();
            getConfigProductDetail(productModel.getSku());
        } else {
            sku = productModel.getSku();
            postCreateCartId();
        }
    }

    private void goToProductDetail(ProductModel productsModel) {
        Intent intent = new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("details",productsModel);
        startActivity(intent);
    }

    private void performSearch() {
        LoadingDialog.showLoadingDialog(this,"Loading...");

        String getUrl = Apis.API_GET_SEARCH_PRODUCTS +
                "?searchCriteria[filter_groups][0][filters][0][field]=category_id" +
                "&searchCriteria[filter_groups][0][filters][0][value]="+ CatId +
                "&searchCriteria[filter_groups][0][filters][0][field]=name" +
                "&searchCriteria[filter_groups][0][filters][0][value]=%25" + searchText + "%25" +
                "&searchCriteria[filter_groups][0][filters][0][condition_type]=like" +
                "&fields=total_count,items[sku,name,price,status,visibility,type_id,media_gallery_entries]" +
                "&searchCriteria[pageSize]=" + DefaultPageSize +"&searchCriteria[currentPage]=" + page_number +
                "&searchCriteria[filter_groups][1][filters][0][field]=price" +
                "&searchCriteria[filter_groups][1][filters][0][value]=" + priceFrom +
                "&searchCriteria[filter_groups][1][filters][0][condition_type]=from" +
                "&searchCriteria[filter_groups][2][filters][0][field]=price" +
                "&searchCriteria[filter_groups][2][filters][0][value]=" + priceTo +
                "&searchCriteria[filter_groups][2][filters][0][condition_type]=to";

        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                ProductResponse.class,
                null,
                Apis.ACCESS_TOKEN,
                "TAG_PRODUCTS_1",
                REQUEST_PRODUCTS,
                this
        );

    }

    private void performPagination() {
        LoadingDialog.showLoadingDialog(this,"Loading...");

        String getUrl = Apis.API_GET_SEARCH_PRODUCTS +
                "?searchCriteria[filter_groups][0][filters][0][field]=category_id" +
                "&searchCriteria[filter_groups][0][filters][0][value]="+ CatId +
                "&fields=total_count,items[sku,name,price,status,visibility,type_id,media_gallery_entries]" +
                "&searchCriteria[pageSize]=" + DefaultPageSize +"&searchCriteria[currentPage]=" + page_number +
                "&searchCriteria[filter_groups][1][filters][0][field]=price" +
                "&searchCriteria[filter_groups][1][filters][0][value]=" + priceFrom +
                "&searchCriteria[filter_groups][1][filters][0][condition_type]=from" +
                "&searchCriteria[filter_groups][2][filters][0][field]=price" +
                "&searchCriteria[filter_groups][2][filters][0][value]=" + priceTo +
                "&searchCriteria[filter_groups][2][filters][0][condition_type]=to";

        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                ProductResponse.class,
                null,
                Apis.ACCESS_TOKEN,
                "TAG_PRODUCTS_1",
                REQUEST_PRODUCTS,
                this
        );

    }

    private void processGetProducts(String response) {
        if(response == null){
            Log.d(TAG, "processJson: Cant get Category");
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        } else {
            try {

                mCatProductList.clear();
                Type type = new TypeToken<ProductResponse>(){}.getType();
                ProductResponse pResponse = gson.fromJson(response, type);

                if (pResponse.getItems() != null && !pResponse.getItems().isEmpty()) {
                    mCatProductList = pResponse.getItems();
                } else {
                    mAdapter.clearProducts(mCatProductList);
                }

                if (pResponse.getTotal_count() != null) {
                    try {
                        CatProductCount = Integer.parseInt(pResponse.getTotal_count());
                        productTotalCount = CatProductCount;
                    } catch (Exception e) {
                        CatProductCount = 0;
                        productTotalCount = CatProductCount;
                    }
                } else {
                    CatProductCount = 0;
                    productTotalCount = CatProductCount;
                }

                if (!mCatProductList.isEmpty()) {
                    if (page_number == 1) {
                        mAdapter.clearProducts(mCatProductList);
                    } else {
                        mAdapter.addProducts(mCatProductList);
                    }
                } else {
                    Toast.makeText(this,
                            "Oops no products are available",
                            Toast.LENGTH_SHORT).show();
                }

                LoadingDialog.cancelLoading();



            } catch (Exception e) {
                e.printStackTrace();
                LoadingDialog.cancelLoading();
                dialogWarning(this, "Sorry ! Can't connect to server, try later");
            }
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

    private void getSimpleProductDetail(String sku) {
        try {
            String query = URLEncoder.encode(sku, "utf-8");
            String getUrl = Apis.API_GET_SIMPLE_PRODUCT_DETAIL + query;

            new NetworkManager(this).doGetCustom(
                    null,
                    getUrl,
                    ProductDetail.class,
                    null,
                    Apis.ACCESS_TOKEN,
                    "TAG_PRODUCT_SIMPLE_DETAIL",
                    REQUEST_SIMPLE_PRODUCT,
                    this
            );

            LoadingDialog.showLoadingDialog(this,"Loading...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getConfigProductDetail(String sku) {
        try {
            String query = URLEncoder.encode(sku, "utf-8");
            String getUrl = Apis.API_GET_CONFIG_PRODUCT_DETAIL + query;

            new NetworkManager(this).doGetCustom(
                    null,
                    getUrl,
                    ConfigProductDetailResponse.class,
                    null,
                    Apis.ACCESS_TOKEN,
                    "TAG_PRODUCT_CONFIG_DETAIL",
                    REQUEST_CONFIG_PRODUCT,
                    this
            );

            LoadingDialog.showLoadingDialog(this,"Loading...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postCreateCartId() {
        try {
            String token =manager.getUserToken();

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_CREATE_CART_ID,
                    Object.class,
                    null,
                    token,
                    "TAG_CREATE_CART_ID",
                    REQUEST_CREATE_CART_ID,
                    this
            );

            LoadingDialog.showLoadingDialog(this,"Loading...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postAddSimpleProduct(String quote_id) {
        try {
            JSONObject map = new JSONObject();
            JSONObject cartItem = new JSONObject();
            try {
                cartItem.put("sku", sku);
                cartItem.put("qty", "1");
                cartItem.put("quote_id", quote_id);
                map.put("cartItem",cartItem);
                Log.d("doJSON Request",map.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_ADD_CART_SIMPLE,
                    Object.class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_ADD_CART_SIMPLE",
                    REQUEST_ADD_CART_SIMPLE,
                    this
            );

        } catch (Exception e) {
            e.printStackTrace();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_GET_NOTIFICATION_ID){
                processJsonNotification(response);
            } else if (requestId == REQUEST_SIMPLE_PRODUCT) {
                processJsonSimpleProductDetail(response);
            } else if (requestId == REQUEST_CONFIG_PRODUCT) {
                processJsonConfigProductDetail(response);
            } else if (requestId == REQUEST_CREATE_CART_ID) {
                processJsonCreateCartId(response);
            } else if (requestId == REQUEST_ADD_CART_SIMPLE) {
                processJsonAddCartSimple(response);
            } else if(requestId == REQUEST_PRODUCTS){
                processGetProducts(response);
            } else if (requestId == REQUEST_CART_COUNT) {
                processJsonCartCount(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
        }
    }

    private void processJsonCreateCartId(String response) {
        Log.d(TAG, response);
        String quoteId = response.replace("\"","");
        manager.saveCartId(quoteId);
        postAddSimpleProduct(quoteId);
    }

    private void processJsonAddCartSimple(String response) {
        Log.d(TAG, response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String pName = jsonObject.optString("name");
            if (pName != null && !pName.equals("") && !pName.isEmpty()) {
                //Log.e(TAG, "fxhfxhx-" + pName + "-fxhfxhx");
                Toast.makeText(this, "" + pName + " added to cart successfully", Toast.LENGTH_SHORT).show();

                getCartCount();
            } else {
                String message = jsonObject.optString("message");
                LoadingDialog.cancelLoading();
                dialogWarning(ProductsActivity.this, "" + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processJsonSimpleProductDetail(String response) {
        Log.d(TAG, response);

        try {
            Type type = new TypeToken<ProductDetail>(){}.getType();
            ProductDetail simpleProductDetail = gson.fromJson(response, type);

            if (simpleProductDetail != null) {

                ProductDetailModel = simpleProductDetail;

                Intent intent = new Intent(this,ProductDetailsActivity.class);
                startActivity(intent);

            }

            LoadingDialog.cancelLoading();

        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(ProductsActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonConfigProductDetail(String response) {
        Log.d(TAG, response);

        try {
            Type type = new TypeToken<ConfigProductDetailResponse>(){}.getType();
            ConfigProductDetailResponse configProductResponse = gson.fromJson(response, type);

            ProductDetail simpleProductDetail = configProductResponse.getData();

            if (simpleProductDetail != null) {

                ProductDetailModel = simpleProductDetail;

                Intent intent = new Intent(this,ProductDetailsActivity.class);
                startActivity(intent);

            }

            LoadingDialog.cancelLoading();

        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(ProductsActivity.this, "Sorry ! Can't connect to server, try later");
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
                Intent intent = new Intent(ProductsActivity.this,NotificationActivity.class);
                intent.putExtra("notification_data", (Serializable) notificationModelList);
                startActivity(intent);
                COUNT = "0";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onBackPressed() {
        if (isSearchBool) {
            isSearchBool = false;
            page_number = 1;
            productSearchView.setQuery("", false);
            priceFrom = 0;
            priceTo = MaxPriceFilterLimit;
            isLoading = true;
            performPagination();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateFilter(int lower, int upper) {
        priceFrom = lower;
        priceTo = upper;
        page_number = 1;
        isLoading = true;
        if (isSearchBool) {
            performSearch();
        } else {
            performPagination();
        }
    }

}