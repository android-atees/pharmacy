package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.interfaces.RateCLickListener;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.cart.CartModel;
import in.ateesinfomedia.relief.models.login.CustomerAddress;
import in.ateesinfomedia.relief.models.shipping.ShippingCost;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.adapter.CartAdapter;
import in.ateesinfomedia.relief.view.adapter.ShippingRateAdapter;

import static in.ateesinfomedia.relief.configurations.Global.CustomerAddressModel;
import static in.ateesinfomedia.relief.configurations.Global.ShippingCostModel;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class ShippingCostActivity extends AppCompatActivity implements NetworkCallback, RateCLickListener {

    private Toolbar toolbar;
    private MyPreferenceManager manager;
    private CustomerAddress mAddressModel;
    private int REQUEST_POST_SHIPPING_RATE = 6982;
    private Gson gson = new Gson();
    private static final String TAG = ShippingCostActivity.class.getName();
    private List<ShippingCost> shippingRateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppCompatButton proceedButton;
    private ShippingRateAdapter mAdapter;
    private boolean shipCostBool = false;

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

        setContentView(R.layout.activity_shipping_cost);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        manager = new MyPreferenceManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.estimate_rv);
        proceedButton = (AppCompatButton) findViewById(R.id.proceed_pay_btn);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        mAddressModel = CustomerAddressModel;

        getShippingRates();

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCheckOutPage();
            }
        });
    }

    private void goToCheckOutPage() {
        if (shipCostBool) {
            Intent intent = new Intent(ShippingCostActivity.this, OrderSummaryActivity.class);
            startActivity(intent);
        } else {
            String message = "Please choose a shipping rate.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void getShippingRates() {
        try {
            JSONObject map = new JSONObject();
            JSONObject customerItem = new JSONObject();
            JSONArray streetArray = new JSONArray();

            customerItem.put("region", mAddressModel.getRegion().getRegion());
            customerItem.put("region_id", mAddressModel.getRegion().getRegion_id());
            customerItem.put("region_code", mAddressModel.getRegion().getRegion_code());
            customerItem.put("country_id", mAddressModel.getCountry_id());
            streetArray.put(mAddressModel.getStreet().get(0));
            customerItem.put("street", streetArray);
            //customerItem.put("street", mAddressModel.getStreet());
            customerItem.put("postcode", mAddressModel.getPostcode());
            customerItem.put("city", mAddressModel.getCity());
            customerItem.put("firstname", mAddressModel.getFirstname());
            customerItem.put("lastname", mAddressModel.getLastname());
            customerItem.put("customer_id", manager.getUserUniqueId());
            customerItem.put("email", manager.getUserMail());
            customerItem.put("telephone", mAddressModel.getTelephone());
            customerItem.put("same_as_billing", 1);

            map.put("address",customerItem);
            Log.d("doJSON Request",map.toString());

            String token = manager.getUserToken();

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_SHIPPING_RATE_LIST,
                    ShippingCost[].class,
                    map,
                    token,
                    "TAG_POST_SHIPPING_RATE",
                    REQUEST_POST_SHIPPING_RATE,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ProcessJsonShippingRate(String response) {

        try {
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<ArrayList<ShippingCost>>(){}.getType();
                ArrayList<ShippingCost> shippingRateResponseList = gson.fromJson(response, type);
                if (shippingRateResponseList != null && !shippingRateResponseList.isEmpty()) {
                    LoadingDialog.cancelLoading();
                    shippingRateList.clear();
                    shippingRateList = shippingRateResponseList;
                    mAdapter = new ShippingRateAdapter(this, shippingRateList,this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(this, "Oops something went wrong");
                }
            } else {
                serverErrorDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            serverErrorDialog();
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
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_POST_SHIPPING_RATE){
                ProcessJsonShippingRate(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
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
    public void itemClicked(int position, ShippingCost shipCost) {
        shipCostBool = true;
        ShippingCostModel = shipCost;
    }
}