package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.cart.CartResponse;
import in.ateesinfomedia.relief.models.login.CustomerAddress;
import in.ateesinfomedia.relief.models.shipping.ShippingCost;
import in.ateesinfomedia.relief.models.shipping.ShippingInfo;
import in.ateesinfomedia.relief.models.shipping.TotalSegments;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.models.state.StateModel;

import static in.ateesinfomedia.relief.configurations.Global.AddressStateList;
import static in.ateesinfomedia.relief.configurations.Global.CustomerAddressModel;
import static in.ateesinfomedia.relief.configurations.Global.ShippingCostModel;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class OrderSummaryActivity extends AppCompatActivity implements NetworkCallback {

    private Toolbar toolbar;
    private MyPreferenceManager manager;
    private int REQUEST_SHIPPING_INFO = 8118;
    private int REQUEST_PLACE_ORDER = 8228;
    private static final String TAG = OrderSummaryActivity.class.getName();
    private Gson gson = new Gson();
    private CustomerAddress mAddressModel;
    private ShippingCost mShippingCost;
    private TextView mUserName, mUserCity, mUserStreet, mUserCountry, mUserPhone;
    private TextView mSubTotal, mDiscount, mShippingCharge, mTax, mGrandTotal;
    private AppCompatButton placeOrderBtn;
    private boolean placeOrderBool = false;

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

        setContentView(R.layout.activity_order_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        manager = new MyPreferenceManager(this);
        mUserName = findViewById(R.id.summary_user_name);
        mUserCity = findViewById(R.id.summary_user_city);
        mUserStreet = findViewById(R.id.summary_user_street);
        mUserCountry = findViewById(R.id.summary_user_country);
        mUserPhone = findViewById(R.id.summary_user_mobile);
        mSubTotal = findViewById(R.id.summary_sub_total);
        mDiscount = findViewById(R.id.summary_discount);
        mShippingCharge = findViewById(R.id.summary_shipping_charge);
        mTax = findViewById(R.id.summary_tax);
        mGrandTotal = findViewById(R.id.summary_grand_total);
        placeOrderBtn = findViewById(R.id.place_order_btn);

        mAddressModel = CustomerAddressModel;
        mShippingCost = ShippingCostModel;

        getShippingInfo();

        String fullName = mAddressModel.getFirstname() + " " + mAddressModel.getLastname();
        mUserName.setText(fullName);
        StringBuilder street = new StringBuilder();
        for (int i = 0; i < mAddressModel.getStreet().size(); i++) {
            street.append(mAddressModel.getStreet().get(i)).append(", ");
        }
        mUserStreet.setText(street);
        StringBuilder city = new StringBuilder();
        city.append(mAddressModel.getCity()).append(", ").append(mAddressModel.getRegion().getRegion()).append(", ").append(mAddressModel.getPostcode());
        mUserCity.setText(city);
        mUserCountry.setText("India");
        mUserPhone.setText(mAddressModel.getTelephone());

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOrder();
            }
        });
    }

    private void checkOrder() {
        if (placeOrderBool) {
            postPlaceOrder();
        } else {
            String message = "Please try again later.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void getShippingInfo() {

        try {
            String token = manager.getUserToken();
            JSONObject map = new JSONObject();
            JSONObject addressInfo = new JSONObject();
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

            addressInfo.put("shipping_address", customerItem);
            addressInfo.put("billing_address", customerItem);
            addressInfo.put("shipping_carrier_code", mShippingCost.getCarrier_code());
            addressInfo.put("shipping_method_code", mShippingCost.getMethod_code());

            map.put("addressInformation",addressInfo);
            Log.d("doJSON Request",map.toString());

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_SHIPPING_INFO,
                    ShippingInfo.class,
                    map,
                    token,
                    "TAG_POST_SHIPPING_INFO",
                    REQUEST_SHIPPING_INFO,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postPlaceOrder() {

        try {
            String token = manager.getUserToken();
            JSONObject map = new JSONObject();
            JSONObject paymentInfo = new JSONObject();
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

            paymentInfo.put("method", "checkmo");

            map.put("paymentMethod", paymentInfo);
            map.put("billing_address", customerItem);
            Log.d("doJSON Request",map.toString());

            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_PLACE_ORDER,
                    Object.class,
                    map,
                    token,
                    "TAG_POST_PLACE_ORDER",
                    REQUEST_PLACE_ORDER,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processJsonShippingInfo(String response) {
        Log.d(TAG, response);
        try {
            Type type = new TypeToken<ShippingInfo>(){}.getType();
            ShippingInfo shippingInfo = gson.fromJson(response, type);
            LoadingDialog.cancelLoading();

            if (shippingInfo.getTotals() != null
                    && shippingInfo.getTotals().getTotal_segments() != null
                    && !shippingInfo.getTotals().getTotal_segments().isEmpty()) {
                placeOrderBool = true;
                //mSubTotal, mDiscount, mShippingCharge, mTax, mGrandTotal;
                ArrayList<TotalSegments> totalSegmentsList = shippingInfo.getTotals().getTotal_segments();
                for (int index = 0; index < totalSegmentsList.size();  index++) {
                    TotalSegments totalSegments = totalSegmentsList.get(index);
                    if (totalSegments.getCode().equals("subtotal")) {
                        float price = Float.parseFloat(totalSegments.getValue());
                        if (price < 0) {
                            mSubTotal.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
                        } else {
                            mSubTotal.setText("₹ " + String.format("%.2f", Math.abs(price)));
                        }
                    }
                    if (totalSegments.getCode().equals("discount")) {
                        float price = Float.parseFloat(totalSegments.getValue());
                        if (price < 0) {
                            mDiscount.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
                        } else {
                            mDiscount.setText("₹ " + String.format("%.2f", Math.abs(price)));
                        }
                    }
                    if (totalSegments.getCode().equals("shipping")) {
                        float price = Float.parseFloat(totalSegments.getValue());
                        if (price < 0) {
                            mShippingCharge.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
                        } else {
                            mShippingCharge.setText("₹ " + String.format("%.2f", Math.abs(price)));
                        }
                    }
                    if (totalSegments.getCode().equals("tax")) {
                        float price = Float.parseFloat(totalSegments.getValue());
                        if (price < 0) {
                            mTax.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
                        } else {
                            mTax.setText("₹ " + String.format("%.2f", Math.abs(price)));
                        }
                    }
                    if (totalSegments.getCode().equals("grand_total")) {
                        float price = Float.parseFloat(totalSegments.getValue());
                        if (price < 0) {
                            mGrandTotal.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
                        } else {
                            mGrandTotal.setText("₹ " + String.format("%.2f", Math.abs(price)));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(OrderSummaryActivity.this, "Sorry ! Can't connect to server, try later");
        }

    }

    private void processJsonPlaceOrder(String response) {
        Log.d(TAG, response);
        try {
            Type type = new TypeToken<Integer>(){}.getType();
            Integer orderId = gson.fromJson(response, type);
            LoadingDialog.cancelLoading();

            Toast.makeText(this, "Order placed successfully.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(OrderSummaryActivity.this, MainActivity.class);
            intent.putExtra("info","login");
            startActivity(intent);
            finishAffinity();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse message = gson.fromJson(response, type);
                LoadingDialog.cancelLoading();
                dialogWarning(OrderSummaryActivity.this, message.getMessage());

            } catch (Exception e1) {
                e.printStackTrace();
                LoadingDialog.cancelLoading();
                dialogWarning(OrderSummaryActivity.this, "Sorry ! Can't connect to server, try later");
            }
        }

    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){

            if (requestId == REQUEST_SHIPPING_INFO){
                processJsonShippingInfo(response);
            } else if (requestId == REQUEST_PLACE_ORDER){
                processJsonPlaceOrder(response);
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
}