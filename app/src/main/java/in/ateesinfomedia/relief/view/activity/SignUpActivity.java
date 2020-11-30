package in.ateesinfomedia.relief.view.activity;

import android.animation.Animator;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
//import in.ateesinfomedia.remedio.components.SmsReceiver;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.cart.CartModel;
import in.ateesinfomedia.relief.models.login.LoginModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;

import static in.ateesinfomedia.relief.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class SignUpActivity extends AppCompatActivity implements NetworkCallback {

    private Button mBtnSignUp;
    private EditText mETUserFName;
    private EditText mETUserLName;
    private EditText mETEmail;
    private EditText mETPhone;
    private EditText mETOtp;
    //private EditText mETReferal;
    private EditText mETPass;
    private Button mIBLogin;
    private Button mIBTerms;
    //private TextView mTvShow;
    private TextView mTvResend;
    //private CheckBox checkBox;
    private CheckBox checkBox1;
    //private TextInputLayout mInLayReferal;
    private boolean isShow;
    private String fname,otp,email,phone,pass,referal,lName;
    private int REQUEST_REGISTRATION = 9090;
    private int REQUEST_CHECK_MOBILE_EXIST = 3657;
    private String otpDigits;
    private String intent_number;
    private boolean isResend = false;
    Gson gson = new Gson();

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mBtnSignUp = (Button) findViewById(R.id.ib_register_next);

        mETUserFName = (EditText) findViewById(R.id.et_register_fullname);
        mETUserLName = (EditText) findViewById(R.id.et_register_last_name);
        mETEmail = (EditText) findViewById(R.id.et_register_email);
        mETPhone = (EditText) findViewById(R.id.et_register_pnone);
        mETOtp = (EditText) findViewById(R.id.et_register_otp);
        //mInLayReferal = (TextInputLayout) findViewById(R.id.input_layout_referal);
        //mETReferal = (EditText) findViewById(R.id.et_register_referal);
        mETPass = (EditText) findViewById(R.id.et_register_pass);
        mIBLogin= (Button) findViewById(R.id.login);
        mIBTerms= (Button) findViewById(R.id.bttn_register_terms);
        //mTvShow = (TextView)  findViewById(R.id.show);
        mTvResend = (TextView)  findViewById(R.id.resend);

        //checkBox = (CheckBox)  findViewById(R.id.checkboxReferal);
        checkBox1 = (CheckBox)  findViewById(R.id.checkboxTerms);

        intent_number = getIntent().getStringExtra("number");

        if (intent_number != null) {
            mETPhone.setText(intent_number);
            mETPhone.setEnabled(false);
            mETPhone.setClickable(false);
        }

        mIBTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,RemediotcActivity.class);
                startActivity(intent);
            }
        });
        /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()){
                    showReferalLayAnim();
                } else {
                    hideReferalLayAnim();
                }
            }
        });*/

        mTvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mETPhone.getText().toString();
                doSubmitNumber(number);
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
        });*/

        /*mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox1.isChecked()) {
                    fname = mETUserFName.getText().toString();
                    otp = mETOtp.getText().toString();
                    email = mETEmail.getText().toString();
                    phone = mETPhone.getText().toString();
                    pass = mETPass.getText().toString();
                    referal = "";

                    doRegister(fname, otp, email, phone, pass, referal);
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Please accept the Terms and Condition for the successful registration.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox1.isChecked()) {
                    fname = mETUserFName.getText().toString();
                    lName = mETUserLName.getText().toString();
                    email = mETEmail.getText().toString();
                    pass = mETPass.getText().toString();
                    phone = mETPhone.getText().toString();
                    otp = mETOtp.getText().toString();

                    doRegister(fname, lName, email, pass, phone, otp);
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Please accept the Terms and Condition for the successful registration.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mIBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mETPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isResend = false;
                mETPhone.setError(null);
                mTvResend.setText("Get OTP");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });
    }

    private void doRegister(String fName, String lName, String email, String pass, String phone, String otp) {

        boolean isError = false;
        View mFocusView = null;

        mETUserFName.setError(null);
        mETUserLName.setError(null);
        mETEmail.setError(null);
        mETPass.setError(null);

        if (!isDataValid(fName)){
            isError = true;
            mETUserFName.setError("Field can't be empty");
            mFocusView = mETUserFName;
        }

        if (!isDataValid(lName)){
            isError = true;
            mETUserLName.setError("Field can't be empty");
            mFocusView = mETUserLName;
        }

        if (!isDataValid(email)){
            isError = true;
            mETEmail.setError("Field can't be empty");
            mFocusView = mETEmail;
        }

        if (!isDataValid(pass)){
            isError = true;
            mETPass.setError("Field can't be empty");
            mFocusView = mETPass;
        }

        if (!isValidEmail(email)){
            isError = true;
            mETEmail.setError("Enter valid email");
            mFocusView = mETEmail;
        }

        if (!isValidPass(pass)){
            isError = true;
            mETPass.setError("Password must contain Lower Case, Upper Case, Digits & Minimum length must be equal or greater than 8");
            mFocusView = mETPass;
        }

        if (!isDataValid(phone)){
            isError = true;
            mETPhone.setError("Field can't be empty");
            mFocusView = mETPhone;
        }

        if (!isValidMobile(phone)){
            isError = true;
            mETPhone.setError("Enter valid number");
            mFocusView = mETPhone;
        }

        if (!isDataValid(otp)){
            isError = true;
            mETOtp.setError("Field can't be empty");
            mFocusView = mETOtp;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            if (isConnected()) {
                try {
                    JSONObject customer = new JSONObject();
                    JSONObject address = new JSONObject();
                    customer.put("otp",otp);
                    customer.put("mobile_number",phone);
                    customer.put("firstname",fName);
                    customer.put("lastname",lName);
                    customer.put("email",email);
                    customer.put("password",pass);
                    customer.put("addresses",address);

                    JSONObject map = new JSONObject();
                    map.put("customer",customer);

                    Log.d("doJSON Request",map.toString());

                    new NetworkManager(this).doPostCustom(
                            Apis.API_POST_USER_REGISTRATION,
                            Object.class,
                            map,
                            Apis.ACCESS_TOKEN,
                            "TAG_LOGIN",
                            REQUEST_REGISTRATION,
                            this
                    );

                    LoadingDialog.showLoadingDialog(this,"Loading...");

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this,"Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(SignUpActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doSubmitNumber(String number) {
        boolean isError = false;
        View mFocusView = null;

        mETPhone.setError(null);

        if (!isDataValid(number)){
            isError = true;
            mETPhone.setError("Field can't be empty");
            mFocusView = mETPhone;
        }

        if (!isValidMobile(number)){
            isError = true;
            mETPhone.setError("Enter valid number");
            mFocusView = mETPhone;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            String sendOtp = "send";
            if (isResend) {
                sendOtp = "resend";
            }
            String getUrl = Apis.API_GET_OTP_RESEND + number + "&type=" + sendOtp;

            new NetworkManager(this).doGetCustom(
                    null,
                    getUrl,
                    Object.class,
                    null,
                    Apis.ACCESS_TOKEN,
                    "TAG_CHECK_MOBILE_EXIST",
                    REQUEST_CHECK_MOBILE_EXIST,
                    this
            );

            LoadingDialog.showLoadingDialog(this,"Loading...");
        }
    }

    public boolean isValidPass(String passwordInput) {
        return !TextUtils.isEmpty(passwordInput) && PASSWORD_PATTERN.matcher(passwordInput).matches();
    }

    public boolean isDataValid(String mETFirstName) {
        if (mETFirstName.equals("") || mETFirstName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isValidEmail(String emailAddress) {
        return !TextUtils.isEmpty(emailAddress) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    private boolean isValidMobile(String phone) {
        if (phone.matches("^(\\[\\-\\s]?)?[0]?()?[6789]\\d{9}$")){
            return true;
        }else {
            return false;
        }
    }

    /*private void doRegister(String fname, String otp, String email, String phone, String pass, String referal) {

        boolean isError = false;
        View mFocusView = null;

        mETUserFName.setError(null);
        mETEmail.setError(null);
        mETPhone.setError(null);
        mETPass.setError(null);
        mETOtp.setError(null);
        //mETReferal.setError(null);

        if (!isDataValid(fname)){
            isError = true;
            mETUserFName.setError("Field can't be empty");
            mFocusView = mETUserFName;
        }

        if (!isDataValid(phone)){
            isError = true;
            mETPhone.setError("Field can't be empty");
            mFocusView = mETPhone;
        }

        if (!isValidMobile(phone)){
            isError = true;
            mETPhone.setError("Enter valid number");
            mFocusView = mETPhone;
        }

        if (!isDataValid(email)){
            isError = true;
            mETEmail.setError("Field can't be empty");
            mFocusView = mETEmail;
        }

        if (!isDataValid(otp)){
            isError = true;
            mETOtp.setError("Field can't be empty");
            mFocusView = mETOtp;
        }

 //       if (checkBox.isChecked()){
 //           if (!isDataValid(referal)){
 //               isError = true;
 //               mETReferal.setError("Field can't be empty");
//                mFocusView = mETReferal;
//            }
//        }

        if (!isValidEmail(email)){
            isError = true;
            mETEmail.setError("Enter valid email");
            mFocusView = mETEmail;
        }

        if (!isDataValid(pass)){
            isError = true;
            mETPass.setError("Field can't be empty");
            mFocusView = mETPass;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            if (isConnected()){

                Map<String,String> map = new HashMap<String,String>();

                map.put("userName",fname);
                map.put("userEmail",email);
                map.put("userMobile",phone);
                map.put("passwords",pass);
                map.put("otp",otp);
                map.put("refid","");
                //if (checkBox.isChecked()){
                    //map.put("refid",referal);
                //} else {
                 //   map.put("refid","");
                //}

                new NetworkManager(this).doPost(map, Apis.API_POST_USER_REGISTRATION,"TAG_REGISTRATION",REQUEST_REGISTRATION,this);

                LoadingDialog.showLoadingDialog(this,"Loading...");
            } else{
                Toast.makeText(SignUpActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doSubmitNumber(String number) {
        boolean isError = false;
        View mFocusView = null;

        mETPhone.setError(null);

        if (!isDataValid(number)){
            isError = true;
            mETPhone.setError("Field can't be empty");
            mFocusView = mETPhone;
        }

        if (!isValidMobile(number)){
            isError = true;
            mETPhone.setError("Enter valid number");
            mFocusView = mETPhone;
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

    /*private void showReferalLayAnim() {
        YoYo.with(Techniques.FadeInDown)
                .duration(1000)
                .repeat(0)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mInLayReferal.setVisibility(View.VISIBLE);
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
                .playOn(mInLayReferal);
    }

    private void hideReferalLayAnim() {
        YoYo.with(Techniques.FadeOutUp)
                .duration(400)
                .repeat(0)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mInLayReferal.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(mInLayReferal);
    }*/

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_REGISTRATION){
                ProcessJsonRegister(response);
            }/* else if (requestId == REQUEST_VERIFY_OTP){
                ProcessJsonVerifyOtp(response);
            }*/ else if (requestId == REQUEST_CHECK_MOBILE_EXIST){
                ProcessJsonResendOtp(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    private void ProcessJsonResendOtp(String response) {
        try {
            LoadingDialog.cancelLoading();
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse resendResponse = gson.fromJson(response, type);
                String message = resendResponse.getMessage();
                if (message != null) {
                    isResend = true;
                    mTvResend.setText("Resend OTP");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    LoadingDialog.cancelLoading();
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

    private void ProcessJsonRegister(String response) {

        try {
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<ArrayList<AddAddressResponse>>(){}.getType();
                ArrayList<AddAddressResponse> loginData = gson.fromJson(response, type);
                String message = loginData.get(0).getMessage();
                if (message == null || message.equals("success")) {
                    Toast.makeText(this, "Registration completed. You can sign in now.", Toast.LENGTH_LONG).show();
                    finish();
                    LoadingDialog.cancelLoading();
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(SignUpActivity.this, message);
                }
            } else {
                serverErrorDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse loginData = gson.fromJson(response, type);
                if (loginData != null) {
                    String message = loginData.getMessage();
                    LoadingDialog.cancelLoading();
                    dialogWarning(SignUpActivity.this, message);
                } else {
                    serverErrorDialog();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                serverErrorDialog();
            }
        }
    }

    private void serverErrorDialog() {
        LoadingDialog.cancelLoading();
        dialogWarning(SignUpActivity.this, "Sorry ! Can't connect to server, try later");
    }

    /*private void ProcessJsonRegister(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                String msg = jsonObject.optString("message");
                dialogWarning(SignUpActivity.this, msg);
                //  Toast.makeText(this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                LoadingDialog.cancelLoading();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(SignUpActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonResendOtp(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            } else {
                String msg = jsonObject.optString("message");
                if (!msg.equals("Otp Sent")){
                    dialogWarning(SignUpActivity.this, msg);
                } else  {
                    mTvResend.setText("Resend OTP");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(SignUpActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonRegister(String response) {

        try {
            if (response != null && !response.equals("null")) {
                Type type = new TypeToken<LoginModel>(){}.getType();
                LoginModel loginData = gson.fromJson(response, type);
                //LoginModel loginData = gson.fromJson(response, LoginModel.class);
                String message = loginData.getMessage();
                if (message == null || message.equals("success")) {
                    Toast.makeText(this, "Registration completed. You can sign in now.", Toast.LENGTH_LONG).show();
                    finish();
                    LoadingDialog.cancelLoading();
                } else {
                    LoadingDialog.cancelLoading();
                    dialogWarning(SignUpActivity.this, message);
                }
            } else {
                serverErrorDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            serverErrorDialog();
        }
    }

    private void doRegister(String fName, String lName, String email, String pass) {

        boolean isError = false;
        View mFocusView = null;

        mETUserFName.setError(null);
        mETUserLName.setError(null);
        mETEmail.setError(null);
        mETPass.setError(null);

        if (!isDataValid(fName)){
            isError = true;
            mETUserFName.setError("Field can't be empty");
            mFocusView = mETUserFName;
        }

        if (!isDataValid(lName)){
            isError = true;
            mETUserLName.setError("Field can't be empty");
            mFocusView = mETUserLName;
        }

        if (!isDataValid(email)){
            isError = true;
            mETEmail.setError("Field can't be empty");
            mFocusView = mETEmail;
        }

        if (!isDataValid(pass)){
            isError = true;
            mETPass.setError("Field can't be empty");
            mFocusView = mETPass;
        }

        if (!isValidEmail(email)){
            isError = true;
            mETEmail.setError("Enter valid email");
            mFocusView = mETEmail;
        }

        if (!isValidPass(pass)){
            isError = true;
            mETPass.setError("Password must contain Lower Case, Upper Case, Digits & Minimum length must be equal or greater than 8");
            mFocusView = mETPass;
        }

        if (isError){
            mFocusView.requestFocus();
            return;
        }else {
            if (isConnected()){
                JSONObject cust = new JSONObject();
                try {
                    cust.put("firstname",fName);
                    cust.put("lastname",lName);
                    cust.put("email",email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject map = new JSONObject();
                try {
                    map.put("customer",cust);
                    map.put("password",pass);
                    Log.d("doJSON Request",map.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new NetworkManager(this).doPostCustom(
                        Apis.API_POST_USER_REGISTRATION,
                        LoginModel.class,
                        map,
                        Apis.ACCESS_TOKEN,
                        "TAG_LOGIN",
                        REQUEST_REGISTRATION,
                        this
                );

                LoadingDialog.showLoadingDialog(this,"Loading...");
            } else{
                Toast.makeText(SignUpActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}