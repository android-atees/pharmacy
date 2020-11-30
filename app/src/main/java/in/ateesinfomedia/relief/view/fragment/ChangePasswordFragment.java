package in.ateesinfomedia.relief.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.cart.CartAttributeInfo;
import in.ateesinfomedia.relief.models.cart.CartModel;
import in.ateesinfomedia.relief.models.login.LoginModel;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.activity.SignUpActivity;

import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class ChangePasswordFragment extends DialogFragment implements NetworkCallback {

    private EditText mETCurrentPass;
    private EditText mETNewPass;
    private EditText mETConfirmPass;
    private TextInputLayout mCurrentPassMain;
    private TextInputLayout mNewPassMain;
    private TextInputLayout mConfirmPassMain;
    private ConstraintLayout mCancel, mSubmit;
    private String sCurrent, sNew, sConfirm;
    private static final String TAG = ChangePasswordFragment.class.getName();
    private MyPreferenceManager manager;
    private int REQUEST_UPDATE_PASSWORD = 6554;
    private Gson gson = new Gson();
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);

        manager = new MyPreferenceManager(getActivity());
        mETCurrentPass = view.findViewById(R.id.change_pass_current);
        mETNewPass = view.findViewById(R.id.change_pass_new);
        mETConfirmPass = view.findViewById(R.id.change_pass_confirm);
        mCurrentPassMain = view.findViewById(R.id.change_pass_current_main);
        mNewPassMain = view.findViewById(R.id.change_pass_new_main);
        mConfirmPassMain = view.findViewById(R.id.change_pass_confirm_main);
        mCancel = view.findViewById(R.id.change_pass_cancel);
        mSubmit = view.findViewById(R.id.change_pass_submit);

        mETNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mNewPassMain.setError(null);
                mConfirmPassMain.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });
        mETConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String currentNewPass = mETNewPass.getText().toString();
                if (!currentNewPass.equals(charSequence.toString())) {
                    mConfirmPassMain.setError("Passwords do not match");
                } else {
                    mConfirmPassMain.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sCurrent = mETCurrentPass.getText().toString();
                sNew = mETNewPass.getText().toString();
                sConfirm = mETConfirmPass.getText().toString();

                validateFields();
            }
        });

    }

    private void validateFields() {
        boolean isError = false;
        View mFocusView = null;

        mCurrentPassMain.setError(null);
        mNewPassMain.setError(null);
        mConfirmPassMain.setError(null);

        if (!isDataValid(sCurrent)){
            isError = true;
            mCurrentPassMain.setError("Field can't be empty");
            mFocusView = mETCurrentPass;
        }

        if (!isDataValid(sNew)){
            isError = true;
            mNewPassMain.setError("Field can't be empty");
            mFocusView = mETNewPass;
        }

        if (!isDataValid(sConfirm)){
            isError = true;
            mConfirmPassMain.setError("Field can't be empty");
            mFocusView = mETConfirmPass;
        }

        if (!isValidPass(sNew)){
            isError = true;
            mNewPassMain.setError("Password must contain Lower Case, Upper Case, Digits & Minimum length must be equal or greater than 8");
            mFocusView = mETNewPass;
        }

        if (!sNew.equals(sConfirm)) {
            isError = true;
            mConfirmPassMain.setError("Passwords do not match");
            mFocusView = mETConfirmPass;
        }

        if (isError){
            mFocusView.requestFocus();
        } else {
            updatePassword();
        }

    }

    private void updatePassword() {

        try {
            JSONObject map = new JSONObject();
            map.put("currentPassword", sCurrent);
            map.put("newPassword", sNew);
            Log.d("doJSON Request", map.toString());

            String urlString = Apis.API_PUT_PASSWORD_UPDATE + manager.getUserUniqueId();

            new NetworkManager(requireContext()).doPutCustom(
                    urlString,
                    Object.class,
                    map,
                    Apis.ACCESS_TOKEN,
                    "TAG_UPDATE_PASSWORD",
                    REQUEST_UPDATE_PASSWORD,
                    this
            );
            LoadingDialog.showLoadingDialog(requireContext(),"Loading....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ProcessJsonUpdatePassword(String response) {

        try {
            if (response != null && !response.equals("null")) {
                boolean responseBool = Boolean.parseBoolean(response);
                if (responseBool) {
                    LoadingDialog.cancelLoading();
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        Toast.makeText(getActivity(), "Password was updated successfully.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                } else {
                    Type type = new TypeToken<AddAddressResponse>(){}.getType();
                    AddAddressResponse updateResponse = gson.fromJson(response, type);
                    if (updateResponse != null && updateResponse.getMessage() != null) {
                        String message = updateResponse.getMessage();
                        LoadingDialog.cancelLoading();
                        dialogWarning(requireActivity(), message);

                        mETCurrentPass.setText(null);
                    } else {
                        LoadingDialog.cancelLoading();
                        Toast.makeText(getActivity(), "Couldn't update password. Try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                serverErrorDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse updateResponse = gson.fromJson(response, type);
                if (updateResponse != null && updateResponse.getMessage() != null) {
                    String message = updateResponse.getMessage();
                    LoadingDialog.cancelLoading();
                    dialogWarning(requireActivity(), message);

                    mETCurrentPass.setText(null);
                } else {
                    serverErrorDialog();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                serverErrorDialog();
            }
        }
    }

    private boolean isDataValid(String mName) {
        if (mName.equals("") || mName.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isValidPass(String passwordInput) {
        return !TextUtils.isEmpty(passwordInput) && PASSWORD_PATTERN.matcher(passwordInput).matches();
    }

    private void serverErrorDialog() {
        try {
            LoadingDialog.cancelLoading();
            dialogWarning(requireContext(), "Sorry ! Can't connect to server, try later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_UPDATE_PASSWORD){
                ProcessJsonUpdatePassword(response);
            } /*else if (requestId == REQUEST_EDIT_ADDRESS){
                ProcessJsonAddAddress(response);
            }*/
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(getActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

}