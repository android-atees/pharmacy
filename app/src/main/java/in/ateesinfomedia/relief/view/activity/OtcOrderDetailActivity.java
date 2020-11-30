package in.ateesinfomedia.relief.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.OtcOrderDetailsModel;
import in.ateesinfomedia.relief.view.adapter.OtcOrderDeatailsAdapter;
import in.ateesinfomedia.relief.view.adapter.OtcOrderModel;

import static in.ateesinfomedia.relief.components.ConnectivityReceiver.isConnected;

public class OtcOrderDetailActivity extends AppCompatActivity implements NetworkCallback, AdapterView.OnItemSelectedListener, AdapterClickListner {

    private List<OtcOrderDetailsModel> orderDetailsList = new ArrayList<>();
    private OtcOrderModel otcOrderModel;

    private RecyclerView mRecyclerView;
    private TextView grandTotalAmt,Grandtotal,orderId,date,itemCount,name,address,place,pincode,subTotal,deliveyFee,tvViewInMap;
    //    private StepperIndicator tracker;
//    private String[] item = new String[]{"Draft","Confirm","Ready","Delivered"};
//    private String[] items = new String[]{"Draft","Confirm","Ready","Cancelled"};
    private NestedScrollView scrollView;
    private SwipeRefreshLayout swipeContainer;
    private int REQUEST_ORDER_DETAILS =8787;
    private OtcOrderDeatailsAdapter mAdapter;
    private Toolbar toolbar;
    private MenuItem mMenu;
    private int REQUEST_CANCEL_OTC_ORDERS_ID = 8790;
    private int REQUEST_CANCEL_REASONS_ID = 9088;
    private AlertDialog mADialogLog;
    String[] reasons = { "The delivery is delayed", "My reason is not listed", "Order placed by mistake", "Need to change shipping address",
            "Bought it from somewhere else","Will be unavailable at home on delivery date","Expected delivery time is too long", "Item Price or shipping cost is too high","Other"};
    private String resson;
    private String discrip;
    private MyPreferenceManager manager;
    private OtcOrderDetailsModel model;

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

        setContentView(R.layout.activity_otc_order_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderDetailsList.clear();

        manager = new MyPreferenceManager(this);

        orderDetailsList =(ArrayList<OtcOrderDetailsModel>) getIntent().getSerializableExtra("Order_details_list");
        otcOrderModel= (OtcOrderModel) getIntent().getSerializableExtra("Order_list");

        scrollView = (NestedScrollView)  findViewById(R.id.scroll);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mRecyclerView = (RecyclerView) findViewById(R.id.orderDetailsRecycler);
        grandTotalAmt = (TextView) findViewById(R.id.grandTotalAmt);
        orderId = (TextView) findViewById(R.id.orderId);
        date = (TextView) findViewById(R.id.date);
        itemCount = (TextView) findViewById(R.id.itemCount);
        tvViewInMap = (TextView) findViewById(R.id.tvViewInMap);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        place = (TextView) findViewById(R.id.place);
        pincode = (TextView) findViewById(R.id.pincode);

        scrollView.post(new Runnable() {

            public void run() {

                scrollView.scrollTo(0,0);
            }
        });

        orderId.requestFocus();

        grandTotalAmt.setText("₹ "+otcOrderModel.getTotal());
//        Grandtotal.setText("₹ "+otcOrderModel.getTotal());
        orderId.setText(otcOrderModel.getOrder_id());
        date.setText(otcOrderModel.getCreate_date());
        itemCount.setText(otcOrderModel.getCount());
        name.setText(orderDetailsList.get(0).getDisplay_name());
        address.setText(orderDetailsList.get(0).getAddressline());
        place.setText(orderDetailsList.get(0).getTown()+ "," +orderDetailsList.get(0).getState());
        pincode.setText(orderDetailsList.get(0).getPincode());

        if (orderDetailsList.get(0).getTracking().equals("0")){
            tvViewInMap.setVisibility(View.GONE);
        } else if (orderDetailsList.get(0).getTracking().equals("1")){
            tvViewInMap.setVisibility(View.VISIBLE);
        } else if (orderDetailsList.get(0).getTracking().equals("2")){
            tvViewInMap.setVisibility(View.GONE);
        }

        tvViewInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtcOrderDetailActivity.this,MapsTrackingActivity.class);
                intent.putExtra("lat",orderDetailsList.get(0).getMlat());
                intent.putExtra("long",orderDetailsList.get(0).getmLong());
                intent.putExtra("orderId",otcOrderModel.getId());
                intent.putExtra("from","otc");
                startActivity(intent);
            }
        });
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                doUpdateData();
//            }
//        });

        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new OtcOrderDeatailsAdapter(this,orderDetailsList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

//        getCancelReasons();
    }

    private void getCancelReasons() {

        new NetworkManager(this).doPost(null,Apis.API_POST_CANCEL_REASONS,"REQUEST_CANCEL_REASONS",REQUEST_CANCEL_REASONS_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cancel,menu);
//        this.mMenu = menu.findItem(R.id.action_cancel);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        ShowCancelPopUp();
//        return super.onOptionsItemSelected(item);
//    }

    private void ShowCancelPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_cancel, null);
        builder.setView(view);

        EditText mEtComment = view.findViewById(R.id.etComment);
        Button save = (Button) view.findViewById(R.id.btn_submit);
        Button cancel = (Button) view.findViewById(R.id.btn_close);
        Spinner spin = (Spinner) view.findViewById(R.id.etSpinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,reasons);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

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
                    Toast.makeText(OtcOrderDetailActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
                        discrip = mEtComment.getText().toString();

                       cancelOrder();
                    } else {
                        Toast.makeText(OtcOrderDetailActivity.this, "Please select the reason", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean isFormValidated() {

//                String phNumber = number.getText().toString();
//
                boolean isError = false;
//                View mFocusView = null;
//
//                number.setError(null);
//
                if (!isDataValid(resson)) {
                    isError = true;
                }

                if (isError) {
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

    private void cancelOrder(){
        Map<String,String> map = new HashMap<>();
        map.put("order_item_id",model.getOrder_item_id());
        map.put("order_id",model.getOrder_id());
        map.put("Product_id",model.getProduct_id());
        map.put("userid",manager.getdelicioId());
        map.put("Reason_select",resson);
        map.put("cancell_dis",discrip);

        new NetworkManager(this).doPost(map, Apis.API_POST_CANCEL_OTC_ORDERS,"REQUEST_CANCEL_OTC_ORDERS",REQUEST_CANCEL_OTC_ORDERS_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResponse(int status, String response, int requestId) {

        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_CANCEL_OTC_ORDERS_ID){
              processCancelOrder(response);
            } else if (requestId == REQUEST_CANCEL_REASONS_ID){
                processCancelOrderReasons(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't connect with the server.", Toast.LENGTH_SHORT).show();
        }
    }

    private void processCancelOrderReasons(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    private void processCancelOrder(String response) {

        LoadingDialog.cancelLoading();

        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                Toast.makeText(this, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),reasons[position] , Toast.LENGTH_LONG).show();
        resson = reasons[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void itemClicked(int position, Object object) {

        model = (OtcOrderDetailsModel) object;
        ShowCancelPopUp();
    }

    @Override
    public void buttonClicked(int position, Object object) {
        //
    }
}
