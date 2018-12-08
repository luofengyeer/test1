package com.cmcc.internalcontact.utils.http;

import com.cmcc.internalcontact.model.PersonBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.model.http.LoginRequestBean;
import com.cmcc.internalcontact.model.http.LoginResponseBean;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    @POST("app/appLogin")
    Call<LoginResponseBean> login(@Body LoginRequestBean loginRequestBean);

    @POST("app/isHasNewContacts")
    Call<HashMap<String, Integer>> isHasNewContacts(@Path("telsVersion") long version);

    @GET("app/updateContacts")
    Call<List<PersonModel>> updateContacts();

    @POST("app/isHasNewDepts")
    Call<HashMap<String, Integer>> isHasNewDept(@Path("deptsVersion") long version);

    @GET("app/updateDepts")
    Call<List<DepartModel>> updateDepartments();

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
