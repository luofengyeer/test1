package com.cmcc.internalcontact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.usecase.LoginUsecase;

public class LauncherActivity extends BaseActivity {

    private Handler handler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new LoginUsecase().isTokenValid()) {
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 500);
    }
}
