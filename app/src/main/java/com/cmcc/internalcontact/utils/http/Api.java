package com.cmcc.internalcontact.utils.http;

import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.model.http.LoginRequestBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("app/appLogin")
    Call<LoginResponseBean> login(@Body LoginRequestBean loginRequestBean);
}
