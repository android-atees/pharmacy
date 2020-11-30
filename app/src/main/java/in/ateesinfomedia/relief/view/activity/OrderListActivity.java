package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.AppHelpers;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.interfaces.OrderClickListener;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.orders.OrderDetail;
import in.ateesinfomedia.relief.models.orders.OrderResponse;
import in.ateesinfomedia.relief.models.orders.OrdersModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.adapter.OrderNewAdapter;

import static in.ateesinfomedia.relief.configurations.Global.OrderDetailModel;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class OrderListActivity extends AppCompatActivity implements NetworkCallback, OrderClickListener {

    private Toolbar toolbar;
    private MyPreferenceManager manager;
    private static final String TAG = OrderListActivity.class.getName();
    private Gson gson = new Gson();
    private RelativeLayout mNoDataLay;
    private ConstraintLayout mDataLay;
    private RecyclerView recyclerView;
    private int REQUEST_ORDER_LIST = 7568;
    private int REQUEST_ORDER_DETAILS = 7578;
    private OrderNewAdapter mAdapter;
    private List<OrdersModel> mOrderList = new ArrayList<>();
    private OrderDetail mOrderDetail;
    private String time = "";

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

        setContentView(R.layout.activity_order_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        manager = new MyPreferenceManager(this);
        mNoDataLay = (RelativeLayout) findViewById(R.id.noDataLayOrder);
        mDataLay = (ConstraintLayout) findViewById(R.id.dataLayOrder);
        recyclerView = (RecyclerView) findViewById(R.id.rv_order_list);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getOrderList();
    }

    private void getOrderList() {
        String getUrl = Apis.API_GET_ORDER_LIST +
                "?searchCriteria[filter_groups][0][filters][0][field]=customer_email&searchCriteria[filter_groups][0][filters][0][value]="
                + manager.getUserMail() + "&searchCriteria[filter_groups][0][filters][0][condition_type]=like&fields=items[increment_id,entity_id,grand_total,status,extension_attributes]";
        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                OrderResponse.class,
                null,
                Apis.ACCESS_TOKEN,
                "REQUEST_ORDER_LIST",
                REQUEST_ORDER_LIST,
                this
        );
        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void getOrderDetail(String entityId) {
        String getUrl = Apis.API_GET_ORDER_DETAILS + entityId;
        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                OrderDetail[].class,
                null,
                Apis.ACCESS_TOKEN,
                "REQUEST_ORDER_DETAILS",
                REQUEST_ORDER_DETAILS,
                this
        );
        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void processJsonOrderList(String response) {
        //Log.d(TAG, response);
        mOrderList.clear();
        try {
            Type type = new TypeToken<OrderResponse>(){}.getType();
            OrderResponse orderResponse = gson.fromJson(response, type);
            LoadingDialog.cancelLoading();

            if (orderResponse != null &&
                    orderResponse.getItems() != null &&
                    !orderResponse.getItems().isEmpty()) {
                mOrderList = orderResponse.getItems();
                if (mOrderList.size() == 0){
                    mNoDataLay.setVisibility(View.VISIBLE);
                    mDataLay.setVisibility(View.GONE);
                } else {
                    mNoDataLay.setVisibility(View.GONE);
                    mDataLay.setVisibility(View.VISIBLE);
                    mAdapter = new OrderNewAdapter(this, mOrderList,this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(OrderListActivity.this, "Sorry ! Can't connect to server, try later");
        }

    }

    private void processJsonOrderDetail(String response) {
        //Log.d(TAG, response);
        try {
            Type type = new TypeToken<ArrayList<OrderDetail>>(){}.getType();
            ArrayList<OrderDetail> orderDetailResponse = gson.fromJson(response, type);
            LoadingDialog.cancelLoading();

            if (orderDetailResponse != null &&
                    !orderDetailResponse.isEmpty() &&
                    orderDetailResponse.get(0) != null) {
                mOrderDetail = orderDetailResponse.get(0);
                OrderDetailModel = mOrderDetail;
                Intent intent = new Intent(OrderListActivity.this, OrderViewDetailActivity.class);
                intent.putExtra("time", time);
                startActivity(intent);


            } else {
                Toast.makeText(this, "Order details currently not available.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse errorResponse = gson.fromJson(response, type);
                LoadingDialog.cancelLoading();

                if (errorResponse != null &&
                        errorResponse.getMessage() != null) {
                    LoadingDialog.cancelLoading();
                    dialogWarning(OrderListActivity.this, errorResponse.getMessage());
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(OrderListActivity.this, "Sorry ! Can't connect to server, try later");
                }

            } catch (Exception e1) {
                e1.printStackTrace();
                LoadingDialog.cancelLoading();
                dialogWarning(OrderListActivity.this, "Sorry ! Can't connect to server, try later");
            }
        }

    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){

            if (requestId == REQUEST_ORDER_LIST){
                processJsonOrderList(response);
            } else if (requestId == REQUEST_ORDER_DETAILS) {
                processJsonOrderDetail(response);
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
    public void onTrackClicked(int position, OrdersModel item) {
        if (item.getExtension_attributes() != null
                && item.getExtension_attributes().getShipping_assignments()!= null
                && !item.getExtension_attributes().getShipping_assignments().isEmpty()) {
            if (item.getExtension_attributes().getShipping_assignments().get(0).getItems() != null
                    && !item.getExtension_attributes().getShipping_assignments().get(0).getItems().isEmpty()) {
                String orderDateTime = item.getExtension_attributes().getShipping_assignments().get(0).getItems().get(0).getCreated_at();
                time = AppHelpers.convertDate(orderDateTime);
            }
        }
        getOrderDetail(item.getEntity_id());
    }
}