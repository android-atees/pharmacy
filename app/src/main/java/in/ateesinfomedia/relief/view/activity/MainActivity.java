package in.ateesinfomedia.relief.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
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

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.NotificationModel;
import in.ateesinfomedia.relief.view.fragment.HomeFragment;
import in.ateesinfomedia.relief.view.fragment.MoreFragment;
import in.ateesinfomedia.relief.view.fragment.OfferFragment;
import in.ateesinfomedia.relief.view.fragment.ProfileFragment;

import static in.ateesinfomedia.relief.configurations.Global.COUNT;

public class MainActivity extends AppCompatActivity implements NetworkCallback {

    private Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;
    private boolean isBack = true;
    private MyPreferenceManager manager;
    private int REQUEST_GET_NOTIFICATION_ID = 5674;
    List<NotificationModel> notificationModelList = new ArrayList<>();
    private MenuItem mMenu;
    private MenuItem mMenu1;
    private Menu menu;

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

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager =new MyPreferenceManager(this);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setupBottomBar();
        showContent();

        String info = getIntent().getStringExtra("info");
        if (info.equals("payment")){
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cart_menu,menu);
//        return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        this.menu = menu;
        this.mMenu = menu.findItem(R.id.action_addcart);
        this.mMenu1 = menu.findItem(R.id.action_notification);

        if (manager.getCartCount() == 0){
            ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
        } else {
            ActionItemBadge.update(this, menu.findItem(R.id.action_addcart), FontAwesome.Icon.faw_cart_arrow_down, ActionItemBadge.BadgeStyles.RED, manager.getCartCount());
        }
//        ActionItemBadge.hide(menu.findItem(R.id.action_addcart));
        if (COUNT.equals("0") || COUNT.isEmpty()){
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

                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_notification:

                getNotifications();
//                Intent intent1 = new Intent(MainActivity.this,NotificationActivity.class);
//                startActivity(intent1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNotifications() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map, Apis.API_POST_GET_NOTIFICATIONS,"REQUEST_GET_NOTIFICATION",REQUEST_GET_NOTIFICATION_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");

    }

    private void showContent() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, HomeFragment.getInstance()).commit();
        isBack = true;
    }

    private void setupBottomBar() {

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home, R.color.bottomBarDefaultTextColor);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.profile, R.drawable.ic_profile, R.color.bottomBarDefaultTextColor);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.offer, R.drawable.ic_bottom_offer, R.color.bottomBarDefaultTextColor);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.more, R.drawable.ic_bottom_more, R.color.bottomBarDefaultTextColor);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.white));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimary));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.bottomBarDefaultTextColor));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(false);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(false);

// Set current item programmatically
//        bottomNavigation.setCurrentItem(1);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,HomeFragment.getInstance(),"NEWS_FRAGMENT").commit();
                    setTitle("Home");
                    isBack = true;
                }/* else if (position == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, UploadFragment.getInstance()).commit();
                    setTitle("Upload");
                    isBack = false;
                } */else if (position == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, ProfileFragment.getInstance()).commit();
                    setTitle("Profile");
                    isBack = false;
                } else if (position == 3){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, MoreFragment.getInstance()).commit();
                    setTitle("More");
                    isBack = false;
                }
                else if (position == 2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, OfferFragment.getInstance()).commit();
                    setTitle("Offer");
                    isBack = false;
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.lightRed));

    }

    public void gotoTab(int i) {
        if (i == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,HomeFragment.getInstance(),"HOME_FRAGMENT").commit();
            setTitle("Home");
            isBack = true;
            bottomNavigation.setCurrentItem(0);
        }/* else if (i == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,UploadFragment.getInstance(),"UPLOAD_FRAGMENT").commit();
            setTitle("Upload");
            isBack = false;
            bottomNavigation.setCurrentItem(1);
        }*/ else if (i == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,ProfileFragment.getInstance(),"PROFILE_FRAGMENT").commit();
            setTitle("Profile");
            isBack = false;
            bottomNavigation.setCurrentItem(1);
        } else if (i == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,OfferFragment.getInstance(),"OFFER_FRAGMENT").commit();
            setTitle("Offers");
            isBack = false;
            bottomNavigation.setCurrentItem(2);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBack){
            super.onBackPressed();

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, HomeFragment.getInstance()).commit();
            bottomNavigation.setCurrentItem(0);
            isBack = true;
        }
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
//                ActionItemBadge.update(this, mMenu1, FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
//                ActionItemBadge.hide(mMenu1);
            } else {
                ActionItemBadge.update(this, mMenu1, FontAwesome.Icon.faw_bell2, ActionItemBadge.BadgeStyles.RED, Integer.valueOf(COUNT));
            }
//        }
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
                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
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