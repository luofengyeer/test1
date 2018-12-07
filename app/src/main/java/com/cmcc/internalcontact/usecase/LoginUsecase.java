package com.cmcc.internalcontact.usecase;


import android.content.Context;
import android.os.Build;

import com.cmcc.internalcontact.model.http.LoginRequestBean;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.utils.AesUtils;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.http.HttpManager;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Response;

public class LoginUsecase {
    public Observable<LoginResponseBean> login(Context context, String account, String password) {
        return Observable.just(account).map(new Function<String, LoginResponseBean>() {
            @Override
            public LoginResponseBean apply(String s) throws Exception {
                LoginRequestBean loginRequestBean = new LoginRequestBean();
                loginRequestBean.setDeviceNo(Build.SERIAL);
                loginRequestBean.setUsername(AesUtils.encrypt(account, Constant.HTTP_KEY));
                loginRequestBean.setPassword(AesUtils.encrypt(password, Constant.HTTP_KEY));
               Call<LoginResponseBean> responseBeanCall = HttpManager.getInstance(context).getApi()
                        .login(loginRequestBean);
                Response<LoginResponseBean> response = responseBeanCall.execute();
                return response.body();
            }
        });
    }
}
