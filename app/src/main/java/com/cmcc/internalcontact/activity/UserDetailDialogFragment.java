package com.cmcc.internalcontact.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserDetailDialogFragment extends DialogFragment {
    private static final String TAG = UserDetailDialogFragment.class.getSimpleName();
    private static final String TAG_DATA = "UserDetailDialogFragment_DATA";
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone1)
    TextView tvUserPhone1;
    @BindView(R.id.tv_user_phone2)
    TextView tvUserPhone2;
    @BindView(R.id.tv_user_tel)
    TextView tvUserTel;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.tv_user_company)
    TextView tvUserCompany;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.iv_call_btn)
    ImageView ivCallBtn;
    @BindView(R.id.iv_message_btn)
    ImageView ivMessageBtn;
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    Unbinder unbinder;
    private PersonModel personModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_user_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Bundle arguments = getArguments();
        if (arguments == null) {
            dismiss();
            return rootView;
        }
        personModel = arguments.getParcelable(TAG_DATA);
        if (personModel != null) {
            tvUserName.setText(personModel.getUsername());
            tvUserEmail.setText(personModel.getEmail());
            tvUserTel.setText(personModel.getTel());
            tvUserPhone1.setText(personModel.getMobile());
            tvUserPhone2.setVisibility(TextUtils.isEmpty(personModel.getMobile2()) ? View.GONE : View.VISIBLE);
            tvUserPhone2.setText(personModel.getMobile2());
            Glide.with(rootView).load(personModel.getHeadPic()).apply(Constant.AVATAR_OPTIONS).into(ivUserAvatar);
        }
        return rootView;
    }

    @Nullable
    @OnClick({R.id.tv_user_phone1, R.id.tv_user_phone2, R.id.tv_user_tel})
    public void onPhoneClick(View view) {
        TextView textView = (TextView) view;
        Utils.call(getActivity(), textView.getText().toString());
    }

    @OnClick({R.id.iv_call_btn})
    public void onCall(View v) {
        if (personModel == null) {
            return;
        }
        Utils.call(getActivity(), "10086");
    }

    @OnClick({R.id.iv_message_btn})
    public void onMessage(View v) {
        if (personModel == null) {
            return;
        }
        Utils.sendSms(getActivity(),"10010");
    }

    public static UserDetailDialogFragment show(FragmentActivity appCompatActivity, PersonModel personModel) {
        if (appCompatActivity == null) {
            return null;
        }
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        UserDetailDialogFragment bottomDialogFragment =
                (UserDetailDialogFragment) fragmentManager.findFragmentByTag(TAG);
        if (null == bottomDialogFragment) {
            bottomDialogFragment = newInstance();
        }

        if (!appCompatActivity.isFinishing()
                && null != bottomDialogFragment
                && !bottomDialogFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(TAG_DATA, personModel);
            bottomDialogFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(bottomDialogFragment, TAG)
                    .commitAllowingStateLoss();
        }
        return bottomDialogFragment;
    }

    public static UserDetailDialogFragment newInstance() {
        return new UserDetailDialogFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
// 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.CENTER;
// 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
