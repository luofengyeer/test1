package com.cmcc.internalcontact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.usecase.LoginUsecase;
import com.cmcc.internalcontact.utils.PermissionsUtils;
import com.cmcc.internalcontact.utils.permission.floatpermission.FloatPermissionManager;

public class LauncherActivity extends BaseActivity {

    private Handler handler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (FloatPermissionManager.checkPermission(this)) {
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
            return;
        }
        PermissionsUtils.showPermissionDialog(this,
                getString(R.string.common_permission_title),
                getString(R.string.income_permission_msg),
                getString(R.string.income_permission_pos),
                getString(R.string.income_permission_nag),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FloatPermissionManager.applyPermission(LauncherActivity.this);
                    }
                });

    }
}
