package com.cmcc.internalcontact.utils;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.cmcc.internalcontact.R;

public class Constant {
    public static final String SWITCH_INCOME_STATUE = "SWITCH_INCOME_STATUE";
    public static final String TAG_HTTP_TOKEN = "TAG_HTTP_TOKEN";
    public static final String TAG_HTTP_TOKEN_EXPIRE = "TAG_HTTP_TOKEN_EXPIRE";
    public static final String BASE_URL = "http://mulinliang.imwork.net/contactus/";
//    public static final String BASE_URL = "http://39.106.139.151:9797/contactus/";
    public static final String EXCEPTION_TOKEN_INVALID = "token invalid";

    public static final String HTTP_KEY = "f4e2e52034348f86b67cde581c0f9eb4";
    public static final RequestOptions AVATAR_OPTIONS = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_avatar)
            .error(R.drawable.ic_avatar)
            .priority(Priority.HIGH);
}
