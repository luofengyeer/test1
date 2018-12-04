package com.cmcc.internalcontact.utils.http;

import android.content.Context;

import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.converter.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class HttpManager {
    private static HttpManager ourInstance;
    private OkHttpClient mOkHttpClient;
    private final int TIME_OUT = 30;
    private Context context;

    public static HttpManager getInstance(Context context) {
        if (ourInstance != null) {
            return ourInstance;
        }
        ourInstance = new HttpManager();
        return ourInstance;
    }

    private HttpManager() {
        initOkHttpClient();
    }

    private void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (HttpManager.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addNetworkInterceptor(new TokenInterceptor(context))
                            .build();
                }
            }
        }
    }

    public Api getApi() {
        Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient)
                //设置数据解析器
                .addConverterFactory(FastJsonConverterFactory.create())
                //设置网络请求的Url地址
                .baseUrl(Constant.BASR_URL)
                .build();
// 创建网络请求接口的实例
        return retrofit.create(Api.class);
    }

}
