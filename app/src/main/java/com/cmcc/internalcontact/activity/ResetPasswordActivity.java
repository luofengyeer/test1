package com.cmcc.internalcontact.activity;

import android.os.Bundle;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;

public class ResetPasswordActivity extends BaseActivity {
    public static final String TAG_LOGIN_CODE = "TAG_LOGIN_CODE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
