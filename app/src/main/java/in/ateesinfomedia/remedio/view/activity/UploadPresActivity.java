package in.ateesinfomedia.remedio.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Pix;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.ateesinfomedia.remedio.BuildConfig;
import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.GPSTracker;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.components.LocationAddress;
import in.ateesinfomedia.remedio.components.VolleyMultipartRequest;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.configurations.Global;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.managers.PermissionManager;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import spencerstudios.com.bungeelib.Bungee;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static in.ateesinfomedia.remedio.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.remedio.configurations.Global.PinCode;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class UploadPresActivity extends AppCompatActivity implements PermissionManager.PermissionCallback, NetworkCallback {

    private Toolbar toolbar;
    private ImageView mImUpload;
    private int CAMERA_IMAGE_REQUEST_ID = 7777;
    private int GALLERY_IMAGE_REQUEST_ID = 8987;
    private String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionManager mPermissionManager;
    private MyPreferenceManager manager;
    private Button mBtnSubmit;
    private Bitmap thumbnail = null;
    private ImageView mImCalendar;
    private TextView mTvDate,mtxtPinCode;
    private EditText mEtTextField,mEtRemark;
    private int REQUEST_UPLOAD_PRESCRIPTION = 9890;
    private int GALLERY_COVER_REQUEST_ID = 5674;
    private int PICK_IMAGE_REQUEST = 1;
    private ArrayList<String> pathList = new ArrayList<>();
    private CardView mCardChangePin;
    private EditText number;
    private TextView notAvail;
    private String pincode;
    private AlertDialog mADialogLog;
    private GPSTracker locationTrack;
    private LocationAddress locationAddress;
    private double latitude,longitude;
    private static final int REQUEST_LOCATION = 2323;
    private RelativeLayout mContainer;
    private int REQUEST_PINCODE_CHECK = 8011;
    private int REQUEST_PINCODE_CHECK_BACK =0022;
    String pictureImagePath = "";



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

        setContentView(R.layout.activity_upload_pres);

        manager = new MyPreferenceManager(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mImUpload = (ImageView) findViewById(R.id.uploadImage);
        mImCalendar = (ImageView) findViewById(R.id.calendar);
        mEtTextField = (EditText) findViewById(R.id.et_textfield);
        mEtRemark = (EditText) findViewById(R.id.et_remarks);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mtxtPinCode = (TextView) findViewById(R.id.pinCode);
        mCardChangePin = (CardView) findViewById(R.id.cardChangePin);
        mContainer = (RelativeLayout) findViewById(R.id.firstmainLay);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPermissionManager = new PermissionManager(this);

        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);

        mPermissionManager.setRequiredPermissions(permissions);

        loadLocation();

        mImCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        mTvDate.setText(simpleDateFormat.format(calendar.getTime()));
//                        Toast.makeText(UploadPresActivity.this, ""+simpleDateFormat.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                    }
                },calendar.get(calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(calendar.DAY_OF_MONTH));
                dialog.setMinDate(calendar);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(calendar.get(calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(calendar.DAY_OF_MONTH)+7);
                dialog.setMaxDate(calendar1);
                dialog.show(getSupportFragmentManager(), "tag");
            }
        });


        mImUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] pictureDialogItems = {
                        "Select photo from gallery",
                        "Capture photo from camera",
                        "Remedio archive"};
                if (mPermissionManager.isPermissionAvailable()) {
                    AlertDialog.Builder pictureDialog = new AlertDialog.Builder(UploadPresActivity.this);
                    pictureDialog.setTitle("Select Action");
                    pictureDialog.setItems(pictureDialogItems,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            chooseImageFromGallary();
                                            break;
                                        case 1:
                                            openBackCamera();
                                         //   takeImageFromCamera();
                                            break;
                                        case 2:
                                            Intent intent=new Intent(UploadPresActivity.this, UploadsActivity.class);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            });
                    pictureDialog.show();
                } else {
                    mPermissionManager.makePermissionRequest(UploadPresActivity.this);
                }
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thumbnail!=null){
                    pinCodeEmptyOrNot();
                    //doCheckPincodeValid();
                } else {
                    Toast.makeText(UploadPresActivity.this, "Please add prescription image..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCardChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditEduQua(UploadPresActivity.this);
            }
        });

        mtxtPinCode.setText(manager.getPincode());
    }

    private void pinCodeEmptyOrNot() {
        String pin=mtxtPinCode.getText().toString().trim();
        if(!pin.equals("")){
            doCheckPincodeValid();
        }else{
            Toast.makeText(this, "Please Add Pincode....", Toast.LENGTH_SHORT).show();
        }
        
    }

    private void doCheckPincodeValid() {

        pincode=mtxtPinCode.getText().toString().trim();
       // Toast.makeText(this, ""+pincode, Toast.LENGTH_SHORT).show();
       // Log.v("??123Pin",""+pincode);
        if(!pincode.isEmpty()){
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id",manager.getdelicioId());
            map.put("pincode",pincode);

            new NetworkManager(this).doPost(map, Apis.API_POST_CHECK_PINCODE,
                    "REQUEST_PINCODE_CHECK_BACK", REQUEST_PINCODE_CHECK_BACK, UploadPresActivity.this);

            LoadingDialog.showLoadingDialog(this,"Loading...");
        }else{
            Toast.makeText(UploadPresActivity.this, "Please Add Pincode...", Toast.LENGTH_SHORT).show();
        }
//        pincode = number.getText().toString();


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
                    pincode = PinCode;
                    PinCode = pincode;
                   // mtxtPinCode.setText(PinCode);

                    mtxtPinCode.setText(manager.getPincode());
                    break;
                default:
                    locationAddress = null;
            }

            Log.v("Address>>",""+locationAddress);

            if(locationAddress!=null){
                manager.setKeyIsUseraddress(true);
            }else {
                manager.setKeyIsUseraddress(false);
            }


