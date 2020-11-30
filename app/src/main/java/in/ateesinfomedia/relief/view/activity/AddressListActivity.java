package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AddressClickListner;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.cart.CartModel;
import in.ateesinfomedia.relief.models.cart.CartResponse;
import in.ateesinfomedia.relief.models.login.CustomerAddress;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.models.state.StateModel;
import in.ateesinfomedia.relief.view.adapter.AddressAdapter;
import in.ateesinfomedia.relief.view.adapter.CartAdapter;
import in.ateesinfomedia.relief.view.fragment.AddAddressFragment;
import in.ateesinfomedia.relief.view.fragment.ShippingCostFragment;

import static in.ateesinfomedia.relief.configurations.Global.AddressStateList;
import static in.ateesinfomedia.relief.configurations.Global.CustomerAddressModel;
import static in.ateesinfomedia.relief.configurations.Global.IsAddressSelected;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class AddressListActivity extends AppCompatActivity implements NetworkCallback,
        AddressClickListner, AddAddressFragment.OnAddAddressListener, ShippingCostFragment.OnShippingCostListener {

    private Toolbar toolbar;
    private MyPreferenceManager manager;
    private int REQUEST_CART_ADDRESS_LIST = 8568;
    private List<CustomerAddress> addressList = new ArrayList<>();
    private static final String TAG = AddressListActivity.class.getName();
    private Gson gson = new Gson();
    private RelativeLayout mNoDataLay;
    private ConstraintLayout mDataLay;
    private AddressAdapter mAdapter;
    private RecyclerView recyclerView;
    private ConstraintLayout addAddress;
    private MaterialCardView addressFooter;
    private int REQUEST_ADDRESS_STATE_LIST = 8668;
    private List<StateModel> stateList = new ArrayList<>();
    private boolean addressSelected = false;
    private AppCompatButton addressDeliverBtn;
    private int REQUEST_ADDRESS_DELETE = 8768;

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

        setContentView(R.layout.activity_address_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mNoDataLay = (RelativeLayout) findViewById(R.id.noDataLayAddress);
        mDataLay = (ConstraintLayout) findViewById(R.id.dataLayAddress);
        recyclerView = (RecyclerView) findViewById(R.id.rv_address_list);
        addAddress = (ConstraintLayout) findViewById(R.id.address_add_new);
        addressFooter = (MaterialCardView) findViewById(R.id.address_footer_cardView);
        addressDeliverBtn = (AppCompatButton) findViewById(R.id.address_deliver_btn);

        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        manager = new MyPreferenceManager(this);

        String info = getIntent().getStringExtra("info");

        getAddressList();

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAddressFragment dialog = new AddAddressFragment();
                Bundle bundle = new Bundle();
                bundle.putString("action", "add");
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "MyCustomDialog");
            }
        });

        addressDeliverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info.equals("cart")) {
                    goToShippingCostActivity();
                } else {
                    goToPrescActivity();
                }
            }
        });
    }

    private void goToShippingCostActivity() {
        if (addressSelected) {
            /*Intent intent = new Intent(AddressListActivity.this, ShippingCostActivity.class);
            startActivity(intent);*/
            ShippingCostFragment dialog = new ShippingCostFragment();
            dialog.show(getSupportFragmentManager(), "MyShippingCostDialog");
        } else {
            String message = "";
            if (addressList.size() == 0) {
                message = "Please add new delivery address.";
            } else {
                message = "Please choose a delivery address.";
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void goToPrescActivity() {
        if (addressSelected) {
            finish();
        } else {
            String message = "";
            if (addressList.size() == 0) {
                message = "Please add new delivery address.";
            } else {
                message = "Please choose a delivery address.";
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAddressList() {
        String token = manager.getUserToken();
        new NetworkManager(this).doGetCustom(
                null,
                Apis.API_GET_CART_ADDRESS_LIST,
                CartResponse.class,
                null,
                token,
                "REQUEST_CART_ADDRESS_LIST",
                REQUEST_CART_ADDRESS_LIST,
                this
        );
        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void getStateList() {
        String token = manager.getUserToken();
        String getUrl = Apis.API_GET_STATE_LIST + "IN";
        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                StateModel[].class,
                null,
                token,
                "REQUEST_ADDRESS_STATE_LIST",
                REQUEST_ADDRESS_STATE_LIST,
                this
        );
        //LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void deleteAddress(CustomerAddress address) {
        String getUrl = Apis.API_GET_DELETE_ADDRESS + address.getId();
        new NetworkManager(this).doGetCustom(
                null,
                getUrl,
                AddAddressResponse[].class,
                null,
                Apis.ACCESS_TOKEN,
                "REQUEST_ADDRESS_DELETE",
                REQUEST_ADDRESS_DELETE,
                this
        );
        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void ProcessJsonDeleteAddress(String response) {

        try {
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<ArrayList<AddAddressResponse>>(){}.getType();
                ArrayList<AddAddressResponse> addressResponse = gson.fromJson(response, type);
                String message = addressResponse.get(0).getMessage();
                if (message.equals("success")) {
                    LoadingDialog.cancelLoading();
                    Toast.makeText(this, "Address removed successfully", Toast.LENGTH_SHORT).show();
                    getAddressList();
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(this, message);
                }
            } else {
                LoadingDialog.cancelLoading();
                dialogWarning(AddressListActivity.this, "Sorry ! Can't connect to server, try later");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(AddressListActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonCartAddress(String response) {
        //Log.d(TAG, response);
        addressList.clear();
        try {
            Type type = new TypeToken<CartResponse>(){}.getType();
            CartResponse cartResponse = gson.fromJson(response, type);
            //LoadingDialog.cancelLoading();

            if (cartResponse != null &&
                    cartResponse.getCustomer() != null &&
                    cartResponse.getCustomer().getAddresses() != null) {
                addressList = cartResponse.getCustomer().getAddresses();
                if (addressList.size() == 0){
                    mNoDataLay.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    addressFooter.setVisibility(View.VISIBLE);
                } else {
                    mNoDataLay.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    addressFooter.setVisibility(View.VISIBLE);
                    mAdapter = new AddressAdapter(this, addressList,this);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            getStateList();
        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(AddressListActivity.this, "Sorry ! Can't connect to server, try later");
        }

    }

    private void processJsonStateList(String response) {
        //Log.d(TAG, response);
        stateList.clear();
        try {
            Type type = new TypeToken<ArrayList<StateModel>>(){}.getType();
            ArrayList<StateModel> mStateList = gson.fromJson(response, type);
            LoadingDialog.cancelLoading();

            if (mStateList != null && !mStateList.isEmpty()) {
                stateList = mStateList;
                AddressStateList = stateList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(AddressListActivity.this, "Sorry ! Can't connect to server, try later");
        }

    }

    @Override
    public void onAddressItemClicked(int position, CustomerAddress address) {
        CustomerAddressModel = address;
        IsAddressSelected = true;
        addressSelected = true;
    }

    @Override
    public void onAddressEditClicked(int position, CustomerAddress address) {
        CustomerAddressModel = address;
        AddAddressFragment dialog = new AddAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("action", "edit");
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "MyCustomDialog");
    }

    @Override
    public void onAddressDeleteClicked(int position, CustomerAddress address) {
        deleteAddress(address);
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){

            if (requestId == REQUEST_CART_ADDRESS_LIST){
                processJsonCartAddress(response);
            } else if (requestId == REQUEST_ADDRESS_STATE_LIST){
                processJsonStateList(response);
            } else if (requestId == REQUEST_ADDRESS_DELETE){
                ProcessJsonDeleteAddress(response);
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
    public void newAddress() {
        getAddressList();
    }

    @Override
    public void shipCostSelected() {
        Intent intent = new Intent(AddressListActivity.this, OrderSummaryActivity.class);
        startActivity(intent);
    }
}