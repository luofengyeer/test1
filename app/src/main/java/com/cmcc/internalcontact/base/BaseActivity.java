package com.cmcc.internalcontact.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cmcc.internalcontact.utils.SharePreferencesUtils;

public class BaseActivity extends AppCompatActivity {
    protected SharePreferencesUtils preferencesUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesUtils = new SharePreferencesUtils(getApplicationContext());
    }
}
