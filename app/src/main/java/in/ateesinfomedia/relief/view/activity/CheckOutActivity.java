package in.ateesinfomedia.relief.view.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.components.PlaceArrayAdapter;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.configurations.Global;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.CartModel;
import in.ateesinfomedia.relief.models.DeliveryAddressModel;
import in.ateesinfomedia.relief.view.adapter.CheckOutAdapter;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import spencerstudios.com.bungeelib.Bungee;

import static in.ateesinfomedia.relief.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.relief.configurations.Global.CartList;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class CheckOutActivity extends AppCompatActivity implements NetworkCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String LOG_TAG = "CheckOutActivity";
    private static final int GOOGLE_API_CLIENT_ID = 1;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private EditText etpincode;
    String mLat,mLong;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private RecyclerView recyclercartItems;
    private List<CartModel> cartList = new ArrayList<>();
    private CheckOutAdapter mAdapter;
    private TextView pincode;
    private CardView cardAddress;
    private CardView cardEdit;
    private TextView mTxtGrandTotalAmt;
    private Button checkout;
    private RadioGroup radioGroup;
    private TextView city;
    private TextView number;
    private TextView address;
    private TextView name;
    private Button addAddress;
    private MyPreferenceManager manager;
    private int REQUEST_CONFIRM_ORDER = 1234;
    private DeliveryAddressModel deliveryAddressModel;
    private Toolbar toolbar;

    private String phone,alt_phone,pincod,stat,cit,addres,nameee;
    private int REQUEST_ADD_DELIVERY_ADDRESS_ID = 8301;
    private AlertDialog mADialogLog;
    private AutoCompleteTextView autoCompleteTextView;

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

        setContentView(R.layout.activity_check_out);

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

        cartList.clear();
        cartList = CartList;
        pincod = Global.PinCode;

        manager = new MyPreferenceManager(this);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        number = (TextView) findViewById(R.id.number);
//        city = (TextView) findViewById(R.id.city);
        pincode = (TextView) findViewById(R.id.pincode);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkout = (Button) findViewById(R.id.btnCheckOut);

        mTxtGrandTotalAmt = (TextView) findViewById(R.id.grandTotalAmt);

        cardAddress = (CardView) findViewById(R.id.cardAddress);
        cardEdit = (CardView) findViewById(R.id.cardEdit);

        addAddress = (Button) findViewById(R.id.addAddress);

        recyclercartItems = (RecyclerView) findViewById(R.id.recyclercartItems);

        recyclercartItems.setNestedScrollingEnabled(false);
        mAdapter = new CheckOutAdapter(this,cartList);
