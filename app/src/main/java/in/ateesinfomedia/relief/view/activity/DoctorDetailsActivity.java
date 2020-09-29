package in.ateesinfomedia.relief.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mostafaaryan.transitionalimageview.TransitionalImageView;
import com.mostafaaryan.transitionalimageview.model.TransitionalImage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.AdapterClickListner;
import in.ateesinfomedia.relief.managers.PermissionManager;
import in.ateesinfomedia.relief.models.DoctorsDetailModel;
import in.ateesinfomedia.relief.models.DoctorsModel;
import in.ateesinfomedia.relief.view.adapter.HospitalsAdapter;

import static in.ateesinfomedia.relief.configurations.Global.DoctorsDetailsList;

public class DoctorDetailsActivity extends AppCompatActivity implements AdapterClickListner, PermissionManager.PermissionCallback {

    private RecyclerView detailsRecyclerView;
    private HospitalsAdapter mAdapter;
    private List<DoctorsDetailModel> doctorsDetailsList = new ArrayList<>();
    private String[] permissions = new String[] {Manifest.permission.CALL_PHONE};
    private PermissionManager mPermissionManager;
    private DoctorsDetailModel doctorsDetailModel;
    private DoctorsModel doctorsModel;
    private TextView mTvDocName,mTvQualification,mTvDepartment;
//    private ImageView mImDocImage;
    private TransitionalImageView mImDocImage;
    private Toolbar toolbar;
    private Bitmap bmp;

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

        setContentView(R.layout.activity_doctor_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPermissionManager = new PermissionManager(this);
        mPermissionManager.setRequiredPermissions(permissions);
        detailsRecyclerView = (RecyclerView) findViewById(R.id.details_recycler_view);
        mTvDocName = (TextView) findViewById(R.id.doc_name);
//        mImDocImage = (ImageView) findViewById(R.id.doc_image);
        mImDocImage = (TransitionalImageView) findViewById(R.id.doc_image);
        mTvQualification = (TextView) findViewById(R.id.qualification);
        mTvDepartment = (TextView) findViewById(R.id.department);

        doctorsDetailsList.clear();
        doctorsDetailsList = DoctorsDetailsList;
        doctorsModel = (DoctorsModel) getIntent().getSerializableExtra("doc_list");

        mTvDocName.setText(doctorsModel.getName());
        mTvQualification.setText(doctorsModel.getQualification());
        mTvDepartment.setText(doctorsModel.getDept_name());

        detailsRecyclerView.setNestedScrollingEnabled(true);
        mAdapter = new HospitalsAdapter(this,doctorsDetailsList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        detailsRecyclerView.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        detailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        detailsRecyclerView.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.default_value));
//            getWindow().setSharedElementEnterTransition(new ChangeBounds());
        }

        Glide.with(this).load(Apis.API_POST_DOCTORS_LOGO_PATH + doctorsModel.getImage()).into(mImDocImage);
        loadUserImage();
    }

    @Override
    public void itemClicked(int position, Object object) {
        doctorsDetailModel = (DoctorsDetailModel) object;
        Log.v("??CALL",""+mPermissionManager.isPermissionAvailable());
        if (mPermissionManager.isPermissionAvailable()) {
           doMakeCall(doctorsDetailModel);
        } else {
            mPermissionManager.makePermissionRequest(DoctorDetailsActivity.this);
        }
    }

    private void doMakeCall(DoctorsDetailModel doctorsDetailModel) {
        showDialogToCall(doctorsDetailModel);
    }

    private void showDialogToCall(final DoctorsDetailModel doctorsDetailModel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Make call?");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to make the call..");

        // On pressing Settings button
        alertDialog.setPositiveButton("call", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+doctorsDetailModel.getHospital_phone()));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.onPermissionResult(grantResults,DoctorDetailsActivity.this,true);
    }

    private void loadUserImage() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bmp = Picasso.with(DoctorDetailsActivity.this).load(Apis.API_POST_DOCTORS_LOGO_PATH+doctorsModel.getImage()).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                            tiv.setImage(b);
                        if (bmp!=null) {
                            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                                    .duration(500)
                                    .backgroundColor(getResources().getColor(R.color.colorBlur))
                                    //.image(R.drawable.sample_image)
                                    .image(bmp)
                                    .create();
                            mImDocImage.setTransitionalImage(transitionalImage);
                        } else {
                            TransitionalImage transitionalImage = new TransitionalImage.Builder()
                                    .duration(500)
                                    .backgroundColor(getResources().getColor(R.color.colorBlur))
                                    .image(R.drawable.ic_splash)
//                                    .image(bmp)
                                    .create();
                            mImDocImage.setTransitionalImage(transitionalImage);
                        }
                    }
                });

//                Toast.makeText(getActivity(), "entered"+getBytes(image),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap getBytes(DoctorsModel logo) {

        URL url = null;
        try {
            url = new URL(Apis.API_POST_DOCTORS_LOGO_PATH + doctorsModel.getImage());
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this, "noo", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        }
//        byte[] decodedBytes = Base64.decode(logo, Base64.DEFAULT);
        Toast.makeText(this, ""+bmp, Toast.LENGTH_SHORT).show();
        Bitmap decodedByte = bmp;
        return decodedByte;
    }

    @Override
    public void onGrandPermission() {
        doMakeCall(doctorsDetailModel);
    }

    @Override
    public void onDenyPermission() {
        Toast.makeText(this, "Allow permission to make the call", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}