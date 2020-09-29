package in.ateesinfomedia.relief.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.app.Activity;

import in.ateesinfomedia.relief.components.PlaceArrayAdapter;
import in.ateesinfomedia.relief.view.adapter.OrderItemsAdapter;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;

import org.json.JSONObject;
import org.json.JSONException;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.DeliveryAddressModel;
import in.ateesinfomedia.relief.models.OrderDetails;
import in.ateesinfomedia.relief.models.OrderModel;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import spencerstudios.com.bungeelib.Bungee;

import static in.ateesinfomedia.relief.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class OrderDetailsActivity extends AppCompatActivity implements NetworkCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String LOG_TAG = "OrderDetailsActivity";
    private static final int GOOGLE_API_CLIENT_ID = 2;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private EditText etpincode;
    String mLat,mLong;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    OrderDetails orderDetails;
    OrderModel orderModel;
    private Toolbar toolbar;
    private TextView itemName,itemPrice,itemQuantity,gstPercentage,gstAmount,
            total,name,number,pincode,address,city,state,grandTotal,tvCancel;
    RelativeLayout addresslay;
    private Button btnCancel,btnConfirm;
    private MyPreferenceManager manager;
    private CardView card_address;
    private CardView cardEdit;
    private AlertDialog mADialogLog;
    private String phone,alt_phone,pincod,stat,cit,addres,nameee;
    private int REQUEST_ADD_DELIVERY_ADDRESS_ID = 8987;
    private TextView edit,tvViewInMap,tvPresImage;
    private Dialog dialog;
    private Button btncan,btncontinue;
    private RadioGroup radio_group;
    private int REQUEST_ORDER_CONFIRMATION_ID = 8909;
    private InstamojoPay instamojoPay;
    private LinearLayout buttons_lay,deliverLay;
    private StepperIndicator tracker;
    private String[] main = new String[]{"Draft","Confirmed","Processing","Shipped","Delivered"};
    private String[] main1 = new String[]{"Draft","Confirmed","Processing","Shipped","Not Delivered"};
    private String[] main2 = new String[]{"Draft","Confirmed","Processing","Shipped","Returned"};
    private String[] main3 = new String[]{"Draft","Confirmed","Processing","Shipped","Cancelled"};
    private List<OrderDetails> orderItemsList = new ArrayList<>();
    private RecyclerView order_items_recycler;
    private OrderItemsAdapter mAdapter;
    private float grand_total_price;
    private boolean isCancelled;
    private RelativeLayout trackLay;
    private AutoCompleteTextView autoCompleteTextView;

    //.......................................
    private List<OrderModel> orderList = new ArrayList<>();
    //......................................


    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
//       pay.put("send_sms", true);
//      pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
//                        .show();

                String[] values = response.split(":");

                String statusString = values[0];
                String[] statusArray = statusString.split("=");
                String status = statusArray[1];

                String orderIdString = values[1];
                String[] orderIdArray = orderIdString.split("=");
                String orderId = orderIdArray[1];


                String paymentIdString = values[3];
                String[] paymentIdArray = paymentIdString.split("=");
                String paymentId = paymentIdArray[1];

                HashMap<String,String> map = new HashMap<>();
                map.put("user_id",manager.getdelicioId());
                map.put("order_id",orderId);
                map.put("order_status",status);
                map.put("payment_type","1");
                map.put("prescription_id",orderModel.getId());
                map.put("transaction_id",paymentId);

                new NetworkManager(OrderDetailsActivity.this).doPost(map,Apis.API_POST_GET_ORDERS_CONFIRM,"REQUEST_ORDER_CONFIRMATION",REQUEST_ORDER_CONFIRMATION_ID,OrderDetailsActivity.this);
                LoadingDialog.showLoadingDialog(OrderDetailsActivity.this,"Loading...");
            }

            @Override
            public void onFailure(int code, String reason) {
//                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
//                        .show();

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                builder.setTitle("PAYMENT FAILED");
                builder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
                builder.setMessage(reason);
                AlertDialog alertDialog =builder.create();
                alertDialog.show();
            }
        };
    }
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

        setContentView(R.layout.activity_order_details);
        // Call the function callInstamojo to start payment here

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);

        manager = new MyPreferenceManager(this);

        orderItemsList.clear();
        orderModel = (OrderModel) getIntent().getSerializableExtra("order_list");
        orderItemsList = (ArrayList<OrderDetails>) getIntent().getSerializableExtra("order_details");
        orderList=(ArrayList<OrderModel>)getIntent().getSerializableExtra("order_status");

        order_items_recycler = (RecyclerView) findViewById(R.id.order_items_recycler);
