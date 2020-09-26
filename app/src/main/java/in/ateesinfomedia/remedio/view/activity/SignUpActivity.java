package in.ateesinfomedia.remedio.view.activity;

import android.animation.Animator;
import android.content.Intent;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
//import in.ateesinfomedia.remedio.components.SmsReceiver;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.managers.NetworkManager;

import static in.ateesinfomedia.remedio.components.ConnectivityReceiver.isConnected;
import static in.ateesinfomedia.remedio.configurations.Global.dialogWarning;

public class SignUpActivity extends AppCompatActivity implements NetworkCallback {

    private Button mBtnSignUp;
    private EditText mETUserFName;
    private EditText mETEmail;
    private EditText mETPhone;
    private EditText mETOtp;
    private EditText mETReferal;
    private EditText mETPass;
    private Button mIBLogin;
    private Button mIBTerms;
    private TextView mTvShow;
    private TextView mTvResend;
    private CheckBox checkBox;
    private CheckBox checkBox1;
    private TextInputLayout mInLayReferal;
    private boolean isShow;
    private String fname,otp,email,phone,pass,referal;
    private int REQUEST_REGISTRATION = 9090;
    private int REQUEST_CHECK_MOBILE_EXIST = 3657;
    private String otpDigits;
    private String intent_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mBtnSignUp = (Button) findViewById(R.id.ib_register_next);

        mETUserFName = (EditText) findViewById(R.id.et_register_fullname);
        mETEmail = (EditText) findViewById(R.id.et_register_email);
        mETPhone = (EditText) findViewById(R.id.et_register_pnone);
        mETOtp = (EditText) findViewById(R.id.et_register_otp);
        mInLayReferal = (TextInputLayout) findViewById(R.id.input_layout_referal);
        mETReferal = (EditText) findViewById(R.id.et_register_referal);
        mETPass = (EditText) findViewById(R.id.et_register_pass);
        mIBLogin= (Button) findViewById(R.id.login);
        mIBTerms= (Button) findViewById(R.id.bttn_register_terms);
        mTvShow = (TextView)  findViewById(R.id.show);
        mTvResend = (TextView)  findViewById(R.id.resend);

        checkBox = (CheckBox)  findViewById(R.id.checkboxReferal);
        checkBox1 = (CheckBox)  findViewById(R.id.checkboxTerms);

        intent_number = getIntent().getStringExtra("number");

        mETPhone.setText(intent_number);
        mETPhone.setEnabled(false);
        mETPhone.setClickable(false);

        mIBTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,RemediotcActivity.class);
                startActivity(intent);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()){
                    showReferalLayAnim();
                } else {
                    hideReferalLayAnim();
                }
            }
        });

        mTvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mETPhone.getText().toString();
                doSubmitNumber(number);
            }
        });

        mTvShow.setOnClickListener(new View.OnClickListener() {
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

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();

                if (checkBox1.isChecked()) {
                    fname = mETUserFName.getText().toString();
                    otp = mETOtp.getText().toString();
                    email = mETEmail.getText().toString();
                    phone = mETPhone.getText().toString();
                    pass = mETPass.getText().toString();
                    referal = mETReferal.getText().toString();

                    doRegister(fname, otp, email, phone, pass, referal);
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

    private void doRegister(String fname, String otp, String email, String phone, String pass, String referal) {

        boolean isError = false;
        View mFocusView = null;

        mETUserFName.setError(null);
        mETEmail.setError(null);
        mETPhone.setError(null);
        mETPass.setError(null);
        mETOtp.setError(null);
        mETReferal.setError(null);

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

        if (checkBox.isChecked()){
            if (!isDataValid(referal)){
                isError = true;
                mETReferal.setError("Field can't be empty");
                mFocusView = mETReferal;
            }
        }

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
                if (checkBox.isChecked()){
                    map.put("refid",referal);
                } else {
                    map.put("refid","");
                }

                new NetworkManager(this).doPost(map, Apis.API_POST_USER_REGISTRATION,"TAG_REGISTRATION",REQUEST_REGISTRATION,this);

                LoadingDialog.showLoadingDialog(this,"Loading...");
            } else{
                Toast.makeText(SignUpActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showReferalLayAnim() {
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
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_REGISTRATION){
                ProcessJsonRegister(response);
            }/* else if (requestId == REQUEST_VERIFY_OTP){
                ProcessJsonVerifyOtp(response);
            }*/ else if (requestId == REQUEST_CHECK_MOBILE_EXIST){
                ProcessJsonResndOtp(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(this, "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void ProcessJsonResndOtp(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dialogWarning(SignUpActivity.this, "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonRegister(String response) {

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

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}