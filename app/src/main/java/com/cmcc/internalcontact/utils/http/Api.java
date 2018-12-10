package com.cmcc.internalcontact.utils.http;

import com.cmcc.internalcontact.model.UpdateAppBean;
import com.cmcc.internalcontact.model.http.GetWaitImgBean;
import com.cmcc.internalcontact.model.http.LoginRequestBean;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.model.http.UpdateContactResponse;
import com.cmcc.internalcontact.model.http.UpdateDeptResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @POST("app/appLogin")
    Call<LoginResponseBean> login(@Body LoginRequestBean loginRequestBean);

    @POST("app/isHasNewContacts")
    Call<HashMap<String, Integer>> isHasNewContacts(@Body HashMap<String, String> version);

    @GET("app/updateContacts")
    Call<UpdateContactResponse> updateContacts();

    @POST("app/isHasNewDepts")
    Call<HashMap<String, Integer>> isHasNewDept(@Body HashMap<String, String> v);

    @GET("app/updateDepts")
    Call<UpdateDeptResponse> updateDepartments();

    @POST("app/isTokenPass")
    Call<HashMap<String, Integer>> isTokenPass();

    @POST("app/updateAppUser")
    Call<Void> updateAppUser(@Body HashMap<String, String> v);
//    Call<Void> updateAppUser(@Body LoginResponseBean.UserInfo personBean);

    @POST("app/uploadPic")
    Call<HashMap<String, String>> updateAvatar(@Body HashMap<String, String> v);

    @POST("app/updateApp")
    Call<UpdateAppBean> updateApp(@Body HashMap<String, String> v);

    @POST("app/getUserInfo")
    Call<LoginResponseBean.UserInfo> updateAppUser();

    @GET("app/getAppWaitImg")
    Call<List<GetWaitImgBean>> getAppWaitImg();
}
