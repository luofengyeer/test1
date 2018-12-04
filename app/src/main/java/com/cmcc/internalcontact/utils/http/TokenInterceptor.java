package com.cmcc.internalcontact.utils.http;

import android.content.Context;

import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private Context context;

    public TokenInterceptor(Context context) {

        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Headers headers = request.headers();
        SharePreferencesUtils sharePreferencesUtils = new SharePreferencesUtils(context);
        String token = sharePreferencesUtils.getString(Constant.TAG_HTTP_TOKEN, null);
        headers.newBuilder().add("token", token).build();
        return chain.proceed(request);
    }
}
