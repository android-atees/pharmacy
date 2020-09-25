package in.ateesinfomedia.remedio.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.DoctorsClickListner;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.ConstituencyModel;
import in.ateesinfomedia.remedio.models.DepartmentModel;
import in.ateesinfomedia.remedio.models.DoctorsDetailModel;
import in.ateesinfomedia.remedio.models.DoctorsModel;
import in.ateesinfomedia.remedio.view.adapter.DoctorsAdapter;
import in.ateesinfomedia.remedio.widgets.searchdialog.OnSearchItemSelected;
import in.ateesinfomedia.remedio.widgets.searchdialog.SearchListItem;
import in.ateesinfomedia.remedio.widgets.searchdialog.SearchableDialog;

import static in.ateesinfomedia.remedio.configurations.Global.ConstituencyList;
import static in.ateesinfomedia.remedio.configurations.Global.DepartmentList;
import static in.ateesinfomedia.remedio.configurations.Global.DoctorsDetailsList;
import static in.ateesinfomedia.remedio.configurations.Global.DoctorsList;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class DoctorActivity extends AppCompatActivity implements NetworkCallback, DoctorsClickListner {

    private EditText mEtDepartment,mEtConstituency;
    private ImageView mImSearch;
    private RecyclerView mRecyclerViewDoctors;

    private List<DepartmentModel> departmentList = new ArrayList<>();
    private List<DoctorsModel> doctorsList = new ArrayList<>();
    private List<DoctorsModel> filteredDoctorsList = new ArrayList<>();
    private List<ConstituencyModel> constituencyList = new ArrayList<>();
    private DoctorsAdapter mAdapter;
    private Toolbar toolbar;
    private List<SearchListItem> searchDepartmentList = new ArrayList<>();
    private List<SearchListItem> searchConstituencyList = new ArrayList<>();
    private SearchListItem mDepartmentObj;
    private SearchListItem mConstituencyObj;
    private int REQUEST_DOCTERES_DETAILS_ID = 8767;
    private DoctorsModel doctorsModel;
    private View view;
    private View view1;

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

        setContentView(R.layout.activity_doctor);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtDepartment = (EditText) findViewById(R.id.et_department);
        mEtConstituency = (EditText) findViewById(R.id.et_constituency);
        mImSearch = (ImageView) findViewById(R.id.im_search);
        mRecyclerViewDoctors = (RecyclerView) findViewById(R.id.recycler_doctors);

        departmentList = DepartmentList;
        doctorsList = DoctorsList;
        constituencyList = ConstituencyList;

        mRecyclerViewDoctors.setNestedScrollingEnabled(true);
        mAdapter = new DoctorsAdapter(this,doctorsList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        ShimmerRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewDoctors.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        mRecyclerViewDoctors.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewDoctors.setAdapter(mAdapter);

        mEtConstituency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadConstituencyList();
                SearchableDialog searchableDialog = new SearchableDialog(DoctorActivity.this, searchConstituencyList, "Select Constituency");
                searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
                    @Override
                    public void onClick(int i, SearchListItem searchListItem) {

                        mConstituencyObj= searchListItem;
                        mEtConstituency.setText(searchListItem.getTitle());
                    }
                });
                searchableDialog.show();
            }
        });

        mEtDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDepartmentList();
                SearchableDialog searchableDialog = new SearchableDialog(DoctorActivity.this, searchDepartmentList, "Select Department");
                searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
                    @Override
                    public void onClick(int i, SearchListItem searchListItem) {

                        mDepartmentObj= searchListItem;
                        mEtDepartment.setText(searchListItem.getTitle());
                    }
                });
                searchableDialog.show();
            }
        });

        mImSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtDepartment.getText().toString().isEmpty() && mEtConstituency.getText().toString().isEmpty()){
                    Toast.makeText(DoctorActivity.this, "Please add any credential", Toast.LENGTH_SHORT).show();
                } else if (!mEtDepartment.getText().toString().isEmpty() && !mEtConstituency.getText().toString().isEmpty()){
//                    Toast.makeText(DoctorActivity.this, "valiues\n" + mEtConstituency.getText().toString() + "\n" + mEtDepartment.getText().toString(), Toast.LENGTH_SHORT).show();
                    doFilterList("both");
                } else {
                    if (mEtDepartment.getText().toString().isEmpty()){
//                        doFilterListOne("constituency");
                        doFilterList("constituency");
                    } else {
                       //doFilterListOne("department");
                        doFilterList("department");
                    }
                }
            }
        });
    }

    private void doFilterListOne(String type) {
        List<DoctorsModel> temp = new ArrayList();
        String mType = type;
//        if (text.length() != 0){
        if (mType.equals("department")) {
            for (DoctorsModel d : doctorsList) {
                //or use .contains(text)
                if (Pattern.compile(Pattern.quote(mType), Pattern.CASE_INSENSITIVE).matcher(d.getDept_name()).find()) {
                    temp.add(d);
                }
            }
        } else {
            for (DoctorsModel d : doctorsList) {
                //or use .contains(text)
                if (Pattern.compile(Pattern.quote(mType), Pattern.CASE_INSENSITIVE).matcher(d.getConstituency_name()).find()) {
                    temp.add(d);
                }
            }
        }
//        }else{
//            temp = filteredRestaurantsList;
//        }
        //update recyclerview
        updateList(temp);
    }

    private void doFilterList(String type) {
        List<DoctorsModel> temp = new ArrayList();
        String dep_name = null;
        String con_name = null;
        if (type.equals("both")) {
            dep_name = mDepartmentObj.getTitle();
            con_name = mConstituencyObj.getTitle();
        } else if (type.equals("constituency")){
            con_name = mConstituencyObj.getTitle();
        } else {
            dep_name = mDepartmentObj.getTitle();
        }
//        if (text.length() != 0){
        if (dep_name!=null && con_name != null){
            for(DoctorsModel d: doctorsList){
                //or use .contains(text)
                if(Pattern.compile(Pattern.quote(dep_name), Pattern.CASE_INSENSITIVE).matcher(d.getDept_name()).find()
                        && Pattern.compile(Pattern.quote(con_name), Pattern.CASE_INSENSITIVE).matcher(d.getConstituency_name()).find()){
                    temp.add(d);
                }
            }
        } else if (dep_name == null && con_name != null) {
            for (DoctorsModel d : doctorsList) {
                //or use .contains(text)
                if (Pattern.compile(Pattern.quote(con_name), Pattern.CASE_INSENSITIVE).matcher(d.getConstituency_name()).find()) {
                    temp.add(d);
                }
            }
        } else if (dep_name != null && con_name == null){
            for (DoctorsModel d : doctorsList) {
                //or use .contains(text)
                if (Pattern.compile(Pattern.quote(dep_name), Pattern.CASE_INSENSITIVE).matcher(d.getDept_name()).find()) {
                    temp.add(d);
                }
            }
        }
        Log.v("FILTER",""+temp);
//        }else{
//            temp = filteredRestaurantsList;
//        }
        //update recyclerview
        updateList(temp);
    }

    private void updateList(List<DoctorsModel> temp) {
        filteredDoctorsList.clear();
        filteredDoctorsList = temp;
        if (filteredDoctorsList.size()<1 || filteredDoctorsList==null){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();

            mEtConstituency.setText("");
            mEtDepartment.setText("");

            mAdapter = new DoctorsAdapter(this,doctorsList,this);
            mRecyclerViewDoctors.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        } else {
            mAdapter = new DoctorsAdapter(this,filteredDoctorsList,this);
            mRecyclerViewDoctors.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void loadConstituencyList() {
        searchConstituencyList.clear();
        for (int i = 0;i<constituencyList.size();i++){
            int id = Integer.valueOf(constituencyList.get(i).getId());
            String name = constituencyList.get(i).getConstituency_name();
            SearchListItem searchListItem = new SearchListItem(id,name);
            searchConstituencyList.add(searchListItem);
        }
    }

    private void loadDepartmentList() {
        searchDepartmentList.clear();
        for (int i = 0;i<departmentList.size();i++){
            int id = Integer.valueOf(departmentList.get(i).getId());
            String name = departmentList.get(i).getDept_name();
            SearchListItem searchListItem = new SearchListItem(id,name);
            searchDepartmentList.add(searchListItem);
        }
    }

//    @Override
//    public void itemClicked(int position, Object object) {
//
//        doctorsModel = (DoctorsModel) object;
//
//        Map<String,String> map = new HashMap<>();
//        map.put("doctor_id",doctorsModel.getId());
//
//        new NetworkManager(this).doPost(map, Apis.API_POST_GET_DOCTORS_DETAILS,"REQUEST_DOCTERES_DETAILS",REQUEST_DOCTERES_DETAILS_ID,this);
//
//        LoadingDialog.showLoadingDialog(this,"Loading....");
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_DOCTERES_DETAILS_ID){
                ProcessJsonDocDetails(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void ProcessJsonDocDetails(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                Toast.makeText(this, "Appointments not available now...", Toast.LENGTH_SHORT).show();
                LoadingDialog.cancelLoading();
            } else {

                List<DoctorsDetailModel> doctorsDetailList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int k = 0;k<jsonArray.length();k++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(k);
                    DoctorsDetailModel doctorsDetailModel = new DoctorsDetailModel();

                    doctorsDetailModel.setId(jsonObject1.optString("id"));
                    doctorsDetailModel.setHospital_name(jsonObject1.optString("hospital_name"));
                    doctorsDetailModel.setHospital_address(jsonObject1.optString("hospital_address"));
                    doctorsDetailModel.setConsulting_days(jsonObject1.optString("consulting_days"));
                    doctorsDetailModel.setConsulting_time(jsonObject1.optString("consulting_time"));
                    doctorsDetailModel.setHospital_phone(jsonObject1.optString("hospital_phone"));

                    doctorsDetailList.add(doctorsDetailModel);
                }
                LoadingDialog.cancelLoading();
                DoctorsDetailsList = doctorsDetailList;
                Intent intent = new Intent(DoctorActivity.this,DoctorDetailsActivity.class);
//                intent.putExtra("doc_list", (Serializable) doctorsModel);
//                startActivity(intent);
//
//
//                Intent intent = new Intent(getActivity(), MenuActivity.class);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


                    View sharedView = view;
                    String transitionName = getString(R.string.doc_image);
//                    String transitionName1 = getString(R.string.doc_name);
                    ActivityOptions transitionActivityOptions = null;
                    intent.putExtra("doc_list", (Serializable) doctorsModel);
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, transitionName);
                    startActivity(intent, transitionActivityOptions.toBundle());
                } else {

                    intent.putExtra("doc_list", (Serializable) doctorsModel);
                    startActivity(intent);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void itemClicked(int position, View view, View view1, Object o) {
        doctorsModel = (DoctorsModel) o;
        this.view = view;
        this.view1 = view1;

        Map<String,String> map = new HashMap<>();
        map.put("doctor_id",doctorsModel.getId());

        new NetworkManager(this).doPost(map, Apis.API_POST_GET_DOCTORS_DETAILS,"REQUEST_DOCTERES_DETAILS",REQUEST_DOCTERES_DETAILS_ID,this);

        LoadingDialog.showLoadingDialog(this,"Loading....");
    }
}