package com.cmcc.internalcontact.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.usecase.LoginUsecase;
import com.cmcc.internalcontact.usecase.UpdateContactUseCase;
import com.cmcc.internalcontact.utils.PermissionsUtils;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.permission.floatpermission.FloatPermissionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LauncherActivity extends BaseActivity {

    @BindView(R.id.iv_background)
    ImageView ivBackground;
    private Handler handler = null;
    private static final List<String> launcherPermission = new ArrayList<>();
    private int requsetCode = 200;
    private int requsetCodeBySeting = 250;

    static {
        launcherPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        launcherPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        launcherPermission.add(Manifest.permission.READ_PHONE_STATE);
        launcherPermission.add(Manifest.permission.READ_CALL_LOG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.ic_launcher_default).apply(new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_launcher_default)
                .priority(Priority.HIGH)).into(ivBackground);
        if (PermissionsUtils.havePermission(this, launcherPermission)) {
            if (!FloatPermissionManager.checkPermission(this)) {
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
                                finish();
                            }
                        });
                return;
            }
            goNext();
            return;
        } else {
            PermissionsUtils.permissionRequest(LauncherActivity.this, requsetCode, new PermissionsUtils.OnPermissionListener() {
                @Override
                public void onSucceed(int requestCode) {
                    if (!FloatPermissionManager.checkPermission(LauncherActivity.this)) {
                        PermissionsUtils.showPermissionDialog(LauncherActivity.this,
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
                                        finish();
                                    }
                                });
                        return;
                    }
                }

                @Override
                public void onFailed(int requestCode) {
                    finish();
                }
            }, launcherPermission);
        }
    }

    private void goNext() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new LoginUsecase().isTokenValid() && SharePreferencesUtils.getInstance().getBoolean(LoginActivity.TAG_AUTO_LOGIN)) {
                    startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                    new UpdateContactUseCase(LauncherActivity.this).updateContact().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new MyObserver(LauncherActivity.this) {
                                @Override
                                public void onNext(Object o) {

                                }
                            });
                } else {
                    startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 500);
    }
}
