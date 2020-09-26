package in.ateesinfomedia.remedio.view.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.components.PlaceArrayAdapter;
//import in.ateesinfomedia.remedio.components.SmsReceiver;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.DeliveryAddressModel;

import static in.ateesinfomedia.remedio.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class ProfileFragment extends Fragment implements View.OnClickListener, NetworkCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String LOG_TAG = "ProfileFragment";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private View mView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private FloatingActionButton mFabChangePass,mFabChangeMob;
    private int REQUEST_PROFILE_EDIT = 8909;
    private String Name;
    private String Emaail;
    private EditText email,name;
    private MyPreferenceManager manager;
    private AlertDialog mADialogLog;
    private ImageView mImEditPro;
    private int TAG_GET_PROFILE_ID = 7865;
    private String namee,emaill,phonee;
    private RelativeLayout mAddDelivery;
    private TextView mTvUserMail;
    private TextView mTvUserName;
    private TextView mTvUserNumber;
    private RelativeLayout cardAddress;
    private CardView cardEdit;
    private TextView pincode;
    private TextView city;
    private TextView numberr;
    private TextView address;
    private TextView mNamee;
    private int REQUEST_ADD_DELIVERY_ADDRESS_ID = 8888;
    private String phone,alt_phone,pincod,stat,cit,addres,nameee;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private Button getOtp;
    private EditText otp;
    private TextInputLayout inputLayoutPassword,inputLayoutNewNum;
    private TextInputLayout inputLayoutotp;
    private int REQUEST_VERIFY_FORGOT_PASSWORD = 6987;
    private int REQUEST_RESEND_OTP = 9999;
    private int REQUEST_FORGOT_PASSWORD = 1111;
    private FloatingActionsMenu mFabMenu;
    private int REQUEST_VERIFY_NEW_NUMBER = 8765;
    private int REQUEST_NUM_CHANGE = 9812;
    private int REQUEST_NUMBER_RESEND_OTP = 6543;
    private String otpDigits;
    private EditText etpincode;
    String mLat,mLong;
    private AutoCompleteTextView autoCompleteTextView;

    public static ProfileFragment getInstance() {
        ProfileFragment profileFragment = new ProfileFragment();

        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);

        manager = new MyPreferenceManager(getActivity());
        mFabMenu = (FloatingActionsMenu) mView.findViewById(R.id.right_labels);
        mFabChangePass = (FloatingActionButton) mView.findViewById(R.id.fab_change_pass);
        mFabChangeMob = (FloatingActionButton) mView.findViewById(R.id.fab_change_mob);
        mImEditPro = (ImageView) mView.findViewById(R.id.imEditPro);
        mTvUserName = (TextView) mView.findViewById(R.id.userName);
        mTvUserNumber = (TextView) mView.findViewById(R.id.userNumber);
        mTvUserMail = (TextView) mView.findViewById(R.id.userMail);

        mNamee = (TextView) mView.findViewById(R.id.namee);
        address = (TextView) mView.findViewById(R.id.address);
        numberr = (TextView) mView.findViewById(R.id.numberr);
//        city = (TextView) mView.findViewById(R.id.city);
        pincode = (TextView) mView.findViewById(R.id.pincode);

        cardAddress = (RelativeLayout) mView.findViewById(R.id.cardAddress);
        mAddDelivery = (RelativeLayout) mView.findViewById(R.id.deliveryAddLay);
        cardEdit = (CardView) mView.findViewById(R.id.cardEdit);

        mFabChangeMob.setOnClickListener(this);
        mFabChangePass.setOnClickListener(this);

        mImEditPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditEduQua(getActivity());
            }
        });

        mAddDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDailog(getActivity(),"add");
            }
        });

        cardEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAddressDailog(getActivity(),"edit");
            }
        });

        getProfileData();

        return mView;
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

