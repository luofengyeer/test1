package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.cmcc.internalcontact.utils.view.OnToolBarButtonClickListener;
import com.cmcc.internalcontact.utils.view.ToolBarButtonType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cmcc.internalcontact.activity.ResetPasswordActivity.TAG_LOGIN_CODE;

public class QuickLoginActivity extends BaseActivity {

    @BindView(R.id.tb_quick_login)
    CommonToolBar toolBar;
    @BindView(R.id.vf_quick_login)
    ViewFlipper viewFlipper;
    @BindView(R.id.ed_quick_login_phone)
    EditText phoneNumberEd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);
        ButterKnife.bind(this);
        toolBar.setBarButtonClickListener(new OnToolBarButtonClickListener() {
            @Override
            public void onClick(View v, ToolBarButtonType type) {
                switch (type) {
                    case LEFT_FIRST_BUTTON:
                        finish();
                        break;
                }
            }
        });
        if (getIntent() != null) {
            String loginCode = getIntent().getStringExtra(TAG_LOGIN_CODE);
            if (!TextUtils.isEmpty(loginCode)) {
                phoneNumberEd.setText(loginCode);
            }
        }
    }

    @OnClick(R.id.btn_quick_login_next)
    public void goNext() {
        viewFlipper.showNext();
    }

}
