package in.ateesinfomedia.relief.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import in.ateesinfomedia.relief.models.login.CustomerAddress;
import in.ateesinfomedia.relief.models.shipping.ShippingCost;
import in.ateesinfomedia.relief.view.activity.OrderSummaryActivity;
import in.ateesinfomedia.relief.view.activity.ShippingCostActivity;
import in.ateesinfomedia.relief.view.adapter.ShippingRateAdapter;

import static in.ateesinfomedia.relief.configurations.Global.CustomerAddressModel;
import static in.ateesinfomedia.relief.configurations.Global.ShippingCostModel;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class ShippingCostFragment extends DialogFragment implements NetworkCallback, RateCLickListener {

    private static final String TAG = ShippingCostFragment.class.getName();
    private MyPreferenceManager manager;
    private Gson gson = new Gson();
    private OnShippingCostListener listener;

    private CustomerAddress mAddressModel;
    private int REQUEST_POST_SHIPPING_RATE = 6982;
    private List<ShippingCost> shippingRateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConstraintLayout mCancel, mSave;
    private ShippingRateAdapter mAdapter;
    private boolean shipCostBool = false;

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
            listener = (OnShippingCostListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach Activity: " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipping_cost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);

        manager = new MyPreferenceManager(getActivity());
        recyclerView = view.findViewById(R.id.shipping_estimate_rv);
        mSave = view.findViewById(R.id.shipping_save);
        mCancel = view.findViewById(R.id.shipping_cancel);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        mAddressModel = CustomerAddressModel;

        getShippingRates();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCheckOutPage();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }

    private void goToCheckOutPage() {
        if (shipCostBool) {
            Dialog dialog = getDialog();
            if (dialog != null) {
                listener.shipCostSelected();
                dialog.dismiss();
            }
        } else {
            String message = "Please choose a shipping rate.";
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
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

            new NetworkManager(requireContext()).doPostCustom(
                    Apis.API_POST_SHIPPING_RATE_LIST,
                    ShippingCost[].class,
                    map,
                    token,
                    "TAG_POST_SHIPPING_RATE",
                    REQUEST_POST_SHIPPING_RATE,
                    this
            );

            LoadingDialog.showLoadingDialog(getActivity(),"Loading....");

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
                    mAdapter = new ShippingRateAdapter(requireContext(), shippingRateList,this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(requireActivity(), "Oops something went wrong");
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
    public void itemClicked(int position, ShippingCost shipCost) {
        shipCostBool = true;
        ShippingCostModel = shipCost;
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_POST_SHIPPING_RATE){
                ProcessJsonShippingRate(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(requireActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
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

    public interface OnShippingCostListener {
        void shipCostSelected();
    }
}