//        itemName = (TextView) findViewById(R.id.itemName);
//        itemPrice = (TextView) findViewById(R.id.itemPrice);
//        itemQuantity = (TextView) findViewById(R.id.itemQuantity);
//        gstPercentage = (TextView) findViewById(R.id.gstPercentage);
        grandTotal = (TextView) findViewById(R.id.grandTotal);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvViewInMap = (TextView) findViewById(R.id.tvViewInMap);
        tvPresImage = (TextView) findViewById(R.id.tvImage);
//        gstAmount = (TextView) findViewById(R.id.gstAmount);
        tracker = (StepperIndicator) findViewById(R.id.tracker);
        trackLay = (RelativeLayout) findViewById(R.id.trackLay);
//        total = (TextView) findViewById(R.id.total);
        addresslay = (RelativeLayout) findViewById(R.id.addresslay);
        buttons_lay = (LinearLayout) findViewById(R.id.buttons_lay);
        deliverLay = (LinearLayout) findViewById(R.id.deliverLay);
        card_address = (CardView) findViewById(R.id.card_address);
//        cardEdit = (CardView) findViewById(R.id.cardEdit);
        edit = (TextView) findViewById(R.id.edit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        pincode = (TextView) findViewById(R.id.pincode);
        address = (TextView) findViewById(R.id.address);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);

