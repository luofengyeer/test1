package com.cmcc.internalcontact.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cmcc.internalcontact.BuildConfig;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.UpdateAppBean;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.usecase.MineInfo;
import com.cmcc.internalcontact.utils.ActivityStackManager;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.Utils;
import com.cmcc.internalcontact.utils.imagepicker.SingleFileLimitInterceptor;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.cmcc.internalcontact.utils.view.CustomDialog;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cmcc.internalcontact.usecase.LoginUsecase.TAG_USE_INFO;

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
    private ProgressDialog dialog;
    private LoginResponseBean.UserInfo personBean;

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
                checkUpdate();
                break;
            case R.id.iv_exit:
                SharePreferencesUtils.getInstance().setString(Constant.TAG_HTTP_TOKEN, "");
                SharePreferencesUtils.getInstance().setLong(Constant.TAG_HTTP_TOKEN_EXPIRE, 0);
                ActivityStackManager.getInstance().finishAllActivity();
                break;
        }
    }

    private void loadMineData() {
        new MineInfo().getMineInfo().unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<PersonBean>(this) {
                    @Override
                    public void onNext(PersonBean personBean) {
                        show(personBean);
                    }
                });
    }

    private void updateMineData() {
        new MineInfo().updateMine(this).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<LoginResponseBean.UserInfo>(this) {
                    @Override
                    public void onNext(LoginResponseBean.UserInfo userInfo) {
                        if (userInfo != null) {
                            userInfo.setHeadPic(Utils.buildHeadPic(userInfo.getHeadPic()));
                            SharePreferencesUtils.getInstance().setString(TAG_USE_INFO, JSON.toJSONString(userInfo));
                        }
                        handler.sendEmptyMessage(MSG_WHAT_GET_MINE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                    }
                });
    }

    private void uploadHeadPic(String headPicPath) {
        new MineInfo().uploadHeadPic(this, headPicPath).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>(this) {
                    @Override
                    public void onNext(String path) {
                        if (TextUtils.isEmpty(path)) {
                            return;
                        }
                        Log.e(TAG, "uploadHeadPic: " + path);
                        String string = SharePreferencesUtils.getInstance().getString(TAG_USE_INFO, "");
                        if (string == null) {
                            return;
                        }
                        LoginResponseBean.UserInfo userInfo = JSON.parseObject(string, LoginResponseBean.UserInfo.class);
                        if (userInfo == null) {
                            return;
                        }
                        userInfo.setHeadPic(Utils.buildHeadPic(path));
                        SharePreferencesUtils.getInstance().setString(TAG_USE_INFO, JSON.toJSONString(userInfo));
                        handler.sendEmptyMessage(MSG_WHAT_GET_MINE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(MineActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUpdate() {
        new MineInfo().checkUpdate(this, String.valueOf(BuildConfig.VERSION_CODE)).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<UpdateAppBean>(this) {
                    @Override
                    public void onNext(UpdateAppBean updateAppBean) {
                        Log.e("checkUpdate", updateAppBean.toString());
                        CustomDialog customDialog = new CustomDialog(MineActivity.this);
                        customDialog.setTitle("提示");
                        customDialog.setMessage("检查到新版本,是否更新", true);
                        customDialog.setOkButton("更新", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                                showDownloadDialog();
                                download(Environment.getExternalStorageDirectory().getAbsolutePath() + "/internalcontact/apk/internalcontact.apk"
                                        , Constant.BASE_AVATRE_URL+updateAppBean.getDownloadPath());
                            }
                        });
                        customDialog.setCancelButton("取消", null);
                        customDialog.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(MineActivity.this, "暂无更新", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void download(String name, String downloadUrl) {
     /*   new MineInfo().downloadApk(this, downloadUrl, name).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<String>(this) {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });*/
        FileDownloader.getImpl().create(downloadUrl)
                .setPath(name)
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("progress", "pending");
                        setDownProgress(0);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int progress = (int) (soFarBytes * 100L / totalBytes);
                        Log.e("progress", "progress");
                        setDownProgress(progress);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        setDownProgress(100);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        String path = task.getPath();
                        Log.e("progress", "completed: " + path);
                        try {
                            Utils.openFile(MineActivity.this, path);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("openFile", "open fail");
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("progress", "paused");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        e.printStackTrace();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Log.e("progress", "error");
                        Toast.makeText(MineActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.e("progress", "warn");
                    }
                })
                .start();
    }

    private void showDownloadDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在下载");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgress(0);
        dialog.show();
    }

    private void setDownProgress(int progress) {
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        dialog.setProgress(progress);
    }

    private void show(PersonBean personBean) {
        if (personBean == null) {
            return;
        }
        Glide.with(MineActivity.this)
                .load(personBean.getAvator())
                .apply(Constant.AVATAR_OPTIONS)
                .into(ivHeadPic);
        tvUsername.setText(personBean.getName());
        tvMobilePhone.setText(personBean.getPhone());
        tvTel.setText(personBean.getTel());
        tvEmail.setText(personBean.getEmail());
        tvJob.setText(personBean.getJob());
        String mechanism = personBean.getMechanism();
        if (mechanism != null) {
            tvMechanism.setText(mechanism);
        }
        String depart = personBean.getDepart();
        if (depart != null) {
            tvCompany.setText(depart);
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
