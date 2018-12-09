package com.cmcc.internalcontact.utils.http;

import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.http.LoginRequestBean;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.model.http.UpdateContactResponse;
import com.cmcc.internalcontact.model.http.UpdateDeptResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    @POST("app/appLogin")
    Call<LoginResponseBean> login(@Body LoginRequestBean loginRequestBean);

    @POST("app/isHasNewContacts")
    Call<HashMap<String, Integer>> isHasNewContacts(@Body HashMap<String,Long>  version);

    @GET("app/updateContacts")
    Call<UpdateContactResponse> updateContacts();

    @POST("app/isHasNewDepts")
    Call<HashMap<String, Integer>> isHasNewDept(@Body HashMap<String,Long> v);

    @GET("app/updateDepts")
    Call<UpdateDeptResponse> updateDepartments();

    @POST("app/isTokenPass")
    Call<HashMap<String, Integer>> isTokenPass();

    @POST("app/updateAppUser")
    Call<Void> updateAppUser(PersonBean personBean);

    @POST("app/app/uploadPic")
    Call<HashMap<String, String>> updateAvatar(String personBean);

    @POST("app/updateApp")
    Call<HashMap<String, Integer>> updateApp(@Path("version") String version, @Path("appType") int appType);

    @POST("app/getUserInfo")
    Call<HashMap<String, Integer>> updateAppUser();
}