//            if (locationAddress.contains("null")){
//                location.setText("Location not found!!!");
//            } else {
//                location.setText(locationAddress);
//            }
        }
    }

    public void dialogEditEduQua(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dailog_layout_pincode, null);
        builder.setView(view);

        number = view.findViewById(R.id.et_pincode);
        TextView save = view.findViewById(R.id.btnSave);
        TextView cancel = view.findViewById(R.id.btnCancel);
        notAvail = view.findViewById(R.id.notAvail);

        number.setText(mtxtPinCode.getText().toString());

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
                    Toast.makeText(UploadPresActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (isFormValidated()){
                        pincode = number.getText().toString();

                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_id",manager.getdelicioId());
                        map.put("pincode",pincode);

                        new NetworkManager(context).doPost(map, Apis.API_POST_CHECK_PINCODE,
                                "REQUEST_PINCODE_CHECK", REQUEST_PINCODE_CHECK, UploadPresActivity.this);

                        LoadingDialog.showLoadingDialog(context,"Loading...");
                    } else {
                        Toast.makeText(UploadPresActivity.this, "Please add your pincode", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            private boolean isFormValidated() {

                String phNumber = number.getText().toString();

                boolean isError = false;
                View mFocusView = null;

                number.setError(null);

                if (!isDataValid(phNumber)) {
                    isError = true;
                    number.setError("Field can't be empty");
                    mFocusView = number;
                }

                if (isError) {
                    mFocusView.requestFocus();
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

    private void doUpload(String date) {

        PrettyDialog prettyDialog=new PrettyDialog(this);
        boolean isError = false;
        View mFocusView = null;

        mTvDate.setError(null);

        if (!isDataValid(date)){
            isError = true;
            mTvDate.setError("Field can't be empty");
            mFocusView = mTvDate;
        }
        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            Log.v("TEST>>",""+date+" "+manager.isUserAddress());
            if(manager.getPincode().isEmpty()){
                prettyDialog.setTitle("Info")
                        .setMessage("Please Add Pincode")
                        .setIcon(R.drawable.pdlg_icon_close)
                        .setIconTint(R.color.pdlg_color_red)
                        .setAnimationEnabled(true)
                        .addButton(
                                "OK",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.pdlg_color_red,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                        Bungee.slideUp(UploadPresActivity.this);
                                    }
                                }
                        ).show();
            }
            else {

                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", manager.getdelicioId());
                map.put("expect_date", date);
                map.put("prescription_details", mEtTextField.getText().toString());
                map.put("remarks", mEtRemark.getText().toString());

                Map<String, VolleyMultipartRequest.DataPart> imageMap = new HashMap<>();

                imageMap.put("prescription", new VolleyMultipartRequest.DataPart(Calendar.getInstance()
                            .getTimeInMillis() / 1000 + ".png", getBytearrayFromBitmap(thumbnail)));






                new NetworkManager(this).doPostMultiData(map, imageMap, Apis.API_POST_UPLOAD_PRESCRIPTION, "TAG_UPLOAD_PRESCRIPTION", REQUEST_UPLOAD_PRESCRIPTION, this);
                LoadingDialog.showLoadingDialog(this,"Loading...");

//            LoadingDialog.showLoadingDialog(this,"Loading...");
            }
        }
    }

    public byte[] getBytearrayFromBitmap(Bitmap bitmap2){
//...................................................................................................//

        int nh = (int) ( bitmap2.getHeight() * (1024.0 / bitmap2.getWidth()) );
        Bitmap bitmap = Bitmap.createScaledBitmap(bitmap2, 1024, nh, true);

//...................................................................................................//
//        byte[] buffer = new byte[0];
        if (bitmap !=null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();
            return buffer;
        }
        return null;
    }

    private void takeImageFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_IMAGE_REQUEST_ID);
    }

    private void chooseImageFromGallary() {
    //    EasyImage.openGallery(UploadPresActivity.this,0);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_REQUEST_ID);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_ID);






//        Pix.start(this,                    //Activity or Fragment Instance
//                GALLERY_IMAGE_REQUEST_ID,                //Request code for activity results
//                1);    //Number of images to restict selection count



//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, GALLERY_COVER_REQUEST_ID);

//        Intent intent = new Intent();
//        intent.setAction(android.content.Intent.ACTION_PICK);
//        intent.setType("image/*");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivityForResult(intent,GALLERY_COVER_REQUEST_ID);

//        Intent intent = new Intent();
//// Show only images, no videos or anything else
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//// Always show the chooser (if there are multiple options available)
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_COVER_REQUEST_ID);

