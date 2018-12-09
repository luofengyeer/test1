package com.cmcc.internalcontact.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.usecase.MineInfo;
import com.cmcc.internalcontact.utils.ActivityStackManager;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.imagepicker.SingleFileLimitInterceptor;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MineActivity extends BaseActivity {
    private static final String TAG = MineActivity.class.getSimpleName();
    private static final int REQUEST_CODE_IMAGE = 10001;
    @BindView(R.id.toolbar_mine)
    CommonToolBar toolbarMine;
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
    @BindView(R.id.iv_head_pic)
    ImageView ivHeadPic;
    @BindView(R.id.tv_version_update)
    TextView tvVersionUpdate;
    @BindView(R.id.iv_exit)
    ImageView ivExit;
    private Handler handler;
    private static final int MSG_WHAT_GET_MINE = 10001;
    private static final int MSG_WHAT_UPDATE_MINE = 10002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mine);
        ButterKnife.bind(this);
        initHandler();
        initData();
    }

    private void initHandler() {
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_WHAT_GET_MINE:
                        loadMineData();
                        break;
                    case MSG_WHAT_UPDATE_MINE:
                        updateMineData();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initData() {
        loadMineData();
        handler.sendEmptyMessage(MSG_WHAT_UPDATE_MINE);
    }

    private void loadMineData() {
        new MineInfo().getMineInfo().unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<PersonBean>(this) {
                    @Override
                    public void onNext(PersonBean personBean) {
                        if (personBean == null) {
                            return;
                        }
                        Glide.with(MineActivity.this).setDefaultRequestOptions(
                                new RequestOptions().error(R.mipmap.ic_edit_info_headimg).circleCrop())
                                .load(personBean.getAvator()).into(ivHeadPic);
                        tvUsername.setText(personBean.getName());
                        tvMobilePhone.setText(personBean.getPhone());
                        tvTel.setText(personBean.getTel());
                        tvEmail.setText(personBean.getEmail());
                        tvJob.setText(personBean.getJob());
                        DepartModel mechanism = personBean.getMechanism();
                        if (mechanism != null) {
                            tvMechanism.setText(mechanism.getDeptName());
                        }
                        DepartModel depart = personBean.getDepart();
                        if (depart != null) {
                            tvCompany.setText(depart.getDeptName());
                        }
                    }
                });
    }

    private void updateMineData() {
        new MineInfo().updateMine(this).unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Void>(this) {
                    @Override
                    public void onNext(Void aVoid) {
                        handler.sendEmptyMessage(MSG_WHAT_GET_MINE);
                    }
                });
    }

    private void uploadHeadPic(String headPicPath) {
        new MineInfo().uploadHeadPic(this, headPicPath).unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>(this) {
                    @Override
                    public void onNext(String path) {
                        Glide.with(MineActivity.this).setDefaultRequestOptions(
                                new RequestOptions().error(R.mipmap.ic_edit_info_headimg).circleCrop())
                                .load(path).into(ivHeadPic);
                    }
                });
    }

    @OnClick({R.id.iv_head_pic, R.id.tv_version_update, R.id.iv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head_pic:
                SImagePicker.from(MineActivity.this)
                        .maxCount(1)
                        .rowCount(3)
                        .showCamera(false)
                        .pickMode(SImagePicker.MODE_IMAGE)
                        .fileInterceptor(new SingleFileLimitInterceptor())
                        .forResult(REQUEST_CODE_IMAGE);
                break;
            case R.id.tv_version_update:
                break;
            case R.id.iv_exit:
                SharePreferencesUtils.getInstance().setString(Constant.TAG_HTTP_TOKEN, "");
                SharePreferencesUtils.getInstance().setLong(Constant.TAG_HTTP_TOKEN_EXPIRE, 0);
                ActivityStackManager.getInstance().finishAllActivity();
                break;
        }
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
            uploadHeadPic(pathList.get(0));
        }
    }
}
