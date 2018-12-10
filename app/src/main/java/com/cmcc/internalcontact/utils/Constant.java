package com.cmcc.internalcontact.utils;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.utils.view.GlideCircleTransform;

public class Constant {
    public static final String SWITCH_INCOME_STATUE = "SWITCH_INCOME_STATUE";
    public static final String TAG_HTTP_TOKEN = "TAG_HTTP_TOKEN";
    public static final String TAG_HTTP_TOKEN_EXPIRE = "TAG_HTTP_TOKEN_EXPIRE";
    //        public static final String BASE_URL = "http://mulinliang.imwork.net/contactus/";
//    public static final String BASE_URL = "http://39.106.139.151:9797/contactus/";
    public static final String BASE_URL = "http://39.106.139.151:9798/contactus/";
    public static final String BASE_AVATRE_URL = "http://39.106.139.151:9798";
    public static final String EXCEPTION_TOKEN_INVALID = "token invalid";

    public static final String HTTP_KEY = "f4e2e52034348f86b67cde581c0f9eb4";
    public static final RequestOptions AVATAR_OPTIONS = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_avatar).transform(new GlideCircleTransform())
            .error(R.drawable.ic_avatar)
            .priority(Priority.HIGH);

    /**
     * 类型-机构
     */
    public static final int TYPE_MECHANISM = 0;
    /**
     * 类型-部门
     */
    public static final int TYPE_COMPANY = 1;

    /**
     * Intent传值-部门信息
     */
    public static final String INTENT_DATA_DEPART = "intent_data_depart";
}