//        itemName.setText(orderDetails.getMedicine_name());
//        itemPrice.setText(orderDetails.getMedicine_price());
//        itemQuantity.setText(orderDetails.getQuantity());
//        gstPercentage.setText(orderDetails.getGst_percentage());
//        gstAmount.setText(orderDetails.getGst());
//        total.setText(orderDetails.getTotal_amount());

        order_items_recycler.setNestedScrollingEnabled(false);
        mAdapter = new OrderItemsAdapter(this,orderItemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        order_items_recycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        order_items_recycler.setItemAnimator(new DefaultItemAnimator());
        order_items_recycler.setAdapter(mAdapter);

        grand_total_price = Float.valueOf(orderModel.getTotal());
        grandTotal.setText("₹ "+grand_total_price);

        if (manager.isAddres()){
            card_address.setVisibility(View.VISIBLE);
            addresslay.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            DeliveryAddressModel deliveryAddressModel = manager.getDeliveryAddress();

            name.setText(deliveryAddressModel.getName());
            number.setText(deliveryAddressModel.getNumber());
            pincode.setText(deliveryAddressModel.getPinCode());
            address.setText(deliveryAddressModel.getAddress());
            mLat = deliveryAddressModel.getmLat();
            mLong = deliveryAddressModel.getmLong();
//            city.setText(deliveryAddressModel.getLocality());

        } else {
            card_address.setVisibility(View.GONE);
            addresslay.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        }

        if (orderModel.getUser_status().equals("1")){
            edit.setVisibility(View.GONE);

            /*name.setText(orderItemsList.get(0).getName());
            number.setText(orderItemsList.get(0).getNumber());
            pincode.setText(orderItemsList.get(0).getPinCode());
            address.setText(orderItemsList.get(0).getAddress());
            mLat = orderItemsList.get(0).getmLat();
            mLong = orderItemsList.get(0).getMlong();

            if (orderItemsList.get(0).getTracking().equals("0")){
                tvViewInMap.setVisibility(View.GONE);
            } else if (orderItemsList.get(0).getTracking().equals("1")){
                tvViewInMap.setVisibility(View.VISIBLE);
            } else if (orderItemsList.get(0).getTracking().equals("2")){
                tvViewInMap.setVisibility(View.GONE);
            }*/
//            city.setText(orderItemsList.get(0).getCityOrDistrict());
//            state.setText(orderItemsList.get(0).getState());

            buttons_lay.setVisibility(View.GONE);
            trackLay.setVisibility(View.VISIBLE);
        } else if (orderModel.getUser_status().equals("2")){
            edit.setVisibility(View.GONE);

//            name.setText(orderItemsList.get(0).getName());
//            number.setText(orderItemsList.get(0).getNumber());
//            pincode.setText(orderItemsList.get(0).getPinCode());
//            address.setText(orderItemsList.get(0).getAddress());
//            city.setText(orderItemsList.get(0).getCityOrDistrict());
//            state.setText(orderItemsList.get(0).getState());
//
//            buttons_lay.setVisibility(View.GONE);
//            trackLay.setVisibility(View.VISIBLE);
//            tracker.setLabels(main3);
//            tracker.setCurrentStep(5);
            buttons_lay.setVisibility(View.GONE);
            tracker.setVisibility(View.GONE);
            tvCancel.setVisibility(View.VISIBLE);
            deliverLay.setVisibility(View.GONE);
            card_address.setVisibility(View.GONE);
        } else {
            buttons_lay.setVisibility(View.VISIBLE);
            trackLay.setVisibility(View.GONE);
        }

        Log.v("??Step",""+orderModel.getOrder_status());
        //Toast.makeText(this, "or"+orderModel.getOrder_status(), Toast.LENGTH_SHORT).show();

        if (orderModel.getOrder_status().equals("1")){
            tracker.setLabels(main);
            tracker.setCurrentStep(1);
        } else if (orderModel.getOrder_status().equals("2")){
            tracker.setLabels(main);
            tracker.setCurrentStep(2);
        } else if (orderModel.getOrder_status().equals("3")){
            tracker.setLabels(main);
            tracker.setCurrentStep(3);
        } else if (orderModel.getOrder_status().equals("4")){
            tracker.setLabels(main);
            tracker.setCurrentStep(4);
        } else if (orderModel.getOrder_status().equals("5")){
            tracker.setLabels(main);
            tracker.setCurrentStep(5);
        } else if (orderModel.getOrder_status().equals("6")){
            tracker.setLabels(main1);
            tracker.setCurrentStep(5);
        } else if (orderModel.getOrder_status().equals("7")){
            tracker.setLabels(main2);
            tracker.setCurrentStep(5);
        } else if (orderModel.getOrder_status().equals("0")){
//            tracker.setLabels(main3);
//            tracker.setCurrentStep(5);
            tracker.setVisibility(View.GONE);
            tvCancel.setVisibility(View.VISIBLE);
            deliverLay.setVisibility(View.GONE);
            card_address.setVisibility(View.GONE);
        }


        addresslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDailog(OrderDetailsActivity.this,"add");
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDailog(OrderDetailsActivity.this,"edit");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentDialog("cancel");
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.isAddres()) {
                    showPaymentDialog("confirm");
                } else {
                    Toast.makeText(OrderDetailsActivity.this, "Please add your delivery address!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvPresImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this,ImageShowActivity.class);
                intent.putExtra("image",orderItemsList.get(0).getPres_image());
                startActivity(intent);
            }
        });
    }

    public float SumofPrice(){
        float sum = 0;
        for(OrderDetails d : orderItemsList)
            sum += Float.valueOf(d.getTotal_amount());
        return sum;
    }

    private void showPaymentDialog(String type) {
        if (type.equals("cancel")){
            isCancelled = true;
            HashMap<String,String> map = new HashMap<>();
            map.put("user_id",manager.getdelicioId());
            map.put("payment_type","2");
            map.put("prescription_id",orderModel.getId());
            map.put("description"," ");
            map.put("reason"," ");
//            Toast.makeText(OrderDetailsActivity.this, ""+orderModel.getId(), Toast.LENGTH_SHORT).show();

            new NetworkManager(OrderDetailsActivity.this).doPost(map,Apis.API_POST_GET_ORDERS_CONFIRM,"REQUEST_ORDER_CONFIRMATION",REQUEST_ORDER_CONFIRMATION_ID,OrderDetailsActivity.this);
            LoadingDialog.showLoadingDialog(this,"Loading...");
        } else {
            dialog = new Dialog(this); // Context, this, etc.
            dialog.setContentView(R.layout.payment_dialog);
//        dialog.setTitle(R.string.dialog_title);

            btncan = (Button) dialog.findViewById(R.id.btn_cancel);
            btncontinue = (Button) dialog.findViewById(R.id.btn_continue);
            radio_group = (RadioGroup) dialog.findViewById(R.id.radio_group);
            RadioButton radio_cod = (RadioButton) dialog.findViewById(R.id.radio_cod);
            RadioButton radio_paynow = (RadioButton) dialog.findViewById(R.id.radio_paynow);

            btncan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btncontinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (radio_group.getCheckedRadioButtonId()) {
                        case R.id.radio_cod:
                            HashMap<String,String> map = new HashMap<>();
                            map.put("user_id",manager.getdelicioId());
                            map.put("payment_type","0");
                            map.put("prescription_id",orderModel.getId());

//                            Toast.makeText(OrderDetailsActivity.this, ""+orderModel.getId(), Toast.LENGTH_SHORT).show();

                            new NetworkManager(OrderDetailsActivity.this).doPost(map,Apis.API_POST_GET_ORDERS_CONFIRM,"REQUEST_ORDER_CONFIRMATION",REQUEST_ORDER_CONFIRMATION_ID,OrderDetailsActivity.this);
                            LoadingDialog.showLoadingDialog(OrderDetailsActivity.this,"Loading...");
                            dialog.dismiss();
                            break;
                        case R.id.radio_paynow:
                            DeliveryAddressModel deliveryAddressModel = manager.getDeliveryAddress();
                            String mai = manager.getUserMail();
                            dialog.dismiss();
//                            Toast.makeText(OrderDetailsActivity.this, ""+mai, Toast.LENGTH_SHORT).show();
//                            callInstamojoPay(mai, deliveryAddressModel.getNumber(), String.valueOf(grand_total_price), "buying medicines", deliveryAddressModel.getName());
                            callInstamojoPay(mai, deliveryAddressModel.getNumber(), "11", "buying medicines", deliveryAddressModel.getName());
                            break;
                    }
                }
            });

            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private final AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            autoCompleteTextView.setText(place.getName()+"\n"+place.getAddress());
            String[] separated = (""+place.getAddress()).split(",");

            for (String s : separated){
                s = s.replaceAll("[^\\d]", "");
                if (s.length() == 6){
                    etpincode.setText(s);
                    break;
                }
            }

            mLat = String.valueOf(place.getLatLng().latitude);
            mLong = String.valueOf(place.getLatLng().longitude);
