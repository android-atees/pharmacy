package in.ateesinfomedia.remedio.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.managers.PermissionManager;

import static in.ateesinfomedia.remedio.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.remedio.configurations.Global.COUNT;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class SplashActivity extends AppCompatActivity implements PermissionManager.PermissionCallback, NetworkCallback {

    private MyPreferenceManager manager;
    private PermissionManager mPermissionManager;
    private String[] permissions = new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private int REQUEST_FEM_TOKEN_ID = 9099;
    private int REQUEST_NOTIFICATION_COUNT_ID = 8976;
    private int REQUEST_CART_COUNT_ID = 0000;
    private boolean isShowSnack;
    private int REQUEST_UPDATE_CHECK = 8765;
    private int REQUEST_LOGOUT_ID = 9824;
    private static final String TAG = SplashActivity.class.getName();

//    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        manager = new MyPreferenceManager(this);
        mPermissionManager = new PermissionManager(this);

        mPermissionManager.setRequiredPermissions(permissions);
        startProcess();

        Log.e("token>>>>>>>>>>======",manager.getUserNotificationTocken());
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this,WelcomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },2000);
    }

    private void startProcess() {
        if (isConnected()){
            checkUpdate();
        } else {
            showSnack(false);
        }

        //        if (mPermissionManager.isPermissionAvailable()) {
//            if (manager.isFirstLaunch()) {
//                doSubmitDeviceSetting();
//            } else {
//                if (manager.isLogin()) {
////                    getNotificationCount();
//                    getCartCount();
//                } else {
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        } else {
//            mPermissionManager.makePermissionRequest(SplashActivity.this);
//        }
    }

    private void checkUpdate() {
        String deciveid = getDeviceId(this);
        String modelName = Build.MANUFACTURER + " " + Build.MODEL;

        Map<String,String> postData = new HashMap<>();

        postData.put("device_id",deciveid);
        //postData.put("notf_id",manager.getUserNotificationTocken());
        postData.put("notf_id",modelName);
        postData.put("device_os","Android");
        Log.v("VERSION>>>",""+deciveid+" "+modelName+" "+manager.getUserNotificationTocken()+" "+"Android");
        new NetworkManager(this).doPost(null, Apis.API_POST_CHECK_UPDATE,"TAG_UPDATE_CHECK", REQUEST_UPDATE_CHECK,this);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            if (isShowSnack){
                message = "Good! Connected to Internet";
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_container), message, Snackbar.LENGTH_LONG)
                        .setAction("RELOAD", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startProcess();
                            }
                        });
                snackbar.show();
            }
        } else {
            isShowSnack = true;
            message = "Sorry! Not connected to internet";
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.main_container), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void getNotificationCount() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map,Apis.API_POST_GET_NOTIFICATIONS_COUNT,"REQUEST_NOTIFICATION_COUNT",REQUEST_NOTIFICATION_COUNT_ID,this);
//        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    private void getCartCount() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());

        new NetworkManager(this).doPost(map,Apis.API_POST_GET_CART_COUNT,"REQUEST_CART_COUNT",REQUEST_CART_COUNT_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    private void doSubmitDeviceSetting() {

        final String device_id = getDeviceId(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = new HashMap<>();
                map.put("device_type","Android");
                map.put("device_id",device_id);
                map.put("fcm_token",manager.getUserNotificationTocken());

                new NetworkManager(SplashActivity.this).doPost(map, Apis.API_POST_UPLOAD_FCM_TOKEN,"REQUEST_FEM_TOKEN",REQUEST_FEM_TOKEN_ID,SplashActivity.this);
                LoadingDialog.showLoadingDialog(SplashActivity.this,"Loading...");
            }
        },2000);
    }

    public  static String getDeviceId(Context context){
        String m_androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_androidId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.onPermissionResult(grantResults,SplashActivity.this,true);
    }

    @Override
    public void onGrandPermission() {
        startProcess();
    }

    @Override
    public void onDenyPermission() {
//        Toast.makeText(this, "Permission not granted...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_FEM_TOKEN_ID){
                processFcmUpdate(response);
            } else if (requestId == REQUEST_NOTIFICATION_COUNT_ID){
                processNotiCount(response);
            } else if (requestId == REQUEST_CART_COUNT_ID){
                processCartCount(response);
            } else  if (requestId == REQUEST_UPDATE_CHECK){
                processJsonCheckUpdate(response);
            } else if (requestId == REQUEST_LOGOUT_ID){
                ProcessJsonLogout(response);
            }

        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void ProcessJsonLogout(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

//                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
//                LoadingDialog.cancelLoading();
                logout();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    private void logout(){

        String fcmID = manager.getUserNotificationTocken();
        manager.setLogOut();
        manager.setUserNotificationTocken(fcmID);

        Intent intentSignOut = new Intent(this,LoginActivity.class);
        intentSignOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentSignOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentSignOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentSignOut);
        finishAffinity();
    }

    private void processJsonCheckUpdate(String response) {
        boolean forcedUpdation = false;
        if(response == null){
            Log.d(TAG, "processJson: Cant check updates");
        }else {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean status = jsonObject.optBoolean("error");

                if (!status) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    JSONObject object = jsonArray.optJSONObject(0);

                    double newVersionString = object.optDouble("version");
                    String newFeatures = object.optString("features");
                    String forceornot = object.optString("force");
                    if (forceornot.equals("0")){
                        forcedUpdation = false;
                    } else {
                        forcedUpdation = true;
                    }

//                    newVersionString = 10.00;//TODO add this Integer.parseInt(newVersionString);
                    int newVersion = (int) newVersionString;

                    checkVersion(newVersion, forcedUpdation, newFeatures);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkVersion(int newVersion, boolean isForced, String features) {

        if (isNewVersion(newVersion) && isForced){
            showVersionUpdate(isForced,features);
        }else if(isNewVersion(newVersion) && !isForced){
            showVersionUpdate(isForced,features);
        }else {
            if (mPermissionManager.isPermissionAvailable()) {
                if (manager.isFirstLaunch()) {
                    doSubmitDeviceSetting();
                } else {
                    if (manager.isLogin()) {
//                    getNotificationCount();
                        if (manager.isFirstLogin()){
                            doLogout();
                        } else {
                            getCartCount();
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } else {
                mPermissionManager.makePermissionRequest(SplashActivity.this);
            }
        }
    }

    private void doLogout() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("device_id",getDeviceId(this));

        new NetworkManager(this).doPost(map, Apis.API_POST_LOGOUT,"REQUEST_LOGOUT",REQUEST_LOGOUT_ID,this);
//        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    public boolean isNewVersion(int newVersion) {
        int appVersion  = getVersion();
        return appVersion < newVersion;
    }

    public int getVersion() {
        //get the current version number and name
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void showVersionUpdate(boolean isForced, String newFeatures) {
        DialogInterface.OnClickListener posContinue = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (manager.isFirstLaunch()) {
                    doSubmitDeviceSetting();
                } else {
                    if (manager.isLogin()) {
//                    getNotificationCount();
                        getCartCount();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        DialogInterface.OnClickListener posUpdate = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                    startActivity(intent);
                }
            }
        };
        DialogInterface.OnClickListener negCancel = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    finishAndRemoveTask();
                } else {
                    System.exit(0);
                }
            }
        };

        if (isForced) {
            AlertDialog.Builder builder = getAlertDialog(this, "New Version Available", newFeatures, "Update", "Cancel", posUpdate, negCancel);
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            AlertDialog.Builder builder = getAlertDialog(this, "New Version Available", newFeatures, "Continue", "Update", posContinue, posUpdate);
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public static AlertDialog.Builder getAlertDialog(Context context, String title, String message, String posButton, String negButton,
                                                     DialogInterface.OnClickListener positiveListner, DialogInterface.OnClickListener negetiveListner){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(posButton, positiveListner );
        builder.setNegativeButton(negButton, negetiveListner );
        return builder;
    }

    private void processCartCount(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                getNotificationCount();
            } else {
                String count = jsonObject.optString("data");

                manager.saveCartCount(Integer.valueOf(count));
                getNotificationCount();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(SplashActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processNotiCount(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                //LoadingDialog.cancelLoading();

                String count = jsonObject.optString("data");

                COUNT = count;
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("info","splash");
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(SplashActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void processFcmUpdate(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(SplashActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowSnack = false;
    }
}