package in.ateesinfomedia.relief.view.activity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.GPSTracker;
import in.ateesinfomedia.relief.components.LocationAddress;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.configurations.Global;
import in.ateesinfomedia.relief.models.LaboratoryModel;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static in.ateesinfomedia.relief.configurations.Global.Latitude;
import static in.ateesinfomedia.relief.configurations.Global.Longitude;

public class LaborataryDetailsActivity extends AppCompatActivity {

    private LaboratoryModel laboratoryModel;
    private TextView mlab_name,mqualification,mviewmap,mdiscription,maddress,mworking;
    private ImageView mlab_image;
    private LocationAddress locationAddress;
    private int REQUEST_LOCATION = 8765;
    private GPSTracker locationTrack;
    private double latitude;
    private double longitude;
    private RelativeLayout mContainer;
    private String lati;
    private String longi;
    private Toolbar toolbar;

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

        setContentView(R.layout.activity_laboratary_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        laboratoryModel = (LaboratoryModel) getIntent().getSerializableExtra("myList");

        mlab_image = (ImageView) findViewById(R.id.lab_image);
        mlab_name = (TextView) findViewById(R.id.lab_name);
        mqualification = (TextView) findViewById(R.id.qualification);
        mdiscription = (TextView) findViewById(R.id.discription);
        maddress = (TextView) findViewById(R.id.address);
        mworking = (TextView) findViewById(R.id.working);
        mviewmap = (TextView) findViewById(R.id.viewmap);
        mContainer = (RelativeLayout) findViewById(R.id.mainLay);

        locationAddress = new LocationAddress();
        loadLocation();

        mlab_name.setText(laboratoryModel.getName());
        mqualification.setText(laboratoryModel.getPlace());
        mdiscription.setText(laboratoryModel.getDescription());
        maddress.setText(laboratoryModel.getAddress()+","+laboratoryModel.getPlace()+",\n"+laboratoryModel.getPincode());
        mworking.setText(laboratoryModel.getOpening_days() + "  " + laboratoryModel.getOpening_time());

        Glide.with(this).load(Apis.API_POST_DOCTORS_LOGO_PATH + laboratoryModel.getImage()).into(mlab_image);

        mviewmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                lati = String.valueOf(Latitude);
                longi = String.valueOf(Longitude);
                Log.d("Locationnnnnnnnnn",lati+"   ,   "+longi+" > "+laboratoryModel.getLat()+" > "+laboratoryModel.getLongi());
//                Toast.makeText(LaborataryDetailsActivity.this,"Location"+lati, Toast.LENGTH_SHORT).show();
                if (lati.equals("") || lati.isEmpty() || lati.equals("null") || (laboratoryModel.getLat().equals("null") || laboratoryModel.getLat().isEmpty()) ){
                    Toast.makeText(LaborataryDetailsActivity.this,"Location not found..", Toast.LENGTH_SHORT).show();
                } else {

                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=" + Double.parseDouble(lati) + "," + Double.parseDouble(longi) + "&daddr=" + Double.parseDouble(laboratoryModel.getLat()) + "," + Double.parseDouble(laboratoryModel.getLongi()) + ","+ laboratoryModel.getPlace());

//                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", lat,lon,place );
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try
                    {
                        startActivity(intent);
                    }
                    catch(ActivityNotFoundException ex)
                    {
                        try
                        {
                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            unrestrictedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(unrestrictedIntent);
                        }
                        catch(ActivityNotFoundException innerEx)
                        {
                            Toast.makeText(LaborataryDetailsActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void loadLocation() {
        if (!mayRequestLocation()) {
            return;
        }

        // create class object
        locationTrack = new GPSTracker(this);

        // check if GPS enabled
        if(locationTrack.canGetLocation()){
            latitude = locationTrack.getLatitude();
            longitude = locationTrack.getLongitude();

            Global.Latitude = latitude;
            Global.Longitude = longitude;
            locationAddress.getAddressFromLocation(latitude, longitude,
                    this, new GeocoderHandler());
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            locationTrack.showSettingsAlert();
        }
    }

    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION) ) {
            Snackbar.make(mContainer, R.string.permission_rationale_location, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                        }
                    });
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        return false;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
//                    pincode = PinCode;
//                    mtxtPinCode.setText(PinCode);
                    break;
                default:
                    locationAddress = null;
            }
//            if (locationAddress.contains("null")){
//                location.setText("Location not found!!!");
//            } else {
//                location.setText(locationAddress);
//            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}