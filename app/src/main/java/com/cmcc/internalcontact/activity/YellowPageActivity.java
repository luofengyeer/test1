package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YellowPageActivity extends BaseActivity {

    @BindView(R.id.tv_110)
    TextView tv110;
    @BindView(R.id.tv_119)
    TextView tv119;
    @BindView(R.id.tv_120)
    TextView tv120;
    @BindView(R.id.tv_122)
    TextView tv122;
    @BindView(R.id.tv_112)
    TextView tv112;
    @BindView(R.id.tv_114)
    TextView tv114;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_yellow_page);
        ButterKnife.bind(this);

        buildTel(tv110);
        buildTel(tv119);
        buildTel(tv120);
        buildTel(tv122);
        buildTel(tv112);
        buildTel(tv114);
    }

    private void buildTel(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Utils.addClickablePart(this, textView.getText().toString(),true), TextView.BufferType.SPANNABLE);
    }
}
