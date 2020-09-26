package in.ateesinfomedia.remedio.managers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ATEES Infomedia Pvt. Ltd. on 10/26/2017.
 */

public class PermissionManager {

    private static final int PERMISSION_CALLBACK_CONSTANT = 1100;
    private static final int REQUEST_PERMISSION_SETTING = 1101;
    private SharedPreferences mPreferenceManager;
    private Context mContext;
    private String[] permissions;
    private Activity mActivity;
    private boolean sentToSettings;
    private AlertDialog mRationaleAlert;
    private PermissionCallback mPermissionCallback;
    private boolean isCustomAlert = false;

    public PermissionManager(Context context){
        this.mContext    = context;
        this.mActivity = (Activity) context;
        mPreferenceManager = context.getSharedPreferences("pref_permission", MODE_PRIVATE);

    }

    public PermissionManager(){
    }

    public void with(Context context){
        this.mContext = context;
        this.mActivity = (Activity) context;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.permissions[0] = requiredPermission;
    }

    public void setRequiredPermissions(String[] requiredPermissions) {
        this.permissions = requiredPermissions;
    }

    public boolean isPermissionAvailable() {
        for (String permission : permissions) {
            if ((ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    public boolean isShowPermissionRationale() {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                return true;
            }
        }
        return false;
    }

    public void requestPermissionWithRationale(String title, String message, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(isCancelable);
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                requestPermission();
            }
        });
        if (isCancelable){
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        showRationaleAlert(builder.create());
    }

    public void showRationaleAlert(AlertDialog alertDialog) {
        mRationaleAlert = alertDialog;
        mRationaleAlert.show();
    }

    public void showCustomRationaleAlert(AlertDialog alertDialog) {
//        isCustomAlert = true;
//        showRationaleAlert(alertDialog);
        // TODO Yet to be customised
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(mActivity, permissions, PERMISSION_CALLBACK_CONSTANT);
    }

    public void alertAndMoveToSettings(final boolean isSentToSettings) {
        //Previously Permission Request was cancelled with 'Dont Ask Again',
        // Redirect to Settings after showing Information about why you need the permission
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Need Permissions");
        builder.setMessage("Proceed to settings to grant the required permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                sentToSettings = isSentToSettings;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                Toast.makeText(mActivity.getBaseContext(), "Go to Permissions to Grant  Requied Permissions", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void onPermissionResult(@NonNull int[] grantResults, PermissionCallback resultCallback, boolean isMandatory) {
        mPermissionCallback = resultCallback;
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                mPermissionCallback.onGrandPermission();
            } else if(!isPermissionAvailable()){
//                mRationaleAlert.show();

                if (isMandatory){
                    makePermissionRequest(mPermissionCallback);
                }else {
                    mPermissionCallback.onDenyPermission();
                }
            }


    }

    public void makePermissionRequest(PermissionCallback resultCallback) {
        mPermissionCallback = resultCallback;
            if (!isPermissionAvailable()) {
                if (isShowPermissionRationale()) {
                    requestPermissionWithRationale("Need Permissions","Your App needs permission to acces certain features of your phone. " +
                            "Grand permission to experience complete features of your application.",false);
                } else if (mPreferenceManager.getBoolean(permissions[0], false)) {
                    alertAndMoveToSettings(sentToSettings);
                } else {
                    //just request the permission
                    requestPermission();
                }
                SharedPreferences.Editor editor = mPreferenceManager.edit();
                editor.putBoolean(permissions[0], true);
                editor.commit();

            } else {
                mPermissionCallback.onGrandPermission();
            }
    }

    public  interface PermissionCallback{

        public void onGrandPermission();
        public void onDenyPermission();
    }

}
