package in.ateesinfomedia.remedio.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.interfaces.DoctorsClickListner;
import in.ateesinfomedia.remedio.models.ConstituencyModel;
import in.ateesinfomedia.remedio.models.DepartmentModel;
import in.ateesinfomedia.remedio.models.DoctorsModel;
import in.ateesinfomedia.remedio.models.LaboratoryModel;
import in.ateesinfomedia.remedio.view.adapter.LaborataryAdapter;
import in.ateesinfomedia.remedio.widgets.searchdialog.OnSearchItemSelected;
import in.ateesinfomedia.remedio.widgets.searchdialog.SearchListItem;
import in.ateesinfomedia.remedio.widgets.searchdialog.SearchableDialog;

import static in.ateesinfomedia.remedio.configurations.Global.ConstituencyList;
import static in.ateesinfomedia.remedio.configurations.Global.HospitalList;

public class HospitalActivity extends AppCompatActivity implements DoctorsClickListner {

    private EditText mEtDepartment,mEtConstituency;
    private ImageView mImSearch;
    private RecyclerView mRecyclerViewDoctors;

    private List<DepartmentModel> departmentList = new ArrayList<>();
    //    private List<DoctorsModel> doctorsList = new ArrayList<>();
    private List<LaboratoryModel> hospitalList = new ArrayList<>();
    //    private List<DoctorsModel> filteredDoctorsList = new ArrayList<>();
    private List<LaboratoryModel> filteredDoctorsList = new ArrayList<>();
    private List<ConstituencyModel> constituencyList = new ArrayList<>();
    private LaborataryAdapter mAdapter;
    private Toolbar toolbar;
    private List<SearchListItem> searchHospitalList = new ArrayList<>();
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

        setContentView(R.layout.activity_hospital);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtDepartment = (EditText) findViewById(R.id.et_department);
        mEtConstituency = (EditText) findViewById(R.id.et_constituency);
        mImSearch = (ImageView) findViewById(R.id.im_search);
        mRecyclerViewDoctors = (RecyclerView) findViewById(R.id.recycler_hospital);

        hospitalList = HospitalList;
        constituencyList = ConstituencyList;
//        LoadHospitals();

        mRecyclerViewDoctors.setNestedScrollingEnabled(true);
        mAdapter = new LaborataryAdapter(this,hospitalList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        ShimmerRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewDoctors.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        mRecyclerViewDoctors.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewDoctors.setAdapter(mAdapter);

        mEtConstituency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadConstituencyList();
                SearchableDialog searchableDialog = new SearchableDialog(HospitalActivity.this, searchConstituencyList, "Select Constituency");
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
//                loadDepartmentList();
                LoadHospitals();
                SearchableDialog searchableDialog = new SearchableDialog(HospitalActivity.this, searchHospitalList, "Select Hospital");
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
                    Toast.makeText(HospitalActivity.this, "Please add any credential", Toast.LENGTH_SHORT).show();
                } else if (!mEtDepartment.getText().toString().isEmpty() && !mEtConstituency.getText().toString().isEmpty()){
//                    Toast.makeText(DoctorActivity.this, "valiues\n" + mEtConstituency.getText().toString() + "\n" + mEtDepartment.getText().toString(), Toast.LENGTH_SHORT).show();

                    doFilterList("both");
                } else {
                    if (mEtDepartment.getText().toString().isEmpty()){
//                        doFilterListOne("constituency");
                        doFilterList("constituency");
                    } else {
//                        doFilterListOne("department");
                        doFilterList("department");
                    }
                }
            }
        });
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

    private void LoadHospitals() {
        searchHospitalList.clear();
        for (int i = 0;i<hospitalList.size();i++){
            String id = hospitalList.get(i).getId();
            String name = hospitalList.get(i).getName();

            SearchListItem searchListItem = new SearchListItem(Integer.valueOf(id),name);
            searchHospitalList.add(searchListItem);

        }
    }

    private void doFilterList(String type) {
        List<LaboratoryModel> temp = new ArrayList();
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
            for(LaboratoryModel d: hospitalList){
                //or use .contains(text)
                if(Pattern.compile(Pattern.quote(dep_name), Pattern.CASE_INSENSITIVE).matcher(d.getName()).find()
                        && Pattern.compile(Pattern.quote(con_name), Pattern.CASE_INSENSITIVE).matcher(d.getConstituency_name()).find()){
                    temp.add(d);
                }
            }
        } else if (dep_name == null && con_name != null) {
            for (LaboratoryModel d : hospitalList) {
                //or use .contains(text)
                if (Pattern.compile(Pattern.quote(con_name), Pattern.CASE_INSENSITIVE).matcher(d.getConstituency_name()).find()) {
                    temp.add(d);
                }
            }
        } else if (dep_name != null && con_name == null){
            for (LaboratoryModel d : hospitalList) {
                //or use .contains(text)
                if (Pattern.compile(Pattern.quote(dep_name), Pattern.CASE_INSENSITIVE).matcher(d.getName()).find()) {
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

    private void updateList(List<LaboratoryModel> temp) {
        filteredDoctorsList.clear();
        filteredDoctorsList = temp;
        if (filteredDoctorsList.size()<1 || filteredDoctorsList==null){
            Toast.makeText(this, "No search result found", Toast.LENGTH_SHORT).show();

            mAdapter = new LaborataryAdapter(this,hospitalList,this);
            mRecyclerViewDoctors.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        } else {
            mAdapter = new LaborataryAdapter(this,filteredDoctorsList,this);
            mRecyclerViewDoctors.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void itemClicked(int position, View view, View view1, Object o) {
        LaboratoryModel laboratoryModel = (LaboratoryModel) o;

        Intent intent = new Intent(HospitalActivity.this,HospitalDetailsActivity.class);
        intent.putExtra("myList",laboratoryModel);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
