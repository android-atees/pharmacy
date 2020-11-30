package in.ateesinfomedia.relief.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.GPSTracker;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.components.LocationAddress;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.configurations.Global;
import in.ateesinfomedia.relief.interfaces.CartItemClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.CartsModel;
import in.ateesinfomedia.relief.models.DeliveryAddressModel;
import in.ateesinfomedia.relief.models.cart.CartAttributeInfo;
import in.ateesinfomedia.relief.models.cart.CartModel;
import in.ateesinfomedia.relief.models.cart.CartResponse;
import in.ateesinfomedia.relief.models.login.LoginModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.adapter.CartAdapter;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static in.ateesinfomedia.relief.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.relief.configurations.Global.PinCode;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;
import static in.ateesinfomedia.relief.configurations.Global.isChanged;

public class CartActivity extends AppCompatActivity implements NetworkCallback, CartItemClickListner {

    private MyPreferenceManager manager;
    private int REQUEST_CART_ITEMS_ID = 8788;
    private List<CartModel> cartList = new ArrayList<>();
    private TextView mTxtSubTotalAmt;
    //private TextView mTxtGrandTotalAmt;
    private TextView mTxtAmount;
    private Button mBtnCheckOut;
    private RecyclerView recyclerView;
    //private TextView mtxtPinCode;
    //private CardView mCardChangePin;
    private CartAdapter mAdapter;
    private RelativeLayout mNoDataLay;
    private LinearLayout mdataLay;
    private MaterialCardView mFooterLay;
    private int REQUEST_ADD_MINUS = 7777;
    private Float price = 0.0f;
    private int quan;
    private int position;
    private int REQUEST_REMOVE = 8765;
    private EditText number;
    private TextView notAvail;
    private String pincode = "";
    private double latitude;
    private double longitude;
    private AlertDialog mADialogLog;
    private int REQUEST_PINCODE_CHECK = 8989;
    private LocationAddress locationAddress;
    private GPSTracker locationTrack;
    private static final int REQUEST_LOCATION = 2323;
    private LinearLayout mContainer;
    private Toolbar toolbar;
    private int REQUEST_PINCODE_CHECK_DATA = 7779;
    private int TAG_GET_PROFILE_ID = 8766;
    private static final String TAG = CartActivity.class.getName();
    private Gson gson = new Gson();
    private int REQUEST_UPDATE_CART_QTY = 7821;
    private int REQUEST_DELETE_CART_ITEM = 7822;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //must be called before setContentView(...)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            Drawable background = this.getResources().getDrawable(R.drawable.ab_gradient);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }

        setContentView(R.layout.activity_cart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My cart");

        locationAddress = new LocationAddress();

        manager = new MyPreferenceManager(this);
        mContainer = (LinearLayout) findViewById(R.id.firstmainLay);
        mTxtAmount = (TextView) findViewById(R.id.txtAmount);
        mBtnCheckOut = (Button) findViewById(R.id.btnCheckOut);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cart_items_list);
        mNoDataLay = (RelativeLayout) findViewById(R.id.noDataLay);
        mdataLay = (LinearLayout) findViewById(R.id.dataLay);
        mFooterLay = (MaterialCardView) findViewById(R.id.cart_footer_cardView);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadLocation();

        getCartItems();

        mBtnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, AddressListActivity.class);
                intent.putExtra("info", "cart");
                startActivity(intent);
            }
        });

        /*mBtnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mtxtPinCode.getText().toString().equals("")){
                    Toast.makeText(CartActivity.this, "Please add your delivery pincode..", Toast.LENGTH_SHORT).show();
                } else {
                    checkPinValid(mtxtPinCode.getText().toString());
                }
            }
        });

        mCardChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditEduQua(CartActivity.this);
            }
        });*/
    }

    private void checkPinValid(String pincode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("pincode",pincode);

        new NetworkManager(this).doPost(map, Apis.API_POST_CHECK_PINCODE,
                "REQUEST_PINCODE_CHECK_DATA", REQUEST_PINCODE_CHECK_DATA, CartActivity.this);

        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    public void loadLocation() {

        if (!mayRequestLocation()) {
            return;
        }

        // create class object
        locationTrack = new GPSTracker(this);

        // check if GPS enabled
        if(locationTrack.canGetLocation()){

            latitude = locationTrack.getLatitude();
            longitude = locationTrack.getLongitude();

            Global.Latitude = latitude;
            Global.Longitude = longitude;
            locationAddress.getAddressFromLocation(latitude, longitude,
                    this, new GeocoderHandler());

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            locationTrack.showSettingsAlert();
        }
    }

    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION) ) {
            Snackbar.make(mContainer, R.string.permission_rationale_location, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                        }
                    });
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        return false;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    pincode = PinCode;
                    PinCode = pincode;
                    //mtxtPinCode.setText(PinCode);
                    //mtxtPinCode.setText(manager.getPincode());
                    break;
                default:
                    locationAddress = null;
            }
