package com.cmcc.internalcontact.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.utils.imagepicker.SingleFileLimitInterceptor;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineActivity extends BaseActivity {
    private static final String TAG = MineActivity.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE = 10001;
    @BindView(R.id.toolbar_mine)
    CommonToolBar toolbarMine;
    @BindView(R.id.iv_head_pic)
    ImageView ivHeadPic;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_mobile_phone)
    TextView tvMobilePhone;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_mechanism)
    TextView tvMechanism;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_job)
    TextView tvJob;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mine);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_head_pic)
    public void onViewClicked() {
        SImagePicker.from(MineActivity.this)
                .maxCount(1)
                .rowCount(3)
                .showCamera(false)
                .pickMode(SImagePicker.MODE_IMAGE)
                .fileInterceptor(new SingleFileLimitInterceptor())
                .forResult(REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            boolean original =
                    data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);
            Log.e(TAG, "path: " + pathList.get(0));
            Log.e(TAG, "original: " + original);
        }
    }
}
