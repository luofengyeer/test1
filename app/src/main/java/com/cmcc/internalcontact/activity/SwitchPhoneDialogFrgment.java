package com.cmcc.internalcontact.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SwitchPhoneDialogFrgment extends DialogFragment {

    private static final String TAG = SwitchPhoneDialogFrgment.class.getSimpleName();
    private static final String TAG_SWITCH_PHONE = "switch_phones";
    @BindView(R.id.tv_switch_phone1)
    TextView tvSwitchPhone1;
    @BindView(R.id.tv_switch_phone2)
    TextView tvSwitchPhone2;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_switch_phone, container, false);
        ArrayList<String> phones = getArguments().getStringArrayList(TAG_SWITCH_PHONE);
        tvSwitchPhone1.setText(phones.get(0));
        tvSwitchPhone2.setText(phones.get(1));
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    public static SwitchPhoneDialogFrgment show(FragmentActivity appCompatActivity, String[] phones) {
        if (appCompatActivity == null || ArraysUtils.isArrayEmpty(phones)) {
            return null;
        }
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        SwitchPhoneDialogFrgment bottomDialogFragment =
                (SwitchPhoneDialogFrgment) fragmentManager.findFragmentByTag(TAG);
        if (null == bottomDialogFragment) {
            bottomDialogFragment = newInstance();
        }

        if (!appCompatActivity.isFinishing()
                && null != bottomDialogFragment
                && !bottomDialogFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putStringArray(TAG_SWITCH_PHONE, phones);
            bottomDialogFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(bottomDialogFragment, TAG)
                    .commitAllowingStateLoss();
        }
        return bottomDialogFragment;
    }

    public static SwitchPhoneDialogFrgment newInstance() {
        return new SwitchPhoneDialogFrgment();
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

    @Nullable
    @OnClick({R.id.lay_switch_phone1, R.id.lay_user_item_tel})
    public void onViewClicked(View view) {
        Utils.call(getActivity(), ((TextView) view).getText().toString());
    }
}