//        ShimmerRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclercartItems.setLayoutManager(/*linearLayoutManager*/mLayoutManager);
        recyclercartItems.setItemAnimator(new DefaultItemAnimator());
        recyclercartItems.setAdapter(mAdapter);

        cardEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDailog(CheckOutActivity.this,"edit");
            }
        });

        if (manager.isAddres()){
            deliveryAddressModel = manager.getDeliveryAddress();
            cardAddress.setVisibility(View.VISIBLE);
            addAddress.setVisibility(View.GONE);
            name.setText(deliveryAddressModel.getName());
            address.setText(deliveryAddressModel.getAddress());
            number.setText(deliveryAddressModel.getNumber());
            mLat = deliveryAddressModel.getmLat();
            mLong = deliveryAddressModel.getmLong();
//            city.setText(deliveryAddressModel.getCityOrDistrict()+","+deliveryAddressModel.getState());
            if (deliveryAddressModel.getPinCode().equals(pincod)){
                pincode.setText(deliveryAddressModel.getPinCode());
            } else {
//                isChange = true;
                cardEdit.performClick();
            }

        } else {
            addAddress.setVisibility(View.VISIBLE);
            cardAddress.setVisibility(View.GONE);
        }
        String offerprice = String.valueOf(SumofPrice());

        if (offerprice.contains(".")){
            String[] arrStr = offerprice.split("\\.", 2);
            if (arrStr[1].length()>=2){
                String second = arrStr[1].substring(0,2);
                mTxtGrandTotalAmt.setText("₹ "+arrStr[0]+"."+second);
            } else {
                mTxtGrandTotalAmt.setText("₹ "+arrStr[0]+"."+arrStr[1]);
            }
        } else {
            mTxtGrandTotalAmt.setText("₹ " + offerprice);
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.isAddres()) {

                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonCod) {
                        doConfirmOrder("cod");
                    } else {
                        doConfirmOrder("payNow");
                    }
                } else {
                    Toast.makeText(CheckOutActivity.this, "Please add your delivery address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDailog(CheckOutActivity.this,"add");
            }
        });

    }

    public float SumofPrice(){
        float sum = 0;
        for(CartModel d : cartList)
//            price = Float.valueOf(d.getProduct_total());
//            quan = Integer.valueOf(d.getQu)
            sum += Integer.valueOf(d.getQuantity()) * Float.valueOf(d.getProduct_total_offer());
        return sum;
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
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        final EditText name = view.findViewById(R.id.et_name);

        Button btnSave = view.findViewById(R.id.btnSave);

        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        autoCompleteTextView.setAdapter(mPlaceArrayAdapter);

        builder.setView(view);
        etpincode.setEnabled(false);
        etpincode.setFocusable(false);
        if (type.equals("edit")){
            title.setText("Edit delivery address");
            DeliveryAddressModel deliveryAddressModel = manager.getDeliveryAddress();
            etpincode.setText(Global.PinCode);
//            state.setText("Kerala");
//            city.setText("Thrissur");
//            city.setFocusable(false);
//            city.setEnabled(false);
//            state.setFocusable(false);
//            state.setEnabled(false);
//            city.setText(deliveryAddressModel.getCityOrDistrict());
//            state.setText(deliveryAddressModel.getState());
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
            etpincode.setText(Global.PinCode);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {

            /*          * Enabled aggressive block sorting*/

            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(CheckOutActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
                        phone = number.getText().toString();
                        alt_phone = alt_number.getText().toString();
                        pincod = etpincode.getText().toString();
//                        stat = state.getText().toString();
//                        cit = city.getText().toString();
                        addres = autoCompleteTextView.getText().toString();
                        nameee = name.getText().toString();

//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("userid",manager.getdelicioId());
//                        map.put("name",nameee);
//                        map.put("address",addres);
//                        map.put("mobile",phone);
//                        map.put("alter_mobile",alt_phone);
//                        map.put("city_district","");
//                        map.put("state","Kerala");
//                        map.put("pincode",pincod);
//                        map.put("lat",mLat);
//                        map.put("long",mLong);
//                        map.put("prescription_id",orderModel.getId());

                        HashMap<String, String> map = new HashMap<>();
                        map.put("userid",manager.getdelicioId());
                        map.put("name",nameee);
                        map.put("address",addres);
                        map.put("mobile",phone);
                        map.put("alter_mobile",alt_phone);
                        map.put("city_district","");
                        map.put("state","Kerala");
                        map.put("pincode",pincod);
                        //map.put("lat",mLat);
//                        map.put("long",mLong);
                        map.put("lat","0.0");
                        map.put("long","0.0");


//                        Toast.makeText(CheckOutActivity.this, "lat  "+mLat+"\nlong"+mLong, Toast.LENGTH_SHORT).show();
                        new NetworkManager(CheckOutActivity.this).doPost(map, Apis.API_POST_ADD_DELIVERY_ADDRESS_PROFILE,
                                "REQUEST_ADD_DELIVERY_ADDRESS", REQUEST_ADD_DELIVERY_ADDRESS_ID, CheckOutActivity.this);

                        LoadingDialog.showLoadingDialog(CheckOutActivity.this,"Loading...");
                    } else {
                        Toast.makeText(CheckOutActivity.this, "Please add all fields", Toast.LENGTH_SHORT).show();
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
        this.mADialogLog.setCancelable(false);
        this.mADialogLog.show();
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

    private void doConfirmOrder(String payment_type) {

            if (payment_type.equals("cod")) {
                Map<String, String> map = new HashMap<String, String>();
                    map.put("user_id", manager.getdelicioId());
                    map.put("payment_type", payment_type);
//                    map.put("platform", "Android");

                    new NetworkManager(this).doPost(map, Apis.API_POST_CONFIRM_ORDER, "TAG_CONFIRM_ORDER", REQUEST_CONFIRM_ORDER, this);

                LoadingDialog.showLoadingDialog(this,"Loading...");

            } else {

            }
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            /*if (requestId == REQUEST_ADD_DELIVERY_ADDRESS){
                ProcessDelivery(response);
            } else*/ if (requestId == REQUEST_CONFIRM_ORDER){
                ProcessConfirmOrder(response);
            } else if (requestId == REQUEST_ADD_DELIVERY_ADDRESS_ID){
                processAddDelivery(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processAddDelivery(String response) {
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

                addAddress.setVisibility(View.GONE);
                cardAddress.setVisibility(View.VISIBLE);

                DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();
                deliveryAddressModel.setName(nameee);
                deliveryAddressModel.setNumber(phone);
                deliveryAddressModel.setAlternativeNumber(alt_phone);
//                deliveryAddressModel.setState(stat);
//                deliveryAddressModel.setCityOrDistrict(cit);
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

    private void ProcessConfirmOrder(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                showDailog("Order Failed!!!","Sorry, there was some trouble with your order, please try again for your favourite goody!");
            } else {
                showDailog("Order Successful!!!","Order has been placed, Your item will reach you in no time..");
                manager.saveCartCount(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(CheckOutActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void showDailog(String title,String message) {
        final PrettyDialog prettyDialog = new PrettyDialog(this);
        if (title.equals("Order Successful!!!")){
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
                                    Intent intent = new Intent(CheckOutActivity.this,MainActivity.class);
                                    intent.putExtra("info","paymentss");
                                    startActivity(intent);
                                    prettyDialog.dismiss();
                                    finishAffinity();
                                    Bungee.slideUp(CheckOutActivity.this);
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
                                    prettyDialog.dismiss();
                                    Bungee.slideUp(CheckOutActivity.this);
                                }
                            }
                    )
                    .addButton(
                            "Try Again",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_red,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonCod){
                                        doConfirmOrder("cod");
                                    } else {

                                    }
                                    prettyDialog.dismiss();
                                }
                            }
                    )
                    .show();
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
