package com.cmcc.internalcontact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;

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
                startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
            }
        }, 1500);
    }
}
