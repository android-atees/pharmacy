package in.ateesinfomedia.remedio.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.configurations.Global;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.MyAdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.managers.PermissionManager;
import in.ateesinfomedia.remedio.models.OrderModel;
import in.ateesinfomedia.remedio.models.OtcOrderDetailsModel;
import in.ateesinfomedia.remedio.view.activity.OtcOrderDetailActivity;
import in.ateesinfomedia.remedio.view.adapter.OrderAdapter;
import in.ateesinfomedia.remedio.view.adapter.OtcOrderAdapter;
import in.ateesinfomedia.remedio.view.adapter.OtcOrderModel;

import static in.ateesinfomedia.remedio.configurations.Global.OTCOrderModelList;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class OtcOrderFragment extends Fragment implements MyAdapterClickListner, PermissionManager.PermissionCallback, NetworkCallback {

    private View mView;
    private MyPreferenceManager manager;
    private RecyclerView mOrderRecycler;
    private TextView mTvNoOrders;
    private List<OtcOrderModel> OtcorderList = new ArrayList<>();
    private OtcOrderAdapter mAdapter;
    private PermissionManager mPermissionManager;
//    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
    private OtcOrderModel otcOrderModel;
    private int REQUEST_OTC_ORDER_DETAILS = 0000;
    private List<OtcOrderDetailsModel> OtcOrderDetailsList = new ArrayList<>();
    private int REQUEST_ORDERS_LIST_ID = 8765;

    public static OtcOrderFragment getInstance() {
        OtcOrderFragment otcOrderFragment = new OtcOrderFragment();

        return otcOrderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_otc_orders, container, false);

        manager = new MyPreferenceManager(getActivity());
        mPermissionManager = new PermissionManager(getActivity());
        mOrderRecycler = (RecyclerView) mView.findViewById(R.id.otcorderRecycler);
        mTvNoOrders = (TextView) mView.findViewById(R.id.tvNodata);

        OtcorderList.clear();
        OtcorderList = Global.OTCOrderModelList;

        if (OtcorderList.size() == 0 || OtcorderList == null){
            mTvNoOrders.setVisibility(View.VISIBLE);
            mOrderRecycler.setVisibility(View.GONE);
            getOrderList();
        } else {
            mTvNoOrders.setVisibility(View.GONE);
            mOrderRecycler.setVisibility(View.VISIBLE);
            mOrderRecycler.setNestedScrollingEnabled(true);
            mAdapter = new OtcOrderAdapter(getActivity(), OtcorderList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mOrderRecycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
            mOrderRecycler.setItemAnimator(new DefaultItemAnimator());
            mOrderRecycler.setAdapter(mAdapter);
//            mPermissionManager.setRequiredPermissions(permissions);
        }
        getOrderList();

        return mView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.onPermissionResult(grantResults,this,true);
    }

    @Override
    public void clicked(View view, int position) {

        otcOrderModel = OtcorderList.get(position);
        Map<String,String> map = new HashMap<String,String>();
        map.put("order_id",otcOrderModel.getId());

        NetworkManager networkManager = new NetworkManager(getActivity());
        networkManager.doPost(map, Apis.API_POST_GET_OTC_ORDER_DETAILS,"TAG_OTC_ORDER_DETAILS",REQUEST_OTC_ORDER_DETAILS,this);

        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
    }

    @Override
    public void Applyclicked(View view, int jobid, int position, String type) {
//        if (mPermissionManager.isPermissionAvailable()){
            showDialogToCall();
//        } else {
//            mPermissionManager.makePermissionRequest(this);
//        }
    }

    @Override
    public void onGrandPermission() {
        showDialogToCall();
    }

    @Override
    public void onDenyPermission() {
        Toast.makeText(getActivity(), "Allow permission to make the call", Toast.LENGTH_SHORT).show();
    }

    private void showDialogToCall() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Make call?");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to make the call..");

        // On pressing Settings button
        alertDialog.setPositiveButton("call", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:6282286867"));
                startActivity(callIntent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        OtcorderList = Global.OTCOrderModelList;
        mAdapter = new OtcOrderAdapter(getActivity(),OtcorderList,this);
        mOrderRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_OTC_ORDER_DETAILS){
                processJsonMyordrDetails(response);
            } else if (requestId  == REQUEST_ORDERS_LIST_ID){
                processOrderList(response);
            }
        } else {
           LoadingDialog.cancelLoading();
            Toast.makeText(getActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processOrderList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
//                Toast.makeText(getActivity(), "Something went wrong!!! Please try again.", Toast.LENGTH_SHORT).show();
                LoadingDialog.cancelLoading();
                mTvNoOrders.setVisibility(View.VISIBLE);
                mOrderRecycler.setVisibility(View.GONE);
            } else {

//                orderList.clear();
//                JSONArray jsonArray1 = jsonObject.optJSONArray("data");
                JSONObject jsonObject2 = jsonObject.optJSONObject("data");
//                JSONArray jsonArray = jsonObject2.optJSONArray("prescription");
//                for (int i = 0;i<jsonArray.length();i++) {
//
//                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
//                    OrderModel orderModel = new OrderModel();
//
//                    orderModel.setId(jsonObject1.optString("id"));
//                    orderModel.setUnique_id(jsonObject1.optString("unique_id"));
//                    orderModel.setPayment_type(jsonObject1.optString("payment_type"));
//                    orderModel.setPayment_status(jsonObject1.optString("payment_status"));
//                    orderModel.setOrder_status(jsonObject1.optString("order_status"));
//                    orderModel.setUser_status(jsonObject1.optString("user_status"));
//                    orderModel.setTotal(jsonObject1.optString("total"));
//                    orderModel.setStatus(jsonObject1.optString("status"));
//                    orderModel.setDate(jsonObject1.optString("create_date"));
//
//                    orderList.add(orderModel);
//
//                }
                JSONArray jsonArray1 = jsonObject2.optJSONArray("otc");
                if (jsonArray1 != null) {
                    OtcorderList.clear();
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject4 = jsonArray1.optJSONObject(i);
                        OtcOrderModel otcOrderModel = new OtcOrderModel();

                        otcOrderModel.setId(jsonObject4.optString("id"));
                        otcOrderModel.setOrder_id(jsonObject4.optString("order_id"));
                        otcOrderModel.setOrder_status(jsonObject4.optString("order_status"));
                        otcOrderModel.setPayment_status(jsonObject4.optString("payment_status"));
                        otcOrderModel.setPayment_type(jsonObject4.optString("payment_type"));
                        otcOrderModel.setCreate_date(jsonObject4.optString("create_date"));
                        otcOrderModel.setTotal(jsonObject4.optString("total"));
                        otcOrderModel.setSub_total(jsonObject4.optString("sub_total"));
                        otcOrderModel.setGst(jsonObject4.optString("gst"));
                        otcOrderModel.setPoints_or_voucher(jsonObject4.optString("points_or_voucher"));
                        otcOrderModel.setPayment_method(jsonObject4.optString("payment_method"));
                        otcOrderModel.setTransaction_id(jsonObject4.optString("transaction_id"));
                        otcOrderModel.setStatus(jsonObject4.optString("status"));
                        otcOrderModel.setCount(jsonObject4.optString("count"));

                        OtcorderList.add(otcOrderModel);
                    }

                    OTCOrderModelList = OtcorderList;
                }

                if (OtcorderList.size()<1 || OtcorderList==null){
                    LoadingDialog.cancelLoading();
                    mTvNoOrders.setVisibility(View.VISIBLE);
                    mOrderRecycler.setVisibility(View.GONE);
                } else {
                    LoadingDialog.cancelLoading();
                    mTvNoOrders.setVisibility(View.GONE);
                    mOrderRecycler.setVisibility(View.VISIBLE);
                    mAdapter = new OtcOrderAdapter(getActivity(),OtcorderList,this);
                    mOrderRecycler.setAdapter(mAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonMyordrDetails(String response) {
        Log.d("trackresponse",response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                OtcOrderDetailsList.clear();
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    OtcOrderDetailsModel otcOrderDetailsModel = new OtcOrderDetailsModel();

                    otcOrderDetailsModel.setOrder_id(jsonObject1.optString("order_id"));
                    otcOrderDetailsModel.setOrder_str_id(jsonObject1.optString("order_str_id"));
                    otcOrderDetailsModel.setPayment_status(jsonObject1.optString("payment_status"));
                    otcOrderDetailsModel.setPayment_type(jsonObject1.optString("payment_type"));
                    otcOrderDetailsModel.setCreate_date(jsonObject1.optString("create_date"));
                    otcOrderDetailsModel.setTotal(jsonObject1.optString("total"));
                    otcOrderDetailsModel.setOrder_item_id(jsonObject1.optString("order_item_id"));
                    otcOrderDetailsModel.setGst_amount(jsonObject1.optString("gst_amount"));
                    otcOrderDetailsModel.setProduct_price(jsonObject1.optString("product_price"));
                    otcOrderDetailsModel.setOrder_status(jsonObject1.optString("order_status"));
                    otcOrderDetailsModel.setQty(jsonObject1.optString("qty"));
                    otcOrderDetailsModel.setProduct_id(jsonObject1.optString("product_id"));
                    otcOrderDetailsModel.setProduct_name(jsonObject1.optString("product_name"));
                    otcOrderDetailsModel.setProduct_image1(jsonObject1.optString("product_image1"));
                    otcOrderDetailsModel.setDisplay_name(jsonObject1.optString("display_name"));
                    otcOrderDetailsModel.setMobile(jsonObject1.optString("mobile"));
                    otcOrderDetailsModel.setAddressline(jsonObject1.optString("addressline"));
                    otcOrderDetailsModel.setPincode(jsonObject1.optString("pincode"));
                    otcOrderDetailsModel.setAlt_phone(jsonObject1.optString("alt_phone"));
                    otcOrderDetailsModel.setLandmark(jsonObject1.optString("landmark"));
                    otcOrderDetailsModel.setTown(jsonObject1.optString("town"));
                    otcOrderDetailsModel.setState(jsonObject1.optString("state"));
                    otcOrderDetailsModel.setMlat(jsonObject1.optString("latitude"));
                    otcOrderDetailsModel.setmLong(jsonObject1.optString("longitude"));
                    otcOrderDetailsModel.setTracking(jsonObject1.optString("tracking"));

                    OtcOrderDetailsList.add(otcOrderDetailsModel);
                }

                LoadingDialog.cancelLoading();
                Intent intent = new Intent(getActivity(), OtcOrderDetailActivity.class);
                intent.putExtra("Order_list", (Serializable) otcOrderModel);
                intent.putExtra("Order_details_list", (Serializable) OtcOrderDetailsList);
                startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

        private void getOrderList() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(getActivity()).doPost(map, Apis.API_POST_GET_ORDERS_LIST,"REQUEST_ORDERS_LIST",REQUEST_ORDERS_LIST_ID,this);
        LoadingDialog.showLoadingDialog(getActivity(),"Loading....");
    }
}
