package com.cmcc.internalcontact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.utils.view.CommonButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cmcc.internalcontact.activity.ResetPasswordActivity.TAG_LOGIN_CODE;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.ed_login_account)
    EditText edAccount;
    @BindView(R.id.ed_login_pwd)
    EditText edPassword;
    @BindView(R.id.sw_auto_login)
    Switch swAutoLogin;
    @BindView(R.id.lay_auto_login)
    View autoLoginLay;
    @BindView(R.id.btn_login)
    CommonButton loginBtn;
    private static final String TAG_AUTO_LOGIN = "TAG_AUTO_LOGIN";
    private boolean isShowPwd = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginBtn.setEnabled(false);
        edAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    return;
                }
                int length = editable.toString().length();
                loginBtn.setEnabled(length == 11 &&edPassword.getText().length() >= 6 && edPassword.getText().length() <= 20 );
            }
        });
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    return;
                }
                int length = editable.toString().length();
                loginBtn.setEnabled(length >= 6 && length <= 20 && edAccount.getText().length() == 11);
            }
        });
    }

    @OnClick(R.id.tv_fast_login)
    public void toFastLogin() {

    }

    @OnClick(R.id.tv_forget_pwd)
    public void toForgetPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(TAG_LOGIN_CODE, edAccount.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.iv_pwd_eye_icon)
    public void onShowPassword() {
        if (isShowPwd) {
            edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edPassword.setSelection(edPassword.getText().length());
        } else {
            edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edPassword.setSelection(edPassword.getText().length());
        }
        isShowPwd = !isShowPwd;
    }

    @OnClick(R.id.btn_login)
    public void toLogin() {
        if (TextUtils.isEmpty(edAccount.getText().toString())) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edPassword.getText().toString())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @OnClick(R.id.lay_auto_login)
    public void openAutoLogin() {
        swAutoLogin.setChecked(!swAutoLogin.isChecked());
        preferencesUtils.setBoolean(TAG_AUTO_LOGIN, swAutoLogin.isChecked());
    }
}
