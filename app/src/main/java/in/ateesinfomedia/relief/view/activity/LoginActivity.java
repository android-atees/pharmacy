package in.ateesinfomedia.relief.view.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
//import in.ateesinfomedia.remedio.components.SmsReceiver;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.CategoryModel;
import in.ateesinfomedia.relief.models.login.CustomerData;
import in.ateesinfomedia.relief.models.login.LoginModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;

import static in.ateesinfomedia.relief.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.relief.configurations.Global.COUNT;
import static in.ateesinfomedia.relief.configurations.Global.CategoryList;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class LoginActivity extends AppCompatActivity implements NetworkCallback {

    private Button mBtnLogin;
    //private Button mBtnRegister;
    //private Button mIBNext;
    private TextView mTvForgot;
    private TextView mTvGoToSignUp;
    //private TextView mTvShow;
    private EditText mETPass;
    private EditText mETNumber;
    //private RelativeLayout mPassLay;
    private int REQUEST_LOGIN = 8989;
    private int REQUEST_CHECK_MOBILE_EXIST = 9099;
    private boolean isPassLay;
    private EditText otp;
    private TextInputLayout inputLayoutPassword;
    private TextInputLayout inputLayoutotp;
    private Button getOtp;
    private String phone;
    private AlertDialog mADialogLog;
    private int REQUEST_VERIFY_FORGOT_PASSWORD =8765;
    private int REQUEST_RESEND_OTP = 9000;
    private int REQUEST_FORGOT_PASSWORD = 1111;
    private String Otpmsg;
    private String otpSender;
    private String otpDigits;
    private MyPreferenceManager manager;
    private int REQUEST_UPDATEFCM_ID = 7456;
    private int REQUEST_NOTIFICATION_COUNT_ID = 9876;
    private String number = "";
    private boolean isShow;
    private int REQUEST_CART_COUNT_ID = 8976;
    private int REQUEST_CATEGORY = 9825;
    private List<CategoryModel> mCateList = new ArrayList<>();
    private static final String TAG = LoginActivity.class.getName();
    Gson gson = new Gson();
    private int REQUEST_FORGOT_PASSWORD_NEW = 1289;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLogin = (Button) findViewById(R.id.login_submit_btn);
        manager = new MyPreferenceManager(this);

        //mPassLay = (RelativeLayout) findViewById(R.id.pass_lay);
        mETNumber = (EditText) findViewById(R.id.login_number_txt);
        mETPass = (EditText) findViewById(R.id.login_password_txt);
        //mTvShow = (TextView) findViewById(R.id.txtShow);
        //mIBNext = (Button) findViewById(R.id.ib_login_next);
        //mBtnRegister = (Button) findViewById(R.id.btnRegister);
        mTvForgot = (TextView) findViewById(R.id.login_forgot_txt);
        mTvGoToSignUp = (TextView) findViewById(R.id.login_goto_sign_up_txt);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mETNumber.getText().toString();
                String pass = mETPass.getText().toString();
                doSubmitLogin(number,pass);

                /*Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();

                if (isPassLay){
                    String number = mETNumber.getText().toString();
                    String pass = mETPass.getText().toString();
                    doSubmitLogin(number,pass);
                } else {
                    number = mETNumber.getText().toString();
                    doSubmitNumber(number);
                }*/
            }
        });

        mTvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "Adding Soon...", Toast.LENGTH_SHORT).show();
                dialogForgotPass(LoginActivity.this,"forgot");
            }
        });

        mTvGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRegister();
            }
        });

        /*mTvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow){
                    mTvShow.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    mTvShow.setText("Show");
                    mETPass.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mETPass.setSelection(mETPass.getText().length());
                    isShow = false;
                } else {
                    mTvShow.setTextColor(getResources().getColor(R.color.colorAccent));
                    mTvShow.setText("Hide");
                    mETPass.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mETPass.setSelection(mETPass.getText().length());
                    isShow = true;
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRegister();
            }
        });*/
    }

    private void dialogForgotPass(final Context context, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_layout_forgot_pass, null);

        final EditText number = view.findViewById(R.id.forgot_pass_email);

        Button submitEmail = view.findViewById(R.id.submitEmail);

        TextInputLayout emailMainLayout = view.findViewById(R.id.input_layout_email);

        builder.setView(view);

        submitEmail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                emailMainLayout.setError(null);
                if (!isConnected()) {
                    Toast.makeText(LoginActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    String fEmail = number.getText().toString();
                    if (fEmail.equals(null) || fEmail.equals("") || !isValidEmail(fEmail)) {
                        emailMainLayout.setError("Enter valid email");
                    } else {

                        JSONObject map = new JSONObject();
                        try {
                            map.put("email",fEmail);
                            map.put("template","email_reset");
                            Log.d("doJSON Request",map.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new NetworkManager(LoginActivity.this).doPutCustom(
                                Apis.API_PUT_FORGOT_PASSWORD,
                                Object.class,
                                map,
                                Apis.ACCESS_TOKEN,
                                "TAG_LOGIN",
                                REQUEST_FORGOT_PASSWORD_NEW,
                                LoginActivity.this
                        );
                        LoadingDialog.showLoadingDialog(LoginActivity.this,"Loading...");

                    }

                }
            }

            public boolean isValidEmail(String emailAddress) {
                return !TextUtils.isEmpty(emailAddress) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
            }
        });

        this.mADialogLog = builder.create();
        this.mADialogLog.setCanceledOnTouchOutside(false);
        this.mADialogLog.show();
    }

    private void dialogEditEduQua(final Context context, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_layout_login, null);

        final EditText number = view.findViewById(R.id.et_login_pnone);
        otp = view.findViewById(R.id.et_login_otp);
        final EditText password = view.findViewById(R.id.et_login_password);
        inputLayoutPassword =  view.findViewById(R.id.input_layout_password);
        inputLayoutotp =  view.findViewById(R.id.input_layout_otp);

        this.number = mETNumber.getText().toString();

        if (this.number.isEmpty() || this.number.equals("")){
            number.setFocusable(true);
            number.setEnabled(true);
        } else {
            number.setFocusable(false);
            number.setEnabled(false);
            number.setText(this.number);
        }

        getOtp = view.findViewById(R.id.getOtp);

        builder.setView(view);

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4){
                    inputLayoutPassword.setVisibility(View.VISIBLE);
                    getOtp.setText("Update Password");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getOtp.setOnClickListener(new View.OnClickListener() {

            /*          * Enabled aggressive block sorting*/

            public void onClick(View view) {
                if (!isConnected()) {
                    Toast.makeText(LoginActivity.this, "Sorry, No internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    if (getOtp.getText().toString().equals("Update Password")){

                        if (isFormValidatedForgot()) {

                            if (isConnected()) {
//                            phone = number.getText().toString();
                                String otpp = otp.getText().toString();
                                String passwor = password.getText().toString();

                                HashMap<String, String> map = new HashMap<>();
                                map.put("userMobile", phone);
                                map.put("otp", otpp);
                                map.put("pass", passwor);

                                new NetworkManager(context).doPost(map, Apis.API_POST_USER_UPDATE_PASS_AND_LOGIN,
                                        "REQUEST_LOGIN_OTP", REQUEST_VERIFY_FORGOT_PASSWORD, LoginActivity.this);

                                LoadingDialog.showLoadingDialog(LoginActivity.this,"Loading....");
                            } else{
                                Toast.makeText(LoginActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "Please add your mobile number", Toast.LENGTH_SHORT).show();
                        }
                    } else if (getOtp.getText().toString().equals("Resend OTP")){

                        if (isConnected()){

                            HashMap<String, String> map = new HashMap<>();
                            map.put("userMobile", phone);

                            new NetworkManager(context).doPost(map, Apis.API_POST_USER_CHECK_MOBILE_EXIST,
                                    "REQUEST_RESEND_OTP", REQUEST_RESEND_OTP, LoginActivity.this);

                            LoadingDialog.showLoadingDialog(LoginActivity.this,"Loading....");
                        } else{
                            Toast.makeText(LoginActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                        }
                    }else {

                        if (isFormValidated()) {
                            if (isConnected()){

                                phone = number.getText().toString();

                                HashMap<String, String> map = new HashMap<>();
                                map.put("userMobile", phone);

                                    new NetworkManager(context).doPost(map, Apis.API_POST_USER_FORGOT_PASSWORD,
                                            "REQUEST_LOGIN_OTP", REQUEST_FORGOT_PASSWORD, LoginActivity.this);

                                LoadingDialog.showLoadingDialog(LoginActivity.this,"Loading....");
                            } else{
                                Toast.makeText(LoginActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Please add your mobile number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            private boolean isFormValidatedForgot() {
                String phNumber = number.getText().toString();
                String otpp = otp.getText().toString();
                String pass = password.getText().toString();

                boolean isError = false;
                View mFocusView = null;

                number.setError(null);
                otp.setError(null);
                password.setError(null);

                if (!isDataValid(phNumber)) {
                    isError = true;
                    number.setError("Field can't be empty");
                    mFocusView = number;
                }
                if (!isValidMobile(phNumber)) {
                    isError = true;
                    number.setError("Enter valid phone number");
                    mFocusView = number;
                }
                if (!isDataValid(otpp)) {
                    isError = true;
                    otp.setError("Field can't be empty");
                    mFocusView = otp;
                }
                if (!isDataValid(pass)) {
                    isError = true;
                    password.setError("Field can't be empty");
                    mFocusView = password;
                }

                if (isError) {
                    mFocusView.requestFocus();
                    return false;
                }

                return true;
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
                if (!isValidMobile(phNumber)) {
                    isError = true;
                    number.setError("Enter valid phone number");
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

    private void doSubmitLogin(String number, String pass) {
        boolean isError = false;
        View mFocusView = null;

        mETNumber.setError(null);
        mETPass.setError(null);

        if (!isDataValid(number)){
            isError = true;
            mETNumber.setError("Field can't be empty");
            mFocusView = mETNumber;
        }

        /*if (!isValidMobile(number)){
            isError = true;
            //mETNumber.setError("Enter valid number");
            mETNumber.setError("Enter valid email");
            mFocusView = mETNumber;
        }*/

        if (!isDataValid(pass)){
            isError = true;
            mETPass.setError("Field can't be empty");
            mFocusView = mETPass;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            /*Map<String,String> map = new HashMap<String,String>();
            map.put("userMobile",number);
            map.put("passwords",pass);
            new NetworkManager(this).doPost(map, Apis.API_POST_USER_LOGIN,"TAG_LOGIN",REQUEST_LOGIN,this);*/

            JSONObject map = new JSONObject();
            try {
                map.put("username", number);
                map.put("password", pass);
                map.put("type", "email");
                Log.d("doJSON Request",map.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*new NetworkManager(this).doJSONPost(
                    map,
                    Apis.API_POST_USER_LOGIN,
                    "TAG_LOGIN",
                    REQUEST_LOGIN,
                    this
            );*/
            new NetworkManager(this).doPostCustom(
                    Apis.API_POST_USER_LOGIN,
                    /*String.class,*/
                    LoginModel[].class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_LOGIN",
                    REQUEST_LOGIN,
                    this
            );
            LoadingDialog.showLoadingDialog(this,"Loading...");
        }
    }

    private void getCategories() {
        new NetworkManager(this).doGet(
                null,
                Apis.API_GET_CATEGORY,
                Apis.ACCESS_TOKEN,
                "TAG_CATEGORY",
                REQUEST_CATEGORY,
                this
        );
    }

    /*private void doSubmitNumber(String number) {
        boolean isError = false;
        View mFocusView = null;

        mETNumber.setError(null);

        if (!isDataValid(number)){
            isError = true;
            mETNumber.setError("Field can't be empty");
            mFocusView = mETNumber;
        }

        if (!isValidMobile(number)){
            isError = true;
            mETNumber.setError("Enter valid number");
            mFocusView = mETNumber;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            Map<String,String> map = new HashMap<String,String>();
            map.put("userMobile",number);

            new NetworkManager(this).doPost(map, Apis.API_POST_USER_CHECK_MOBILE_EXIST,"TAG_CHECK_MOBILE_EXIST",REQUEST_CHECK_MOBILE_EXIST,this);

            LoadingDialog.showLoadingDialog(this,"Loading...");
        }
    }*/

    public boolean isDataValid(String mETFirstName) {
        if (mETFirstName.equals("") || mETFirstName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidMobile(String phone) {
        if (phone.matches("^(\\[\\-\\s]?)?[0]?()?[6789]\\d{9}$")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_CHECK_MOBILE_EXIST){
                //ProcessJsonCheckMobileExist(response);
            } else if (requestId == REQUEST_LOGIN){
                ProcessJsonLogin(response);
            } else if (requestId == REQUEST_RESEND_OTP){
                ProcessJsonResndOtp(response);
            }  else if (requestId == REQUEST_FORGOT_PASSWORD){
                ProcessJsonForgotPass(response);
            } else if (requestId == REQUEST_VERIFY_FORGOT_PASSWORD){
                ProcessJsonVerifyPass(response);
            } else if (requestId == REQUEST_UPDATEFCM_ID){
                ProcessJsonUpdateFcm(response);
            } else if (requestId == REQUEST_NOTIFICATION_COUNT_ID){
                ProcessJsonNotiCount(response);
            } else if (requestId == REQUEST_CART_COUNT_ID){
                ProcessJsonCartCount(response);
            } else if (requestId == REQUEST_CATEGORY) {
                processJsonCategory(response);
            } else if (requestId == REQUEST_FORGOT_PASSWORD_NEW) {
                ProcessJsonPasswordForgot(response);
            }

        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void ProcessJsonCartCount(String response) {
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
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonNotiCount(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();

                String count = jsonObject.optString("data");

                COUNT = count;
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("info","login");
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonUpdateFcm(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
//                LoadingDialog.cancelLoading();
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                intent.putExtra("info","login");
//                startActivity(intent);
//                finish();
                getCartCount();
//                getNotificationCount();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
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
//        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    private void ProcessJsonVerifyPass(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
                dialogWarning(LoginActivity.this, msg);
            } else {

                mADialogLog.dismiss();
                Toast.makeText(this, "You have successfully updated your new password", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonForgotPass(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
                loadRegister();
            } else {

                Toast.makeText(this, "OTP Send", Toast.LENGTH_SHORT).show();
                getOtp.setText("Resend OTP");
                inputLayoutotp.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonResndOtp(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                String msg = jsonObject.optString("message");
                dialogWarning(LoginActivity.this, msg);
            } else {
                Toast.makeText(this, "OTP Send", Toast.LENGTH_SHORT).show();
//                openDialog(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    /*private void ProcessJsonLogin(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                String msg = jsonObject.optString("message");
                dialogWarning(LoginActivity.this, msg);
            } else {
                manager.setLogIn(true);
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                manager.savedelicioId(jsonObject1.optString("id"));
                String name = jsonObject1.optString("firstname");
                String number = jsonObject1.optString("phone");
                String mail = jsonObject1.optString("email");
                String unique_id = jsonObject1.optString("unique_id");


                manager.saveUsereDetails(name,mail,number,unique_id);

                doUpdateFcm();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }*/

    private void ProcessJsonLogin(String response) {
        try {
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<ArrayList<LoginModel>>(){}.getType();
                ArrayList<LoginModel> loginResponseList = gson.fromJson(response, type);
                if (!loginResponseList.isEmpty()) {
                    String message = loginResponseList.get(0).getMessage();
                    if (!message.equals("success")) {
                        LoadingDialog.cancelLoading();
                        dialogWarning(LoginActivity.this, message);
                    } else {
                        String token = loginResponseList.get(0).getToken();
                        CustomerData cData = loginResponseList.get(0).getCustomer_data();
                        if (cData != null) {
                            String mName = cData.getFirstname() + " " + cData.getLastname();
                            String mId = cData.getId();
                            String mEmail = cData.getEmail();

                            manager.saveUserName(mName);
                            manager.saveUserUniqueId(mId);
                            manager.saveUserEmail(mEmail);
                            manager.saveUserToken(token);
                            manager.setLogIn(true);

                            getCategories();

                        } else {
                            serverErrorDialog();
                        }

                        /*LoadingDialog.cancelLoading();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("info","login");
                        startActivity(intent);
                        finish();*/
                    }
                } else {
                    serverErrorDialog();
                }
            } else {
                serverErrorDialog();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            try {
                Type type = new TypeToken<ArrayList<AddAddressResponse>>(){}.getType();
                ArrayList<AddAddressResponse> loginErrorResponseList = gson.fromJson(response, type);
                if (loginErrorResponseList != null && !loginErrorResponseList.isEmpty()) {
                    String message = loginErrorResponseList.get(0).getMessage();
                    if (!message.equals("success")) {
                        LoadingDialog.cancelLoading();
                        dialogWarning(LoginActivity.this, message);
                    } else {
                        serverErrorDialog();
                    }
                } else {
                    serverErrorDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
                serverErrorDialog();
            }
        }
    }

    private void processJsonCategory(String response) {
        if(response == null){
            Log.d(TAG, "processJson: Cant get Category");
            dialogWarning(this, "Sorry ! Can't connect to server, try later");
        } else {
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONArray jsonArray1 = jsonObject1.optJSONArray("children_data");
                JSONObject jsonObject2 = jsonArray1.optJSONObject(0);
                JSONArray jsonArray2 = jsonObject2.optJSONArray("children_data");

                mCateList.clear();

                for (int i = 0;i<jsonArray2.length();i++){

                    JSONObject jsonObject3 = jsonArray2.getJSONObject(i);

                    CategoryModel categoryModel = new CategoryModel();
                    categoryModel.setId(jsonObject3.optString("id"));
                    categoryModel.setName(jsonObject3.optString("name"));

                    mCateList.add(categoryModel);
                }

                CategoryList = mCateList;

                LoadingDialog.cancelLoading();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("info","login");
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                dialogWarning(this, "Sorry ! Can't connect to server, try later");
            }
        }
    }

    private void ProcessJsonPasswordForgot(String response) {
        Log.d(TAG, response);
        try {
            boolean passBool = Boolean.parseBoolean(response);
            if (passBool) {
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Email successfully sent", Toast.LENGTH_SHORT).show();
            } else {
                String message = "Oops no user was found associated with this email";
                LoadingDialog.cancelLoading();
                dialogWarning(this, message);
                /*Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse errorMessage = gson.fromJson(response, type);
                if (errorMessage != null) {
                    String message = errorMessage.getMessage();
                    LoadingDialog.cancelLoading();
                    dialogWarning(this, message);
                } else {
                    serverErrorDialog();
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            Type type = new TypeToken<AddAddressResponse>(){}.getType();
            AddAddressResponse errorMessage = gson.fromJson(response, type);
            if (errorMessage != null) {
                String message = errorMessage.getMessage();
                LoadingDialog.cancelLoading();
                dialogWarning(this, message);
            } else {
                serverErrorDialog();
            }
        }
    }

    private void serverErrorDialog() {
        LoadingDialog.cancelLoading();
        dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
    }

    private void doUpdateFcm() {
        Map<String,String> map = new HashMap<>();
        map.put("device_type","Android");
        map.put("device_id",getDeviceId(this));
        map.put("fcm_token",manager.getUserNotificationTocken());
        map.put("user_id",manager.getdelicioId());
        new NetworkManager(this).doPost(map, Apis.API_POST_UPDATEFCM,"REQUEST_UPDATEFCM",REQUEST_UPDATEFCM_ID,this);
//        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    /*private void ProcessJsonCheckMobileExist(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                String msg = jsonObject.optString("message");
                LoadingDialog.cancelLoading();
                if (msg.equals("Otp Sent")){
                    loadRegister();
                } else {
                    showPassLayAnim();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(LoginActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }*/

    public  static String getDeviceId(Context context){
        String m_androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_androidId;
    }

    /*private void showPassLayAnim() {
        YoYo.with(Techniques.BounceInUp)
                .duration(1000)
                .repeat(0)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mPassLay.setVisibility(View.VISIBLE);
                        isPassLay = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        mPermissionManager.setRequiredPermissions(permissions);
//                        startProcess();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(mPassLay);
    }*/

    private void loadRegister() {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        //intent.putExtra("number",number);
        startActivity(intent);
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }
}
