package in.ateesinfomedia.relief.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.NotificationModel;
import in.ateesinfomedia.relief.models.ProductsModel;
import in.ateesinfomedia.relief.view.adapter.ProductsAdapter;
import in.ateesinfomedia.relief.widgets.searchview.MaterialSearchView;

import static in.ateesinfomedia.relief.configurations.Global.COUNT;
import static in.ateesinfomedia.relief.configurations.Global.ProductList;

public class ProductsActivity extends AppCompatActivity implements AdapterClickListner, NetworkCallback {

    private List<ProductsModel> mProductsList = new ArrayList<>();
    private List<ProductsModel> filteredProductsList = new ArrayList<>();
    private RecyclerView mPProductRecycler;
    private ProductsAdapter mAdapter;
    private String title;
    private Toolbar toolbar;
    private MenuItem mMenu;
    private MenuItem mMenu1;
    private MyPreferenceManager manager;
    private int REQUEST_GET_NOTIFICATION_ID = 8111;
    private List<NotificationModel> notificationModelList = new ArrayList<>();
    private MaterialSearchView mSearchView;
    private MenuItem mMenu2;

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
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new MyPreferenceManager(this);
        mPProductRecycler = (RecyclerView) findViewById(R.id.productsRecycler);

        mProductsList.clear();
//        mProductsList = (List<ProductsModel>) getIntent().getSerializableExtra("myList");
        mProductsList = ProductList;
        title = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(title);

        mSearchView.setHint("search by product name");
        mSearchView.setCloseIcon(getResources().getDrawable(R.drawable.ic_action_navigation_close));
        mSearchView.hideTintView();

        mPProductRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mPProductRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ProductsAdapter(this,mProductsList,this);
        mPProductRecycler.setAdapter(mAdapter);

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
//        getMenuInflater().inflate(R.menu.cart_menu,menu);
//        return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        this.mMenu = menu.findItem(R.id.action_addcart);
        this.mMenu1 = menu.findItem(R.id.action_notification);
        this.mMenu2 = menu.findItem(R.id.action_search);

        if (manager.getCartCount() == 0){
            ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
        } else {
            ActionItemBadge.update(this, menu.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
        }
//        ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
        if (COUNT.equals("0") || COUNT.isEmpty() || COUNT.equals("null") ){
            ActionItemBadge.hide(menu.findItem(R.id.action_notification));
        } else {
            ActionItemBadge.update(this, menu.findItem(R.id.action_notification), FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
        }

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

            case R.id.action_addcart:

                Intent intent = new Intent(ProductsActivity.this,CartActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_notification:

                getNotifications();
//                Intent intent1 = new Intent(MainActivity.this,NotificationActivity.class);
//                startActivity(intent1);

                return true;
            case R.id.action_search:
                mSearchView.showSearch(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filter(String text){
        List<ProductsModel> temp = new ArrayList();
        if (text.length() != 0){
            for(ProductsModel d: mProductsList){
                //or use .contains(text)
                if(Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(d.getProduct_name()).find()){
                    temp.add(d);
                }
            }
        }else{
            temp = filteredProductsList;
        }
        //update recyclerview
        updateList(temp);
    }

    public void updateList(List<ProductsModel> list) {
        filteredProductsList = list;

        if (filteredProductsList.size() < 1 || filteredProductsList == null){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();
            mAdapter = new ProductsAdapter(this,mProductsList,this);
            mPProductRecycler.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

//            mTvRestaurantCount.setText("About "+String.valueOf(restaurantsList.size())+" restaurant listed");
        } else {

//        getView().showToast(Toast.makeText(getAppContext(),mRestaurantList.get(0).getRestaurantName(),Toast.LENGTH_SHORT));
            mAdapter = new ProductsAdapter(this,filteredProductsList,this);
            mPProductRecycler.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

//            mTvRestaurantCount.setText("About "+String.valueOf(filteredRestaurantsList.size())+" restaurant listed");
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

        ProductsModel productsModel = (ProductsModel) object;

        Intent intent = new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("details",productsModel);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (manager.isLogin()){
        if (manager.getCartCount()> 0) {
            ActionItemBadge.update(this, mMenu, FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
        } /*else {
                ActionItemBadge.hide(mMenu);
            }*/

        if (COUNT.isEmpty() || COUNT.equals("null") || COUNT.equals("0")){
//            ActionItemBadge.hide(mMenu1);
        } else {
                ActionItemBadge.update(this, mMenu1, FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
            }
//        }
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_GET_NOTIFICATION_ID){
                processJsonNotification(response);
            }
        } else {
            LoadingDialog.cancelLoading();
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
}