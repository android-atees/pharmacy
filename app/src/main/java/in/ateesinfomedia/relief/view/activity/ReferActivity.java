package in.ateesinfomedia.relief.view.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.managers.PermissionManager;

import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class ReferActivity extends AppCompatActivity implements NetworkCallback, PermissionManager.PermissionCallback {

    private Toolbar toolbar;
    private EditText mEtRefer;
    private Button mBtnInvite;
    private MyPreferenceManager manager;
    private int REQUEST_REFERAL = 9099;
    private PermissionManager mPermissionManager;
    private String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
    private ImageView mImContactList;

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

        setContentView(R.layout.activity_refer);

        manager = new MyPreferenceManager(this);
        mPermissionManager = new PermissionManager(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtRefer = (EditText) findViewById(R.id.et_refer);
        mBtnInvite = (Button) findViewById(R.id.btn_invite);
        mImContactList = (ImageView) findViewById(R.id.contact_list);

        mPermissionManager.setRequiredPermissions(permissions);

        mBtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String referalId = mEtRefer.getText().toString();
//                doSubmitReferal(referalId);

                String unique = manager.getUserUniqueId();
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "REMEDIO");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "Hai welcome to Remedio, your friend invited you to join Remedio Pharmaceuticals. Use this "+ unique +" as reference id while registering to earn points and get more offers.Click here to register https://play.google.com/store/apps/details?id=in.ateesinfomedia.remedio&hl=en \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {

                }
            }
        });

        mImContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPermissionManager.isPermissionAvailable()){
                    Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(pickContact, 1);
                } else {
                    mPermissionManager.makePermissionRequest(ReferActivity.this);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri contactData = data.getData();
        Cursor c = getContentResolver().query(contactData, null, null, null, null);
        if (c.moveToFirst()) {
            int phoneIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String num = c.getString(phoneIndex);
            mEtRefer.setText(num);
//            Toast.makeText(ReferActivity.this, "Number=" + num, Toast.LENGTH_LONG).show();
        }
    }

    private void doSubmitReferal(String referalId) {
        boolean isError = false;
        View mFocusView = null;

        mEtRefer.setError(null);

        if (!isDataValid(referalId)){
            isError = true;
            mEtRefer.setError("Field can't be empty");
            mFocusView = mEtRefer;
        }

        if (!isValidMobile(referalId)){
            isError = true;
            mEtRefer.setError("Enter valid number");
            mFocusView = mEtRefer;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            Map<String,String> map = new HashMap<String,String>();
            map.put("user_id",manager.getdelicioId());
            map.put("mobilenumber",referalId);

            new NetworkManager(this).doPost(map, Apis.API_POST_REFERAL,"TAG_REFERAL",REQUEST_REFERAL,this);

            LoadingDialog.showLoadingDialog(this,"Loading...");
        }
    }

    public boolean isDataValid(String mETFirstName) {
        if (mETFirstName.equals("") || mETFirstName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidMobile(String phone) {
        if (phone.matches("^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onResponse(int status, String response, int requestId) {

        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_REFERAL){
                processReferal(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processReferal(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong..Please try again!!", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Successfully Referred", Toast.LENGTH_SHORT).show();
                mEtRefer.setText("");
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
    public void onGrandPermission() {

    }

    @Override
    public void onDenyPermission() {
        Toast.makeText(this, "Allow permission to get the contacts", Toast.LENGTH_SHORT).show();
    }
}
