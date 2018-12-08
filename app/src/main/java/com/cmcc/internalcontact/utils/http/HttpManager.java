package com.cmcc.internalcontact.utils.http;

import android.content.Context;

import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.converter.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
        ourInstance = new HttpManager(context);
        return ourInstance;
    }

    private HttpManager(Context context) {
        this.context = context;
        initOkHttpClient();
    }

    private void initOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (HttpManager.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addNetworkInterceptor(httpLoggingInterceptor)
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
                .baseUrl(Constant.BASE_URL)
                .build();
// 创建网络请求接口的实例
        return retrofit.create(Api.class);
    }

}