//            etpincode.setText(mpinCode);

//            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
//            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
//            if (attributions != null) {
//                mAttTextView.setText(Html.fromHtml(attributions.toString()));
//            }
        }
    };

    private void showAddAddressDailog(final Context context, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sailog_layout_address, null);

        final TextView title = view.findViewById(R.id.title);
        etpincode = view.findViewById(R.id.et_pincode);
//        final EditText state = view.findViewById(R.id.et_state);
//        final EditText city = view.findViewById(R.id.et_city);
        final EditText alt_number = view.findViewById(R.id.et_alt_number);
        final EditText number = view.findViewById(R.id.et_number);
//        final EditText address = view.findViewById(R.id.et_address);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        final EditText name = view.findViewById(R.id.et_name);

        Button btnSave = view.findViewById(R.id.btnSave);

        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        autoCompleteTextView.setAdapter(mPlaceArrayAdapter);

        builder.setView(view);
        if (type.equals("edit")){
            title.setText("Edit delivery address");
            DeliveryAddressModel deliveryAddressModel = manager.getDeliveryAddress();
            etpincode.setText(deliveryAddressModel.getPinCode());
//            state.setText("Kerala");
//            city.setText("Thrissur");
//            city.setFocusable(false);
//            city.setEnabled(false);
//            state.setFocusable(false);
//            state.setEnabled(false);
            alt_number.setText(deliveryAddressModel.getAlternativeNumber());
            number.setText(deliveryAddressModel.getNumber());
            autoCompleteTextView.setText(deliveryAddressModel.getAddress());
            name.setText(deliveryAddressModel.getName());
        } else {
            title.setText("Add delivery address");
//            city.setText("Thrissur");
//            state.setText("Kerala");
//            city.setFocusable(false);
//            city.setEnabled(false);
//            state.setFocusable(false);
//            state.setEnabled(false);
//            pincode.setText(Global.PinCode);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {

            /*          * Enabled aggressive block sorting*/

            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(OrderDetailsActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
                        phone = number.getText().toString();
                        alt_phone = alt_number.getText().toString();
                        pincod = etpincode.getText().toString();
//                        stat = state.getText().toString();
//                        cit = city.getText().toString();
                        addres = autoCompleteTextView.getText().toString();
                        nameee = name.getText().toString();

                        HashMap<String, String> map = new HashMap<>();
                        map.put("userid",manager.getdelicioId());
                        map.put("name",nameee);
                        map.put("address",addres);
                        map.put("phone",phone);
                        map.put("alternative_phone",alt_phone);
                        map.put("locality","");
                        map.put("state","Kerala");
                        map.put("pincode",pincod);
                        map.put("prescription_id",orderModel.getId());
                        //map.put("lat",mLat);
                        //map.put("long",mLong);
                        map.put("lat","0.0");
                        map.put("long","0.0");

                        new NetworkManager(context).doPost(map, Apis.API_POST_ADD_DELIVERY_ADDRESS,
                                "REQUEST_ADD_DELIVERY_ADDRESS", REQUEST_ADD_DELIVERY_ADDRESS_ID, OrderDetailsActivity.this);

                        LoadingDialog.showLoadingDialog(OrderDetailsActivity.this,"Loading...");
                    } else {
                        Toast.makeText(OrderDetailsActivity.this, "Please add all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean isFormValidated() {

                String phone = number.getText().toString();
                String pincod = etpincode.getText().toString();
//                String stat = state.getText().toString();
//                String cit = city.getText().toString();
                String addres = autoCompleteTextView.getText().toString();
                String namee = name.getText().toString();

                boolean isError = false;
                View mFocusView = null;

                number.setError(null);
                etpincode.setError(null);
//                state.setError(null);
//                city.setError(null);
                autoCompleteTextView.setError(null);
                name.setError(null);

                if (!isDataValid(phone)) {
                    isError = true;
                    number.setError("Field can't be empty");
                    mFocusView = number;
                }
                if (!isValidMobile(phone)) {
                    isError = true;
                    number.setError("Add valid number");
                    mFocusView = number;
                }
                if (!isDataValid(pincod)) {
                    isError = true;
                    etpincode.setError("Field can't be empty");
                    mFocusView = etpincode;
                }
//                if (!isDataValid(stat)) {
//                    isError = true;
//                    state.setError("Field can't be empty");
//                    mFocusView = state;
//                }
//                if (!isDataValid(cit)) {
//                    isError = true;
//                    city.setError("Field can't be empty");
//                    mFocusView = city;
//                }
                if (!isDataValid(addres)) {
                    isError = true;
                    autoCompleteTextView.setError("Field can't be empty");
                    mFocusView = autoCompleteTextView;
                }
                if (!isDataValid(namee)) {
                    isError = true;
                    name.setError("Field can't be empty");
                    mFocusView = name;
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean isDataValid(String mETFirstName) {
        if (mETFirstName.equals("") || mETFirstName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidMobile(String phone) {
        if (phone.matches("^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$")){
            return true;
        }else {
            return false;
        }
    }

    public boolean isValidEmail(String emailAddress) {

        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher = regexPattern.matcher(emailAddress);
        if(regMatcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResponse(int status, String response, int requestId) {

        Log.v("~~RES",""+status+" , "+response+" , "+requestId);
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_ADD_DELIVERY_ADDRESS_ID){
                processAddDeliveryAddress(response);
            } else if (requestId == REQUEST_ORDER_CONFIRMATION_ID){
                processOrderConfirmation(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processOrderConfirmation(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                if (isCancelled){
                    Toast.makeText(this, "Something went wrong...cancel done.", Toast.LENGTH_SHORT).show();
                    showDailog("ORDER CANCELLED", "");
                    isCancelled = false;
                } else {
                    Toast.makeText(this, "Something went wrong...Payment done.", Toast.LENGTH_SHORT).show();
                    showDailog("ORDER SUCCESS", "");
                }

            } else {
                LoadingDialog.cancelLoading();
//                Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
                if (isCancelled){
                    showDailog("ORDER CANCELLED", "");
                    isCancelled = false;
                } else {
                    showDailog("ORDER SUCCESS", "");
                }
//                Intent intent = new Intent(OrderDetailsActivity.this,MainActivity.class);
//                startActivity(intent);
//                finishAffinity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }

    }

    private void showDailog(String title,String message) {
        final PrettyDialog prettyDialog = new PrettyDialog(this);
        if (title.equals("ORDER SUCCESS")){
            prettyDialog .setTitle(title)
                    .setMessage(message)
                    .setIcon(R.drawable.pdlg_icon_success)
                    .setIconTint(R.color.pdlg_color_green)
                    .setAnimationEnabled(true)

                    .addButton(
                            "OK",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_green,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(OrderDetailsActivity.this,MainActivity.class);
                                    intent.putExtra("info","payment");
                                    startActivity(intent);
                                    prettyDialog.dismiss();
                                    finishAffinity();
                                    Bungee.slideUp(OrderDetailsActivity.this);
                                }
                            }
                    )
                    .show();
        } else {
            prettyDialog .setTitle(title)
                    .setMessage(message)
                    .setIcon(R.drawable.pdlg_icon_close)
                    .setIconTint(R.color.pdlg_color_red)
                    .setAnimationEnabled(true)
                    .addButton(
                            "OK",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_red,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(OrderDetailsActivity.this,MainActivity.class);
                                    intent.putExtra("info","cancel");
                                    prettyDialog.dismiss();
                                    startActivity(intent);
                                    Bungee.slideUp(OrderDetailsActivity.this);
                                }
                            }
                    )
//                    .addButton(
//                            "Try Again",     // button text
//                            R.color.pdlg_color_white,  // button text color
//                            R.color.pdlg_color_red,  // button background color
//                            new PrettyDialogCallback() {  // button OnClick listener
//                                @Override
//                                public void onClick() {
////                                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonCod){
////                                        doConfirmOrder("cod");
////                                    } else {
////
////                                    }
//                                    prettyDialog.dismiss();
//                                }
//                            }
//                    )
                    .show();
        }
    }

    private void processAddDeliveryAddress(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            Boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, jsonObject.optString("data"), Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show();

                name.setText(nameee);
                number.setText(phone);
                pincode.setText(pincod);
                address.setText(addres);
//                city.setText(cit);
//                state.setText(stat);

                addresslay.setVisibility(View.GONE);
                card_address.setVisibility(View.VISIBLE);

                DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();
                deliveryAddressModel.setName(nameee);
                deliveryAddressModel.setNumber(phone);
                deliveryAddressModel.setAlternativeNumber(alt_phone);
//                deliveryAddressModel.setState(stat);
//                deliveryAddressModel.setLocality(cit);
                deliveryAddressModel.setAddress(addres);
                deliveryAddressModel.setPinCode(pincod);
                deliveryAddressModel.setmLat(mLat);
                deliveryAddressModel.setmLong(mLong);

                manager.saveDeliveryAddress(deliveryAddressModel);

                mADialogLog.dismiss();
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
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            if (instamojoPay != null){
                unregisterReceiver(instamojoPay);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}