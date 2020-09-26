package in.ateesinfomedia.remedio.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.ateesinfomedia.remedio.BuildConfig;
import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.components.MyDividerItemDecoration;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.managers.PermissionManager;
import in.ateesinfomedia.remedio.view.activity.LoginActivity;
import in.ateesinfomedia.remedio.view.activity.MainActivity;
import in.ateesinfomedia.remedio.view.activity.OrderActivity;
import in.ateesinfomedia.remedio.view.activity.ReferActivity;
import in.ateesinfomedia.remedio.view.activity.RewardsActivity;
import in.ateesinfomedia.remedio.view.activity.SignUpActivity;
import in.ateesinfomedia.remedio.view.activity.UploadPresActivity;
import in.ateesinfomedia.remedio.view.adapter.MoreMenusAdapter;

public class MoreFragment extends Fragment implements AdapterClickListner, NetworkCallback, PermissionManager.PermissionCallback {

    private View mView;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private MoreMenusAdapter mMoreMenuAdapter;
    private MyPreferenceManager manager;
    private int REQUEST_LOGOUT_ID = 9823;
    private TextView mTvVersion;
    private PermissionManager mPermissionManager;
//    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

    public static MoreFragment getInstance() {
        MoreFragment moreFragment = new MoreFragment();

        return moreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_more, container, false);

        String versionName = BuildConfig.VERSION_NAME;

        manager = new MyPreferenceManager(getActivity());
        recyclerView = (RecyclerView) mView.findViewById(R.id.remedio_more_recycler_view);
        mPermissionManager = new PermissionManager(getActivity());

        recyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mTvVersion = (TextView) mView.findViewById(R.id.version);
        mMoreMenuAdapter = new MoreMenusAdapter(getActivity(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 0));
        recyclerView.setAdapter(mMoreMenuAdapter);
//        mPermissionManager.setRequiredPermissions(permissions);

        mTvVersion.setText("Version "+versionName);

        return mView;
    }

    @Override
    public void itemClicked(int position, Object object) {

        switch (position){

            case 0:
                ((MainActivity)getActivity()).gotoTab(0);
                break;
            case 1:
                ((MainActivity)getActivity()).gotoTab(1);
                break;
            case 2:
                Intent uploadIntent = new Intent(getActivity(), UploadPresActivity.class);
                startActivity(uploadIntent);
                break;
            case 3:
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
                break;
            case 4:
                /*Intent referIntent = new Intent(getActivity(), ReferActivity.class);
                startActivity(referIntent);*/
                Toast.makeText(requireContext(),
                        "Coming Soon...",
                        Toast.LENGTH_SHORT).show();
                break;
            case 5:
                /*Intent rewardIntent = new Intent(getActivity(), RewardsActivity.class);
                startActivity(rewardIntent);*/
                Toast.makeText(requireContext(),
                        "Coming Soon...",
                        Toast.LENGTH_SHORT).show();
                break;
            case 6:
//                if (mPermissionManager.isPermissionAvailable()){
                    showDialogToCall("6282286867");
//                } else {
//                    mPermissionManager.makePermissionRequest(this);
//                }
                break;
            case 7:
                doRating();
                break;
            case 8:
                doLogout();
                break;
        }
    }

    private void doLogout() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("device_id",getDeviceId(getActivity()));

        new NetworkManager(getActivity()).doPost(map, Apis.API_POST_LOGOUT,"REQUEST_LOGOUT",REQUEST_LOGOUT_ID,this);
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
    }

    public  static String getDeviceId(Context context){
        String m_androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_androidId;
    }

    private void doRating() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    private void showDialogToCall(final String number) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Make call?");

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to make the call..");

        // On pressing Settings button
        alertDialog.setPositiveButton("call", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+number));
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

    private void logout(){

        String fcmID = manager.getUserNotificationTocken();
        manager.setLogOut();
        manager.setUserNotificationTocken(fcmID);

        Intent intentSignOut = new Intent(getActivity(),LoginActivity.class);
        intentSignOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentSignOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentSignOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentSignOut);
        getActivity().finishAffinity();
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_LOGOUT_ID){
                ProcessJsonLogout(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(getActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void ProcessJsonLogout(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

                LoadingDialog.cancelLoading();
                Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                logout();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onGrandPermission() {
        showDialogToCall("6282286867");
    }

    @Override
    public void onDenyPermission() {
        Toast.makeText(getActivity(), "Allow permission to make the call", Toast.LENGTH_SHORT).show();
    }
}