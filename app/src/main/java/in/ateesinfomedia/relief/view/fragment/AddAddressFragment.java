package in.ateesinfomedia.relief.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reginald.editspinner.EditSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.login.CustomerAddress;
import in.ateesinfomedia.relief.models.login.LoginModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.models.state.StateModel;
import in.ateesinfomedia.relief.view.activity.SignUpActivity;

import static in.ateesinfomedia.relief.configurations.Global.AddressStateList;
import static in.ateesinfomedia.relief.configurations.Global.CustomerAddressModel;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class AddAddressFragment extends DialogFragment implements NetworkCallback {

    private EditSpinner stateSpinner;
    private EditText mFirstName, mLastName, mPhoneNumber, mStreetAddress, mCity, mPinCode;
    private ConstraintLayout mCancel, mSave;
    private String fName, lName, mNumber, sAddress, sCity, sPinCode, mState;
    private String regionCode, region, regionId;
    private String[] stateArrayList;
    private List<StateModel> stateList = new ArrayList<>();
    private static final String TAG = AddAddressFragment.class.getName();
    private MyPreferenceManager manager;
    private int REQUEST_ADD_NEW_ADDRESS = 9190;
    private int REQUEST_EDIT_ADDRESS = 9290;
    private Gson gson = new Gson();
    private TextView headerText;
    private boolean add = true;
    private CustomerAddress customerAddress;

    public OnAddAddressListener onAddAddressListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onAddAddressListener = (OnAddAddressListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach Activity: " + e.getMessage());
            /*try {
                onAddAddressListener = (OnAddAddressListener) getParentFragment();
            } catch (ClassCastException e1) {
                Log.e(TAG, "onAttach Fragment: " + e1.getMessage());
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_address, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);

        manager = new MyPreferenceManager(getActivity());
        stateSpinner = view.findViewById(R.id.add_address_state);
        mFirstName = view.findViewById(R.id.add_address_first_name);
        mLastName = view.findViewById(R.id.add_address_last_name);
        mPhoneNumber = view.findViewById(R.id.add_address_phone_number);
        mStreetAddress = view.findViewById(R.id.add_address_street_address);
        mCity = view.findViewById(R.id.add_address_city);
        mPinCode = view.findViewById(R.id.add_address_pincode);
        mCancel = view.findViewById(R.id.address_cancel);
        mSave = view.findViewById(R.id.address_save);
        headerText = view.findViewById(R.id.add_address_heading);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fName = mFirstName.getText().toString();
                lName = mLastName.getText().toString();
                mNumber = mPhoneNumber.getText().toString();
                sAddress = mStreetAddress.getText().toString();
                sCity = mCity.getText().toString();
                sPinCode = mPinCode.getText().toString();

                validateAddress();
                /*Dialog dialog = getDialog();
                if (dialog != null) {
                    onAddAddressListener.newAddress();
                    dialog.dismiss();
                }*/
            }
        });

        stateSpinner.setInputType(InputType.TYPE_NULL);

        stateList = AddressStateList;

        ArrayList<String> stringArrayList = new ArrayList<String>();

        for (int index = 0; index < stateList.size(); index++) {
            stringArrayList.add(stateList.get(index).getLabel());
        }

        stateArrayList = stringArrayList.toArray(new String[stringArrayList.size()]);

        ListAdapter adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                stateArrayList);
        stateSpinner.setAdapter(adapter);

        stateSpinner.selectItem(0);
        regionCode = stateList.get(0).getCode();
        region = stateList.get(0).getLabel();
        regionId = stateList.get(0).getValue();

        stateSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftInputPanel();
                /*if (stateSpinner.isPopupShowing()) {
                    stateSpinner.dismissDropDown();
                } else {
                    stateSpinner.showDropDown();
                }*/
                stateSpinner.showDropDown();
                return true;
            }
        });

        stateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 || position < stateList.size()) {
                    regionCode = stateList.get(position).getCode();
                    region = stateList.get(position).getLabel();
                    regionId = stateList.get(position).getValue();
                    Log.d(TAG, "regionCode : " + regionCode + "\nregion : " + region + "\nregionId : " + regionId);
                }
            }
        });

        String action = getArguments().getString("action");
        Log.d(TAG, "Action: " + action);
        if (action.equals("edit")) {
            add = false;
            headerText.setText("EDIT DELIVERY ADDRESS");
            customerAddress = CustomerAddressModel;
            mFirstName.setText(customerAddress.getFirstname());
            mLastName.setText(customerAddress.getLastname());
            mPhoneNumber.setText(customerAddress.getTelephone());
            StringBuilder street = new StringBuilder();
            for (int i = 0; i < customerAddress.getStreet().size(); i++) {
                street.append(customerAddress.getStreet().get(i)).append(" ");
            }
            mStreetAddress.setText(street);
            mCity.setText(customerAddress.getCity());
            mPinCode.setText(customerAddress.getPostcode());
            for (int index = 0; index < stateList.size(); index++) {
                if (stateList.get(index).getCode().equals(customerAddress.getRegion().getRegion_code())) {
                    stateSpinner.selectItem(index);
                    regionCode = stateList.get(index).getCode();
                    region = stateList.get(index).getLabel();
                    regionId = stateList.get(index).getValue();
                }
            }
        } else {
            headerText.setText("ADD DELIVERY ADDRESS");
            add = true;
        }

    }

    private void validateAddress() {
        boolean isError = false;
        View mFocusView = null;

        mFirstName.setError(null);
        mLastName.setError(null);
        mPhoneNumber.setError(null);
        mStreetAddress.setError(null);
        mCity.setError(null);
        mPinCode.setError(null);

        if (!isDataValid(fName)){
            isError = true;
            mFirstName.setError("Field can't be empty");
            mFocusView = mFirstName;
        }

        if (!isDataValid(lName)){
            isError = true;
            mLastName.setError("Field can't be empty");
            mFocusView = mLastName;
        }

        if (!isDataValid(mNumber)){
            isError = true;
            mPhoneNumber.setError("Field can't be empty");
            mFocusView = mPhoneNumber;
        }

        if (!isDataValid(sAddress)){
            isError = true;
            mStreetAddress.setError("Field can't be empty");
            mFocusView = mStreetAddress;
        }

        if (!isDataValid(sCity)){
            isError = true;
            mCity.setError("Field can't be empty");
            mFocusView = mCity;
        }

        if (!isDataValid(sPinCode)){
            isError = true;
            mPinCode.setError("Field can't be empty");
            mFocusView = mPinCode;
        }

        if (!isValidMobile(mNumber)){
            isError = true;
            mPhoneNumber.setError("Enter valid number");
            mFocusView = mPhoneNumber;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        } else {
            if (add) {
                saveAddress();
            } else {
                editAddress();
            }
        }

    }

    private void saveAddress() {
        try {
            JSONObject map = new JSONObject();
            JSONObject customerItem = new JSONObject();
            JSONObject regionItem = new JSONObject();
            //ArrayList<String> streetArray = new ArrayList<String>();
            JSONArray streetArray = new JSONArray();
            String userId = manager.getUserUniqueId();

            regionItem.put("region_code", regionCode);
            regionItem.put("region", region);
            regionItem.put("region_id", regionId);

            streetArray.put(sAddress);
            //String[] streetArrayList;
            //streetArray.add(sAddress);
            //streetArrayList = streetArray.toArray(new String[streetArray.size()]);

            customerItem.put("id", userId);
            customerItem.put("region", regionItem);
            customerItem.put("country_id", "IN");
            customerItem.put("street", streetArray);
            customerItem.put("telephone", mNumber);
            customerItem.put("postcode", sPinCode);
            customerItem.put("city", sCity);
            customerItem.put("firstname", fName);
            customerItem.put("lastname", lName);
            customerItem.put("default_shipping", true);
            customerItem.put("default_billing", true);

            map.put("customer",customerItem);
            Log.d("doJSON Request",map.toString());

            new NetworkManager(requireContext()).doPostCustom(
                    Apis.API_POST_ADD_NEW_ADDRESS,
                    AddAddressResponse[].class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_ADD_NEW_ADDRESS",
                    REQUEST_ADD_NEW_ADDRESS,
                    this
            );
            LoadingDialog.showLoadingDialog(getActivity(),"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editAddress() {
        try {
            JSONObject map = new JSONObject();
            JSONObject customerItem = new JSONObject();
            JSONObject regionItem = new JSONObject();
            //ArrayList<String> streetArray = new ArrayList<String>();
            JSONArray streetArray = new JSONArray();
            String userId = manager.getUserUniqueId();

            regionItem.put("region_code", regionCode);
            regionItem.put("region", region);
            regionItem.put("region_id", regionId);

            streetArray.put(sAddress);
            //String[] streetArrayList;
            //streetArray.add(sAddress);
            //streetArrayList = streetArray.toArray(new String[streetArray.size()]);

            customerItem.put("id", customerAddress.getId());
            customerItem.put("region", regionItem);
            customerItem.put("country_id", "IN");
            customerItem.put("street", streetArray);
            customerItem.put("telephone", mNumber);
            customerItem.put("postcode", sPinCode);
            customerItem.put("city", sCity);
            customerItem.put("firstname", fName);
            customerItem.put("lastname", lName);
            customerItem.put("default_shipping", true);
            customerItem.put("default_billing", true);

            map.put("address",customerItem);
            Log.d("doJSON Request",map.toString());

            new NetworkManager(requireContext()).doPostCustom(
                    Apis.API_POST_EDIT_ADDRESS,
                    AddAddressResponse[].class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_EDIT_ADDRESS",
                    REQUEST_EDIT_ADDRESS,
                    this
            );
            LoadingDialog.showLoadingDialog(getActivity(),"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDataValid(String mName) {
        if (mName.equals("") || mName.isEmpty()){
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

    private void hideSoftInputPanel() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    private void ProcessJsonAddAddress(String response) {

        try {
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<ArrayList<AddAddressResponse>>(){}.getType();
                ArrayList<AddAddressResponse> addressResponse = gson.fromJson(response, type);
                String message = addressResponse.get(0).getMessage();
                if (message.equals("success")) {
                    LoadingDialog.cancelLoading();
                    if (add) {
                        Toast.makeText(requireActivity(), "New address added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), "Address updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        onAddAddressListener.newAddress();
                        dialog.dismiss();
                    }
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(requireActivity(), message);
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
            dialogWarning(requireActivity(), "Sorry ! Can't connect to server, try later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_ADD_NEW_ADDRESS){
                ProcessJsonAddAddress(response);
            } else if (requestId == REQUEST_EDIT_ADDRESS){
                ProcessJsonAddAddress(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(getActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    public interface OnAddAddressListener {
        void newAddress();
    }

}