package com.cmcc.internalcontact.utils.converter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cmcc.internalcontact.BuildConfig;
import com.cmcc.internalcontact.model.http.HttpBaseBean;
import com.cmcc.internalcontact.utils.AesUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

import static com.cmcc.internalcontact.utils.Constant.EXCEPTION_TOKEN_INVALID;

/**
 * ResponseBody转换器
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {

        BufferedSource bufferedSource = Okio.buffer(value.source());
        String source = bufferedSource.readUtf8();
        bufferedSource.close();
        HttpBaseBean httpBaseBean = JSON.parseObject(source, HttpBaseBean.class);
        if (httpBaseBean.getCode() == 401) {
            throw new IOException(EXCEPTION_TOKEN_INVALID);
        }
        if (httpBaseBean.getCode() != 0) {
            throw new IOException("http错误：" + httpBaseBean.getMsg() + ",code:" + httpBaseBean.getCode());
        }
        String decrypt = AesUtils.decrypt(httpBaseBean.getData());
        if (BuildConfig.DEBUG) {
            Log.d("ServerResponseData:", "data=" + decrypt);
        }
        return JsonManager.jsonToBean(decrypt, type);

    }
}
