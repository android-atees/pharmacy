package in.ateesinfomedia.remedio.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.DeliveryAddressModel;
import in.ateesinfomedia.remedio.models.OrderDetails;
import in.ateesinfomedia.remedio.models.OrderModel;
import in.ateesinfomedia.remedio.view.activity.OrderActivity;
import in.ateesinfomedia.remedio.view.activity.OrderDetailsActivity;
import in.ateesinfomedia.remedio.view.adapter.OrderAdapter;
import in.ateesinfomedia.remedio.view.adapter.OtcOrderModel;

import static in.ateesinfomedia.remedio.configurations.Global.OTCOrderModelList;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class PrescriptionOrderFragment extends Fragment implements NetworkCallback, AdapterClickListner {

    private Context context;
    private MyPreferenceManager manager;
    private int REQUEST_ORDERS_LIST_ID = 8907;
    private List<OrderModel> orderList = new ArrayList<>();
    private List<OtcOrderModel> otcOrderModelList = new ArrayList<>();
    private RecyclerView mOrderRecycler;
    private OrderAdapter mAdapter;
    private TextView mTvNoOrders;
    private Toolbar toolbar;
    private OrderModel orderModel;
    private int REQUEST_ORDER_DETAILS_ID = 7786;
    private OrderDetails orderDetailsModel;
    private int REQUEST_DELIVERY_ADDRESS_ID = 7865;
    private List<OrderDetails> orderDetailsList = new ArrayList<>();
    private View mView;

    public static PrescriptionOrderFragment getInstance() {
        PrescriptionOrderFragment prescriptionOrderFragment = new PrescriptionOrderFragment();

        return prescriptionOrderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_prescription_orders, container, false);


        context=getContext();
        manager = new MyPreferenceManager(getActivity());
        mOrderRecycler = (RecyclerView) mView.findViewById(R.id.orderRecycler);
        mTvNoOrders = (TextView) mView.findViewById(R.id.tvNodata);

        mOrderRecycler.setNestedScrollingEnabled(true);
//        mAdapter = new OrderAdapter(this,orderList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mOrderRecycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        mOrderRecycler.setItemAnimator(new DefaultItemAnimator());
//        mOrderRecycler.setAdapter(mAdapter);
        getOrderList();


        return mView;
    }

    private void getOrderList() {

        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(getActivity()).doPost(map, Apis.API_POST_GET_ORDERS_LIST,"REQUEST_ORDERS_LIST",REQUEST_ORDERS_LIST_ID,this);
        LoadingDialog.showLoadingDialog(context,"Loading....");
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_ORDERS_LIST_ID){
                processOrderList(response);
            } else if (requestId == REQUEST_ORDER_DETAILS_ID){
                processOrderDetails(response);
            } else if (requestId == REQUEST_DELIVERY_ADDRESS_ID){
                processDeliveryAddress(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(context, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processDeliveryAddress(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
                intent.putExtra("order_list", (Serializable) orderModel);
                intent.putExtra("order_details", (Serializable) orderDetailsList);
                startActivity(intent);
            } else {
                JSONArray jsonArray1 = jsonObject.optJSONArray("data");
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
                }
                LoadingDialog.cancelLoading();

                Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
                intent.putExtra("order_list", (Serializable) orderModel);
                intent.putExtra("order_details", (Serializable) orderDetailsList);
                intent.putExtra("order_status",(Serializable)orderList);

                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void processOrderDetails(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(context, "Something went wrong..", Toast.LENGTH_SHORT).show();

            } else {
                orderDetailsList.clear();
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int i = 0;i<jsonArray.length();i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    OrderDetails orderDetails = new OrderDetails();

                    orderDetails.setId(jsonObject1.optString("id"));
                    orderDetails.setPrescription_id(jsonObject1.optString("prescription_id"));
                    orderDetails.setMedicine_id(jsonObject1.optString("medicine_id"));
                    orderDetails.setBatch_id(jsonObject1.optString("batch_id"));
                    orderDetails.setMedicine_mrp(jsonObject1.optString("medicine_mrp"));
                    orderDetails.setExpairy_date(jsonObject1.optString("expairy_date"));
                    orderDetails.setManufacture_id(jsonObject1.optString("manufacture_id"));
                    orderDetails.setMedicine_name(jsonObject1.optString("medicine_name"));
                    orderDetails.setMedicine_brand(jsonObject1.optString("medicine_brand"));
                    orderDetails.setMedicine_price(jsonObject1.optString("medicine_price"));
                    orderDetails.setCgst_amount(jsonObject1.optString("cgst_amount"));
                    orderDetails.setQuantity(jsonObject1.optString("quantity"));
                    orderDetails.setCgst_percentage(jsonObject1.optString("cgst_percentage"));
                    orderDetails.setSgst_amount(jsonObject1.optString("sgst_amount"));
                    orderDetails.setSgst_percentage(jsonObject1.optString("sgst_percentage"));
//                    orderDetails.setGst(jsonObject1.optString("gst"));
                    orderDetails.setTotal_amount(jsonObject1.optString("total_amount"));
                    orderDetails.setUser_status(jsonObject1.optString("user_status"));
                    orderDetails.setManufacture_name(jsonObject1.optString("manufacture_name"));
                    orderDetails.setBatch(jsonObject1.optString("batch"));
                    orderDetails.setHsn_code(jsonObject1.optString("hsn_code"));
                    orderDetails.setHsn_code(jsonObject1.optString("hsn_code"));

                    orderDetails.setName(jsonObject1.optString("fname"));
                    orderDetails.setNumber(jsonObject1.optString("phone"));
                    orderDetails.setAddress(jsonObject1.optString("address"));
                    orderDetails.setPinCode(jsonObject1.optString("pincode"));
                    orderDetails.setCityOrDistrict(jsonObject1.optString("city"));
                    orderDetails.setState(jsonObject1.optString("state"));
                    orderDetails.setLandmark(jsonObject1.optString("landmark"));
                    orderDetails.setAlternativeNumber(jsonObject1.optString("alter_number"));
                    orderDetails.setmLat(jsonObject1.optString("latitude"));
                    orderDetails.setMlong(jsonObject1.optString("longitude"));
                    orderDetails.setTracking(jsonObject1.optString("tracking"));
                    orderDetails.setPres_image(jsonObject1.optString("presc_photo"));


//                    orderDetailsModel = orderDetails;
                    orderDetailsList.add(orderDetails);
                }

//                Intent intent = new Intent(OrderActivity.this,OrderDetailsActivity.class);
//                intent.putExtra("order_list", (Serializable) orderModel);
//                intent.putExtra("order_details", (Serializable) orderDetails);
//                startActivity(intent);

                getDeliveryAddress();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void getDeliveryAddress() {
        Map<String,String> map = new HashMap<>();
        map.put("userid",manager.getdelicioId());

        new NetworkManager(getActivity()).doPost(map,Apis.API_POST_GET_DELIVERY_ADDRESS,"REQUEST_DELIVERY_ADDRESS",REQUEST_DELIVERY_ADDRESS_ID,this);
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

                orderList.clear();
//                JSONArray jsonArray1 = jsonObject.optJSONArray("data");
                JSONObject jsonObject2 = jsonObject.optJSONObject("data");
                JSONArray jsonArray = jsonObject2.optJSONArray("prescription");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        OrderModel orderModel = new OrderModel();

                        orderModel.setId(jsonObject1.optString("id"));
                        orderModel.setUnique_id(jsonObject1.optString("unique_id"));
                        orderModel.setPayment_type(jsonObject1.optString("payment_type"));
                        orderModel.setPayment_status(jsonObject1.optString("payment_status"));
                        orderModel.setOrder_status(jsonObject1.optString("order_status"));
                        orderModel.setUser_status(jsonObject1.optString("user_status"));
                        orderModel.setTotal(jsonObject1.optString("total"));
                        orderModel.setStatus(jsonObject1.optString("status"));
                        orderModel.setDate(jsonObject1.optString("create_date"));

                        orderList.add(orderModel);

                    }
                }
                JSONArray jsonArray1 = jsonObject2.optJSONArray("otc");
                if (jsonArray1 != null) {
                    otcOrderModelList.clear();
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


                        otcOrderModelList.add(otcOrderModel);
                    }

                    OTCOrderModelList = otcOrderModelList;
                }

                if (orderList.size()<1 || orderList==null){
                    LoadingDialog.cancelLoading();
                    mTvNoOrders.setVisibility(View.VISIBLE);
                    mOrderRecycler.setVisibility(View.GONE);
                } else {
                    LoadingDialog.cancelLoading();
                    mTvNoOrders.setVisibility(View.GONE);
                    mOrderRecycler.setVisibility(View.VISIBLE);
                    mAdapter = new OrderAdapter(getActivity(),orderList,this);
                    mOrderRecycler.setAdapter(mAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void itemClicked(int position, Object object) {

        orderModel = (OrderModel) object;
        if (orderModel.getStatus().equals("0")){
            Toast.makeText(context, "Quotation not created...please wait for a while.", Toast.LENGTH_SHORT).show();
        } else {
            String pres_id = orderModel.getId();
            getOrderDetails(pres_id);
        }
    }

    private void getOrderDetails(String pres_id) {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("prescription_id",pres_id);

        new NetworkManager(getActivity()).doPost(map,Apis.API_POST_GET_ORDERS_DETAILS,"REQUEST_ORDER_DETAILS",REQUEST_ORDER_DETAILS_ID,this);

        LoadingDialog.showLoadingDialog(context,"Loading...");
    }
}
