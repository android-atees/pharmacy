package in.ateesinfomedia.remedio.view.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.configurations.Global;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.EarningsModel;
import in.ateesinfomedia.remedio.view.fragment.ReferenceEarnFragment;
import in.ateesinfomedia.remedio.view.fragment.SaleEarnFragment;

import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class RewardsActivity extends AppCompatActivity implements NetworkCallback {

    private  float sales_earn=0.0f;
    private  float ref_earn=0.0f;
    private  float grand_total=0.0f;


    private Toolbar toolbar;
    private MyPreferenceManager manager;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int REQUEST_EARNINGS_ID = 8999;
    CardView tprice_card;
    private TextView total;


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

        setContentView(R.layout.activity_rewards);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new MyPreferenceManager(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tprice_card=(CardView) findViewById(R.id.tprice_card);
        total=(TextView) findViewById(R.id.tprice_amt);


        doGetEarnings();
    }

    private void doGetEarnings() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map, Apis.API_POST_GET_EARNINGSES,"REQUEST_EARNINGS",REQUEST_EARNINGS_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading....");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReferenceEarnFragment(), "Reference Earnings");
        adapter.addFragment(new SaleEarnFragment(), "Sale Earnings");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_EARNINGS_ID){
                processGetEarnings(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processGetEarnings(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                List<EarningsModel> referenceEarnList = new ArrayList<>();
                List<EarningsModel> saleEarnList = new ArrayList<>();

                JSONObject jsonObject1 = jsonObject.optJSONObject("data");

                JSONArray jsonArray = jsonObject1.optJSONArray("reference");
                if (jsonArray != null && jsonArray.length()>0) {
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                        EarningsModel earningsModel = new EarningsModel();

                        earningsModel.setName(jsonObject2.optString("firstname"));
                        earningsModel.setCreate_date(jsonObject2.optString("create_date"));
                        earningsModel.setPhone(jsonObject2.optString("phone"));
                        earningsModel.setRef_amount(jsonObject2.optString("ref_amount"));

                        ref_earn=Float.parseFloat(jsonObject2.optString("ref_amount"))+ref_earn;

                        referenceEarnList.add(earningsModel);
                    }

                   Global.TOTAL_REF_EARN=String.valueOf(ref_earn);
                }

                JSONArray jsonArray1 = jsonObject1.optJSONArray("sale");
                if (jsonArray1 != null && jsonArray1.length()>0) {
                    for (int i = 0;i<jsonArray1.length();i++){
                        JSONObject jsonObject3 = jsonArray1.optJSONObject(i);
                        EarningsModel earningsModel = new EarningsModel();

                        earningsModel.setName(jsonObject3.optString("firstname"));
                        earningsModel.setCreate_date(jsonObject3.optString("create_date"));
                        earningsModel.setPhone(jsonObject3.optString("phone"));
                        earningsModel.setRef_amount(jsonObject3.optString("commission"));

                        sales_earn=Float.parseFloat(jsonObject3.optString("commission"))+sales_earn;

                        saleEarnList.add(earningsModel);
                    }

                    Global.TOTAL_SALES_EARN=String.valueOf(sales_earn);
                }

                Global.ReferenceEarnList = referenceEarnList;
                Global.SaleEarnList= saleEarnList;
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
                LoadingDialog.cancelLoading();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        }

        grand_total=sales_earn+ref_earn;

        total.setText("₹ "+grand_total);
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