//            for (String s : separated){
//                s = s.replaceAll("[^\\d]", "");
//                if (s.length() == 6){
//                    etpincode.setText(s);
//                    break;
//                }
//            }

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

    private void showAddAddressDailog(final Context context,String type) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
//            city.setText("Thrissur");
//            state.setText("Kerala");
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
//            pincode.setText(Global.PinCode);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {

            /*          * Enabled aggressive block sorting*/

            public void onClick(View view) {
                if (!isConnected()) {
                     Toast.makeText(getActivity(), "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
                        phone = number.getText().toString();
                        alt_phone = alt_number.getText().toString();
                        pincod = etpincode.getText().toString();
//                        stat = state.getText().toString();
//                        cit = city.getText().toString();
                        addres = autoCompleteTextView.getText().toString();
                        nameee = name.getText().toString();
                   //     Log.d("values",manager.getdelicioId()+"\n"+namee+addres+phone+alt_phone+"\n"+pincod+"\n"+mLat+"\n"+mLong+"\n");

                        HashMap<String, String> map = new HashMap<>();
                        map.put("userid",manager.getdelicioId());
                        map.put("name",nameee);
                        map.put("address",addres);
                        map.put("mobile",phone);
                        map.put("alter_mobile",alt_phone);
                        map.put("city_district","");
                        map.put("state","Kerala");
                        map.put("pincode",pincod);
//                        map.put("lat",mLat);
//                        map.put("long",mLong);
                        map.put("lat","0.0");
                        map.put("long","0.0");


                        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
                        new NetworkManager(context).doPost(map, Apis.API_POST_ADD_DELIVERY_ADDRESS_PROFILE,
                                "REQUEST_ADD_DELIVERY_ADDRESS", REQUEST_ADD_DELIVERY_ADDRESS_ID, ProfileFragment.this);


                    } else {
                        Toast.makeText(getActivity(), "Please add all fields", Toast.LENGTH_SHORT).show();
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

//                Toast.makeText(getActivity(), "phone "+phone+"\npin "+pincod+"\naddress "+addres+"\nname "+namee, Toast.LENGTH_SHORT).show();

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

    private void getProfileData() {
        Map<String,String> map = new HashMap<>();
        map.put("userid",manager.getdelicioId());

        new NetworkManager(getActivity()).doPost(map,Apis.API_POST_GET_PROFILE,"REQUEST_GET_PROFILE",TAG_GET_PROFILE_ID,this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_change_mob:
                dialogEditEduQua(getActivity(),"number change");
//                Toast.makeText(getActivity(), "number changed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_change_pass:
//                Toast.makeText(getActivity(), "password changed", Toast.LENGTH_SHORT).show();
                dialogEditEduQua(getActivity(),"forgot");
                break;
        }
    }

    private void dialogEditEduQua(final Context context, final String type) {
        if (type.equals("forgot")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_layout_login, null);

            final EditText number = view.findViewById(R.id.et_login_pnone);
            otp = view.findViewById(R.id.et_login_otp);
            final EditText password = view.findViewById(R.id.et_login_password);
            inputLayoutPassword = view.findViewById(R.id.input_layout_password);
            inputLayoutotp = view.findViewById(R.id.input_layout_otp);

            getOtp = view.findViewById(R.id.getOtp);

            if (this.phonee.isEmpty() || this.phonee.equals("")){
                number.setFocusable(true);
                number.setEnabled(true);
            } else {
                number.setFocusable(false);
                number.setEnabled(false);
                number.setText(this.phonee);
            }

            builder.setView(view);

            otp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 4) {
                        inputLayoutPassword.setVisibility(View.VISIBLE);
                        getOtp.setText("Update Password");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            getOtp.setOnClickListener(new View.OnClickListener() {

                /*          * Enabled aggressive block sorting*/

                public void onClick(View view) {
                    if (!isConnected()) {
                        Toast.makeText(context, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                    } else {

                        if (getOtp.getText().toString().equals("Update Password")) {

                            if (isFormValidatedForgot()) {

                                if (isConnected()) {
//                            phone = number.getText().toString();
                                    String otpp = otp.getText().toString();
                                    String passwor = password.getText().toString();

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("userMobile", phone);
                                    map.put("otp", otpp);
                                    map.put("pass", passwor);

                                    new NetworkManager(context).doPost(map, Apis.API_POST_USER_UPDATE_PASS_AND_LOGIN,
                                            "REQUEST_LOGIN_OTP", REQUEST_VERIFY_FORGOT_PASSWORD, ProfileFragment.this);

                                    LoadingDialog.showLoadingDialog(context, "Loading....");
                                } else {
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Toast.makeText(context, "Please add your mobile number", Toast.LENGTH_SHORT).show();
                            }
                        } else if (getOtp.getText().toString().equals("Resend OTP")) {

                            if (isConnected()) {

                                HashMap<String, String> map = new HashMap<>();
                                map.put("userMobile", phone);

                                new NetworkManager(context).doPost(map, Apis.API_POST_USER_CHECK_MOBILE_EXIST,
                                        "REQUEST_RESEND_OTP", REQUEST_RESEND_OTP, ProfileFragment.this);

                                LoadingDialog.showLoadingDialog(context, "Loading....");
                            } else {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        } else {

                            if (isFormValidated()) {
                                if (isConnected()) {

                                    phone = number.getText().toString();

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("userMobile", phone);

                                    new NetworkManager(context).doPost(map, Apis.API_POST_USER_FORGOT_PASSWORD,
                                            "REQUEST_LOGIN_OTP", REQUEST_FORGOT_PASSWORD, ProfileFragment.this);

                                    LoadingDialog.showLoadingDialog(context, "Loading....");
                                } else {
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Please add your mobile number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                private boolean isFormValidatedForgot() {
                    String phNumber = number.getText().toString();
                    String otpp = otp.getText().toString();
                    String pass = password.getText().toString();

                    boolean isError = false;
                    View mFocusView = null;

                    number.setError(null);
                    otp.setError(null);
                    password.setError(null);

                    if (!isDataValid(phNumber)) {
                        isError = true;
                        number.setError("Field can't be empty");
                        mFocusView = number;
                    }
                    if (!isValidMobile(phNumber)) {
                        isError = true;
                        number.setError("Enter valid phone number");
                        mFocusView = number;
                    }
                    if (!isDataValid(otpp)) {
                        isError = true;
                        otp.setError("Field can't be empty");
                        mFocusView = otp;
                    }
                    if (!isDataValid(pass)) {
                        isError = true;
                        password.setError("Field can't be empty");
                        mFocusView = password;
                    }

                    if (isError) {
                        mFocusView.requestFocus();
                        return false;
                    }

                    return true;
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
                    if (!isValidMobile(phNumber)) {
                        isError = true;
                        number.setError("Enter valid phone number");
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_layout_phone, null);

            final EditText number = view.findViewById(R.id.et_login_pnone);
            otp = view.findViewById(R.id.et_login_otp);
//            final EditText password = view.findViewById(R.id.et_login_password);xfgjn
//            inputLayoutNewNum= view.findViewById(R.id.input_layout_new_num);
            inputLayoutotp = view.findViewById(R.id.input_layout_otp);

            getOtp = view.findViewById(R.id.getOtp);

            builder.setView(view);

            otp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 4) {
//                        inputLayoutPassword.setVisibility(View.VISIBLE);
                        getOtp.setText("Update Number");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            getOtp.setOnClickListener(new View.OnClickListener() {

                /*          * Enabled aggressive block sorting*/

                public void onClick(View view) {
                    if (!isConnected()) {
                        Toast.makeText(context, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                    } else {

                        if (getOtp.getText().toString().equals("Update Number")) {

                            if (isFormValidatedForgot()) {

                                if (isConnected()) {
//                            phone = number.getText().toString();
                                    String otpp = otp.getText().toString();
//                                    String passwor = password.getText().toString();

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("userid", manager.getdelicioId());
                                    map.put("otp", otpp);
                                    map.put("phone", phone);

                                    new NetworkManager(context).doPost(map, Apis.API_POST_USER_UPDATE_NEW_NUMBER,
                                            "REQUEST_LOGIN_OTP", REQUEST_VERIFY_NEW_NUMBER, ProfileFragment.this);

                                    LoadingDialog.showLoadingDialog(context, "Loading....");
                                } else {
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(context, "Please add your mobile number", Toast.LENGTH_SHORT).show();
                            }
                        } else if (getOtp.getText().toString().equals("Resend OTP")) {

                            if (isConnected()) {

                                HashMap<String, String> map = new HashMap<>();
                                map.put("phone", phone);
                                map.put("userid", manager.getdelicioId());

                                new NetworkManager(context).doPost(map, Apis.API_POST_USER_UPDATE_NUMBER,
                                        "REQUEST_RESEND_OTP", REQUEST_NUMBER_RESEND_OTP, ProfileFragment.this);

                                LoadingDialog.showLoadingDialog(context, "Loading....");
                            } else {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        } else {

                            if (isFormValidated()) {
                                if (isConnected()) {

                                    phone = number.getText().toString();

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("phone", phone);
                                    map.put("userid", manager.getdelicioId());

                                    new NetworkManager(context).doPost(map, Apis.API_POST_USER_UPDATE_NUMBER,
                                            "REQUEST_LOGIN_OTP", REQUEST_NUM_CHANGE, ProfileFragment.this);

                                    LoadingDialog.showLoadingDialog(context, "Loading....");
                                } else {
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Please add your mobile number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                private boolean isFormValidatedForgot() {
                    String phNumber = number.getText().toString();
                    String otpp = otp.getText().toString();
//                    String pass = password.getText().toString();

                    boolean isError = false;
                    View mFocusView = null;

                    number.setError(null);
                    otp.setError(null);
//                    password.setError(null);

                    if (!isDataValid(phNumber)) {
                        isError = true;
                        number.setError("Field can't be empty");
                        mFocusView = number;
                    }
                    if (!isValidMobile(phNumber)) {
                        isError = true;
                        number.setError("Enter valid phone number");
                        mFocusView = number;
                    }
                    if (!isDataValid(otpp)) {
                        isError = true;
                        otp.setError("Field can't be empty");
                        mFocusView = otp;
                    }
//                    if (!isDataValid(pass)) {
//                        isError = true;
//                        password.setError("Field can't be empty");
//                        mFocusView = password;
//                    }

                    if (isError) {
                        mFocusView.requestFocus();
                        return false;
                    }

                    return true;
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
                    if (!isValidMobile(phNumber)) {
                        isError = true;
                        number.setError("Enter valid phone number");
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
        }

//        this.mADialogLog = builder.create();
        this.mADialogLog.setCanceledOnTouchOutside(false);
        this.mADialogLog.show();
    }

    public void dialogEditEduQua(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dailog_layout_profile_edit, null);
        builder.setView(view);

//        number = view.findViewById(R.id.et_number);
        name = view.findViewById(R.id.et_name);
        email = view.findViewById(R.id.et_email);

//        number.setEnabled(false);

        TextView save = view.findViewById(R.id.btnSave);
//        if (manager.getUserMail().equals("guest@delicio.in")){
//            number.setText(manager.getUserNumber());
//        } else {
//            number.setText(manager.getUserNumber());

            name.setText(namee);
            email.setText(emaill);
//        }

        save.setOnClickListener(new View.OnClickListener() {

            /*          * Enabled aggressive block sorting*/

            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(getActivity(), "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
//                        String phNumber = number.getText().toString();
                        Name = name.getText().toString();
                        Emaail = email.getText().toString();

                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_id",manager.getdelicioId());
                        map.put("first_name",Name);
                        map.put("email",Emaail);

                        Log.v("Profile>>",""+manager.getdelicioId()+"  "+Name+" "+Emaail);

                        new NetworkManager(context).doPost(map, Apis.API_POST_UPDATE_PROFILE,
                                "REQUEST_PROFILE_EDIT", REQUEST_PROFILE_EDIT, ProfileFragment.this);

                        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
                    } else {
                        Toast.makeText(getActivity(), "Please add your profile details", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean isFormValidated() {

                String nam = name.getText().toString();
                String mai = email.getText().toString();

                boolean isError = false;
                View mFocusView = null;

                name.setError(null);
                email.setError(null);

                if (!isDataValid(mai)) {
                    isError = true;
                    email.setError("Field can't be empty");
                    mFocusView = email;
                }
                if (!isValidEmail(mai)) {
                    isError = true;
                    email.setError("Enter valid email");
                    mFocusView = email;
                }
                if (!isDataValid(nam)) {
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

    public boolean isDataValid(String mETFirstName) {
        if (mETFirstName.equals("") || mETFirstName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidMobile(String phone) {
        if (phone.matches("^(\\[\\-\\s]?)?[0]?()?[6789]\\d{9}$")){
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
        Log.v("ResponseADD>>",""+response+"  >>"+requestId);
        if (status == NetworkManager.SUCCESS){
            if (requestId == TAG_GET_PROFILE_ID){
              processGetProfile(response);
            } else if (requestId == REQUEST_ADD_DELIVERY_ADDRESS_ID){
                processAddDeliveryAddress(response);
            } else if (requestId == REQUEST_PROFILE_EDIT){
                processEditProfile(response);
            } else if (requestId == REQUEST_VERIFY_FORGOT_PASSWORD){
                processVerifyPass(response);
            } else if (requestId == REQUEST_RESEND_OTP){
                processResendOtp(response);
            } else if (requestId == REQUEST_FORGOT_PASSWORD){
                processForgotPass(response);
            } else if (requestId == REQUEST_VERIFY_NEW_NUMBER){
                processVerifyNumer(response);
            } else if (requestId == REQUEST_NUM_CHANGE){
                processNumChange(response);
            } else if (requestId == REQUEST_NUMBER_RESEND_OTP){
                processNumResendOtp(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(getActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();

        }
    }

    private void processNumResendOtp(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
                dialogWarning(getActivity(), msg);
            } else {
                Toast.makeText(getContext(), "Otp Sent Successfully", Toast.LENGTH_SHORT).show();
//                openDialog(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processNumChange(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("data");
                dialogWarning(getActivity(), msg);
//                loadRegister();
            } else {
                //Toast.makeText(getContext(), "Otp Sent Successfully", Toast.LENGTH_SHORT).show();
                getOtp.setText("Resend OTP");
                inputLayoutotp.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processVerifyNumer(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                //String msg = jsonObject.optString("message");
                String msg = "Invalid OTP";
                dialogWarning(getActivity(), msg);
            } else {
                mADialogLog.dismiss();
                mFabMenu.collapse();
                mTvUserNumber.setText(phone);
                Toast.makeText(getActivity(), "You have successfully updated your new number", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processForgotPass(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
//                loadRegister();
            } else {

                getOtp.setText("Resend OTP");
                inputLayoutotp.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processResendOtp(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
                dialogWarning(getActivity(), msg);
            } else {
                Toast.makeText(getContext(), "Otp Sent Successfully", Toast.LENGTH_SHORT).show();
//                openDialog(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processVerifyPass(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
                dialogWarning(getActivity(), msg);
            } else {
                mADialogLog.dismiss();
                mFabMenu.collapse();
                Toast.makeText(getActivity(), "You have successfully updated your new password", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processEditProfile(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            String data=jsonObject.optString("data");

            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                getProfileData();
                mTvUserName.setText(Name);
                mTvUserMail.setText(Emaail);
                String unique = manager.getUserUniqueId();
                manager.saveUsereDetails(namee,emaill,phonee,unique);
                mADialogLog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processAddDeliveryAddress(String response) {
        Log.v("??res",""+response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                                if(jsonObject.optString("data").contains("pincode not valid")) {
                                    Toast.makeText(getContext(), "Pincode Code Not Valid", Toast.LENGTH_SHORT).show();
                                }
                //Toast.makeText(getActivity(), jsonObject.optString("data"), Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                cardAddress.setVisibility(View.VISIBLE);
                mAddDelivery.setVisibility(View.GONE);
                mNamee.setText(nameee);
                address.setText(addres);
                numberr.setText(phone);
//                city.setText(cit);
                pincode.setText(pincod);

                DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();
                deliveryAddressModel.setName(nameee);
                deliveryAddressModel.setNumber(phone);
                deliveryAddressModel.setPinCode(pincod);
//                deliveryAddressModel.setCityOrDistrict(cit);
                deliveryAddressModel.setAddress(addres);
//                deliveryAddressModel.setState("Kerala");
//                deliveryAddressModel.setLandmark("");
                deliveryAddressModel.setAlternativeNumber(alt_phone);
                deliveryAddressModel.setmLat(mLat);
                deliveryAddressModel.setmLong(mLong);
                manager.saveDeliveryAddress(deliveryAddressModel);

                manager.setKeyIsUseraddress(true);

                mADialogLog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processGetProfile(String response) {
        Log.v("??ErrorResp",""+response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                namee = jsonObject1.optString("firstname");
                emaill = jsonObject1.optString("email");
                phonee = jsonObject1.optString("phone");

                mTvUserName.setText(namee);
                mTvUserNumber.setText(phonee);
                mTvUserMail.setText(emaill);

                JSONArray jsonArray1 = jsonObject.getJSONArray("delivery_details");

                Log.e("??Array????",""+jsonArray1);

               // !jsonArray1.isNull(0)
               // jsonArray1.length()>0

                if (jsonArray1 != null && !jsonArray1.isNull(0) ) {
                    JSONObject jsonObject2 = jsonArray1.optJSONObject(0);

                    DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();

                    deliveryAddressModel.setName(jsonObject2.optString("name"));
                    deliveryAddressModel.setNumber(jsonObject2.optString("phone"));
                    deliveryAddressModel.setPinCode(jsonObject2.optString("pincode"));
                    manager.setPincode(jsonObject2.optString("pincode"));
//                    deliveryAddressModel.setCityOrDistrict(jsonObject2.optString("locality"));
                    deliveryAddressModel.setAddress(jsonObject2.optString("address"));
//                    deliveryAddressModel.setState(jsonObject2.optString("state"));
                    deliveryAddressModel.setLandmark(jsonObject2.optString("landmark"));
                    deliveryAddressModel.setAlternativeNumber(jsonObject2.optString("altertnative_phone"));
                    deliveryAddressModel.setmLat(jsonObject2.optString("latitude"));
                    deliveryAddressModel.setmLong(jsonObject2.optString("longitude"));

                    manager.saveDeliveryAddress(deliveryAddressModel);

                    cardAddress.setVisibility(View.VISIBLE);
                    mAddDelivery.setVisibility(View.GONE);

                    mNamee.setText(jsonObject2.optString("name"));
                    address.setText(jsonObject2.optString("address"));
                    numberr.setText(jsonObject2.optString("phone"));
//                    city.setText(jsonObject2.optString("locality"));
                    pincode.setText(jsonObject2.optString("pincode"));
                    mLat = jsonObject2.optString("latitude");
                    mLong = jsonObject2.optString("longitude");
                }else{
                    manager.setPincode("");
                }
            }
        } catch (JSONException e) {
            Log.e("??ERROR",""+e);
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

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

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}