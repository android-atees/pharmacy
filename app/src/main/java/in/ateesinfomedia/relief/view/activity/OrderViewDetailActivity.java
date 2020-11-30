package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.AppHelpers;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.models.orders.OrderDetail;
import in.ateesinfomedia.relief.models.orders.OrderDetailItem;
import in.ateesinfomedia.relief.view.adapter.OrderProductAdapter;

import static in.ateesinfomedia.relief.configurations.Global.OrderDetailModel;

public class OrderViewDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MyPreferenceManager manager;
    private static final String TAG = OrderViewDetailActivity.class.getName();
    private RecyclerView recyclerView;
    private TextView mSubTotal, mDiscount, mShippingCharge, mTax, mGrandTotal;
    private TextView mOrderId, mOrderDate, mOrderStatus, mShipCity, mShipStreet, mPayMethod;
    private OrderDetail mOrderDetail;

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

        setContentView(R.layout.activity_order_view_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        manager = new MyPreferenceManager(this);
        mSubTotal = findViewById(R.id.order_detail_sub_total);
        mDiscount = findViewById(R.id.order_detail_discount);
        mShippingCharge = findViewById(R.id.order_detail_shipping_charge);
        mTax = findViewById(R.id.order_detail_tax);
        mGrandTotal = findViewById(R.id.order_detail_grand_total);
        mOrderId = findViewById(R.id.order_detail_id);
        mOrderDate = findViewById(R.id.order_detail_date);
        mOrderStatus = findViewById(R.id.order_detail_status);
        mShipCity = findViewById(R.id.order_detail_ship_city);
        mShipStreet = findViewById(R.id.order_detail_ship_street);
        mPayMethod = findViewById(R.id.order_detail_pay_method);
        recyclerView = (RecyclerView) findViewById(R.id.order_detail_rv);

        mOrderDetail = OrderDetailModel;

        String mTime = getIntent().getStringExtra("time");

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        mOrderId.setText("#" + mOrderDetail.getIncrement_id());
        if (mOrderDetail.getOrder_created_at() != null) {
            String orderDateTime = mOrderDetail.getOrder_created_at();
            mTime = AppHelpers.convertDate(orderDateTime);
        }
        mOrderDate.setText(mTime);
        int drawableBg = R.drawable.bg_order_processing;
        String status = "Processing";
        if (mOrderDetail.getStatus().equals("pending")) {
            drawableBg = R.drawable.bg_order_pending;
            status = "Pending";
        } else if (mOrderDetail.getStatus().equals("completed")) {
            drawableBg = R.drawable.bg_order_complete;
            status = "Completed";
        } else if (mOrderDetail.getStatus().equals("cancelled")) {
            drawableBg = R.drawable.bg_order_cancelled;
            status = "Cancelled";
        }
        mOrderStatus.setText(status);
        mOrderStatus.setBackgroundResource(drawableBg);

        if (mOrderDetail.getSub_total() != null) {
            float price = Float.parseFloat(mOrderDetail.getSub_total());
            if (price < 0) {
                mSubTotal.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
            } else {
                mSubTotal.setText("₹ " + String.format("%.2f", Math.abs(price)));
            }
        }
        if (mOrderDetail.getDiscount() != null) {
            float price = Float.parseFloat(mOrderDetail.getDiscount());
            if (price < 0) {
                mDiscount.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
            } else {
                mDiscount.setText("₹ " + String.format("%.2f", Math.abs(price)));
            }
        }
        if (mOrderDetail.getShipping_amount() != null) {
            float price = Float.parseFloat(mOrderDetail.getShipping_amount());
            if (price < 0) {
                mShippingCharge.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
            } else {
                mShippingCharge.setText("₹ " + String.format("%.2f", Math.abs(price)));
            }
        }
        if (mOrderDetail.getTax() != null) {
            float price = Float.parseFloat(mOrderDetail.getTax());
            if (price < 0) {
                mTax.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
            } else {
                mTax.setText("₹ " + String.format("%.2f", Math.abs(price)));
            }
        }
        if (mOrderDetail.getGrand_total() != null) {
            float price = Float.parseFloat(mOrderDetail.getGrand_total());
            if (price < 0) {
                mGrandTotal.setText("- ₹ " + String.format("%.2f", Math.abs(price)));
            } else {
                mGrandTotal.setText("₹ " + String.format("%.2f", Math.abs(price)));
            }
        }
        if (mOrderDetail.getItems() != null && !mOrderDetail.getItems().isEmpty()) {
            ArrayList<OrderDetailItem> items = mOrderDetail.getItems();
            OrderProductAdapter mAdapter = new OrderProductAdapter(this, items);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        if (mOrderDetail.getShipping_address() != null) {
            StringBuilder street = new StringBuilder();
            for (int i = 0; i < mOrderDetail.getShipping_address().getStreet().size(); i++) {
                street.append(mOrderDetail.getShipping_address().getStreet().get(i)).append(", ");
            }
            mShipStreet.setText(street);
            StringBuilder city = new StringBuilder();
            city.append(mOrderDetail.getShipping_address().getCity()).append(", ").append(mOrderDetail.getShipping_address().getState());
            mShipCity.setText(city);
        }
        if (mOrderDetail.getPayment_info() != null) {
            mPayMethod.setText(mOrderDetail.getPayment_info().getInfo());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}