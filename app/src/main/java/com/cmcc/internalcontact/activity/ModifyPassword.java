package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.usecase.MineInfo;
import com.cmcc.internalcontact.utils.DigestUtils;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.view.CommonToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cmcc.internalcontact.usecase.LoginUsecase.TAG_USE_INFO;

public class ModifyPassword extends BaseActivity {
    @BindView(R.id.toolbar_modify_pwd)
    CommonToolBar toolbarModifyPwd;
    @BindView(R.id.ed_old_pwd)
    EditText edOldPwd;
    @BindView(R.id.ed_new_pwd)
    EditText edNewPwd;
    @BindView(R.id.ed_re_new_pwd)
    EditText edReNewPwd;
    LoginResponseBean.UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_modify_pwd);
        ButterKnife.bind(this);
        String string = SharePreferencesUtils.getInstance().getString(TAG_USE_INFO, "");
        userInfo = JSON.parseObject(string, LoginResponseBean.UserInfo.class);
    }


    @OnClick(R.id.btn_modify_btn)
    public void onViewClicked() {
        if (TextUtils.isEmpty(edOldPwd.getText().toString())) {
            Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!userInfo.getPassword().equals(DigestUtils.encrypt(edOldPwd.getText().toString(), null))) {
            Toast.makeText(this, "旧密码输入错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edNewPwd.getText().toString())) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edReNewPwd.getText().toString())) {
            Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!edNewPwd.getText().toString().equals(edReNewPwd.getText().toString())) {
            Toast.makeText(this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }
        new MineInfo().updateUserInfo(ModifyPassword.this, "password", edNewPwd.getText().toString())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObserver<Object>(this) {
            @Override
            public void onNext(Object aVoid) {
                Toast.makeText(ModifyPassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Toast.makeText(ModifyPassword.this, "密码修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