//        if (Build.VERSION.SDK_INT <19){
//            Intent intent = new Intent();
//            intent.setType("image/jpeg");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("image/jpeg");
//            startActivityForResult(intent, GALLERY_COVER_REQUEST_ID);
//        }

//        Intent mIntent = new Intent(this, PickImageActivity.class);
//        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 1);
//        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
//        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        Uri uri = null;
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_IMAGE_REQUEST_ID) {

            //..................................Gallery Pick..........................................

            Uri mediaUri = data.getData();
            String mediaPath = mediaUri.getPath();

            try {
                InputStream inputStream = getBaseContext().getContentResolver().openInputStream(mediaUri);
                Bitmap bm = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                byte[] byteArray = stream.toByteArray();

                thumbnail=bm;
                Glide.with(this).load(bm).into(mImUpload);
   //             Log.v("BitmapSizeGall??",""+bm.getHeight()+" , "+bm.getWidth());


                //mImUpload.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            //.................................Gallery Pick...........................................
//        super.onActivityResult(requestCode, resultCode, data);
//
//        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
//                    @Override
//                    public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
//                        File userPhoto = imageFile;
//
////                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////                FirebaseHelper.pushProfilePhoto(userId, userPhoto);
//                        mImUpload.setImageBitmap(BitmapFactory.decodeFile(userPhoto.getPath()));
//                    }
//                });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (resultCode == RESULT_OK) {
//                    if (requestCode == GALLERY_IMAGE_REQUEST_ID) {
//                        // Get the url from data
//                        final Uri selectedImageUri = data.getData();
//                        if (null != selectedImageUri) {
//                            // Get the path from the Uri
//                            String path = getPathFromURI(UploadPresActivity.this,selectedImageUri);
//                            Log.i("IMAGEEEEEEEEE", "Image Path : " + path);
//                            // Set the image in ImageView
//                            findViewById(R.id.uploadImage).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ((ImageView) findViewById(R.id.uploadImage)).setImageURI(selectedImageUri);
//                                }
//                            });
//
//                        }
//                    }
//                }
//            }
//        }).start();

            //..................................Gallery Pick..........................................


//            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
//            File f = new File(returnValue.get(0));
//            thumbnail = new BitmapDrawable(getResources(), f.getAbsolutePath()).getBitmap();
//
//
////            thumbnail = com.fxn.utility.Utility.getScaledBitmap(512, com.fxn.utility.Utility.getExifCorrectedBitmap(f));
//            mImUpload.setImageBitmap(thumbnail);

//


                            //.................................Gallery Pick...........................................


                } /*else if (resultCode == -1 && requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            this.pathList = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            if (this.pathList != null && !this.pathList.isEmpty()) {
                StringBuilder sb=new StringBuilder("");
                for(int i=0;i<pathList.size();i++) {
                    sb.append("Photo"+(i+1)+":"+pathList.get(i));
                    sb.append("\n");
                }
//                tvResult.setText(sb.toString()); // here this is textview for sample use...
            }
        }*/
//        } else  if (requestCode == PICK_IMAGE_REQUEST) {
//            uri = data.getData();
//            try {
//                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                mImUpload.setImageBitmap(thumbnail);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else if (requestCode == GALLERY_COVER_REQUEST_ID) {
//            uri = data.getData();
//            final int takeFlags = data.getFlags()
//                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            // Check for the freshest data.
//            getContentResolver().takePersistableUriPermission(uri, takeFlags);
//
//            String id = uri.getLastPathSegment().split(":")[1];
//            final String[] imageColumns = {MediaStore.Images.Media.DATA };
//            final String imageOrderBy = null;
//
//            Uri uris = getUri();
//            String selectedImagePath = "path";
//
//            Cursor imageCursor = managedQuery(uri, imageColumns,
//                    MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);
//
//            if (imageCursor.moveToFirst()) {
//                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            Log.e("path",selectedImagePath ); // use selectedImagePath
//            if (selectedImagePath != null) {
//                File f = new File(selectedImagePath);
//                uri = Uri.fromFile(f);
//            }
//            // Set the image in ImageView
//            mImUpload.setImageURI(uri);
//        }

//        loadSomeStreamAsynkTask(originalUri);
        /*else if (requestCode == GALLERY_COVER_REQUEST_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//             // Log.d(TAG, String.valueOf(bitmap));
//
//                ImageView imageView = (ImageView) findViewById(R.id.imageView);
//                imageView.setImageBitmap(bitmap);
//                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                mImUpload.setImageBitmap(thumbnail);
                // Get the path from the Uri
                final String path = getPathFromURI(uri);
                if (path != null) {
                    File f = new File(path);
                    uri = Uri.fromFile(f);
                }
                // Set the image in ImageView
                mImUpload.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        /*if (requestCode == GALLERY_COVER_REQUEST_ID) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    mImUpload.setImageBitmap(thumbnail);
//                    int origWidth = thumbnail.getWidth();
//                    int origHeight = thumbnail.getHeight();
//
//                    final int destWidth = 600;
//
//                    if(origWidth > destWidth){
////                        int destHeight = origHeight/( origWidth / destWidth ) ;
//                        Bitmap b2 = Bitmap.createScaledBitmap(thumbnail, destWidth, 600, false);
//                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//                        b2.compress(Bitmap.CompressFormat.JPEG,80 , outStream);
//                        uploadImage(b2);
//                    } else {
//                        uploadImage(thumbnail);
//                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UploadPresActivity.this, "Please select an valid image!", Toast.LENGTH_SHORT).show();
                }
            }

        }*/
        else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_IMAGE_REQUEST_ID){

//            thumbnail = (Bitmap) data.getExtras().get("data");
//
//
////           mCover = thumbnail;
//            Glide.with(this).load(thumbnail).into(mImUpload);

//Uri outputFileUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, newfile);

            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                thumbnail=myBitmap;
                Glide.with(this).load(myBitmap).into(mImUpload);
    //            Log.v("BitmapSizeCam??",""+myBitmap.getHeight()+" , "+myBitmap.getWidth());

            }


            //mImUpload.setImageBitmap(thumbnail);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.onPermissionResult(grantResults,UploadPresActivity.this,true);
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @SuppressLint("NewApi")
    public static String getPathAPI19(Context context, Uri uri) {
        String filePath = "";
        String fileId = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = fileId.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String selector = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getPathAPI11To18(Context context, Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;

    }

    @Override
    public void onGrandPermission() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(UploadPresActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from default camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseImageFromGallary();
                                break;
                            case 1:
                                takeImageFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onDenyPermission() {
        Toast.makeText(UploadPresActivity.this, "Grant permission to pick image", Toast.LENGTH_SHORT).show();
        mPermissionManager.makePermissionRequest(UploadPresActivity.this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
//        Toast.makeText(this, "dwwwta", Toast.LENGTH_SHORT).show();
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_UPLOAD_PRESCRIPTION){
                LoadingDialog.cancelLoading();
//                Toast.makeText(this, "duuuta", Toast.LENGTH_SHORT).show();
                ProcessJsonUploadPres(response);
            } else if (requestId == REQUEST_PINCODE_CHECK){
                processJsonCheckPincode(response);
            } else if (requestId == REQUEST_PINCODE_CHECK_BACK){
                processJsonCheckPincodeBack(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processJsonCheckPincodeBack(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

                Toast.makeText(UploadPresActivity.this, "Please Add Pincode...", Toast.LENGTH_SHORT).show();
//                notAvail.setVisibility(View.VISIBLE);
                LoadingDialog.cancelLoading();
                Intent intent = new Intent(UploadPresActivity.this,NoLocationActivity.class);
                startActivity(intent);
            } else {
                manager.setPincode(mtxtPinCode.getText().toString());
                LoadingDialog.cancelLoading();
                String date = mTvDate.getText().toString();
                doUpload(date);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(UploadPresActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processJsonCheckPincode(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

//                notAvail.setVisibility(View.VISIBLE);

                Intent intent = new Intent(UploadPresActivity.this,NoLocationActivity.class);
                startActivity(intent);
            } else {

                mADialogLog.dismiss();
                mtxtPinCode.setText(pincode);
                manager.setPincode(pincode);
                Global.PinCode = pincode;
            }
        } catch (JSONException e) {
            e.printStackTrace();

            dialogWarning(UploadPresActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonUploadPres(String response) {

//        Toast.makeText(this, "daaata", Toast.LENGTH_SHORT).show();
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            boolean error = jsonObject.optBoolean("error");
//            if (error){
//                LoadingDialog.cancelLoading();
////                Toast.makeText(this, "Something went wrong.Please try again!!!", Toast.LENGTH_SHORT).show();
//                showDailog("UPLOAD FAILED!", String.valueOf(R.string.upload_failed));
//            } else {
//                LoadingDialog.cancelLoading();
////                Toast.makeText(this, "Successfully uploaded prescription.", Toast.LENGTH_SHORT).show();
//                showDailog("UPLOAD SUCCESS!", String.valueOf(R.string.upload_sucess));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            LoadingDialog.cancelLoading();
//        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_UPLOAD_PRESCRIPTION){
                prodessUploadPres(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void prodessUploadPres(String response) {
        Log.v("Thumbanil??",""+response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
//                Toast.makeText(this, "Something went wrong.Please try again!!!", Toast.LENGTH_SHORT).show();
                showDailog("UPLOAD FAILED!", this.getResources().getString(R.string.upload_failed));
            } else {
                LoadingDialog.cancelLoading();
//                Toast.makeText(this, "Successfully uploaded prescription.", Toast.LENGTH_SHORT).show();
                showDailog("UPLOAD SUCCESS!", this.getResources().getString(R.string.upload_sucess));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(UploadPresActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void showDailog(String title,String message) {
        final PrettyDialog prettyDialog = new PrettyDialog(this);
        if (title.equals("UPLOAD SUCCESS!")){
            prettyDialog .setTitle(title)
                    .setMessage(message)
                    .setIcon(R.drawable.pdlg_icon_success)
                    .setIconTint(R.color.pdlg_color_green)
                    .setAnimationEnabled(true)

                    .addButton(
                            "OK",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_green,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(UploadPresActivity.this,OrderActivity.class);
                                    intent.putExtra("info","payment");
                                    startActivity(intent);
                                    prettyDialog.dismiss();
                                    finish();
                                    Bungee.slideUp(UploadPresActivity.this);
                                }
                            }
                    )
                    .show();
        } else {
            prettyDialog.setTitle(title)
                    .setMessage(message)
                    .setIcon(R.drawable.pdlg_icon_close)
                    .setIconTint(R.color.pdlg_color_red)
                    .setAnimationEnabled(true)
                    .addButton(
                            "OK",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_red,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                    Bungee.slideUp(UploadPresActivity.this);
                                }
                            }
                    )
                    .addButton(
                            "Try Again",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_red,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
//                                    if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonCod){
//                                        doConfirmOrder("cod");
//                                    } else {
//
//                                    }
//                                    String date = mTvDate.getText().toString();
//                                    doUpload(date);
                                  //  doCheckPincodeValid();

                                    pinCodeEmptyOrNot();
                                    prettyDialog.dismiss();
                                }
                            }
                    )
                    .show();
        }
    }




    //.............................................Camera Algorithm.............................................


    private void openBackCamera() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
//        Uri outputFileUri = Uri.fromFile(file);
        Uri outputFileUri = FileProvider.getUriForFile(UploadPresActivity.this, BuildConfig.APPLICATION_ID, file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST_ID);
    }



    //.............................................Camera Algorithm.............................................
}
//359154070168200