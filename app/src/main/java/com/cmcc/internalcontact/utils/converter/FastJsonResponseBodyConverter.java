package com.cmcc.internalcontact.utils.converter;

import android.support.annotation.NonNull;


import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 *  ResponseBody转换器
 *  @author hjl
 *  @date 2017/5/11
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
        return JsonManager.jsonToBean(source,type);
    }
}