//            if (locationAddress.contains("null")){
//                location.setText("Location not found!!!");
//            } else {
//                location.setText(locationAddress);
//            }
        }
    }

    public void dialogEditEduQua(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dailog_layout_pincode, null);
        builder.setView(view);

        number = view.findViewById(R.id.et_pincode);
        TextView save = view.findViewById(R.id.btnSave);
        TextView cancel = view.findViewById(R.id.btnCancel);
        notAvail = view.findViewById(R.id.notAvail);

        //number.setText(mtxtPinCode.getText().toString());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mADialogLog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            /*          * Enabled aggressive block sorting*/

            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(CartActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
                        pincode = number.getText().toString();

                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_id",manager.getdelicioId());
                        map.put("pincode",pincode);

                        new NetworkManager(context).doPost(map, Apis.API_POST_CHECK_PINCODE,
                                "REQUEST_PINCODE_CHECK", REQUEST_PINCODE_CHECK, CartActivity.this);

                        LoadingDialog.showLoadingDialog(context,"Loading...");
                    } else {
                        Toast.makeText(CartActivity.this, "Please add your Pincode...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean isFormValidated() {

                String phNumber = number.getText().toString();

                boolean isError = false;
                View mFocusView = null;

                number.setError(null);

                if (!isDataValid(phNumber)) {
                    isError = true;
                    number.setError("Field can't be empty");
                    mFocusView = number;
                }

                if (isError) {
                    mFocusView.requestFocus();
                    return false;
                }

                return true;
            }
        });

        this.mADialogLog = builder.create();
        this.mADialogLog.setCanceledOnTouchOutside(false);
        this.mADialogLog.show();
    }

    public boolean isDataValid(String mETFirstName) {
        if (mETFirstName.equals("") || mETFirstName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private void getCartItems() {
        /*Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map, Apis.API_POST_GET_CART_ITEMS,"REQUEST_CART_ITEMS",REQUEST_CART_ITEMS_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading....");*/
        String token = manager.getUserToken();
        String getUrl = Apis.API_GET_CART_LIST + manager.getCartId();
        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                CartModel[].class,
                null,
                token,
                "REQUEST_CART_ITEMS",
                REQUEST_CART_ITEMS_ID,
                this
        );
        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    @Override
    public void onResponse(int status, String response, int requestId) {

        if (status == NetworkManager.SUCCESS){

            if (requestId == REQUEST_CART_ITEMS_ID){
                processJsonCartItems(response);
            } else if (requestId == REQUEST_ADD_MINUS){
                processJsonaddOrMinus(response);
            } else if (requestId == REQUEST_REMOVE){
                processJsonRemove(response);
            } else if (requestId == REQUEST_PINCODE_CHECK){
                processJsonCheckPincode(response);
            } else if (requestId == REQUEST_PINCODE_CHECK_DATA){
                processJsonCheckPincodeAvail(response);
            } else if (requestId == TAG_GET_PROFILE_ID){
                processJsonDelivery(response);
            } else if (requestId == REQUEST_UPDATE_CART_QTY){
                processJsonCartQty(response);
            } else if (requestId == REQUEST_DELETE_CART_ITEM){
                processJsonCartDelete(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
        }
    }

    private void processJsonCartQty(String response) {
        Log.d(TAG, response);
        try {
            Type type = new TypeToken<CartModel>(){}.getType();
            CartModel cartResponse = gson.fromJson(response, type);
            Log.d(TAG, cartResponse.getQty());
            getCartItems();
        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonCartDelete(String response) {
        Log.d(TAG, response);
        try {
            Type type = new TypeToken<Boolean>(){}.getType();
            Boolean deleteResponse = gson.fromJson(response, type);
            if (deleteResponse) {
                getCartItems();
            } else {
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e1) {
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse deleteResponse = gson.fromJson(response, type);
                LoadingDialog.cancelLoading();
                dialogWarning(CartActivity.this, deleteResponse.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                LoadingDialog.cancelLoading();
                dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
            }
        }
    }

    private void processJsonDelivery(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();

                JSONArray jsonArray1 = jsonObject.optJSONArray("delivery_details");
                if (jsonArray1 != null && jsonArray1.length()>0) {
                    JSONObject jsonObject2 = jsonArray1.optJSONObject(0);

                    DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();

                    deliveryAddressModel.setName(jsonObject2.optString("name"));
                    deliveryAddressModel.setNumber(jsonObject2.optString("phone"));
                    deliveryAddressModel.setPinCode(jsonObject2.optString("pincode"));
                    deliveryAddressModel.setLocality(jsonObject2.optString("locality"));
                    deliveryAddressModel.setAddress(jsonObject2.optString("address"));
                    deliveryAddressModel.setState(jsonObject2.optString("state"));
                    deliveryAddressModel.setLandmark(jsonObject2.optString("landmark"));
                    deliveryAddressModel.setAlternativeNumber(jsonObject2.optString("altertnative_phone"));
                    deliveryAddressModel.setmLat(jsonObject2.optString("latitude"));
                    deliveryAddressModel.setmLong(jsonObject2.optString("longitude"));

                    manager.saveDeliveryAddress(deliveryAddressModel);
                    Intent intent = new Intent(CartActivity.this,CheckOutActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CartActivity.this,CheckOutActivity.class);
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonCheckPincodeAvail(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
//                dialogWarning(CartActivity.this, "Your Pincode is not available, we will launch there soon.");
                Intent intent = new Intent(CartActivity.this,NoLocationActivity.class);
                startActivity(intent);
            } else {
                //PinCode = mtxtPinCode.getText().toString();
                getDeliveryAddress();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void getDeliveryAddress() {

        Map<String,String> map = new HashMap<>();
        map.put("userid",manager.getdelicioId());

        new NetworkManager(this).doPost(map,Apis.API_POST_GET_PROFILE,"REQUEST_GET_PROFILE",TAG_GET_PROFILE_ID,this);
    }

    private void processJsonCheckPincode(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

//                notAvail.setVisibility(View.VISIBLE);

                Intent intent = new Intent(CartActivity.this,NoLocationActivity.class);
                startActivity(intent);
            } else {

                manager.setPincode(pincode);
                mADialogLog.dismiss();
               // mtxtPinCode.setText(pincode);
                //mtxtPinCode.setText(manager.getPincode());
                //Global.PinCode = mtxtPinCode.getText().toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();

            dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonRemove(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
               LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();

                int cartcount = jsonObject.optInt("count");
                manager.saveCartCount(cartcount);

                cartList.remove(position);
//                CartList.clear();
//                CartList = cartList;
                mAdapter.notifyItemChanged(position);
                mAdapter.notifyItemRemoved(position);

                manager.saveChanged(true);
                isChanged = true;

                if (cartList.size() < 1 || cartList == null){
                    Intent intent = new Intent(CartActivity.this,MainActivity.class);
                    intent.putExtra("info","cart");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();
                } else {

//                    float subTotal = SumofPrice();
//                    float deliveryTotal = (SumofPrice() * 10.0f) / 100;
//                    float grandTotal = subTotal + deliveryTotal;
//
//                    mTxtSubTotalAmt.setText("₹ " + subTotal);
//                    mTxtDeliveryAmt.setText("₹ " + deliveryTotal);
//                    mTxtGrandTotalAmt.setText("₹ " + grandTotal);
//                    mTxtAmount.setText("₹ " + grandTotal);
//                    totalAmount = grandTotal;

                    getCartItems();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonaddOrMinus(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();

            } else {
                LoadingDialog.cancelLoading();
                getCartItems();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonCartItems(String response) {
        Log.d(TAG, response);
        cartList.clear();
        try {
            Type type = new TypeToken<ArrayList<CartModel>>(){}.getType();
            ArrayList<CartModel> cartResponse = gson.fromJson(response, type);

            if (cartResponse != null) {
                cartList = cartResponse;
                if (cartList.size() == 0){
                    manager.saveCartCount(0);
                    mNoDataLay.setVisibility(View.VISIBLE);
                    mdataLay.setVisibility(View.GONE);
                    mFooterLay.setVisibility(View.GONE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle("My cart");
                    }
                } else {
                    manager.saveCartCount(cartList.size());
                    mNoDataLay.setVisibility(View.GONE);
                    mdataLay.setVisibility(View.VISIBLE);
                    mFooterLay.setVisibility(View.VISIBLE);
                    mAdapter = new CartAdapter(this,cartList,this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle("My cart("+cartList.size()+")");
                    }

                    showTotalPrice();
                }
            }

            LoadingDialog.cancelLoading();
        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(CartActivity.this, "Sorry ! Can't connect to server, try later");
        }


    }

    private void showTotalPrice() {
        float total = 0;

        for (int i = 0; i < cartList.size(); i++) {
            float price = Float.parseFloat(cartList.get(i).getPrice());
            int qty = Integer.parseInt(cartList.get(i).getQty());
            total = total + (price * qty);
        }

        mTxtAmount.setText("₹ " + String.format("%.2f", total));
    }

    /*private void processJsonCartitems(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                mNoDataLay.setVisibility(View.VISIBLE);
                mdataLay.setVisibility(View.GONE);
            } else {
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                cartList.clear();
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    CartModel cartModel = new CartModel();

                    cartModel.setId(jsonObject1.optString("product_id"));
                    cartModel.setQuantity(jsonObject1.optString("cquantity"));
                    cartModel.setProduct_name(jsonObject1.optString("product_name"));
                    cartModel.setProduct_total(jsonObject1.optString("product_total"));
                    cartModel.setProduct_total_offer(jsonObject1.optString("product_total_offer"));
                    cartModel.setProduct_image1(jsonObject1.optString("product_image1"));

                    cartList.add(cartModel);
                }
                try{
                    LoadingDialog.cancelLoading();
                }catch (Exception e){
                    Toast.makeText(this, "Something Went Wrong....", Toast.LENGTH_SHORT).show();
                }

                CartList = cartList;
                if (cartList.size() == 0 || cartList == null){
                    mNoDataLay.setVisibility(View.VISIBLE);
                    mdataLay.setVisibility(View.GONE);
                } else {
                    mNoDataLay.setVisibility(View.GONE);
                    mdataLay.setVisibility(View.VISIBLE);
                    mAdapter = new CartAdapter(this,cartList,this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    getSupportActionBar().setTitle("My cart("+cartList.size()+")");

                    showTotalPrice();
                }
//                mAdapter = new CartAdapter(this,cartList,this);
//                recyclerView.setAdapter(mAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }*/

    /*private void showTotalPrice() {
        String offerprice = String.valueOf(SumofPrice());
        if (offerprice.contains(".")){
            String[] arrStr = offerprice.split("\\.", 2);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                //mTxtGrandTotalAmt.setText("₹ "+arrStr[0]+"."+second);
                mTxtAmount.setText("₹ "+arrStr[0]+"."+second);
            } else {
                //mTxtGrandTotalAmt.setText("₹ "+arrStr[0]+"."+arrStr[1]);
                mTxtAmount.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }
        } else {
            //mTxtGrandTotalAmt.setText("₹ " + offerprice);
            mTxtAmount.setText("₹ " + offerprice);
        }
    }*/

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    private void updateItemQuantity(int value, CartModel item) {

        try {
            JSONObject map = new JSONObject();
            JSONObject cartItem = new JSONObject();
            if (item.getProduct_type().equals("configurable")) {
                JSONObject prodOptions = new JSONObject();
                JSONObject extAttribute = new JSONObject();
                JSONArray configItemOptionArray = new JSONArray();
                if (item.getOptions() != null
                        && item.getOptions().getAttributes_info() != null
                        && !item.getOptions().getAttributes_info().isEmpty()) {
                    ArrayList<CartAttributeInfo> extAttributeArray = item.getOptions().getAttributes_info();
                    for (int index = 0; index < extAttributeArray.size(); index++) {
                        JSONObject configItemOption = new JSONObject();
                        configItemOption.put("option_id", extAttributeArray.get(index).getOption_id());
                        configItemOption.put("option_value", extAttributeArray.get(index).getOption_value());
                        configItemOptionArray.put(configItemOption);
                    }
                }
                extAttribute.put("configurable_item_options", configItemOptionArray);
                prodOptions.put("extension_attributes", extAttribute);
                cartItem.put("product_option", prodOptions);
            }
            cartItem.put("sku", item.getSku());
            cartItem.put("qty", value);
            cartItem.put("quote_id", manager.getCartId());
            map.put("cartItem",cartItem);
            Log.d("doJSON Request",map.toString());

            String urlString = Apis.API_UPDATE_CART_QTY + item.getItem_id();

            new NetworkManager(this).doPutCustom(
                    urlString,
                    CartModel.class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_UPDATE_CART_QTY",
                    REQUEST_UPDATE_CART_QTY,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCartItem(CartModel item) {

        try {
            JSONObject map = new JSONObject();
            JSONObject cartItem = new JSONObject();
            if (item.getProduct_type().equals("configurable")) {
                JSONObject prodOptions = new JSONObject();
                JSONObject extAttribute = new JSONObject();
                JSONArray configItemOptionArray = new JSONArray();
                if (item.getOptions() != null
                        && item.getOptions().getAttributes_info() != null
                        && !item.getOptions().getAttributes_info().isEmpty()) {
                    ArrayList<CartAttributeInfo> extAttributeArray = item.getOptions().getAttributes_info();
                    for (int index = 0; index < extAttributeArray.size(); index++) {
                        JSONObject configItemOption = new JSONObject();
                        configItemOption.put("option_id", extAttributeArray.get(index).getOption_id());
                        configItemOption.put("option_value", extAttributeArray.get(index).getOption_value());
                        configItemOptionArray.put(configItemOption);
                    }
                }
                extAttribute.put("configurable_item_options", configItemOptionArray);
                prodOptions.put("extension_attributes", extAttribute);
                cartItem.put("product_option", prodOptions);
            }
            cartItem.put("sku", item.getSku());
            cartItem.put("qty", item.getQty());
            cartItem.put("quote_id", manager.getCartId());
            map.put("cartItem",cartItem);
            Log.d("doJSON Request",map.toString());

            String urlString = Apis.API_DELETE_CART_ITEM + manager.getCartId() + "/items/" + item.getItem_id();

            new NetworkManager(this).doDeleteCustom(
                    urlString,
                    Object.class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_UPDATE_CART_QTY",
                    REQUEST_DELETE_CART_ITEM,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRemoveItemClicked(int position, CartModel item) {
        this.position = position;

        deleteCartItem(item);

        /*Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("type","remove");

        NetworkManager networkManager = new NetworkManager(this);
        networkManager.doPost(map, Apis.API_POST_PLUS_OR_MINUS_OR_REMOVE,"TAG_REMOVE",REQUEST_REMOVE,this);

        LoadingDialog.showLoadingDialog(this,"Loading...");*/
    }

    @Override
    public void onEditQuantityClicked(int position, String type, int value, CartModel item) {

        updateItemQuantity(value, item);

        /*Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("count",String.valueOf(value));
        map.put("type",type);

        new NetworkManager(this).doPost(map, Apis.API_POST_PLUS_OR_MINUS_OR_REMOVE,"TAG_REMOVE",REQUEST_ADD_MINUS,this);

        LoadingDialog.showLoadingDialog(this,"Loading...");*/
    }

    public float SumofPrice(){
        float sum = 0;
        //for(CartsModel d : cartList)
//            price = Float.valueOf(d.getProduct_total());
//            quan = Integer.valueOf(d.getQu)
            //sum += Integer.valueOf(d.getQuantity()) * Float.valueOf(d.getProduct_total_offer());
        return sum;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}