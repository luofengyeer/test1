package com.cmcc.internalcontact.usecase;


import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.cmcc.internalcontact.model.http.LoginRequestBean;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.utils.AesUtils;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.http.HttpManager;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;

public class LoginUsecase {
    public static final String TAG_USE_INFO = "TAG_USE_INFO";

    public Observable<LoginResponseBean> login(Context context, String account, String password) {
        return Observable.just(account).map(new Function<String, LoginResponseBean>() {
            @Override
            public LoginResponseBean apply(String s) throws Exception {
                LoginRequestBean loginRequestBean = new LoginRequestBean();
                loginRequestBean.setDeviceNo(AesUtils.encrypt(Build.SERIAL));
                loginRequestBean.setUsername(AesUtils.encrypt(account));
                loginRequestBean.setPassword(AesUtils.encrypt(password));
                loginRequestBean.setDeviceType(AesUtils.encrypt(loginRequestBean.getDeviceType()));
                Call<LoginResponseBean> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .login(loginRequestBean);
                Response<LoginResponseBean> response = responseBeanCall.execute();
                return response.body();
            }
        });
    }

    public void saveUseInfo(LoginResponseBean userInfo) {
        SharePreferencesUtils.getInstance().setString(TAG_USE_INFO, JSON.toJSONString(userInfo.getUserInfo()));
        saveToken(userInfo.getToken(), System.currentTimeMillis() + (userInfo.getExpire()*1000));
    }

    public void saveToken(String token, long tokenExpire) {
        SharePreferencesUtils.getInstance().setString(Constant.TAG_HTTP_TOKEN, token);
        SharePreferencesUtils.getInstance().setLong(Constant.TAG_HTTP_TOKEN_EXPIRE, tokenExpire);
    }

    public boolean isTokenValid() {
        long expireTime = SharePreferencesUtils.getInstance().getLong(Constant.TAG_HTTP_TOKEN_EXPIRE, -1);
        return expireTime > System.currentTimeMillis();
    }
}
