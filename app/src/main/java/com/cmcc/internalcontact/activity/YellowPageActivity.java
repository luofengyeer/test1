package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YellowPageActivity extends BaseActivity {


    @BindView(R.id.wv_yellow_page)
    WebView wvYellowPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_yellow_page);
        ButterKnife.bind(this);
        wvYellowPage.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvYellowPage.getSettings().setLoadWithOverviewMode(true);
        wvYellowPage.getSettings().setAllowFileAccess(true);
        wvYellowPage.loadUrl(Constant.YELLOW_PAGE_URL);
    }

    private void buildTel(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Utils.addClickablePart(this, textView.getText().toString(), true), TextView.BufferType.SPANNABLE);
    }
}
