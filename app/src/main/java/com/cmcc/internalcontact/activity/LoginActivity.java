package com.cmcc.internalcontact.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.usecase.LoginUsecase;
import com.cmcc.internalcontact.utils.view.CommonButton;
import com.cmcc.internalcontact.utils.view.StatusBarUtil;
import com.cmcc.internalcontact.utils.view.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cmcc.internalcontact.activity.ResetPasswordActivity.TAG_LOGIN_CODE;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.ed_login_account)
    EditText edAccount;
    @BindView(R.id.ed_login_pwd)
    EditText edPassword;
    @BindView(R.id.sw_auto_login)
    SwitchButton swAutoLogin;
    @BindView(R.id.lay_auto_login)
    View autoLoginLay;
    @BindView(R.id.btn_login)
    CommonButton loginBtn;
    public static final String TAG_AUTO_LOGIN = "TAG_AUTO_LOGIN";
    @BindView(R.id.lay_login_title)
    View commonToolBar;
    @BindView(R.id.iv_pwd_eye_icon)
    ToggleButton ivPwdEyeIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, commonToolBar);
//        loginBtn.setEnabled(false);
        swAutoLogin.setOpen(preferencesUtils.getBoolean(TAG_AUTO_LOGIN));
        swAutoLogin.setOnStateChangeListener(new SwitchButton.OnStateChangeListener() {
            @Override
            public void onChange(View v, boolean isOpen) {
                preferencesUtils.setBoolean(TAG_AUTO_LOGIN, isOpen);
                swAutoLogin.setOpen(isOpen);
            }
        });
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
                loginBtn.setEnabled(length == 11 && edPassword.getText().length() >= 6 && edPassword.getText().length() <= 20);
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
        Intent intent = new Intent(this, QuickLoginActivity.class);
        intent.putExtra(TAG_LOGIN_CODE, edAccount.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.tv_forget_pwd)
    public void toForgetPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(TAG_LOGIN_CODE, edAccount.getText().toString());
        startActivity(intent);
    }

    @OnCheckedChanged(R.id.iv_pwd_eye_icon)
    public void onShowPassword(CompoundButton compoundButton, boolean b) {
        if (b) {
            edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edPassword.setSelection(edPassword.getText().length());
        } else {
            edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edPassword.setSelection(edPassword.getText().length());
        }
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录请稍后...");
        LoginUsecase loginUsecase = new LoginUsecase();
        progressDialog.show();
        loginUsecase.login(this, edAccount.getText().toString().trim(), edPassword.getText().toString().trim())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObserver<LoginResponseBean>(this) {

            @Override
            public void onNext(LoginResponseBean loginResponseBean) {
                progressDialog.dismiss();
                loginUsecase.saveUseInfo(loginResponseBean);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onComplete() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "登录失败，" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onTokenValid() {
        Toast.makeText(getApplicationContext(), "登录已过期，请重新登录。", Toast.LENGTH_SHORT).show();

    }
}
